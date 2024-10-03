/*package com.ShanStationaries;

public class Login {

    public int validate(String uname, String psswd, Users us) {
       
        for (User user : us.getUserList()) {
            if (user.getUsername().equals(uname)) {
              
                if (user.getPassword().equals(psswd)) {
                  
                    if (user.getRole().equals("admin")) {
                        return 1; // Admin
                    } else if (user.getRole().equals("user")) {
                        return 2; // User
                    }
                }
                return 3; 
            }
        }
        return 4; 
    }
}*/

package com.ShanStationaries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    public int validate(String uname, String psswd, Users users) {
        try (Connection con = new DBUtil().getDBConnection();
             PreparedStatement pstmt = con.prepareStatement("SELECT * FROM users WHERE username = ?")) {
             
            pstmt.setString(1, uname);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                String dbRole = rs.getString("role");

                // Check if the password matches
                if (dbPassword.equals(psswd)) {
                    // Return role-based validation
                    return dbRole.equals("admin") ? 1 : 2; // Admin or User
                } else {
                    return 3; // Incorrect password
                }
            } else {
                return 4; // Username not found
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 4; // In case of any error
    }
}
