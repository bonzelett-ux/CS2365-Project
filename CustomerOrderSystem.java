import java.util.Scanner;

public class CustomerOrderSystem
{
    //main method to run everything in the COS
    public static void main(String[] args)
    {
        //create the OrderSystem so the COS can work and also the scanner is made too
        OrderSystem system = new OrderSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("CUSTOMER ORDER SYSTEM (COS)");

        //Here is the loop to keep running the COS until you exit (has options for use cases each loop that you can change between loops)
        while (running)
        {
            System.out.println("---------------- Menu -----------------");

            if (system.getCurrentCustomer() == null) // Not logged in
            {
                System.out.println("1. Create Account");
                System.out.println("2. Log In");
                System.out.println("7. Browse Catalog (Guest)");
                System.out.println("9. Exit");
            }
            else // Logged in allows access to the actions that you need to log in for
            {
                Customer customer = system.getCurrentCustomer();
                System.out.println("Logged in as: " + customer.getName());
                System.out.println("------------");
                System.out.println("1. Create Account");
                System.out.println("2. Log In");
                System.out.println("3. Select Items");
                System.out.println("4. View Cart");
                System.out.println("5. Make Order");
                System.out.println("6. View My Orders");
                System.out.println("7. Browse Catalog");
                System.out.println("8. Log Out");
                System.out.println("9. Exit");
            }
            System.out.println("-------------------------");
            System.out.print("Enter your choice: (type a number shown above here)");
            String choice = scanner.nextLine();
            switch (choice)
            {
                case "1":
                    system.createAccount();
                    break;
                case "2":
                    system.login();
                    break;
                case "3":
                    if (system.getCurrentCustomer() != null)
                    {
                        system.selectItems();
                    }
                    break;
                case "4":
                    if (system.getCurrentCustomer() != null)
                    {
                        system.displayCart();
                    }
                    break;
                case "5":
                    if (system.getCurrentCustomer() != null)
                    {
                        system.makeOrder();
                    }
                    break;
                case "6":
                    if (system.getCurrentCustomer() != null)
                    {
                        system.viewOrders();
                    }
                    break;
                case "7":
                    system.displayCatalog();
                    break;
                case "8":
                    if (system.getCurrentCustomer() != null)
                    {
                        system.logout();
                    }
                    break;
                case "9":
                    System.out.println("\nExited COS");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }
}