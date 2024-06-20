package io.dynamic.threadpool.registry.config;

import io.dynamic.threadpool.registry.core.BaseInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RegistryConfiguration {

    @Autowired
    private BaseInstanceRegistry baseInstanceRegistry;

    @PostConstruct
    public void registryInit() {
        baseInstanceRegistry.postInit();
    }

}
