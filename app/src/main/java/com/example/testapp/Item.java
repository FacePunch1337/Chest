package com.example.testapp;

public class Item {
    private String name;
    private String imageUrl;
    private String itemClass;
    private String rarity;
    private String properties;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }


    public void showItem(){
        System.out.println("Name: " + getName());
        System.out.println("Image URL: " + getImageUrl());
        System.out.println("Item Class: " + getItemClass());
        System.out.println("Rarity: " + getRarity());
        System.out.println("Properties: " + getProperties());
        System.out.println("Description: " + getDescription());
    }
}


