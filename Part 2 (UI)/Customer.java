package com.example.oop3;

public class Customer
{
    // Private fields
    private String customerId;
    private String password;
    private String name;
    private String address;
    private String creditCard;
    private boolean loggedIn;
    private String securityQuestion;
    private String securityAnswer;

    // Default Constructor
    public Customer()
    {
        this.loggedIn = false;
    }

    // Constructor to initialize fields
    public Customer(String customerId, String password, String name, String address, String creditCard, String securityQuestion, String securityAnswer)
    {
        this.customerId = customerId;
        this.password = password;
        this.name = name;
        this.address = address;
        this.creditCard = creditCard;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.loggedIn = false;
    }

    // Validates password
    // password is a string passed into the function to validate it
    public static boolean validatePasswordFormat(String password)
    {
        if (password == null || password.length() < 6)
        {
            return false;
        }
        boolean hasDigit = false;
        boolean hasUpperCase = false;
        boolean hasSpecialChar = false;
        String specialChars = "!@#$%&*";
        char[] passwordArray = password.toCharArray();
        for (int i = 0; i < passwordArray.length; i++)
        {
            char c = passwordArray[i];
            if (Character.isDigit(c))
            {
                hasDigit = true;
            }
            if (Character.isUpperCase(c))
            {
                hasUpperCase = true;
            }

            if (specialChars.indexOf(c) >= 0)
            {
                hasSpecialChar = true;
            }
        }
        return hasDigit && hasUpperCase && hasSpecialChar;
    }

    // Validate login credentials
    public boolean validateCredentials(String inputPassword)
    {
        return this.password != null && this.password.equals(inputPassword);
    }

    // Validate security answer
    public boolean validateSecurityAnswer(String inputAnswer)
    {
        return this.securityAnswer != null &&
                this.securityAnswer.equalsIgnoreCase(inputAnswer);
    }

    // Log in the customer
    public boolean login(String inputPassword, String securityAnswerInput)
    {
        if (validateCredentials(inputPassword) && validateSecurityAnswer(securityAnswerInput))
        {
            this.loggedIn = true;
            return true;
        }
        return false;
    }

    // Log out the customer
    public void logout()
    {
        this.loggedIn = false;
    }

    // Displays customer info
    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Credit Card: " + creditCard);
        System.out.println("Logged In: " + loggedIn);
    }

    // Getters
    public String getCustomerId()
    {
        return customerId;
    }
    public String getPassword()
    {
        return password;
    }
    public String getName()
    {
        return name;
    }
    public String getAddress()
    {
        return address;
    }
    public String getCreditCard()
    {
        return creditCard;
    }
    public boolean isLoggedIn()
    {
        return loggedIn;
    }
    public String getSecurityQuestion()
    {
        return securityQuestion;
    }
    public String getSecurityAnswer()
    {
        return securityAnswer;
    }

    // Setters
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    public void setPassword(String password)
    {
        if (validatePasswordFormat(password))
        {
            this.password = password;
        }
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public void setCreditCard(String creditCard)
    {
        this.creditCard = creditCard;
    }
    public void setLoggedIn(boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }
    public void setSecurityQuestion(String securityQuestion)
    {
        this.securityQuestion = securityQuestion;
    }
    public void setSecurityAnswer(String securityAnswer)
    {
        this.securityAnswer = securityAnswer;
    }
}