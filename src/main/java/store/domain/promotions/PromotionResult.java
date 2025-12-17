package store.domain.promotions;

public class PromotionResult {
    private final boolean hasPromotion;
    private final int promotionAppliedQuantity;
    private final int freeQuantity;
    private final int regularQuantity;
    private final int additionalQuantity;

    public PromotionResult(boolean hasPromotion, int promotionAppliedQuantity, int freeQuantity,
                           int regularQuantity, int additionalQuantity) {
        this.hasPromotion = hasPromotion;
        this.promotionAppliedQuantity = promotionAppliedQuantity;
        this.freeQuantity = freeQuantity;
        this.regularQuantity = regularQuantity;
        this.additionalQuantity = additionalQuantity;
    }

    public boolean hasPromotion() {
        return hasPromotion;
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
        return hasPromotion && regularQuantity > 0;
    }

}