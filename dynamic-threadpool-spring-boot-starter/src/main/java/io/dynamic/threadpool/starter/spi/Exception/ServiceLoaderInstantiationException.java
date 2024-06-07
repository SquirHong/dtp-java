package io.dynamic.threadpool.starter.spi.Exception;

/**
 * Service loader instantiation exception.
 */
public class ServiceLoaderInstantiationException extends RuntimeException {

    public ServiceLoaderInstantiationException(final Class<?> clazz, final Exception cause) {
        super(String.format("Can not find public default constructor for SPI class `%s`", clazz.getName()), cause);
    }

}

