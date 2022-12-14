/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership.  The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.hadoop.yarn.server.federation.policies.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.server.federation.policies.FederationPolicyInitializationContext;
import org.apache.hadoop.yarn.server.federation.policies.FederationPolicyInitializationContextValidator;
import org.apache.hadoop.yarn.server.federation.policies.exceptions.FederationPolicyInitializationException;
import org.apache.hadoop.yarn.server.federation.store.records.SubClusterId;
import org.apache.hadoop.yarn.server.federation.store.records.SubClusterInfo;

/**
 * This {@link FederationRouterPolicy} pick a subcluster based on the hash of
 * the job's queue name. Useful to provide a default behavior when too many
 * queues exist in a system. This also ensures that all jobs belonging to a
 * queue are mapped to the same sub-cluster (likely help with locality).
 */
public class HashBasedRouterPolicy extends AbstractRouterPolicy {

  @Override
  public void reinitialize(
      FederationPolicyInitializationContext federationPolicyContext)
      throws FederationPolicyInitializationException {
    FederationPolicyInitializationContextValidator
        .validate(federationPolicyContext, this.getClass().getCanonicalName());

    // note: this overrides BaseRouterPolicy and ignores the weights
    setPolicyContext(federationPolicyContext);
  }

  @Override
  protected SubClusterId chooseSubCluster(String queue,
      Map<SubClusterId, SubClusterInfo> preSelectSubclusters) throws YarnException {
    int chosenPosition = Math.abs(queue.hashCode() % preSelectSubclusters.size());
    List<SubClusterId> list = new ArrayList<>(preSelectSubclusters.keySet());
    Collections.sort(list);
    return list.get(chosenPosition);
  }
}
