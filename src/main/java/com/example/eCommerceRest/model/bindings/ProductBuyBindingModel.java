package com.example.eCommerceRest.model.bindings;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


public class ProductBuyBindingModel {

        @NotNull(message = "Quantity buy cannot be null!!!")
        @DecimalMin(value = "1", message = "Тhe quantity buy cannot be a negative value!!!")
        private BigDecimal quantity;

    public ProductBuyBindingModel() {
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
