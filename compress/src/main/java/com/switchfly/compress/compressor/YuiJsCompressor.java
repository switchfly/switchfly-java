package com.switchfly.compress.compressor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.apache.log4j.Logger;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

public class YuiJsCompressor implements AssetCompressor {
    private static final Logger _logger = Logger.getLogger(YuiJsCompressor.class);

    @Override
    public String compress(String source, String name) throws IOException {

        StringReader input = new StringReader(source);

        JavaScriptCompressor javaScriptCompressor = new JavaScriptCompressor(input, new JsErrorReporter());

        StringWriter out = new StringWriter();

        javaScriptCompressor.compress(out, -1, true, false, false, false);

        return out.toString();
    }

    private static class JsErrorReporter implements ErrorReporter {
        @Override
        public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
            _logger.warn(createErrorMessage(message, sourceName, line, lineSource));
        }

        @Override
        public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
            _logger.error(createErrorMessage(message, sourceName, line, lineSource));
        }

        @Override
        public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
            _logger.error(createErrorMessage(message, sourceName, line, lineSource));
            return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
        }

        private String createErrorMessage(String message, String sourceName, int line, String lineSource) {
            return message + " in " + sourceName + " at " + line + ". Source: " + lineSource;
        }
    }
}
