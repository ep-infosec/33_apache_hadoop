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
package org.apache.hadoop.yarn.server.timeline;

import org.apache.hadoop.util.Sets;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.timeline.TimelineEntityGroupId;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;

class EntityGroupPlugInForTest extends TimelineEntityGroupPlugin {

  static final String APP_ID_FILTER_NAME = "appid";

  @Override
  public Set<TimelineEntityGroupId> getTimelineEntityGroupId(String entityType,
      NameValuePair primaryFilter,
      Collection<NameValuePair> secondaryFilters) {
    ApplicationId appId = ApplicationId.fromString(
        primaryFilter.getValue().toString());
    return Sets.newHashSet(getStandardTimelineGroupId(appId));
  }

  @Override
  public Set<TimelineEntityGroupId> getTimelineEntityGroupId(String entityId,
      String entityType) {
    ApplicationId appId = ApplicationId.fromString(
        entityId);
    return Sets.newHashSet(getStandardTimelineGroupId(appId));
  }

  @Override
  public Set<TimelineEntityGroupId> getTimelineEntityGroupId(String entityType,
      SortedSet<String> entityIds,
      Set<String> eventTypes) {
    return Sets.newHashSet();
  }

  static TimelineEntityGroupId getStandardTimelineGroupId(ApplicationId appId) {
    return TimelineEntityGroupId.newInstance(appId, "test");
  }
}
