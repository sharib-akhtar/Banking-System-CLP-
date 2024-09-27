package bankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner in;
  public Accounts(Connection connection, Scanner in)
    {
        this.connection=connection;
        this.in=in;
    }
  public long openAccount(String email)
  {
      if(!accounts_exists(email))
          {
           String query="Insert into accounts values(?,?,?,?,?);";
           in.nextLine();
           System.out.println("Enter your Full Name");
           String name=in.nextLine();
           System.out.println("Enter the Initial Amount of money You wish to store");
           double money=in.nextDouble();
           in.nextLine();
           System.out.println("Enter Your Transaction Pin");
           String pin=in.nextLine();
           try
           {
               long account_no=generateAccountno();
               PreparedStatement preparedStatement= connection.prepareStatement(query);
               preparedStatement.setLong(1,account_no);
               preparedStatement.setString(2,name);
               preparedStatement.setString(3,email);
               preparedStatement.setDouble(4,money);
               preparedStatement.setString(5,pin);
               int rowsAffected=preparedStatement.executeUpdate();
               if(rowsAffected>0)
               {
                   return account_no;
               }
               else
               {
                    throw new RuntimeException("Account Creation failed!!!");
               }
           }
           catch(SQLException e)
           {
               e.printStackTrace();
           }
          }
      throw new RuntimeException("Account already exists...!!!");
  }
  private long generateAccountno()
  {
      String query="Select account_number from accounts order by account_number desc limit 1;";
      try
      {
          PreparedStatement preparedStatement= connection.prepareStatement(query);
          ResultSet resultSet=preparedStatement.executeQuery();
          if(resultSet.next()) {
              long account_no = resultSet.getLong("account_number");
              return account_no+1;
          }
          else{
              return 10001000;
          }
      }
      catch(SQLException e)
      {
          e.printStackTrace();
      }
      return 10001000;
  }
  public long getAccount_number(String email)
  {
      String query="Select account_number from accounts where email=?;";
      try
      {
          PreparedStatement preparedStatement=connection.prepareStatement(query);
          preparedStatement.setString(1,email);
          ResultSet resultSet=preparedStatement.executeQuery();
          if(resultSet.next())
          {
              return resultSet.getLong("account_number");
          }
      }
      catch(SQLException e)
      {
          e.printStackTrace();
      }
      throw new RuntimeException("Account doesn't exist!!!");
  }
  public boolean accounts_exists(String email)
  {
      String query="Select account_number from accounts where email=?;";
      try
      {
          PreparedStatement preparedStatement=connection.prepareStatement(query);
          preparedStatement.setString(1,email);
          ResultSet resultSet=preparedStatement.executeQuery();
          if(resultSet.next())
          {
              return true;
          }
          else
              return false;
      }
      catch(SQLException e)
      {
          e.printStackTrace();
      }
      return false;
  }

}
