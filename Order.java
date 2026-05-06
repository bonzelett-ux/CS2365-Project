import java.util.ArrayList;
import java.util.Date;

public class Order
{
    // Private attributes
    private Date orderDate;
    private String customerId;
    private ArrayList<Product> orderedProducts;
    private String deliveryMethod; // "MAIL" or "PICKUP"
    private double shippingFee;
    private double subtotal;
    private double tax;
    private double total;
    private String authorizationNumber;

    // Constructor
    public Order(String customerId, ArrayList<Product> orderedProducts, String deliveryMethod)
    {
        this.orderDate = new Date();
        this.customerId = customerId;
        this.orderedProducts = new ArrayList<>(orderedProducts);
        this.deliveryMethod = deliveryMethod;
        if(deliveryMethod.equals("MAIL"))
        {
            this.shippingFee = 3.0;
        }
        else
        {
            this.shippingFee = 0;
        }
    }

    // Calculate subtotal from all ordered products
    public double calculateSubtotal()
    {
        subtotal = 0.0;
        for (int i = 0; i < orderedProducts.size(); i++)
        {
            Product product = orderedProducts.get(i);
            subtotal += product.getSalePrice() * product.getQuantity();
        }
        return subtotal;
    }

    // Calculate tax based on subtotal
    public double calculateTax(double taxRate)
    {
        this.tax = calculateSubtotal() * taxRate;
        return this.tax;
    }

    // Calculate total (subtotal + tax + shipping)
    public double calculateTotal()
    {
        this.total = calculateSubtotal() + this.tax + this.shippingFee;
        return this.total;
    }

    // Set authorization number from bank
    public void setAuthorizationNumber(String authNumber)
    {
        this.authorizationNumber = authNumber;
    }

    // Display complete order summary
    public void displayOrderSummary()
    {
        System.out.println("\n                          ORDER SUMMARY");
        System.out.println("Order Date: " + orderDate);
        System.out.println("Customer ID: " + customerId);
        System.out.println("\nOrdered Products:");
        System.out.println("------------------------------------------------");

        for (int i = 0; i < orderedProducts.size(); i++)
        {
            Product product = orderedProducts.get(i);
            System.out.printf("%-20s x%d @ $%.2f = $%.2f%n",product.getProductName(),product.getQuantity(),product.getSalePrice(),product.getSalePrice() * product.getQuantity());
        }

        System.out.println("-------------------------------------------");
        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("Tax: $%.2f%n", tax);
        System.out.printf("Delivery Method: %s%n", deliveryMethod);
        System.out.printf("Shipping Fee: $%.2f%n", shippingFee);
        System.out.println("----------------------------------------");
        System.out.printf("TOTAL: $%.2f%n", total);

        if (authorizationNumber != null)
        {
            System.out.println("Authorization Number: " + authorizationNumber);
        }
    }

    // Display brief order info (for order history)
    public void displayOrderInfo()
    {
        System.out.printf("\nDate: %s | Total: $%.2f%n", orderDate, total);
        System.out.println("Products:");
        for (int i = 0; i < orderedProducts.size(); i++)
        {
            Product product = orderedProducts.get(i);
            System.out.printf("  - %s (x%d)%n", product.getProductName(), product.getQuantity());
        }
    }

    // Check if order is confirmed (has authorization number)
    public boolean isConfirmed()
    {
        return authorizationNumber != null && !authorizationNumber.isEmpty();
    }

    // Getters
    public Date getOrderDate()
    {
        return orderDate;
    }
    public String getCustomerId()
    {
        return customerId;
    }
    public ArrayList<Product> getOrderedProducts()
    {
        return orderedProducts;
    }
    public String getDeliveryMethod()
    {
        return deliveryMethod;
    }
    public double getShippingFee()
    {
        return shippingFee;
    }
    public double getSubtotal()
    {
        return subtotal;
    }
    public double getTax()
    {
        return tax;
    }
    public double getTotal()
    {
        return total;
    }
    public String getAuthorizationNumber()
    {
        return authorizationNumber;
    }

    // Setters
    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }
    public void setOrderedProducts(ArrayList<Product> orderedProducts)
    {
        this.orderedProducts = orderedProducts;
    }
    public void setDeliveryMethod(String deliveryMethod)
    {
        this.deliveryMethod = deliveryMethod;
        this.shippingFee = deliveryMethod.equalsIgnoreCase("MAIL") ? 3.00 : 0.00;
    }
    public void setShippingFee(double shippingFee)
    {
        this.shippingFee = shippingFee;
    }
    public void setSubtotal(double subtotal)
    {
        this.subtotal = subtotal;
    }
    public void setTax(double tax)
    {
        this.tax = tax;
    }
    public void setTotal(double total)
    {
        this.total = total;
    }
}