package com.Main;

import com.ShanStationaries.*;

import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Users users = new Users();
        Inventory inventory = new Inventory();
        Login login = new Login();
        
        int userRole = 0; // 0 = None, 1 = Admin, 2 = User
        String currentUsername = "";

        while (true) {
            System.out.println("1. Register \n2. Login\n3. Exit");
            int choice = getIntInput(sc);

            if (choice == 1) {
                // Registration
                System.out.println("Enter Your Details: Username, Password, Role (admin/user)");
                String username = sc.nextLine();
                String password = sc.nextLine();
                String role = sc.nextLine();

                try {
                    users.addUser(username, password, role);
                    System.out.println("Registration Successful!");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else if (choice == 2) {
                // Login
                System.out.println("Enter Your Details: Username, Password");
                String username = sc.nextLine();
                String password = sc.nextLine();
                
                int loginResult = login.validate(username, password, users);
                
                if (loginResult == 1) {
                    System.out.println("Welcome Admin!");
                    userRole = 1;
                    currentUsername = username;
                } else if (loginResult == 2) {
                    System.out.println("Welcome User!");
                    userRole = 2;
                    currentUsername = username;
                } else {
                    System.out.println("Invalid Credentials or User Not Found.");
                    userRole = 0;
                }

                while (userRole != 0) {
                    if (userRole == 1) {
                        System.out.println("\n1. Add Item \n2. Update Item\n3. Remove Item\n4. Search Item\n5. Show Items\n6. Logout");
                        int adminChoice = getIntInput(sc);

                        switch (adminChoice) {
                            case 1:
                                // Add Item
                                System.out.println("Enter Item Details: ItemNo, Name, Price, Brand, Dealer, Quantity");
                                int itemNo = getIntInput(sc);
                                String name = sc.nextLine();
                                int price = getIntInput(sc);
                                String brand = sc.nextLine();
                                String dealer = sc.nextLine();
                                int quantity = getIntInput(sc);

                                Details newItem = new Details(itemNo, name, price, brand, dealer, quantity);
                                inventory.add(newItem);
                                break;

                            case 2:
                                // Update Item
                                System.out.println("Enter ItemNo to Update: ");
                                itemNo = getIntInput(sc);
                                
                                System.out.println("Enter Field to Update (ItemNo/Name/Price/Brand/Dealer): ");
                                String fieldToUpdate = sc.nextLine();
                                System.out.println("Enter New Value: ");
                                String newValue = sc.nextLine();

                                inventory.update(itemNo, fieldToUpdate, newValue);
                                break;

                            case 3:
                                // Remove Item
                                System.out.println("1. Clear The List \n2. Remove Specific Item");
                                int removeChoice = getIntInput(sc);

                                if (removeChoice == 1) {
                                    inventory.remove(-1); 
                                } else {
                                    System.out.println("Enter ItemNo to Remove: ");
                                    int idToRemove = getIntInput(sc);
                                    inventory.remove(idToRemove);
                                }
                                break;

                            case 4:
                                // Search Item
                                System.out.println("Enter ItemNo to Search: ");
                                int idToSearch = getIntInput(sc);
                                System.out.println(inventory.search(idToSearch));
                                break;

                            case 5:
                                // Show Items
                                inventory.display(null, "admin");
                                break;

                            case 6:
                                // Logout
                                userRole = 0;
                                currentUsername = "";
                                System.out.println("Logged out successfully.");
                                break;

                            default:
                                System.out.println("Invalid Option");
                        }
                    } else if (userRole == 2) {
                        // User options
                        System.out.println("\n1. View Items \n2. Order Item \n3. Logout");
                        int userChoice = getIntInput(sc);

                        switch (userChoice) {
                            case 1:
                                // View Items
                            	System.out.println("Sort by: \n1. Name \n2. Price \n3. No Sort");
                                int sortChoice = sc.nextInt();
                                sc.nextLine(); 

                                List<Details> itemList = inventory.getProductList(); 
                                switch (sortChoice) {
                                    case 1:
                                    	 inventory.sortByName();// Sort by name
                                        break;
                                    case 2:
                                    	inventory.sortByPrice();  // Sort by price
                                        break;
                                    case 3:
                                        // No sorting required
                                        break;
                                    default:
                                        System.out.println("Invalid Option");
                                }
                                inventory.display("user");
                                break;

                            case 2:
                                // Order Item
                                System.out.println("Enter Item Name and Quantity: ");
                                String itemName = sc.nextLine();
                                int orderQty = getIntInput(sc);
                                inventory.order(itemName, orderQty);
                                break;

                            case 3:
                                // Logout
                                userRole = 0;
                                currentUsername = "";
                                System.out.println("Logged out successfully.");
                                break;

                            default:
                                System.out.println("Invalid Option");
                        }
                    }
                }
            
            } else if (choice == 3) {
                // Exit
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid Option");
            }
        }
        sc.close();
    }

    // Method for reading integer input
    public static int getIntInput(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
