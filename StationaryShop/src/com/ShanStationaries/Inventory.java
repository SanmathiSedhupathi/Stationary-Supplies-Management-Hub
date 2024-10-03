/*package com.ShanStationaries;

import java.util.*;

public class Inventory {
    Scanner sc = new Scanner(System.in);
    List<Details> prdt_lst = new ArrayList<>();

    // Adding items
    public void add(Details det) {
        prdt_lst.add(det);
        System.out.println("Item Added Successfully !!");
    }

    // Display the Items
    public void display(Integer id, String role) {
        if (id != null) {
            for (Details d : prdt_lst) {
                if (d.getItemNo() == id) {
                    if ("admin".equalsIgnoreCase(role)) {
                        System.out.println(d);
                    } else {
                        System.out.println("Details [ItemNo=" + d.getItemNo() + ", Name=" + d.getName() + ", Price=" + d.getPrice() + "]");
                    }
                }
            }
        } else {
            for (Details d : prdt_lst) {
                if ("admin".equalsIgnoreCase(role)) {
                    System.out.println(d);
                } else {
                    System.out.println("Details [ItemNo=" + d.getItemNo() + ", Name=" + d.getName() + ", Price=" + d.getPrice() + "]");
                }
            }
        }
    }

    // Searching the element
    public Details search(int id) {
        for (Details d : prdt_lst) {
            if (d.getItemNo() == id)
                return d;
        }
        return null;
    }

    // Removing an element
    public void remove(int id) {
        int flag = 0;
        if (id == -1) {
            prdt_lst.clear();
            System.out.println("Cleared List Successfully!!!");
        } else {
            Iterator<Details> iterator = prdt_lst.iterator();
            while (iterator.hasNext()) {
                Details d = iterator.next();
                if (d.getItemNo() == id) {
                    iterator.remove();
                    flag = 1;
                    System.out.println("Deleted Successfully !!");
                    break;
                }
            }
            if (flag == 0)
                System.out.println("No such record ");
        }
    }

    // Updating the element
    public void update(int id, String field, List<?> args) {
        int flag = 0;
        Details d = this.search(id);
        if (d != null) {
            if (field.equals("ItemNo"))
                d.setItemNo((int) args.get(0));
            else if (field.equals("Name"))
                d.setName((String) args.get(0));
            else if (field.equals("Price"))
                d.setPrice((int) args.get(0));
            else if (field.equals("Brand"))
                d.setBrand((String) args.get(0));
            else if (field.equals("Dealer")) {
                d.setDealer_Name((String) args.get(0));
                d.setDealer_Id(generateDealerId(d.getDealer_Name())); // Ensure dealer ID is set
            } else {
                System.out.println("No Such Field");
                flag = 1;
            }

            if (flag == 0) {
                System.out.println("Updated Successfully");
                this.display(null, "admin"); // Assuming admin view for update
            }
        } else {
            System.out.println("No Such Id");
        }
    }

    // Ordering items
    public void order(String name, int qty) {
        boolean itemFound = false;

        for (Details d : prdt_lst) {
            if (d.getName().equalsIgnoreCase(name)) {
                itemFound = true;
                try {
                    d.reduceQuantity(qty);
                    System.out.println("Order placed successfully.");
                    generateBill(d, qty); 
                } catch (QuantityUnavailableException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
        }

        if (!itemFound) {
            System.out.println("No item found with the name: " + name);
        }
    }

    private String generateDealerId(String dealerName) {
        Random random = new Random();
        int randomNum = random.nextInt(1000); 
        String dealerPrefix = dealerName.length() >= 2 ? dealerName.substring(0, 2).toUpperCase() : dealerName.toUpperCase();
        return dealerPrefix + String.format("%03d", randomNum); 
    }

    // Bill generation
    public void generateBill(Details item, int qty) {
        int totalPrice = item.getPrice() * qty;

        // Bill Header
        System.out.println("--------------------------------------------------");
        System.out.println("                   BILL                           ");
        System.out.println("--------------------------------------------------");

        // Item Details
        System.out.printf("%-15s %-20s %10s %10s%n", "Item No", "Item Name", "Qty", "Price");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-15d %-20s %10d %10d%n", item.getItemNo(), item.getName(), qty, item.getPrice());
        System.out.println("--------------------------------------------------");

        // Total Price
        System.out.printf("%-35s %10s %10d%n", "Total Price", "", totalPrice);

        System.out.println("          Thank you for shopping with us!         ");
    }

    public List<Details> getProductList() {
        return prdt_lst;
    }
}*/
package com.ShanStationaries;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Inventory {
    Connection con;
    
    public Inventory() {
        con = new DBUtil().getDBConnection(); // Assume DBUtil is handling the connection
    }

    // Adding items
    public void add(Details det) {
        String query = "INSERT INTO products (ItemNo, Name, Price, Brand, Dealer_Name, Dealer_Id, Quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, det.getItemNo());
            pstmt.setString(2, det.getName());
            pstmt.setInt(3, det.getPrice());
            pstmt.setString(4, det.getBrand());
            pstmt.setString(5, det.getDealer_Name());
            pstmt.setString(6, det.getDealer_Id());
            pstmt.setInt(7, det.getQuantity());

            pstmt.executeUpdate();
            System.out.println("Item Added Successfully!!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display the Items
    public void display(Integer id, String role) {
        String query = (id != null) ? "SELECT * FROM products WHERE ItemNo = ?" : "SELECT * FROM products";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            if (id != null) {
                pstmt.setInt(1, id);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if ("admin".equalsIgnoreCase(role)) {
                    System.out.printf("ItemNo=%d, Name=%s, Price=%d, Brand=%s, Dealer_Name=%s, Quantity=%d%n",
                            rs.getInt("ItemNo"), rs.getString("Name"), rs.getInt("Price"),
                            rs.getString("Brand"), rs.getString("Dealer_Name"), rs.getInt("Quantity"));
                } else {
                    System.out.printf("ItemNo=%d, Name=%s, Price=%d%n",
                            rs.getInt("ItemNo"), rs.getString("Name"), rs.getInt("Price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Searching the element
    public Details search(int id) {
        String query = "SELECT * FROM products WHERE ItemNo = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Details(rs.getInt("ItemNo"), rs.getString("Name"), rs.getInt("Price"),
                        rs.getString("Brand"), rs.getString("Dealer_Name"), rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Removing an element
    public void remove(int id) {
        String query = (id == -1) ? "DELETE FROM products" : "DELETE FROM products WHERE ItemNo = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            if (id != -1) {
                pstmt.setInt(1, id);
            }
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deleted Successfully!!");
            } else {
                System.out.println("No such record.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updating the element
    public void update(int id, String field, Object newValue) {
        // Validating field to prevent SQL injection
        if (!isValidField(field)) {
            System.out.println("Invalid field specified for update.");
            return;
        }

        String query = "UPDATE products SET " + field + " = ? WHERE ItemNo = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            // Debugging output
            System.out.println("Updating field: " + field + " with new value: " + newValue + " of type: " + newValue.getClass().getSimpleName());

            // Setting the parameter based on type
            if (newValue instanceof Integer) {
                pstmt.setInt(1, (Integer) newValue);
            } else if (newValue instanceof String) {
                pstmt.setString(1, (String) newValue);
            } else {
                System.out.println("Unsupported data type for newValue.");
                return;
            }
            pstmt.setInt(2, id); // Set the ItemNo parameter
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Updated Successfully!!");
            } else {
                System.out.println("No such Id.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidField(String field) {
        // List of valid fields for update
        return field.equals("Name") || field.equals("Price") || field.equals("Brand") || 
               field.equals("Dealer_Name") || field.equals("Dealer_Id") || field.equals("Quantity");
    }

    // Ordering items
    public void order(String name, int qty) {
        String query = "SELECT * FROM products WHERE Name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int availableQty = rs.getInt("Quantity");
                if (availableQty >= qty) {
                    int newQty = availableQty - qty;
                    update(rs.getInt("ItemNo"), "Quantity", newQty);
                    System.out.println("Order placed successfully.");
                    generateBill(rs.getInt("ItemNo"), rs.getString("Name"), rs.getInt("Price"), qty);
                } else {
                    throw new QuantityUnavailableException("Quantity unavailable for item: " + name);
                }
            } else {
                System.out.println("No item found with the name: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (QuantityUnavailableException e) {
            System.out.println(e.getMessage());
        }
    }

    // Bill generation
    public void generateBill(int itemNo, String name, int price, int qty) {
        int totalPrice = price * qty;

        System.out.println("--------------------------------------------------");
        System.out.println("                   BILL                           ");
        System.out.println("--------------------------------------------------");

        System.out.printf("%-15s %-20s %10s %10s%n", "Item No", "Item Name", "Qty", "Price");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-15d %-20s %10d %10d%n", itemNo, name, qty, price);
        System.out.println("--------------------------------------------------");
        System.out.printf("%-35s %10s %10d%n", "Total Price", "", totalPrice);

        System.out.println("          Thank you for shopping with us!         ");
    }

    // Method to sort items by name
    public void sortByName() {
        Collections.sort(getProductList(), new Comparator<Details>() {
            public int compare(Details d1, Details d2) {
                return d1.getName().compareTo(d2.getName());
            }
        });
        
    }

    // Method to sort items by price
    public void sortByPrice() {
        Collections.sort(getProductList(), new Comparator<Details>() {
            public int compare(Details d1, Details d2) {
                return Integer.compare(d1.getPrice(), d2.getPrice());
            }
        });
    }

    // Method to display items
    public void display(String role) {
        for (Details item : getProductList()) {
            System.out.println(item);
        }
    }
    
    public List<Details> getProductList() {
        List<Details> prdt_lst = new ArrayList<>();
        String query = "SELECT ItemNo, Name, Price, Brand, Dealer_Name,Dealer_Id, Quantity FROM products"; 
        try (PreparedStatement preparedStatement = con.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int itemNo = resultSet.getInt("ItemNo");
                String name = resultSet.getString("Name");
                int price = resultSet.getInt("Price");
                String brand = resultSet.getString("Brand");
                String dealer = resultSet.getString("Dealer_Name");
                int quantity = resultSet.getInt("Quantity");

                Details detail = new Details(itemNo, name, price, brand, dealer, quantity);
                prdt_lst.add(detail);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product list: " + e.getMessage());
        }

        return prdt_lst;
    }
}


