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

package org.apache.hadoop.yarn.nodelabels;

import java.io.IOException;

/**
 * Interface to capture operations on AttributeValue.
 */
public interface AttributeValue {

  /**
   * @return original value which was set.
   */
  String getValue();

  /**
   * validate the value based on the type and initialize for further compare
   * operations.
   *
   * @param value value.
   * @throws IOException io error occur.
   */
  void validateAndInitializeValue(String value) throws IOException;

  /**
   * compare the value against the other based on the
   * AttributeExpressionOperation.
   *
   * @param other attribute value.
   * @param op attribute expression operation.
   * @return true if value <code>other</code> matches the current value for the
   *         operation <code>op</code>.
   */
  boolean compareForOperation(AttributeValue other,
      AttributeExpressionOperation op);
}
