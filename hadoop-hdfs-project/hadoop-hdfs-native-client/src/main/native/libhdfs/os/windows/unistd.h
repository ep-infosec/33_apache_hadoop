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

#ifndef LIBHDFS_UNISTD_H
#define LIBHDFS_UNISTD_H

/* On Windows, unistd.h does not exist, so manually define what we need. */

#include <process.h> /* Declares getpid(). */
#include <Windows.h>

/* Re-route sleep to Sleep, converting units from seconds to milliseconds. */
#define sleep(seconds) Sleep((seconds) * 1000)
#endif
