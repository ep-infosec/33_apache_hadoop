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
package org.apache.hadoop.fs.viewfs;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

/**
 * File system instance getter.
 */
@InterfaceAudience.LimitedPrivate({"Common"})
@InterfaceStability.Unstable
public class FsGetter {

  /**
   * Gets new file system instance of given uri.
   * @param uri uri.
   * @param conf configuration.
   * @throws IOException raised on errors performing I/O.
   * @return file system.
   */
  public FileSystem getNewInstance(URI uri, Configuration conf)
      throws IOException {
    return FileSystem.newInstance(uri, conf);
  }

  /**
   * Gets file system instance of given uri.
   *
   * @param uri uri.
   * @param conf configuration.
   * @throws IOException raised on errors performing I/O.
   * @return FileSystem.
   */
  public FileSystem get(URI uri, Configuration conf) throws IOException {
    return FileSystem.get(uri, conf);
  }
}