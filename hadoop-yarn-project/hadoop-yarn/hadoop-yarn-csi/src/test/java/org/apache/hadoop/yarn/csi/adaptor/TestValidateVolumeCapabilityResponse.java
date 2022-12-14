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
package org.apache.hadoop.yarn.csi.adaptor;

import org.apache.hadoop.yarn.api.protocolrecords.ValidateVolumeCapabilitiesResponse;
import org.apache.hadoop.yarn.api.protocolrecords.impl.pb.ValidateVolumeCapabilitiesResponsePBImpl;
import org.apache.hadoop.yarn.proto.CsiAdaptorProtos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UT for message exchanges.
 */
public class TestValidateVolumeCapabilityResponse {

  @Test
  void testPBRecord() {
    CsiAdaptorProtos.ValidateVolumeCapabilitiesResponse proto =
        CsiAdaptorProtos.ValidateVolumeCapabilitiesResponse.newBuilder()
            .setSupported(true)
            .setMessage("capability is supported")
            .build();

    ValidateVolumeCapabilitiesResponsePBImpl pbImpl =
        new ValidateVolumeCapabilitiesResponsePBImpl(proto);

    assertEquals(true, pbImpl.isSupported());
    assertEquals("capability is supported", pbImpl.getResponseMessage());
    assertEquals(proto, pbImpl.getProto());
  }

  @Test
  void testNewInstance() {
    ValidateVolumeCapabilitiesResponse pbImpl =
        ValidateVolumeCapabilitiesResponsePBImpl
            .newInstance(false, "capability not supported");
    assertEquals(false, pbImpl.isSupported());
    assertEquals("capability not supported",
        pbImpl.getResponseMessage());

    CsiAdaptorProtos.ValidateVolumeCapabilitiesResponse proto =
        ((ValidateVolumeCapabilitiesResponsePBImpl) pbImpl).getProto();
    assertEquals(false, proto.getSupported());
    assertEquals("capability not supported", proto.getMessage());
  }
}