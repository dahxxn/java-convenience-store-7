package store.domain.receipt;

import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private final Map<String, PurchaseDetail> purchaseDetails;
    private final Map<String, Integer> giftDetails;
    private int totalAmount;
    private int promotionDiscount;
    private int membershipDiscount;

    public Receipt() {
        this.purchaseDetails = new HashMap<>();
        this.giftDetails = new HashMap<>();
        this.totalAmount = 0;
        this.promotionDiscount = 0;
        this.membershipDiscount = 0;
    }

    public void addPurchase(String productName, int quantity, int price) {
        purchaseDetails.put(productName, new PurchaseDetail(quantity, price));
        totalAmount += quantity * price;
    }

    public void addGift(String productName, int quantity) {
        giftDetails.put(productName, giftDetails.getOrDefault(productName, 0) + quantity);
    }

    public void addPromotionDiscount(int discount) {
        this.promotionDiscount += discount;
    }

    public void setMembershipDiscount(int discount) {
        this.membershipDiscount = discount;
    }

    public Map<String, PurchaseDetail> getPurchaseDetails() {
        return purchaseDetails;
    }

    public Map<String, Integer> getGiftDetails() {
        return giftDetails;
    }

    public int getTotalQuantity() {
        return purchaseDetails.values().stream()
                .mapToInt(PurchaseDetail::getQuantity)
                .sum();
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return totalAmount - promotionDiscount - membershipDiscount;
    }

    public static class PurchaseDetail {
        private final int quantity;
        private final int price;

        public PurchaseDetail(int quantity, int price) {
            this.quantity = quantity;
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getPrice() {
            return price;
        }

        public int getTotalPrice() {
            return quantity * price;
        }
    }
}