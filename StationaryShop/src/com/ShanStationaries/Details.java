package com.ShanStationaries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Details implements Comparable<Details> {
    private int ItemNo;
    private String Name;
    private int Price;
    private String Brand;
    private String Dealer_Id;
    private String Dealer_Name;
    private int Quantity;

    // Getters and Setters for all fields
    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getItemNo() {
        return ItemNo;
    }

    public void setItemNo(int itemNo) {
        ItemNo = itemNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getDealer_Id() {
        return Dealer_Id;
    }

    public void setDealer_Id(String dealer_Id) {
        Dealer_Id = dealer_Id;
    }

    public String getDealer_Name() {
        return Dealer_Name;
    }

    public void setDealer_Name(String dealer_Name) {
        Dealer_Name = dealer_Name;
    }

    // Method to reduce quantity
    public void reduceQuantity(int amount) throws QuantityUnavailableException {
        if (Quantity >= amount) {
            Quantity -= amount;
        } else {
            throw new QuantityUnavailableException("Requested quantity exceeds available quantity.");
        }
    }

    // Constructor with parameters
    public Details(int itemNo, String name, int price, String brand, String dealer_Name, int quantity) {
        this.ItemNo = itemNo;
        this.Name = name;
        this.Price = price;
        this.Brand = brand;
        this.Dealer_Name = dealer_Name;
        this.Quantity = quantity;
        this.Dealer_Id = null; // Will be generated dynamically later if needed
    }

    // Default constructor
    public Details() {
    }

    // Overriding toString method for better display
    @Override
    public String toString() {
        return "Details [ItemNo=" + ItemNo + ", Name=" + Name + ", Price=" + Price + ", Brand=" + Brand
                + ", Dealer_Id=" + Dealer_Id + ", Dealer_Name=" + Dealer_Name + ", Quantity=" + Quantity + "]";
    }

    // Overriding compareTo for sorting by name
    @Override
    public int compareTo(Details d) {
        return this.Name.compareTo(d.Name);
    }
}
