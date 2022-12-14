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

package org.apache.hadoop.yarn.server.federation.store.records.impl.pb;

import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.classification.InterfaceStability.Unstable;
import org.apache.hadoop.yarn.federation.proto.YarnServerFederationProtos;
import org.apache.hadoop.yarn.federation.proto.YarnServerFederationProtos.GetApplicationsHomeSubClusterRequestProto;
import org.apache.hadoop.yarn.federation.proto.YarnServerFederationProtos.GetApplicationsHomeSubClusterRequestProtoOrBuilder;
import org.apache.hadoop.yarn.server.federation.store.records.GetApplicationsHomeSubClusterRequest;

import org.apache.hadoop.thirdparty.protobuf.TextFormat;
import org.apache.hadoop.yarn.server.federation.store.records.SubClusterId;

/**
 * Protocol buffer based implementation of
 * {@link GetApplicationsHomeSubClusterRequest}.
 */
@Private
@Unstable
public class GetApplicationsHomeSubClusterRequestPBImpl
    extends GetApplicationsHomeSubClusterRequest {

  private GetApplicationsHomeSubClusterRequestProto proto =
      GetApplicationsHomeSubClusterRequestProto.getDefaultInstance();
  private GetApplicationsHomeSubClusterRequestProto.Builder builder = null;
  private boolean viaProto = false;

  public GetApplicationsHomeSubClusterRequestPBImpl() {
    builder = GetApplicationsHomeSubClusterRequestProto.newBuilder();
  }

  public GetApplicationsHomeSubClusterRequestPBImpl(
      GetApplicationsHomeSubClusterRequestProto proto) {
    this.proto = proto;
    viaProto = true;
  }

  public GetApplicationsHomeSubClusterRequestProto getProto() {
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
      builder = GetApplicationsHomeSubClusterRequestProto.newBuilder(proto);
    }
    viaProto = false;
  }

  @Override
  public SubClusterId getSubClusterId() {
    GetApplicationsHomeSubClusterRequestProtoOrBuilder p = viaProto ? proto : builder;
    if (!p.hasSubClusterId()) {
      return null;
    }
    return convertFromProtoFormat(p.getSubClusterId());
  }

  @Override
  public void setSubClusterId(SubClusterId subClusterId) {
    maybeInitBuilder();
    if (subClusterId == null) {
      builder.clearSubClusterId();
      return;
    }
    builder.setSubClusterId(convertToProtoFormat(subClusterId));
  }

  private SubClusterId convertFromProtoFormat(YarnServerFederationProtos.SubClusterIdProto sc) {
    return new SubClusterIdPBImpl(sc);
  }

  private YarnServerFederationProtos.SubClusterIdProto convertToProtoFormat(SubClusterId sc) {
    return ((SubClusterIdPBImpl) sc).getProto();
  }
}
