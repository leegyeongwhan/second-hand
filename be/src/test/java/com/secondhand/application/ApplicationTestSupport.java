package com.secondhand.application;

import com.secondhand.domain.image.S3Uploader;
import com.secondhand.testcontainers.SupportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ApplicationTest
public abstract class ApplicationTestSupport{

    @MockBean
    protected S3Uploader s3Uploader;

    @Autowired
    protected SupportRepository supportRepository;
}

