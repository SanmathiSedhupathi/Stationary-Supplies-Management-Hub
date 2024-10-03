/*package com.ShanStationaries;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Users {
    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(String username, String password, String role) {
        if (validatePassword(password)) {
            userList.add(new User(username, password, role));
        } else {
            throw new IllegalArgumentException("Password does not meet complexity requirements.");
        }
    }

    private boolean validatePassword(String password) {
        
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(password).matches();
    }
}*/

package com.ShanStationaries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class Users {

    // Add a user to the database
    public void addUser(String username, String password, String role) throws SQLException {
        if (validatePassword(password)) {
            try (Connection con = new DBUtil().getDBConnection();
                 PreparedStatement pstmt = con.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
                 
                pstmt.setString(1, username);
                pstmt.setString(2, password); 
                pstmt.setString(3, role);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("User added successfully.");
                }
            }
        } else {
            throw new IllegalArgumentException("Password does not meet complexity requirements.");
        }
    }

    // Retrieve a user from the database based on the username
    public User getUserByUsername(String username) throws SQLException {
        User user = null;
        try (Connection con = new DBUtil().getDBConnection();
             PreparedStatement pstmt = con.prepareStatement("SELECT * FROM users WHERE username = ?")) {
             
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                String dbRole = rs.getString("role");

                user = new User(dbUsername, dbPassword, dbRole);
            }
        }
        return user;
    }

    // Validate password complexity
    private boolean validatePassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(password).matches();
    }
}
