package store.domain.shopping;

public class ShoppingItem {
    String productName;
    int quantity;

    public ShoppingItem(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void setQuantity(int amount) {
        this.quantity = amount;
    }


}
