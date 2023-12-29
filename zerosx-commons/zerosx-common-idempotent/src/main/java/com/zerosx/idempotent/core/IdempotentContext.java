/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zerosx.idempotent.core;

import com.zerosx.idempotent.anno.Idempotent;
import com.zerosx.idempotent.enums.IdempotentTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 幂等参数包装
 */
@Setter
@Getter
public final class IdempotentContext {

    /**
     * 幂等注解
     */
    private Idempotent idempotent;

    /**
     * 锁标识，{@link IdempotentTypeEnum#PARAM}
     */
    private String lockKey;

    /**
     * 请求路径
     */
    private String servletPath;

    /**
     * 请求参数
     */
    private String requestParam;

}
