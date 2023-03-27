package com.example.vmo_project.dto;

import com.example.vmo_project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;

    public UserDto(User entity) {
        this.setId(entity.getId());
        this.username = entity.getUsername();
    }
}
