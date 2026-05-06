package com.example.oop3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerOrderSystem extends Application {
    private OrderSystem orderSystem;
    private Stage primaryStage;

    //Here is the overridden start method to set the main menu stage and create the ordersystem
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.orderSystem = new OrderSystem();
        showMainMenu();
    }
    //Here is the main menu function/code where the layout of the main menu is created and the buttons
    //that do the processes are greyed out if unavailable for you to use and are normal if you can use them
    private void showMainMenu() {
        Label titleLabel = new Label("CUSTOMER ORDER SYSTEM");

        Label statusLabel = new Label();
        if (orderSystem.getCurrentCustomer() != null) {
            statusLabel.setText("Logged in as: " + orderSystem.getCurrentCustomer().getName());
        } else {
            statusLabel.setText("Not logged in");
        }

        Button createAccountBtn = new Button("1. Create Account");
        createAccountBtn.setOnAction(e -> showCreateAccount());

        Button loginBtn = new Button("2. Log In");
        loginBtn.setOnAction(e -> showLogin());

        Button catalogBtn = new Button("3. Browse Catalog");
        catalogBtn.setOnAction(e -> showCatalog());

        Button selectItemsBtn = new Button("4. Select Items");
        selectItemsBtn.setOnAction(e -> showSelectItems());
        selectItemsBtn.setDisable(orderSystem.getCurrentCustomer() == null);

        Button viewCartBtn = new Button("5. View Cart");
        viewCartBtn.setOnAction(e -> showCart());
        viewCartBtn.setDisable(orderSystem.getCurrentCustomer() == null);

        Button makeOrderBtn = new Button("6. Make Order");
        makeOrderBtn.setOnAction(e -> showMakeOrder());
        makeOrderBtn.setDisable(orderSystem.getCurrentCustomer() == null);

        Button viewOrdersBtn = new Button("7. View My Orders");
        viewOrdersBtn.setOnAction(e -> showMyOrders());
        viewOrdersBtn.setDisable(orderSystem.getCurrentCustomer() == null);

        Button logoutBtn = new Button("8. Log Out");
        logoutBtn.setOnAction(e -> {
            orderSystem.logout();
            showMainMenu();
        });
        logoutBtn.setDisable(orderSystem.getCurrentCustomer() == null);

        Button exitBtn = new Button("9. Exit");
        exitBtn.setOnAction(e -> primaryStage.close());

        VBox vbox = new VBox(10, titleLabel, statusLabel, createAccountBtn, loginBtn,
                catalogBtn, selectItemsBtn, viewCartBtn, makeOrderBtn,
                viewOrdersBtn, logoutBtn, exitBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 400, 500);
        primaryStage.setTitle("Customer Order System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //Here is the function for showing the steps of create account that is used when its button is pressed
    //Now the inputs are in text fields instead of proj2's scanner things
    //available when not logged in so not greyed out if logged out
    private void showCreateAccount() {
        Label titleLabel = new Label("CREATE ACCOUNT");

        TextField custIdField = new TextField();
        custIdField.setPromptText("Customer ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        TextField creditField = new TextField();
        creditField.setPromptText("Credit Card Number");

        ComboBox<String> secQCombo = new ComboBox<>();
        for (String q : orderSystem.getSecurityQuestions()) {
            secQCombo.getItems().add(q);
        }
        secQCombo.setValue(orderSystem.getSecurityQuestions().get(0));

        TextField secAField = new TextField();
        secAField.setPromptText("Security Answer");
        Label messageLabel = new Label();

        Button createBtn = new Button("Create Account");
        createBtn.setOnAction(e -> {
            String custId = custIdField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String address = addressField.getText();
            String credit = creditField.getText();
            String secQ = secQCombo.getValue();
            String secA = secAField.getText();

            if (custId.isEmpty() || password.isEmpty() || name.isEmpty() ||
                    address.isEmpty() || credit.isEmpty() || secA.isEmpty()) {
                messageLabel.setText("All fields are required!");
                return;
            }

            if (orderSystem.findCustomer(custId) != null) {
                messageLabel.setText("Customer ID already exists!");
                return;
            }

            if (!Customer.validatePasswordFormat(password)) {
                messageLabel.setText("Invalid password format!");
                return;
            }

            boolean success = orderSystem.createAccount(custId, password, name,
                    address, credit, secQ, secA);
            if (success) {
                messageLabel.setText("Account created successfully!");
            } else {
                messageLabel.setText("Failed to create account!");
            }
        });
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showMainMenu());

        VBox vbox = new VBox(10, titleLabel, custIdField, passwordField, nameField,
                addressField, creditField, secQCombo, secAField,
                messageLabel, createBtn, backBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 400, 550);
        primaryStage.setScene(scene);
    }

    //Function for showing login, again happens on button press and takes 4 inputs into textfields
    //accessible when not logged in so its not grey if logged out
    private void showLogin() {
        Label titleLabel = new Label("LOGIN");

        TextField custIdField = new TextField();
        custIdField.setPromptText("Customer ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label secQLabel = new Label();
        secQLabel.setVisible(false);

        TextField secAField = new TextField();
        secAField.setPromptText("Security Answer");
        secAField.setVisible(false);

        Label messageLabel = new Label();

        Button loginBtn = new Button("Login");
        final Customer[] tempCustomer = {null};
        final int[] attempts = {0};

        loginBtn.setOnAction(e -> {
            if (tempCustomer[0] == null) {
                String custId = custIdField.getText();
                String password = passwordField.getText();

                Customer customer = orderSystem.findCustomer(custId);
                if (customer == null) {
                    messageLabel.setText("No account found with this ID.");
                    return;
                }

                if (!customer.validateCredentials(password)) {
                    attempts[0]++;
                    messageLabel.setText("Incorrect password. Attempt " + attempts[0] + " of 3");
                    if (attempts[0] >= 3) {
                        messageLabel.setText("Maximum attempts reached.");
                        showMainMenu();
                    }
                    return;
                }

                tempCustomer[0] = customer;
                secQLabel.setText(customer.getSecurityQuestion());
                secQLabel.setVisible(true);
                secAField.setVisible(true);
                custIdField.setDisable(true);
                passwordField.setDisable(true);
            } else {
                String answer = secAField.getText();
                if (tempCustomer[0].validateSecurityAnswer(answer)) {
                    tempCustomer[0].setLoggedIn(true);
                    orderSystem.setCurrentCustomer(tempCustomer[0]);
                    messageLabel.setText("Login successful!");
                    showMainMenu();
                } else {
                    messageLabel.setText("Incorrect security answer.");
                }
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showMainMenu());

        VBox vbox = new VBox(10, titleLabel, custIdField, passwordField,
                secQLabel, secAField, messageLabel, loginBtn, backBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
    }
    //shows the catalog - just like before, button push activates this
    //accessible when not logged in so its not grey if logged out
    private void showCatalog() {
        Label titleLabel = new Label("PRODUCT CATALOG");

        TextArea catalogArea = new TextArea();
        catalogArea.setEditable(false);

        StringBuilder catalog = new StringBuilder();
        catalog.append("ID\tProduct Name\t\tRegular\tSale");
        catalog.append("\n-------------------------------------------------------------\n");

        for (Product product : orderSystem.getProducts()) {
            catalog.append(String.format("%s\t %s\t\t$%.2f\t$%.2f\t%n",
                    product.getProductId(), product.getProductName(),
                    product.getRegularPrice(), product.getSalePrice()));
            catalog.append("\t" + product.getDescription() + "\n");
        }

        catalogArea.setText(catalog.toString());

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showMainMenu());

        VBox vbox = new VBox(10, titleLabel, catalogArea, backBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 600, 500);
        primaryStage.setScene(scene);
    }
    //shows the menu for selecting items
    //NOT accessible when not logged in so its grey if logged out
    private void showSelectItems() {
        Label titleLabel = new Label("SELECT ITEMS");

        TextArea catalogArea = new TextArea();
        catalogArea.setEditable(false);

        StringBuilder catalog = new StringBuilder();
        for (Product product : orderSystem.getProducts()) {
            catalog.append(String.format("ID: %s | %s | $%.2f | Stock: %d%n",
                    product.getProductId(), product.getProductName(),
                    product.getSalePrice(), product.getQuantity()));
        }
        catalogArea.setText(catalog.toString());

        TextField productIdField = new TextField();
        productIdField.setPromptText("Product ID");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Label messageLabel = new Label();

        Button addBtn = new Button("Add to Cart");
        addBtn.setOnAction(e -> {
            String productId = productIdField.getText();
            String quantityStr = quantityField.getText();

            try {
                int quantity = Integer.parseInt(quantityStr);
                boolean success = orderSystem.addToCart(productId, quantity);

                if (success) {
                    messageLabel.setText("Added to cart!");
                    productIdField.clear();
                    quantityField.clear();
                } else {
                    messageLabel.setText("Failed. Check ID and quantity.");
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid quantity!");
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showMainMenu());

        VBox vbox = new VBox(10, titleLabel, catalogArea, productIdField,
                quantityField, messageLabel, addBtn, backBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 600, 550);
        primaryStage.setScene(scene);
    }
    //shows the cart when button for it is pressed
    //NOT accessible when not logged in so its grey if logged out
    private void showCart() {
        Label titleLabel = new Label("SHOPPING CART");

        TextArea cartArea = new TextArea();
        cartArea.setEditable(false);

        StringBuilder cartContent = new StringBuilder();
        if (orderSystem.getCart().isEmpty()) {
            cartContent.append("Your cart is empty.");
        } else {
            for (Product product : orderSystem.getCart()) {
                cartContent.append(String.format("%-25s x%-3d @ $%-8.2f = $%.2f%n",
                        product.getProductName(), product.getQuantity(),
                        product.getSalePrice(),
                        product.getSalePrice() * product.getQuantity()));
            }
            cartContent.append("-----------------------------------\n");
            cartContent.append(String.format("Subtotal: $%.2f%n", orderSystem.calculateCartTotal()));
            cartContent.append(String.format("Tax (8%%): $%.2f%n", orderSystem.calculateTax()));
            cartContent.append(String.format("Total: $%.2f%n",
                    orderSystem.calculateCartTotal() + orderSystem.calculateTax()));
        }
        cartArea.setText(cartContent.toString());

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showMainMenu());

        VBox vbox = new VBox(10, titleLabel, cartArea, backBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 600, 500);
        primaryStage.setScene(scene);
    }
    //shows the menu to order when button for it is pressed
    //NOT accessible when not logged in so its grey if logged out
    private void showMakeOrder() {
        Label titleLabel = new Label("MAKE ORDER");

        if (orderSystem.getCart().isEmpty()) {
            Label emptyLabel = new Label("Your cart is empty!");
            Button backBtn = new Button("Back");
            backBtn.setOnAction(e -> showMainMenu());

            VBox vbox = new VBox(10, titleLabel, emptyLabel, backBtn);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(20));

            Scene scene = new Scene(vbox, 400, 300);
            primaryStage.setScene(scene);
            return;
        }

        TextArea summaryArea = new TextArea();
        summaryArea.setEditable(false);

        double subtotal = orderSystem.calculateCartTotal();
        double tax = orderSystem.calculateTax();

        StringBuilder summary = new StringBuilder();
        for (Product product : orderSystem.getCart()) {
            summary.append(String.format("%s x%d @ $%.2f = $%.2f%n",
                    product.getProductName(), product.getQuantity(),
                    product.getSalePrice(),
                    product.getSalePrice() * product.getQuantity()));
        }
        summary.append("\n");
        summary.append(String.format("Subtotal: $%.2f%n", subtotal));
        summary.append(String.format("Tax: $%.2f%n", tax));
        summaryArea.setText(summary.toString());

        ComboBox<String> deliveryCombo = new ComboBox<>();
        deliveryCombo.getItems().addAll("MAIL ($3.00 shipping)", "PICKUP (Free)");
        deliveryCombo.setValue("PICKUP (Free)");

        Label totalLabel = new Label();

        deliveryCombo.setOnAction(e -> {
            double shipping = deliveryCombo.getValue().startsWith("MAIL") ? 3.0 : 0.0;
            double total = subtotal + tax + shipping;
            totalLabel.setText(String.format("TOTAL: $%.2f", total));
        });
        totalLabel.setText(String.format("TOTAL: $%.2f", subtotal + tax));

        Label messageLabel = new Label();

        Button confirmBtn = new Button("Confirm Order");
        confirmBtn.setOnAction(e -> {
            String delivery = deliveryCombo.getValue().startsWith("MAIL") ? "MAIL" : "PICKUP";
            Order order = orderSystem.makeOrder(delivery);

            if (order != null) {
                messageLabel.setText("Order confirmed! Auth: " + order.getAuthorizationNumber());
            } else {
                messageLabel.setText("Payment failed!");
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showMainMenu());

        VBox vbox = new VBox(10, titleLabel, summaryArea, deliveryCombo,
                totalLabel, messageLabel, confirmBtn, backBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 600, 550);
        primaryStage.setScene(scene);
    }
    //shows the previous orders when button for it is pressed
    //NOT accessible when not logged in so its grey if logged out
    private void showMyOrders() {
        Label titleLabel = new Label("MY ORDERS");

        TextArea ordersArea = new TextArea();
        ordersArea.setEditable(false);

        java.util.ArrayList<Order> customerOrders =
                orderSystem.getCustomerOrders(orderSystem.getCurrentCustomer().getCustomerId());

        StringBuilder ordersContent = new StringBuilder();
        if (customerOrders.isEmpty()) {
            ordersContent.append("You have no orders.");
        } else {
            for (Order order : customerOrders) {
                ordersContent.append(String.format("\nDate: %s | Total: $%.2f%n",
                        order.getOrderDate(), order.getTotal()));
                ordersContent.append("Products:\n");
                for (Product product : order.getOrderedProducts()) {
                    ordersContent.append(String.format("  - %s (x%d)%n",
                            product.getProductName(), product.getQuantity()));
                }
                ordersContent.append("-----------------------------------\n");
            }
        }
        ordersArea.setText(ordersContent.toString());

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showMainMenu());

        VBox vbox = new VBox(10, titleLabel, ordersArea, backBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 600, 550);
        primaryStage.setScene(scene);
    }
    //main just launches program
    public static void main(String[] args) {
        launch();
    }
}