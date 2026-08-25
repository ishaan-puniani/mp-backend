package com.mp.be.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(BrevoConfig.class)
public class BrevoConfigTest {

    @Autowired
    private BrevoConfig brevoConfig;

    @Test
    public void contextLoads() {
        assertThat(brevoConfig).isNotNull();
    }
} 