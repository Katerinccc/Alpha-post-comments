package com.posada.santiago.alphapostsandcomments.application.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JwtProvider {

    private String secretKey = "my-new-super-secret-key-for-alpha";

    private long validTime= 1000 * 60 * 60;


}
