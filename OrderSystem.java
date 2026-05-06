import java.util.ArrayList;
import java.util.Scanner;

public class OrderSystem
{
    // Private fields
    private ArrayList<Customer> customers;
    private ArrayList<Product> products;
    private ArrayList<Product> cart;
    private ArrayList<Order> orders;
    private Customer currentCustomer;
    private ArrayList<String> securityQuestions;
    private Bank bank;
    private final double TAX_RATE = 0.08;
    private final double SHIPPING_FEE = 3.00;
    private Scanner scanner;

    // Constructor
    public OrderSystem()
    {
        customers = new ArrayList<>();
        products = new ArrayList<>();
        cart = new ArrayList<>();
        orders = new ArrayList<>();
        securityQuestions = new ArrayList<>();
        bank = new Bank();
        scanner = new Scanner(System.in);
        currentCustomer = null;

        //CREATE BANKS
        bank.addCreditCard("1234567890123456", 5000.0, 0.0);
        bank.addCreditCard("1111111111111111", 500.0, 0.0);
        bank.addCreditCard("1234123412341234", 10000.0, 1000.0);

        //CREATE PRODUCTS
        products.add(new Product("1", "Laptop", "High-performance laptop", 999.99, 899.99, 10));
        products.add(new Product("2", "Wireless Mouse", "wireless mouse", 29.99, 24.99, 50));
        products.add(new Product("3", "Keyboard", "Keyboard", 79.99, 69.99, 30));
        products.add(new Product("4", "Ball", "Just a red ball", 3.99, 2.99, 15));
        products.add(new Product("5", "Headphones", "High volume headphones", 199.99, 179.99, 25));
        products.add(new Product("6", "Desk Lamp", "LED desk lamp", 34.99, 29.99, 60));

        //CREATE SECURITY Qs
        securityQuestions.add("What is your mother's maiden name?");
        securityQuestions.add("What was the name of your first pet?");
        securityQuestions.add("What city were you born in?");
        securityQuestions.add("What is your favorite book?");
        securityQuestions.add("What is your favorite song?");

    }

