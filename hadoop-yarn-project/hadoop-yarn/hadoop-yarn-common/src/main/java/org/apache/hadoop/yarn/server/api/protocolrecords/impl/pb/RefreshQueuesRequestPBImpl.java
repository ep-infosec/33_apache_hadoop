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

package org.apache.hadoop.yarn.server.api.protocolrecords.impl.pb;

import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.classification.InterfaceStability.Unstable;
import org.apache.hadoop.yarn.proto.YarnServerResourceManagerServiceProtos.RefreshQueuesRequestProto;
import org.apache.hadoop.yarn.proto.YarnServerResourceManagerServiceProtos.RefreshQueuesRequestProtoOrBuilder;
import org.apache.hadoop.yarn.server.api.protocolrecords.RefreshQueuesRequest;

import org.apache.hadoop.thirdparty.protobuf.TextFormat;

@Private
@Unstable
public class RefreshQueuesRequestPBImpl extends RefreshQueuesRequest {

  private RefreshQueuesRequestProto proto = RefreshQueuesRequestProto.getDefaultInstance();
  private RefreshQueuesRequestProto.Builder builder = null;
  private boolean viaProto = false;
  
  public RefreshQueuesRequestPBImpl() {
    builder = RefreshQueuesRequestProto.newBuilder();
  }

  public RefreshQueuesRequestPBImpl(RefreshQueuesRequestProto proto) {
    this.proto = proto;
    viaProto = true;
  }
  
  public RefreshQueuesRequestProto getProto() {
    proto = viaProto ? proto : builder.build();
    viaProto = true;
    return proto;
  }

  @Override
  public int hashCode() {
    return getProto().hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other.getClass().isAssignableFrom(this.getClass())) {
      return this.getProto().equals(this.getClass().cast(other).getProto());
    }
    return false;
  }

  @Override
  public String toString() {
    return TextFormat.shortDebugString(getProto());
  }

  private void maybeInitBuilder() {
    if (viaProto || builder == null) {
      builder = RefreshQueuesRequestProto.newBuilder(proto);
    }
    viaProto = false;
  }

  @Override
  public String getSubClusterId() {
    RefreshQueuesRequestProtoOrBuilder p = viaProto ? proto : builder;
    return (p.hasSubClusterId()) ? p.getSubClusterId() : null;
  }

  @Override
  public void setSubClusterId(String clusterId) {
    maybeInitBuilder();
    if (clusterId == null) {
      builder.clearSubClusterId();
      return;
    }
    builder.setSubClusterId(clusterId);
  }
}
