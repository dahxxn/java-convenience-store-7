package store.domain.promotions;

public class PromotionResult {
    private final int promotionAppliedQuantity;
    private final int freeQuantity;
    private final int regularQuantity;
    private final int additionalQuantity;
    
    public PromotionResult(int promotionAppliedQuantity, int freeQuantity,
                           int regularQuantity, int additionalQuantity) {
        this.promotionAppliedQuantity = promotionAppliedQuantity;
        this.freeQuantity = freeQuantity;
        this.regularQuantity = regularQuantity;
        this.additionalQuantity = additionalQuantity;
    }

    public int getPromotionAppliedQuantity() {
        return promotionAppliedQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getRegularQuantity() {
        return regularQuantity;
    }

    public int getAdditionalQuantity() {
        return additionalQuantity;
    }

    public boolean hasAdditionalPromotion() {
        return additionalQuantity > 0;
    }

    public boolean hasRegularPriceItems() {
        return regularQuantity > 0;
    }
}