    // Create new customer account
    public boolean createAccount()
    {
        System.out.println("\n                    CREATE ACCOUNT");
        String customerId;
        while (true)
        {
            System.out.print("Enter Customer ID: ");
            customerId = scanner.nextLine();
            if (findCustomer(customerId) != null)
            {
                System.out.println("Customer ID already exists. Please enter a different ID.");
            }
            else
            {
                break;
            }
        }

        String password;
        while (true)
        {
            System.out.print("Enter Password (min 6 chars, 1 digit, 1 special char !@#$%&*, 1 uppercase): ");
            password = scanner.nextLine();

            if (Customer.validatePasswordFormat(password))
            {
                break;
            }
            else
            {
                System.out.println("Invalid password format. Please try again.");
            }
        }

        String name, address, creditCard;
        while (true)
        {
            System.out.print("Enter Name: ");
            name = scanner.nextLine();
            System.out.print("Enter Address: ");
            address = scanner.nextLine();
            System.out.print("Enter Credit Card Number: ");
            creditCard = scanner.nextLine();
            if (!name.isEmpty() && !address.isEmpty() && !creditCard.isEmpty())
            {
                System.out.println("Account information validated successfully!");
                break;
            }
            else
            {
                System.out.println("Name, address, or credit card cannot be empty. Please try again.");
            }
        }

        System.out.println("\nSecurity Questions:");
        for (int i = 0; i < securityQuestions.size(); i++)
        {
            System.out.println((i + 1) + ". "+ securityQuestions.get(i));
        }
        int questionIndex;
        while (true)
        {
            System.out.print("Select a security question (1-" +securityQuestions.size()+ "): ");
            try {
                questionIndex = Integer.parseInt(scanner.nextLine()) - 1;
                if (questionIndex >= 0 && questionIndex < securityQuestions.size())
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid selection. Please try again.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        String securityQuestion = securityQuestions.get(questionIndex);
        System.out.print("Enter your answer: ");
        String securityAnswer = scanner.nextLine();
        Customer newCustomer = new Customer(customerId, password, name, address, creditCard, securityQuestion, securityAnswer);
        customers.add(newCustomer);

        System.out.println("\n                   Account created successfully!");
        return true;
    }

    // Login process
    public boolean login()
    {
        System.out.println("\n                  LOGIN ");

        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS)
        {
            System.out.print("Enter Customer ID: ");
            String customerId = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            Customer customer = findCustomer(customerId);
            if (customer == null)
            {
                System.out.println("No account found with this ID.");
                return false;
            }


            if (!customer.validateCredentials(password))
            {
                attempts++;
                System.out.println("Incorrect password. Attempt " + attempts + " of " + MAX_ATTEMPTS);
                if (attempts >= MAX_ATTEMPTS)
                {
                    System.out.println("Maximum attempts reached. Login failed.");
                    return false;
                }
                //continue;
            }
            System.out.println("\nSecurity Question: " + customer.getSecurityQuestion());
            System.out.print("Your Answer: ");
            String answer = scanner.nextLine();

            if (customer.validateSecurityAnswer(answer))
            {
                customer.setLoggedIn(true);
                currentCustomer = customer;
                System.out.println("\nWelcome, " + customer.getName() + "!");
                return true;
            }
            else
            {
                System.out.println("Incorrect security answer. Login failed.");
                return false;
            }
        }
        return false;
    }

    // Logout function to log out the customer if they are logged in
    public void logout()
    {
        if (currentCustomer != null)
        {
            currentCustomer.logout();
            System.out.println("\nYou have been logged out successfully.");
            currentCustomer = null;
            clearCart();
        }
        else
        {
            System.out.println("\nNo user is currently logged in.");
        }
    }

    // Display product catalog so customers know what there is to buy or order
    public void displayCatalog()
    {
        System.out.println("\n                       PRODUCT CATALOG ");
        System.out.printf("%-10s %-25s %-10s %-10s%n", "ID", "Product Name", "Regular", "Sale");
        System.out.println("-------------------------------------------------------------");

        for (int i = 0; i < products.size(); i++)
        {
            Product product = products.get(i);
            product.displayProductInfo();
        }
        System.out.println("------------------------------------------------------------\n");
    }

    // Select items and add to cart from the listed catalog in the previous function after they call it
    public void selectItems()
    {
        displayCatalog();
        while (true)
        {
            System.out.print("Enter Product ID (or 'done' to finish): ");
            String productId = scanner.nextLine();

            if (productId.equalsIgnoreCase("done"))
            {
                break;
            }
            Product product = findProduct(productId);
            if (product == null)
            {
                System.out.println("Product not found.");
                continue;
            }

            System.out.print("Enter quantity: ");
            try
            {
                int quantity = Integer.parseInt(scanner.nextLine());
                if (quantity <= 0)
                {
                    System.out.println("Quantity must be positive.");
                    continue;
                }
                if (quantity > product.getQuantity())
                {
                    System.out.println("Insufficient stock. Available: " + product.getQuantity());
                    continue;
                }
                Product cartProduct = new Product(product);
                cartProduct.setQuantity(quantity);
                cart.add(cartProduct);

                System.out.println(quantity + " x " + product.getProductName() + " added to cart.");
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid quantity.");
            }
        }

        if (!cart.isEmpty())
        {
            displayCart();
        }
    }

    // Display cart contents
    public void displayCart()
    {
        if (cart.isEmpty())
        {
            System.out.println("\nYour cart is empty.");
            return;
        }

        System.out.println("\n                         SHOPPING CART");
        for (int i = 0; i < cart.size(); i++)
        {
            Product product = cart.get(i);
            product.displayWithQuantity();
        }
        System.out.println("-----------------------------------");
        System.out.printf("Subtotal: $%.2f%n", calculateCartTotal());
        System.out.printf("Tax (%.0f%%): $%.2f%n", TAX_RATE * 100, calculateTax());
        System.out.printf("Total: $%.2f%n", calculateCartTotal() + calculateTax());
    }

    // Make order process
    public void makeOrder()
    {
        if (currentCustomer == null)
        {
            System.out.println("\nYou must be logged in to place an order.");
            return;
        }

        if (cart.isEmpty())
        {
            System.out.println("\nYour cart is empty.");
            return;
        }

        System.out.println("\n                               MAKE ORDER ");
        displayCart();
        System.out.println("Delivery Methods:");
        System.out.println("1. Mail (Shipping Fee: $" + SHIPPING_FEE + ")");
        System.out.println("2. In-Store Pickup (Free)");
        System.out.print("Select delivery method (1 or 2): ");

        String deliveryMethod;
        double shippingFee = 0.0;
        String choice = scanner.nextLine();
        if (choice.equals("1"))
        {
            deliveryMethod = "MAIL";
            shippingFee = SHIPPING_FEE;
        }
        else if (choice.equals("2"))
        {
            deliveryMethod = "PICKUP";
            shippingFee = 0.0;
        }
        else
        {
            System.out.println("Invalid selection. Order cancelled.");
            return;
        }

        double subtotal = calculateCartTotal();
        double tax = calculateTax();
        double total = subtotal + tax + shippingFee;

        System.out.println("\n--- Order Summary ---");
        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("Tax: $%.2f%n", tax);
        System.out.printf("Shipping: $%.2f%n", shippingFee);
        System.out.printf("TOTAL: $%.2f%n", total);

        System.out.println("\nProcessing payment with credit card: " + currentCustomer.getCreditCard());

        String authNumber = bank.processPayment(currentCustomer.getCreditCard(), total);

        if (authNumber == null)
        {
            System.out.print("Payment declined. Would you like to enter a different credit card? (yes/no): ");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("yes"))
            {
                System.out.print("Enter new credit card number: ");
                String newCard = scanner.nextLine();
                currentCustomer.setCreditCard(newCard);

                authNumber = bank.processPayment(newCard, total);
                if (authNumber == null)
                {
                    System.out.println("Payment declined again. Order cancelled.");
                    return;
                }
            }
            else
            {
                System.out.println("Order cancelled.");
                return;
            }
        }
        Order order = new Order(currentCustomer.getCustomerId(), cart, deliveryMethod);
        order.calculateSubtotal();
        order.calculateTax(TAX_RATE);
        order.calculateTotal();
        order.setAuthorizationNumber(authNumber);
        orders.add(order);

        for (int i = 0; i < cart.size(); i++)
        {
            Product cartItem = cart.get(i);
            Product catalogProduct = findProduct(cartItem.getProductId());
            int newQuantity = catalogProduct.getQuantity() - cartItem.getQuantity();
            catalogProduct.setQuantity(newQuantity);
        }
        System.out.println("\n*** ORDER CONFIRMED ***");
        order.displayOrderSummary();
        clearCart();
    }

