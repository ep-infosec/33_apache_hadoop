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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.hadoop.yarn.api.records.NodeLabel;

@XmlRootElement(name = "nodeLabelInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class NodeLabelInfo {

  private String name;
  private boolean exclusivity;
  private PartitionInfo partitionInfo;
  private Integer activeNMs;

  public NodeLabelInfo() {
    // JAXB needs this
  }

  public NodeLabelInfo(String name) {
    this.name = name;
    this.exclusivity = true;
  }

  public NodeLabelInfo(String name, boolean exclusivity) {
    this.name = name;
    this.exclusivity = exclusivity;
  }

  public NodeLabelInfo(NodeLabel label) {
    this.name = label.getName();
    this.exclusivity = label.isExclusive();
  }

  public NodeLabelInfo(NodeLabel label, PartitionInfo partitionInfo) {
    this(label);
    this.partitionInfo = partitionInfo;
  }

  public String getName() {
    return name;
  }

  public boolean getExclusivity() {
    return exclusivity;
  }

  public PartitionInfo getPartitionInfo() {
    return partitionInfo;
  }

  public Integer getActiveNMs() {
    return activeNMs;
  }

  public void setActiveNMs(Integer activeNMs) {
    this.activeNMs = activeNMs;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setExclusivity(boolean exclusivity) {
    this.exclusivity = exclusivity;
  }

  public void setPartitionInfo(PartitionInfo partitionInfo) {
    this.partitionInfo = partitionInfo;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    NodeLabelInfo other = (NodeLabelInfo) obj;
    if (!getName().equals(other.getName())) {
      return false;
    }
    if (getExclusivity() != other.getExclusivity()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return (getName().hashCode() << 16) + (getExclusivity() ? 1 : 0);
  }
}
