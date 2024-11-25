package io.dynamic.threadpool.starter.core;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * Config empty analyzer.
 */
public class ConfigEmptyAnalyzer extends AbstractFailureAnalyzer<ConfigEmptyException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, ConfigEmptyException cause) {
        return new FailureAnalysis(cause.getDescription(), cause.getAction(), cause);
    }

}
