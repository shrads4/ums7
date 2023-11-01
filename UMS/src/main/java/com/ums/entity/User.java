package com.ums.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
public class User extends Base {
    @NotEmpty(message = "Username is required")
    private String userName;

    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Mobile number is required")
    private Number mobileNumber;

    private Boolean isDelete = false;
}
