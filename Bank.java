import java.util.ArrayList;

public class Bank
{
    // Private fields for bank
    private ArrayList<String> creditCards;
    private ArrayList<Double> creditLimits;
    private ArrayList<Double> currentBalances;

    // Constructor for bank class
    public Bank()
    {
        creditCards = new ArrayList<>();
        creditLimits = new ArrayList<>();
        currentBalances = new ArrayList<>();
    }

    // Add a credit card to the bank system
    public void addCreditCard(String creditCard, double creditLimit, double currentBalance)
    {
        if (!creditCards.contains(creditCard))
        {
            creditCards.add(creditCard);
            creditLimits.add(creditLimit);
            currentBalances.add(currentBalance);
        }
    }

    // Validate if credit card exists in the creditCards arraylist for the bank
    public boolean validateCreditCard(String creditCard)
    {
        return creditCards.contains(creditCard);
    }
    // Get index of a credit card in the arraylist of credit cards
    private int getCreditCardIndex(String creditCard)
    {
        return creditCards.indexOf(creditCard);
    }
    // Get available credit for a specific card
    public double getAvailableCredit(String creditCard)
    {
        return creditLimits.get(getCreditCardIndex(creditCard)) - currentBalances.get(getCreditCardIndex(creditCard));
    }

    // Process payment allows payment and returns a 4 digit authorization num if it is less than credit limit and the card exists
    //  and it also doesnt allow the order to go if the needs arent met
    public String processPayment(String creditCard, double amount)
    {
        if (validateCreditCard(creditCard)==false)
        {
            System.out.println("Bank: Invalid credit card number.");
            return null;
        }

        int index = getCreditCardIndex(creditCard);
        double availableCredit = getAvailableCredit(creditCard);
        if (amount <= 0)
        {
            System.out.println("Bank: Invalid payment amount.");
            return null;
        }

        if (amount > availableCredit)
        {
            System.out.println("Bank: Insufficient credit. Available: $" + String.format("%.2f", availableCredit));
            return null;
        }
        double newBalance = currentBalances.get(index) + amount;
        currentBalances.set(index, newBalance);
        int authNum = (int)(Math.random() * 9000) + 1000;
        System.out.println("Bank: Payment approved. Authorization: " + authNum);
        return String.valueOf(authNum);
    }
}