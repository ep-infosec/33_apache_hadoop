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

package org.apache.hadoop.fs.s3a;

import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.ArrayList;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.MultipartUploadListing;
import com.amazonaws.services.s3.model.Region;

/**
 * An {@link S3ClientFactory} that returns Mockito mocks of the {@link AmazonS3}
 * interface suitable for unit testing.
 */
@SuppressWarnings("deprecation")
public class MockS3ClientFactory implements S3ClientFactory {

  @Override
  public AmazonS3 createS3Client(URI uri,
      final S3ClientCreationParameters parameters) {
    AmazonS3 s3 = mock(AmazonS3.class);
    String bucket = uri.getHost();
    when(s3.doesBucketExist(bucket)).thenReturn(true);
    when(s3.doesBucketExistV2(bucket)).thenReturn(true);
    // this listing is used in startup if purging is enabled, so
    // return a stub value
    MultipartUploadListing noUploads = new MultipartUploadListing();
    noUploads.setMultipartUploads(new ArrayList<>(0));
    when(s3.listMultipartUploads(any()))
        .thenReturn(noUploads);
    when(s3.getBucketLocation(anyString()))
        .thenReturn(Region.US_West.toString());
    return s3;
  }
}
