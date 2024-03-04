package com.ums.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Product")
//@Getter
//@Setter
public class Product extends Base {
    @NotEmpty(message = "Product name is required")
    private String productName;

    @NotEmpty(message = "Product type is required")
    private String productType;

    @NotNull(message = "Product Id is required")
    private Product productId;

    private Boolean isDelete = false;
}
