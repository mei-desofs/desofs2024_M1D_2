package com.isep.desofs;

import com.isep.desofs.config.AsyncSyncConfiguration;
import com.isep.desofs.config.EmbeddedRedis;
import com.isep.desofs.config.EmbeddedSQL;
import com.isep.desofs.config.JacksonConfiguration;
import com.isep.desofs.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { OnlineShopApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedRedis
@EmbeddedSQL
public @interface IntegrationTest {
}
