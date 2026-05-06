public class Product
{
    // Private fields for product
    private String productId;
    private String productName;
    private String description;
    private double regularPrice;
    private double salePrice;
    private int quantity;

    // Constructor for product
    public Product(String productId, String productName, String description, double regularPrice, double salePrice, int quantity)
    {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.quantity = quantity;
    }

    // Copy constructor so you can have products in a cart and products in stock
    public Product(Product other)
    {
        this.productId = other.productId;
        this.productName = other.productName;
        this.description = other.description;
        this.regularPrice = other.regularPrice;
        this.salePrice = other.salePrice;
        this.quantity = other.quantity;
    }

    // Display product information for catalog
    public void displayProductInfo()
    {
        System.out.printf("%-10s %-25s $%-8.2f $%-8.2f%n", productId, productName, regularPrice, salePrice);
        System.out.println("           " + description);
    }

    // Display product info for the order/cart so it can use quantity and multiply
    public void displayWithQuantity()
    {
        System.out.printf("%-25s x%-3d @ $%-8.2f = $%.2f%n",
                productName, quantity, salePrice, salePrice * quantity);
    }

    // Update quantity
    public void updateQuantity(int quantity)
    {
        if (quantity >= 0)
        {
            this.quantity = quantity;
        }
    }

    // Getters
    public String getProductId()
    {
        return productId;
    }
    public String getProductName()
    {
        return productName;
    }
    public String getDescription()
    {
        return description;
    }
    public double getRegularPrice()
    {
        return regularPrice;
    }
    public double getSalePrice()
    {
        return salePrice;
    }
    public int getQuantity()
    {
        return quantity;
    }

    // Setters
    public void setProductId(String productId)
    {
        this.productId = productId;
    }
    public void setProductName(String productName)
    {
        this.productName = productName;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public void setRegularPrice(double regularPrice)
    {
        this.regularPrice = regularPrice;
    }
    public void setSalePrice(double salePrice)
    {
        this.salePrice = salePrice;
    }
    public void setQuantity(int quantity)
    {
        if (quantity >= 0)
        {
            this.quantity = quantity;
        }
    }
}