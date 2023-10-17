package com.secondhand.documentation;

import com.secondhand.web.controller.*;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest({
        MemberController.class,
        CategoryController.class,
        ChatRoomController.class,
        ProductController.class,
        TownController.class,
})
@AutoConfigureRestDocs
public @interface DocumentationTest {
}

