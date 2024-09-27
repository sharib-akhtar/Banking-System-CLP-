package bankingSystem;
import java.sql.*;
import java.util.Scanner;
public class Main {
    private static final String name="root";
    private static final String pass="Admin";
    private static final String url="jdbc:mysql://localhost:3306/bankingsystem";
    public static void main(String[] args)throws ClassNotFoundException,SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
            Connection connection=DriverManager.getConnection(url,name,pass);
            Scanner in=new Scanner(System.in);
            User user=new User(connection,in);
            Accounts accounts=new Accounts(connection,in);
            AccountManager accountManager=new AccountManager(connection,in);
            long account_number;
            String email;
            while(true)
            {
                System.out.println("*** Welcome to the Banking System ***");
                System.out.println();
                System.out.println("1: Register");
                System.out.println("2: Login");
                System.out.println("3: Exit");
                System.out.println("Enter Your choice");
                int choice1=in.nextInt();
                switch (choice1)
                {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email=user.login();
                        if(email!=null)
                        {
                            System.out.println();
                            System.out.println("User logged In!");
                            if(!accounts.accounts_exists(email))
                            {
                                System.out.println();
                                System.out.println("1. Open a Bank Account");
                                System.out.println("Press any other key to Exit");
                                if(in.nextInt() == 1)
                                {
                                    account_number=accounts.openAccount(email);
                                    System.out.println("Account creation successfull...!!!");
                                    System.out.println("Your account number is="+account_number);
                                }
                                else
                                {
                                    break;
                                }
                            }
                            account_number=accounts.getAccount_number(email);
                            int choice2=0;
                            while(choice2 !=5)
                            {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2=in.nextInt();
                                switch (choice2)
                                {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter a valid choice...!!!");
                                        break;
                                }
                            }
                        }
                        else {
                            System.out.println("Invalid email or Password");
                        }
                    case 3:
                        System.out.println("<<<...Thankyou for using BANKING SYSTEM...>>>");
                        System.out.println("Exiting system....");
                        return;
                    default:
                        System.out.println("Enter valid choice");
                        break;
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}