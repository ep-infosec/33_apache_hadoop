/*
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

package org.apache.hadoop.yarn.server.timeline;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOverrideTimelineStoreYarnClient {

  @Test
  void testLifecycleAndOverride() throws Throwable {
    YarnConfiguration conf = new YarnConfiguration();
    try (NoRMStore store = new NoRMStore()) {
      store.init(conf);
      store.start();
      assertEquals(EntityGroupFSTimelineStore.AppState.ACTIVE,
          store.getAppState(ApplicationId.newInstance(1, 1)));
      store.stop();
    }
  }

  private static class NoRMStore extends EntityGroupFSTimelineStore {
    @Override
    protected YarnClient createAndInitYarnClient(Configuration conf) {
      return null;
    }

    @Override
    protected AppState getAppState(ApplicationId appId)
        throws IOException {
      return AppState.ACTIVE;
    }
  }
}
