#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Default LIBHDFSPP_DIR to the default install location.  You can override
#    it by add -DLIBHDFSPP_DIR=... to your cmake invocation
set(LIBHDFSPP_DIR CACHE STRING ${CMAKE_INSTALL_PREFIX})

include_directories( ${LIBHDFSPP_DIR}/include ../../lib )
link_directories( ${LIBHDFSPP_DIR}/lib )

add_executable(cat_c cat.c)
target_link_libraries(cat_c hdfspp_static uriparser2)