package bankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner in;
    AccountManager(Connection connection, Scanner in)
    {
       this.connection=connection;
        this.in=in;
    }
    public void credit_money(long account_no)throws SQLException
    {
        in.nextLine();
        System.out.println("Enter the amount:");
        double amount=in.nextDouble();
        in.nextLine();
        System.out.println("Enter the transaction pin");
        String pin=in.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_no != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("Select * from accounts where account_number=? and security_pin=?;");
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String query = "Update accounts set balance=balance+? where account_number=?;";
                    preparedStatement.setDouble(1, amount);
                    preparedStatement.setLong(2, account_no);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Transaction successfull....\n" + amount + "Credited Successfully!!!");
                        connection.commit();
                        connection.setAutoCommit(true);
                    } else {
                        System.out.println("Transaction failed!!!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid transaction pin!!!");

                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void debit_money(long account_no)throws SQLException
    {
        in.nextLine();
        System.out.println("Enter the amount:");
        double amount=in.nextDouble();
        in.nextLine();
        System.out.println("Enter the transaction pin");
        String pin=in.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_no != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("Select * from accounts where account_number=? and security_pin=?;");
                preparedStatement.setLong(1, account_no);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double current_bal=resultSet.getDouble("balance");
                    if(amount<=current_bal) {
                        String query = "Update accounts set balance=balance-? where account_number=?;";
                        preparedStatement.setDouble(1, amount);
                        preparedStatement.setLong(2, account_no);
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Transaction successfull....\n" + amount + "Debited Successfully!!!");
                            connection.commit();
                            connection.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction failed!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("Insufficient Balance!!!...");
                    }
                } else {
                    System.out.println("Invalid transaction pin!!!");
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void transfer_money(long sender_account_number)throws SQLException
    {
        in.nextLine();
        System.out.println("Enter the receiver's account number");
        long receiver_account_number=in.nextLong();
        System.out.println("Enter the amount to be transferred");
        double amount=in.nextDouble();
        in.nextLine();
        System.out.println("Enter the transaction pin");
        String pin=in.nextLine();
        try
        {
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0) {
                PreparedStatement preparedStatement = connection.prepareStatement("Select * from accounts where account_number=? and security_pin=?;");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double current_bal = resultSet.getDouble("balance");
                    if (amount <= current_bal) {
                        String debit_query = "Update accounts set balance=balance-? where account_number=?;";
                        String credit_query = "Update accounts set balance=balance+? where account_number=?;";
                        PreparedStatement debit_prepareStatement = connection.prepareStatement(debit_query);
                        PreparedStatement credit_prepareStatement = connection.prepareStatement(credit_query);
                        debit_prepareStatement.setDouble(1, amount);
                        debit_prepareStatement.setLong(2, sender_account_number);
                        credit_prepareStatement.setDouble(1, amount);
                        credit_prepareStatement.setLong(2, receiver_account_number);
                        int rowsAffected1 = debit_prepareStatement.executeUpdate();
                        int rowsAffected2 = credit_prepareStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction successfull....\n" + amount + " Transferred Successfully!!!");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!!!...");
                    }
                } else {
                    System.out.println("Invalid transaction pin!!!");
                }
            }
            else {
                System.out.println("Invalid account number");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void getBalance(long account_no)
    {
        in.nextLine();
        System.out.println("Enter the transaction pin");
        String pin=in.nextLine();
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from accounts where account_number=? and security_pin=?;");
            preparedStatement.setLong(1, account_no);
            preparedStatement.setString(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                double balance=resultSet.getDouble("balance");
                System.out.println("Balance="+balance);
            }
            else {
                System.out.println("Invalid Pin!!!");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
