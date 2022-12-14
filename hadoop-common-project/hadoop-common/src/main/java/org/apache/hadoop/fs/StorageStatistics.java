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
package org.apache.hadoop.fs;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.fs.statistics.StoreStatisticNames;

import java.util.Iterator;

/**
 * StorageStatistics contains statistics data for a FileSystem or FileContext
 * instance.
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public abstract class StorageStatistics {

  /**
   * These are common statistic names.
   * <p>
   * The following names are considered general and preserved across different
   * StorageStatistics classes. When implementing a new StorageStatistics, it is
   * highly recommended to use the common statistic names.
   * <p>
   * When adding new common statistic name constants, please make them unique.
   * By convention, they are implicitly unique:
   * <ul>
   *   <li>the name of the constants are uppercase, words separated by
   *   underscores.</li>
   *   <li>the value of the constants are lowercase of the constant names.</li>
   * </ul>
   * See {@link StoreStatisticNames} for the field names used here
   * and elsewhere.
   */
  @InterfaceStability.Evolving
  public interface CommonStatisticNames {
    // The following names are for file system operation invocations
    String OP_APPEND = StoreStatisticNames.OP_APPEND;
    String OP_COPY_FROM_LOCAL_FILE =
        StoreStatisticNames.OP_COPY_FROM_LOCAL_FILE;
    String OP_CREATE = StoreStatisticNames.OP_CREATE;
    String OP_CREATE_NON_RECURSIVE =
        StoreStatisticNames.OP_CREATE_NON_RECURSIVE;
    String OP_DELETE = StoreStatisticNames.OP_DELETE;
    String OP_EXISTS = StoreStatisticNames.OP_EXISTS;
    String OP_GET_CONTENT_SUMMARY =
        StoreStatisticNames.OP_GET_CONTENT_SUMMARY;
    String OP_GET_DELEGATION_TOKEN =
        StoreStatisticNames.OP_GET_DELEGATION_TOKEN;
    String OP_GET_FILE_CHECKSUM = StoreStatisticNames.OP_GET_FILE_CHECKSUM;
    String OP_GET_FILE_STATUS = StoreStatisticNames.OP_GET_FILE_STATUS;
    String OP_GET_STATUS = StoreStatisticNames.OP_GET_STATUS;
    String OP_GLOB_STATUS = StoreStatisticNames.OP_GLOB_STATUS;
    String OP_IS_FILE = StoreStatisticNames.OP_IS_FILE;
    String OP_IS_DIRECTORY = StoreStatisticNames.OP_IS_DIRECTORY;
    String OP_LIST_FILES = StoreStatisticNames.OP_LIST_FILES;
    String OP_LIST_LOCATED_STATUS =
        StoreStatisticNames.OP_LIST_LOCATED_STATUS;
    String OP_LIST_STATUS = StoreStatisticNames.OP_LIST_STATUS;
    String OP_MKDIRS = StoreStatisticNames.OP_MKDIRS;
    String OP_MODIFY_ACL_ENTRIES = StoreStatisticNames.OP_MODIFY_ACL_ENTRIES;
    String OP_OPEN = StoreStatisticNames.OP_OPEN;
    String OP_REMOVE_ACL = StoreStatisticNames.OP_REMOVE_ACL;
    String OP_REMOVE_ACL_ENTRIES = StoreStatisticNames.OP_REMOVE_ACL_ENTRIES;
    String OP_REMOVE_DEFAULT_ACL = StoreStatisticNames.OP_REMOVE_DEFAULT_ACL;
    String OP_RENAME = StoreStatisticNames.OP_RENAME;
    String OP_SET_ACL = StoreStatisticNames.OP_SET_ACL;
    String OP_SET_OWNER = StoreStatisticNames.OP_SET_OWNER;
    String OP_SET_PERMISSION = StoreStatisticNames.OP_SET_PERMISSION;
    String OP_SET_TIMES = StoreStatisticNames.OP_SET_TIMES;
    String OP_TRUNCATE = StoreStatisticNames.OP_TRUNCATE;
  }

  /**
   * A 64-bit storage statistic.
   */
  public static class LongStatistic {
    private final String name;
    private final long value;

    public LongStatistic(String name, long value) {
      this.name = name;
      this.value = value;
    }

    /**
     * @return    The name of this statistic.
     */
    public String getName() {
      return name;
    }

    /**
     * @return    The value of this statistic.
     */
    public long getValue() {
      return value;
    }

    @Override
    public String toString() {
      return name + " = " + value;
    }
  }

  private final String name;

  public StorageStatistics(String name) {
    this.name = name;
  }

  /**
   * Get the name of this StorageStatistics object.
   * @return name of this StorageStatistics object
   */
  public String getName() {
    return name;
  }

  /**
   * @return the associated file system scheme if this is scheme specific,
   * else return null.
   */
  public String getScheme() {
    return null;
  }

  /**
   * Get an iterator over all the currently tracked long statistics.
   *
   * The values returned will depend on the type of FileSystem or FileContext
   * object.  The values do not necessarily reflect a snapshot in time.
   *
   * @return LongStatistic Iterator.
   */
  public abstract Iterator<LongStatistic> getLongStatistics();

  /**
   * Get the value of a statistic.
   *
   * @param key key.
   * @return         null if the statistic is not being tracked or is not a
   *                 long statistic. The value of the statistic, otherwise.
   */
  public abstract Long getLong(String key);

  /**
   * Return true if a statistic is being tracked.
   *
   * @param key key.
   * @return         True only if the statistic is being tracked.
   */
  public abstract boolean isTracked(String key);

  /**
   * Reset all the statistic data.
   */
  public abstract void reset();
}
