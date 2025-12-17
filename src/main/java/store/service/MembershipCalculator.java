package store.service;

public class MembershipCalculator {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    public int calculate(int nonPromotionAmount) {
        int discount = (int) (nonPromotionAmount * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discount, MAX_MEMBERSHIP_DISCOUNT);
    }
}