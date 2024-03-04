package com.ums.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order extends Base {

    @NotNull
    @Size(min = 2, max = 20, message = "orderName length must be between 2 and 20 characters.")
    @NotEmpty(message = "Username is required")
    private String orderName;

    @NotNull
    @Column(unique = true)
    private String orderCode;

    private String orderDescription;

    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = true)
    private Order productType;



//    public Order (String orderName, String orderCode, String orderDescription, Order productType) {
//        this.OrderName = orderName;
//        this.OrderCode = orderCode;
//        this.Orderdescription = orderDescription;
//        this.productType = productType;
//    }

    // Getter and setter for 'name' field
//    public String getOrderName() {
//        return toTitleCase(orderName);
//    }
//
//    public void setOrderName(String module) {
//        this.orderName = module.toLowerCase();
//    }

    private String toTitleCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}

