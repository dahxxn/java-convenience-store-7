package store.domain.products;

import store.domain.promotions.Promotion;

public class Product {
    String name;
    int price;
    int quantity;
    Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public void decreaseQuantity(int amount) {
        quantity -= amount;
    }

    public boolean isSufficientStock(int requestQuantity) {
        return quantity >= requestQuantity;
    }
}
