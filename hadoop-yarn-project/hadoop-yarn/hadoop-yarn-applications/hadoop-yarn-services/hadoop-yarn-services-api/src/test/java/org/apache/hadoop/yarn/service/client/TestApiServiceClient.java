/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.yarn.service.client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Lists;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.apache.hadoop.yarn.service.exceptions.LauncherExitCodes.*;

/**
 * Test case for CLI to API Service.
 *
 */
public class TestApiServiceClient {
  private static ApiServiceClient asc;
  private static ApiServiceClient badAsc;
  private static Server server;

  /**
   * A mocked version of API Service for testing purpose.
   *
   */
  @SuppressWarnings("serial")
  public static class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      System.out.println("Get was called");
      if (req.getPathInfo() != null
          && req.getPathInfo().contains("nonexistent-app")) {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      } else {
        resp.setStatus(HttpServletResponse.SC_OK);
      }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      resp.setStatus(HttpServletResponse.SC_OK);
    }

  }

  @BeforeAll
  public static void setup() throws Exception {
    server = new Server(8088);
    ((QueuedThreadPool)server.getThreadPool()).setMaxThreads(20);
    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/app");
    server.setHandler(context);
    context.addServlet(new ServletHolder(TestServlet.class), "/*");
    ((ServerConnector)server.getConnectors()[0]).setHost("localhost");
    server.start();

    Configuration conf = new Configuration();
    conf.set("yarn.resourcemanager.webapp.address",
        "localhost:8088");
    asc = new ApiServiceClient();
    asc.serviceInit(conf);

    Configuration conf2 = new Configuration();
    conf2.set("yarn.resourcemanager.webapp.address",
        "localhost:8089");
    badAsc = new ApiServiceClient();
    badAsc.serviceInit(conf2);
  }

  @AfterAll
  public static void tearDown() throws Exception {
    server.stop();
  }

  @Test
  void testGetRMWebAddress() throws Exception {
    Configuration conf = new Configuration();
    conf.setBoolean(YarnConfiguration.RM_HA_ENABLED, true);
    conf.set(YarnConfiguration.RM_HA_IDS, "rm1");
    conf.set(YarnConfiguration.RM_HA_ID, "rm1");
    conf.set("yarn.resourcemanager.webapp.address.rm1", "localhost:0");
    ApiServiceClient asc1 = new ApiServiceClient(conf);
    boolean exceptionCaught = false;
    String diagnosticsMsg = null;
    try {
      String rmWebAddress = asc1.getRMWebAddress();
    } catch (IOException e) {
      exceptionCaught = true;
      diagnosticsMsg = e.getMessage();
    }
    assertTrue(exceptionCaught, "ApiServiceClient failed to throw exception");
    assertTrue(diagnosticsMsg.contains("Error connecting to localhost:0"),
        "Exception Message does not match");
  }

  @Test
  void testLaunch() {
    String fileName = "target/test-classes/example-app.json";
    String appName = "example-app";
    long lifetime = 3600L;
    String queue = "default";
    try {
      int result = asc.actionLaunch(fileName, appName, lifetime, queue);
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testBadLaunch() {
    String fileName = "unknown_file";
    String appName = "unknown_app";
    long lifetime = 3600L;
    String queue = "default";
    try {
      int result = badAsc.actionLaunch(fileName, appName, lifetime, queue);
      assertEquals(EXIT_EXCEPTION_THROWN, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testStatus() {
    String appName = "nonexistent-app";
    try {
      String result = asc.getStatusString(appName);
      assertEquals(" Service " + appName + " not found", result, "Status reponse don't match");
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testStop() {
    String appName = "example-app";
    try {
      int result = asc.actionStop(appName);
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testBadStop() {
    String appName = "unknown_app";
    try {
      int result = badAsc.actionStop(appName);
      assertEquals(EXIT_EXCEPTION_THROWN, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testStart() {
    String appName = "example-app";
    try {
      int result = asc.actionStart(appName);
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testBadStart() {
    String appName = "unknown_app";
    try {
      int result = badAsc.actionStart(appName);
      assertEquals(EXIT_EXCEPTION_THROWN, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testSave() {
    String fileName = "target/test-classes/example-app.json";
    String appName = "example-app";
    long lifetime = 3600L;
    String queue = "default";
    try {
      int result = asc.actionSave(fileName, appName, lifetime, queue);
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testBadSave() {
    String fileName = "unknown_file";
    String appName = "unknown_app";
    long lifetime = 3600L;
    String queue = "default";
    try {
      int result = badAsc.actionSave(fileName, appName, lifetime, queue);
      assertEquals(EXIT_EXCEPTION_THROWN, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testFlex() {
    String appName = "example-app";
    HashMap<String, String> componentCounts = new HashMap<String, String>();
    try {
      int result = asc.actionFlex(appName, componentCounts);
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testBadFlex() {
    String appName = "unknown_app";
    HashMap<String, String> componentCounts = new HashMap<String, String>();
    try {
      int result = badAsc.actionFlex(appName, componentCounts);
      assertEquals(EXIT_EXCEPTION_THROWN, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testDestroy() {
    String appName = "example-app";
    try {
      int result = asc.actionDestroy(appName);
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testBadDestroy() {
    String appName = "unknown_app";
    try {
      int result = badAsc.actionDestroy(appName);
      assertEquals(EXIT_EXCEPTION_THROWN, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testInitiateServiceUpgrade() {
    String appName = "example-app";
    String upgradeFileName = "target/test-classes/example-app.json";
    try {
      int result = asc.initiateUpgrade(appName, upgradeFileName, false);
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testInstancesUpgrade() {
    String appName = "example-app";
    try {
      int result = asc.actionUpgradeInstances(appName, Lists.newArrayList(
          "comp-1", "comp-2"));
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testComponentsUpgrade() {
    String appName = "example-app";
    try {
      int result = asc.actionUpgradeComponents(appName, Lists.newArrayList(
          "comp"));
      assertEquals(EXIT_SUCCESS, result);
    } catch (IOException | YarnException e) {
      fail();
    }
  }

  @Test
  void testNoneSecureApiClient() throws IOException {
    String url = asc.getServicePath("/foobar");
    assertTrue(url.contains("user.name"),
        "User.name flag is missing in service path.");
    assertTrue(url.contains(System.getProperty("user.name")),
        "User.name flag is not matching JVM user.");
  }

}
