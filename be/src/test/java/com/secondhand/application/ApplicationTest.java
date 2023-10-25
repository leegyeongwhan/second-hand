package com.secondhand.application;

import com.secondhand.testcontainers.DatabaseInitializerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(DatabaseInitializerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public @interface ApplicationTest {
}
