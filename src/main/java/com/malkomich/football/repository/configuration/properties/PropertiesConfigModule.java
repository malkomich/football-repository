package com.malkomich.football.repository.configuration.properties;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PropertiesConfigModule extends AbstractModule {
    private final Vertx vertx;

    @Override
    protected void configure() {
        bind(PropertiesConfig.class).toInstance(propertyConfig());
    }

    private PropertiesConfig propertyConfig() {
        return new PropertiesConfig(vertx);
    }
}
