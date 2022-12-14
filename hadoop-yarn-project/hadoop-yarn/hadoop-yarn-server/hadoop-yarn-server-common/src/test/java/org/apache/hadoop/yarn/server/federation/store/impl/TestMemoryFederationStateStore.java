/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership.  The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.hadoop.yarn.server.federation.store.impl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.server.federation.store.FederationStateStore;

/**
 * Unit tests for MemoryFederationStateStore.
 */
public class TestMemoryFederationStateStore
    extends FederationStateStoreBaseTest {

  @Override
  protected FederationStateStore createStateStore() {
    Configuration conf = new Configuration();
    conf.setInt(YarnConfiguration.FEDERATION_STATESTORE_MAX_APPLICATIONS, 10);
    super.setConf(conf);
    return new MemoryFederationStateStore();
  }
}