package com.example.localmarketbagroot;

public class ProductDB {
    private String name;
    private String category;
    private String url;
    private int price;
    //define products table in database
    public ProductDB() {
        // Default constructor required for calls to DataSnapshot.getValue(ProductDB.class)
    }

    public ProductDB(String name, String category, String url, int price)
    {
      this.name = name;
      this.category = category;
      this.url = url;
      this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public int getPrice() {
        return price;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }
    public void setPrice(int price)
    {
        this.price = price;
    }
}
