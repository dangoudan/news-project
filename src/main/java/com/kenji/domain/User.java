package com.kenji.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String account;
    private String password;
    private String email;
    private String picUrl;
    private int status;
    private int ctime;

}
