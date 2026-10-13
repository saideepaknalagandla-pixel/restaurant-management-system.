package com.restaurant.model;

import java.math.BigDecimal;

public class CartItem {
    private final MenuItem item;
    private int quantity;
    public CartItem(MenuItem item, int quantity){this.item=item;this.quantity=quantity;}
    public MenuItem getItem(){return item;}
    public int getQuantity(){return quantity;}
    public void setQuantity(int quantity){this.quantity=quantity;}
    public BigDecimal getSubtotal(){return item.getPrice().multiply(BigDecimal.valueOf(quantity));}
}