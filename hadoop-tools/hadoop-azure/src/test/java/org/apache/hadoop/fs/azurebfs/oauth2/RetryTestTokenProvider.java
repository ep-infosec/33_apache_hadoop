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
package org.apache.hadoop.fs.azurebfs.oauth2;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.azurebfs.extensions.CustomTokenProviderAdaptee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Token provider which should throw exception and trigger retries
 */
public class RetryTestTokenProvider implements CustomTokenProviderAdaptee {

  private static final Logger LOG = LoggerFactory.getLogger(
      RetryTestTokenProvider.class);

  // Need to track first token fetch otherwise will get counted as a retry too.
  private boolean isThisFirstTokenFetch = true;
  private int retryCount = 0;

  @Override
  public void initialize(Configuration configuration, String accountName)
      throws IOException {

  }

  /**
   * Clear earlier retry details and reset RetryTestTokenProvider instance to
   * state of first access token fetch call.
   */
  public void resetStatusToFirstTokenFetch() {
    isThisFirstTokenFetch = true;
    retryCount = 0;
  }

  @Override
  public String getAccessToken() throws IOException {
    if (isThisFirstTokenFetch) {
      isThisFirstTokenFetch = false;
    } else {
      retryCount++;
    }

    LOG.debug("RetryTestTokenProvider: Throw an exception in fetching tokens");
    throw new IOException("test exception");
  }

  @Override
  public Date getExpiryTime() {
    return new Date();
  }

  public static RetryTestTokenProvider getCurrentRetryTestProviderInstance(
      AccessTokenProvider customTokenProvider) {
    return (RetryTestTokenProvider) ((CustomTokenProviderAdapter) customTokenProvider).getCustomTokenProviderAdaptee();
  }

  public int getRetryCount() {
    return retryCount;
  }
}
