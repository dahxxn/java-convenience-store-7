package store.service;

import java.util.List;
import store.domain.Inventory.Inventory;
import store.domain.products.Product;
import store.domain.promotions.PromotionCalculator;
import store.domain.promotions.PromotionResult;
import store.domain.shopping.ShoppingItem;

public class PurchaseService {
    private final PromotionCalculator promotionCalculator;
    private final Inventory inventory;

    public PurchaseService(Inventory inventory) {
        this.inventory = inventory;
        this.promotionCalculator = new PromotionCalculator();
    }

    public PromotionResult getPromotionResult(ShoppingItem item) {
        return promotionCalculator.calculate(
                item.getProductName(),
                item.getQuantity(),
                inventory
        );
    }

    public void processPayment(List<ShoppingItem> items) {
        for (ShoppingItem item : items) {
            decreaseStock(item.getProductName(), item.getQuantity());
        }
    }

    private void decreaseStock(String productName, int quantity) {
        Product promotionProduct = inventory.findProduct(productName, true);
        if (promotionProduct != null) {
            int promotionDecrease = Math.min(quantity, promotionProduct.getQuantity());
            promotionProduct.decreaseQuantity(promotionDecrease);
            quantity -= promotionDecrease;
        }

        if (quantity > 0) {
            Product regularProduct = inventory.findProduct(productName, false);
            if (regularProduct != null) {
                regularProduct.decreaseQuantity(quantity);
            }
        }
    }

    public int getProductPrice(String productName) {
        List<Product> products = inventory.findProductsByName(productName);
        return products.isEmpty() ? 0 : products.get(0).getPrice();
    }
}