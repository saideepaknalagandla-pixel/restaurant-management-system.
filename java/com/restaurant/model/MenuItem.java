package com.restaurant.model;

import java.math.BigDecimal;

public class MenuItem {
    private final int id;
    private final String name, description, category, imageUrl;
    private final BigDecimal price;

    public MenuItem(int id, String name, String description, String category, BigDecimal price, String imageUrl) {
        this.id=id; this.name=name; this.description=description; this.category=category; this.price=price; this.imageUrl=imageUrl;
    }
    public int getId(){return id;} public String getName(){return name;}
    public String getDescription(){return description;} public String getCategory(){return category;}
    public BigDecimal getPrice(){return price;} public String getImageUrl(){return imageUrl;}
}