package com.example.oop3;
import java.util.ArrayList;

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

    // Constructor
    public OrderSystem()
    {
        customers = new ArrayList<>();
        products = new ArrayList<>();
        cart = new ArrayList<>();
        orders = new ArrayList<>();
        securityQuestions = new ArrayList<>();
        bank = new Bank();
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
    public boolean createAccount(String customerId, String password, String name,
                                 String address, String creditCard,
                                 String securityQuestion, String securityAnswer)
    {
        if (findCustomer(customerId) != null) {
            return false;
        }

        if (!Customer.validatePasswordFormat(password)) {
            return false;
        }

        if (name.isEmpty() || address.isEmpty() || creditCard.isEmpty() || securityAnswer.isEmpty()) {
            return false;
        }

        Customer newCustomer = new Customer(customerId, password, name, address,
                creditCard, securityQuestion, securityAnswer);
        customers.add(newCustomer);
        return true;
    }

    // Logout function
    public void logout()
    {
        if (currentCustomer != null) {
            currentCustomer.logout();
            currentCustomer = null;
            clearCart();
        }
    }

    // Add single item to cart
    public boolean addToCart(String productId, int quantity)
    {
        Product product = findProduct(productId);
        if (product == null) {
            return false;
        }

        if (quantity <= 0 || quantity > product.getQuantity()) {
            return false;
        }

        Product cartProduct = new Product(product);
        cartProduct.setQuantity(quantity);
        cart.add(cartProduct);
        return true;
    }

    // Make order
    public Order makeOrder(String deliveryMethod)
    {
        if (currentCustomer == null || cart.isEmpty()) {
            return null;
        }

        double shippingFee = deliveryMethod.equals("MAIL") ? SHIPPING_FEE : 0.0;
        double subtotal = calculateCartTotal();
        double tax = calculateTax();
        double total = subtotal + tax + shippingFee;

        String authNumber = bank.processPayment(currentCustomer.getCreditCard(), total);

        if (authNumber == null) {
            return null;
        }

        Order order = new Order(currentCustomer.getCustomerId(), cart, deliveryMethod);
        order.calculateSubtotal();
        order.calculateTax(TAX_RATE);
        order.calculateTotal();
        order.setAuthorizationNumber(authNumber);
        orders.add(order);

        for (Product cartItem : cart) {
            Product catalogProduct = findProduct(cartItem.getProductId());
            int newQuantity = catalogProduct.getQuantity() - cartItem.getQuantity();
            catalogProduct.setQuantity(newQuantity);
        }

        clearCart();
        return order;
    }

    // Find customer by ID
    public Customer findCustomer(String customerId)
    {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    // Find product by ID
    public Product findProduct(String productId)
    {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    // Calculate cart subtotal
    public double calculateCartTotal()
    {
        double total = 0.0;
        for (Product product : cart) {
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
        for (Order order : orders) {
            if (order.getCustomerId().equals(customerId)) {
                customerOrders.add(order);
            }
        }
        return customerOrders;
    }

    // Getters
    public Customer getCurrentCustomer()
    {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer customer)
    {
        this.currentCustomer = customer;
    }

    public ArrayList<String> getSecurityQuestions()
    {
        return securityQuestions;
    }

    public ArrayList<Product> getProducts()
    {
        return products;
    }

    public ArrayList<Product> getCart()
    {
        return cart;
    }
}