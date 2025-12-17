package store.service;

import java.util.List;
import store.domain.Inventory.Inventory;
import store.domain.products.Product;
import store.domain.promotions.PromotionCalculator;
import store.domain.promotions.PromotionResult;
import store.domain.receipt.Receipt;
import store.domain.shopping.ShoppingItem;

public class PurchaseService {
    private final PromotionCalculator promotionCalculator;
    private final MembershipCalculator membershipCalculator;
    private final Inventory inventory;

    public PurchaseService(Inventory inventory) {
        this.inventory = inventory;
        this.promotionCalculator = new PromotionCalculator();
        this.membershipCalculator = new MembershipCalculator();
    }

    public PromotionResult getPromotionResult(ShoppingItem item) {
        return promotionCalculator.calculate(
                item.getProductName(),
                item.getQuantity(),
                inventory
        );
    }

    public Receipt createReceipt(List<ShoppingItem> items, List<PromotionResult> results, boolean applyMembership) {
        Receipt receipt = new Receipt();
        int nonPromotionAmount = 0;

        for (int i = 0; i < items.size(); i++) {
            ShoppingItem item = items.get(i);
            PromotionResult result = results.get(i);
            int price = getProductPrice(item.getProductName());

            receipt.addPurchase(item.getProductName(), item.getQuantity(), price);

            if (result.hasPromotion()) {
                if (result.getFreeQuantity() > 0) {
                    receipt.addGift(item.getProductName(), result.getFreeQuantity());
                    receipt.addPromotionDiscount(result.getFreeQuantity() * price);
                }
                nonPromotionAmount += result.getRegularQuantity() * price;
            } else {
                nonPromotionAmount += item.getQuantity() * price;
            }
        }

        if (applyMembership) {
            int membershipDiscount = membershipCalculator.calculate(nonPromotionAmount);
            receipt.setMembershipDiscount(membershipDiscount);
        }

        return receipt;
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