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

package org.apache.hadoop.yarn.server.resourcemanager.webapp.dao;

import java.util.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "nodeToLabelsInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class NodeToLabelsInfo {

  private HashMap<String, NodeLabelsInfo> nodeToLabels =
      new HashMap<String, NodeLabelsInfo>();

  public NodeToLabelsInfo() {
    // JAXB needs this
  }

  public NodeToLabelsInfo(HashMap<String, NodeLabelsInfo> nodeToLabels) {
    if (nodeToLabels != null) {
      this.nodeToLabels.putAll(nodeToLabels);
    }
  }

  public HashMap<String, NodeLabelsInfo> getNodeToLabels() {
    return nodeToLabels;
  }

  public void setNodeToLabels(HashMap<String, NodeLabelsInfo> nodeToLabels) {
    this.nodeToLabels = nodeToLabels;
  }
}
