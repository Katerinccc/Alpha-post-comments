package com.posada.santiago.alphapostsandcomments.application.generic.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {

    private String userEmail;
    private String password;

}
