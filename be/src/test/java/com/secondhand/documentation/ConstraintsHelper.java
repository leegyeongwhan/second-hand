package com.secondhand.documentation;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.stream.Collectors;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public class ConstraintsHelper {

    private static ConstraintDescriptions simpleRequestConstraints;

    public static <T> FieldDescriptor withPath(String path, Class<T> classType) {
        simpleRequestConstraints = new ConstraintDescriptions(classType);

        return fieldWithPath(path)
                .attributes(
                        key("constraints").value(simpleRequestConstraints.descriptionsForProperty(path)
                                .stream()
                                .collect(Collectors.joining("\n\n"))));
    }
}

