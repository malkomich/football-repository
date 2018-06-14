package com.malkomich.football.repository.configuration.properties;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.val;

import java.io.File;
import java.util.Objects;

public class PropertiesConfig {
    private static final String FILE = "config.json";
    private static final int SCAN_PERIOD = 2000;
    private ConfigRetriever configRetriever;

    PropertiesConfig(final Vertx vertx) {
        final ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions().setScanPeriod(SCAN_PERIOD);
        fileStore(configRetrieverOptions);
        envStore(configRetrieverOptions);
        configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
    }

    public JsonObject config() {
        return configRetriever.getCachedConfig();
    }

    public void config(final Handler<AsyncResult<JsonObject>> completionHandler) {
        val configJson = configRetriever.getCachedConfig();
        if (configJson != null && !configJson.isEmpty()) {
            completionHandler.handle(Future.succeededFuture(configJson));
            return;
        }
        configRetriever.getConfig(asyncResult ->
            completionHandler.handle(Future.succeededFuture(asyncResult.result())));
    }

    private void fileStore(final ConfigRetrieverOptions configRetrieverOptions) {
        final String route = Objects.requireNonNull(getClass().getClassLoader().getResource(FILE)).getPath();
        if (new File(route).exists()) {
            final ConfigStoreOptions config = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", route));
            configRetrieverOptions.addStore(config);
        }
    }

    private void envStore(final ConfigRetrieverOptions configRetrieverOptions) {
        final ConfigStoreOptions config = new ConfigStoreOptions()
            .setType("env");
        configRetrieverOptions.addStore(config);
    }

}
