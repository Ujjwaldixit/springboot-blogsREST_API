package com.blogs.dto;

import com.sun.istack.NotNull;
import lombok.NonNull;

public class UserDto {
    @NotNull
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String password;
    @NotNull
    private String email;
}
