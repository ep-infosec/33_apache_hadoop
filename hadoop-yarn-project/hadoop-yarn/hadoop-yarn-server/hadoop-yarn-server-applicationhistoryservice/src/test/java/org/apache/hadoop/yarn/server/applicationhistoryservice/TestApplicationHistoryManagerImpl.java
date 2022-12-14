/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.yarn.server.applicationhistoryservice;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationAttemptId;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestApplicationHistoryManagerImpl extends
    ApplicationHistoryStoreTestUtils {
  private ApplicationHistoryManagerImpl applicationHistoryManagerImpl = null;

  @BeforeEach
  public void setup() throws Exception {
    Configuration config = new Configuration();
    config.setClass(YarnConfiguration.APPLICATION_HISTORY_STORE,
        MemoryApplicationHistoryStore.class, ApplicationHistoryStore.class);
    applicationHistoryManagerImpl = new ApplicationHistoryManagerImpl();
    applicationHistoryManagerImpl.init(config);
    applicationHistoryManagerImpl.start();
    store = applicationHistoryManagerImpl.getHistoryStore();
  }

  @AfterEach
  public void tearDown() throws Exception {
    applicationHistoryManagerImpl.stop();
  }

  @Test
  void testApplicationReport() throws IOException, YarnException {
    ApplicationId appId = null;
    appId = ApplicationId.newInstance(0, 1);
    writeApplicationStartData(appId);
    writeApplicationFinishData(appId);
    ApplicationAttemptId appAttemptId =
        ApplicationAttemptId.newInstance(appId, 1);
    writeApplicationAttemptStartData(appAttemptId);
    writeApplicationAttemptFinishData(appAttemptId);
    ApplicationReport appReport =
        applicationHistoryManagerImpl.getApplication(appId);
    assertNotNull(appReport);
    assertEquals(appId, appReport.getApplicationId());
    assertEquals(appAttemptId,
        appReport.getCurrentApplicationAttemptId());
    assertEquals(appAttemptId.toString(), appReport.getHost());
    assertEquals("test type", appReport.getApplicationType().toString());
    assertEquals("test queue", appReport.getQueue().toString());
  }

  @Test
  void testApplications() throws IOException {
    ApplicationId appId1 = ApplicationId.newInstance(0, 1);
    ApplicationId appId2 = ApplicationId.newInstance(0, 2);
    ApplicationId appId3 = ApplicationId.newInstance(0, 3);
    writeApplicationStartData(appId1, 1000);
    writeApplicationFinishData(appId1);
    writeApplicationStartData(appId2, 3000);
    writeApplicationFinishData(appId2);
    writeApplicationStartData(appId3, 4000);
    writeApplicationFinishData(appId3);
    Map<ApplicationId, ApplicationReport> reports =
        applicationHistoryManagerImpl.getApplications(2, 2000L, 5000L);
    assertNotNull(reports);
    assertEquals(2, reports.size());
    assertNull(reports.get("1"));
    assertNull(reports.get("2"));
    assertNull(reports.get("3"));
  }
}
