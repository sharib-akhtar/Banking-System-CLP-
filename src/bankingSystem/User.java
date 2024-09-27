package bankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner in;

    public User(Connection connection, Scanner in) {
        this.connection=connection;
        this.in=in;
    }

    public void register() {
        in.nextLine();
        System.out.println("Enter Your full Name");
        String name = in.nextLine();
        System.out.println("Enter Your Email_id");
        String email = in.nextLine();
        System.out.println("Enter your password");
        String pass = in.nextLine();
        if (user_exists(email)) {
            System.out.println("User already exists for this email!!!!!");
            System.out.println();
            return;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into user values (?,?,?);");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, pass);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Registration Successfull...!!!");
            } else
                System.out.println("Registration failed...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login() {
        in.nextLine();
        System.out.println("Enter Your email_id");
        String email = in.nextLine();
        System.out.println("Enter your password");
        String pass = in.nextLine();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Select email,password from user where email=? and password=?;");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Login successfull...!!!");
                return email;
            } else {
                System.out.println("Wrong email id or password");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean user_exists(String email)
    {
        String query="Select * from user where email=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return true;
            }
            else {
                return false;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}