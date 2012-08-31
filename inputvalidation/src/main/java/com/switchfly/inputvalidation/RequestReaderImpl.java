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

import java.io.IOException;
import java.io.OutputStream;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.google.sitebricks.headless.Request;

@RequestScoped
public class RequestReaderImpl implements RequestReader {

    private final Request _request;

    @Inject
    RequestReaderImpl(Request request) {
        _request = request;
    }

    @Override
    public RequestParameter read(String name) {
        return new RequestParameter(name, _request.param(name));
    }

    @Override
    public <E> ParameterRequestRead<E> read(Class<E> type) {
        RequestRead<E> requestRead = _request.read(type);
        return new ParameterRequestRead<E>(requestRead);
    }

    @Override
    public void readTo(OutputStream out) throws IOException {
        _request.readTo(out);
    }

    @Override
    public <E> AsyncRequestRead<E> readAsync(Class<E> type) {
        return _request.readAsync(type);
    }

    @Override
    public Multimap<String, String> headers() {
        return _request.headers();
    }

    @Override
    public Multimap<String, String> params() {
        return _request.params();
    }

    @Override
    public Multimap<String, String> matrix() {
        return _request.matrix();
    }

    @Override
    public String matrixParam(String name) {
        return _request.matrixParam(name);
    }

    @Override
    public String param(String name) {
        return _request.param(name);
    }

    @Override
    public String header(String name) {
        return _request.header(name);
    }

    @Override
    public String uri() {
        return _request.uri();
    }

    @Override
    public String path() {
        return _request.path();
    }

    @Override
    public String context() {
        return _request.context();
    }

    @Override
    public String method() {
        return _request.method();
    }
}