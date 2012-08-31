/*
 * Copyright 2012 Switchfly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.switchfly.inputvalidation;

import com.google.sitebricks.client.Transport;
import com.google.sitebricks.headless.Request;

public class ParameterRequestRead<E> implements Request.RequestRead<E> {

    private final Request.RequestRead<E> _requestRead;

    public ParameterRequestRead(Request.RequestRead<E> requestRead) {
        _requestRead = requestRead;
    }

    @Override
    public E as(Class<? extends Transport> transport) {
        return _requestRead.as(transport);
    }

    public Parameter<E> from(Class<? extends Transport> transport) {
        E entity = as(transport);
        return new Parameter<E>("request", entity);
    }
}