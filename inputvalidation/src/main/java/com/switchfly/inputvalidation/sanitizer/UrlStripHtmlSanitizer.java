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

package com.switchfly.inputvalidation.sanitizer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import com.switchfly.inputvalidation.ValidationStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class UrlStripHtmlSanitizer implements Sanitizer<String> {

    private static final String ENCODING = "UTF-8";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";

    @Override
    public String execute(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }

        URI uri;
        try {
            uri = new URI(content);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<NameValuePair> cleanedPairs = parse(uri);

        String queryString = cleanedPairs.isEmpty() ? null : URLEncodedUtils.format(cleanedPairs, ENCODING);

        try {
            return URIUtils.createURI(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath(), queryString, uri.getFragment()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<NameValuePair> parse(URI uri) {
        final String query = uri.getRawQuery();
        return parse(query);
    }

    public static List<NameValuePair> parse(String queryString) {
        List<NameValuePair> result = Collections.emptyList();
        if (queryString != null && queryString.length() > 0) {
            result = new ArrayList<NameValuePair>();
            parse(result, new Scanner(queryString));
        }
        return result;
    }

    private static void parse(List<NameValuePair> parameters, Scanner scanner) {
        scanner.useDelimiter(PARAMETER_SEPARATOR);
        while (scanner.hasNext()) {
            String[] nameValue = scanner.next().split(NAME_VALUE_SEPARATOR, 2);
            if (nameValue.length == 0 || nameValue.length > 2) {
                throw new IllegalArgumentException("bad parameter");
            }
            String name = ValidationStrategy.PROPERTY_NAME.cleanStrategy().validate(nameValue[0]);
            String value = null;
            if (nameValue.length == 2) {
                value = ValidationStrategy.DEFAULT_WEB_CONTENT_VALIDATION.validate(nameValue[1]);
            }
            parameters.add(new BasicNameValuePair(name, value));
        }
    }
}
