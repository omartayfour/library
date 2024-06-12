package com.vodafone.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;


@Component
public class SecurityConfig {

    public static String encodePassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

}