    // View customer orders
    public void viewOrders()
    {
        if (currentCustomer == null)
        {
            System.out.println("\n: You must be logged in to view orders.");
            return;
        }
        ArrayList<Order> customerOrders = getCustomerOrders(currentCustomer.getCustomerId());
        if (customerOrders.isEmpty())
        {
            System.out.println("\nYou have no orders.");
            return;
        }
        System.out.println("\n                       YOUR ORDERS");
        for (int i = 0; i < customerOrders.size(); i++)
        {
            Order order = customerOrders.get(i);
            order.displayOrderInfo();
        }
    }

    // Find customer by ID
    public Customer findCustomer(String customerId)
    {
        for (int i = 0; i < customers.size(); i++)
        {
            Customer customer = customers.get(i);
            if (customer.getCustomerId().equals(customerId))
            {
                return customer;
            }
        }
        return null;
    }

    // Find product by ID
    public Product findProduct(String productId)
    {
        for (int i = 0; i < products.size(); i++)
        {
            Product product = products.get(i);
            if (product.getProductId().equals(productId))
            {
                return product;
            }
        }
        return null;
    }

    // Calculate cart subtotal
    public double calculateCartTotal()
    {
        double total = 0.0;
        for (int i = 0; i < cart.size(); i++)
        {
            Product product = cart.get(i);
            total += product.getSalePrice() * product.getQuantity();
        }
        return total;
    }

    // Calculate tax
    public double calculateTax()
    {
        return calculateCartTotal() * TAX_RATE;
    }

    // Clear cart
    public void clearCart()
    {
        cart.clear();
    }

    // Get orders for specific customer
    public ArrayList<Order> getCustomerOrders(String customerId)
    {
        ArrayList<Order> customerOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++)
        {
            Order order = orders.get(i);
            if (order.getCustomerId().equals(customerId))
            {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    // Getter for current customer so you can access their info & login status
    public Customer getCurrentCustomer()
    {
        return currentCustomer;
    }
}