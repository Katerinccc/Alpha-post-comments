package com.posada.santiago.alphapostsandcomments.application.generic.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String userEmail;
    private String password;

    @Builder.Default()
    private boolean active = true;
    @Builder.Default()
    private List<String> roles = new ArrayList<>();




}
