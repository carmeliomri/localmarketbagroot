package com.example.localmarketbagroot;
//basic defenition of cart item
public class CartDataModel {
    private final String text;
    private final String imageUrl; // Use String for URL

    public CartDataModel(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

