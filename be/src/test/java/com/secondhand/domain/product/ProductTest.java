package com.secondhand.domain.product;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.javax.validation.plugin.JavaxValidationPlugin;
import com.secondhand.domain.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    Product product;

    public static FixtureMonkey fixtureMonkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .plugin(new JavaxValidationPlugin()) // or new JavaxValidationPlugin()
                .defaultNotNull(true)
                .build();
    }

    @BeforeEach
    void init() {
        product = fixtureMonkey().giveMeBuilder(Product.class)
                .set("title", "Origin Title")
                .set("contents", "Origin contents")
                .sample();
    }

    @Test
    @DisplayName("")
    void update() {
    }

}
