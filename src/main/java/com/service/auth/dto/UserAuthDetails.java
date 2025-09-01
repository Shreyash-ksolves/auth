package com.service.auth.dto;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserAuthDetails {
    private String username;
    private String role;
}
