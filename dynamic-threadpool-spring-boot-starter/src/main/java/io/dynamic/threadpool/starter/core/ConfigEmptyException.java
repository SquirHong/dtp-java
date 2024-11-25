package io.dynamic.threadpool.starter.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Config empty exception.
 */
@Data
@AllArgsConstructor
public class ConfigEmptyException extends RuntimeException {

    private String description;

    private String action;

}
