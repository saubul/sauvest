package ru.sauvest.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String name;

    private String surname;

    public String ssoToken;

}
