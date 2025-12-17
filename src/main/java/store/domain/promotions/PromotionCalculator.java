package store.domain.promotions;

import store.domain.Inventory.Inventory;
import store.domain.products.Product;

public class PromotionCalculator {

    public PromotionResult calculate(String productName, int requestQuantity, Inventory inventory) {
        Product promotionProduct = inventory.findProduct(productName, true);

        if (promotionProduct == null) {
            return new PromotionResult(false, 0, 0, 0, 0);
        }

        Promotion promotion = promotionProduct.getPromotion();
        if (promotion == null || !promotion.isActive()) {
            return new PromotionResult(false, 0, 0, 0, 0);
        }

        return calculateWithPromotion(promotionProduct, requestQuantity, inventory);
    }

    private PromotionResult calculateWithPromotion(Product promotionProduct,
                                                   int requestQuantity,
                                                   Inventory inventory) {
        Promotion promotion = promotionProduct.getPromotion();
        int promotionStock = promotionProduct.getQuantity();
        int promotionUnit = promotion.getPromotionUnit();

        if (promotionStock == 0) {
            return new PromotionResult(false, 0, 0, 0, 0);
        }

        int maxPromotionSets = promotionStock / promotionUnit;

        int requestedSets = requestQuantity / promotionUnit;

        int appliedSets = Math.min(maxPromotionSets, requestedSets);

        int promotionAppliedQuantity = appliedSets * promotionUnit;
        int freeQuantity = appliedSets * promotion.getGet();

        int remainingQuantity = requestQuantity - promotionAppliedQuantity;

        int additionalQuantity = checkAdditionalPromotion(
                remainingQuantity, promotionStock, promotionAppliedQuantity, promotion);

        int regularQuantity = calculateRegularQuantity(
                remainingQuantity, additionalQuantity, promotionStock, promotionAppliedQuantity);

        return new PromotionResult(true, promotionAppliedQuantity, freeQuantity,
                regularQuantity, additionalQuantity);
    }

    private int checkAdditionalPromotion(int remainingQuantity, int promotionStock,
                                         int promotionAppliedQuantity, Promotion promotion) {
        if (remainingQuantity == promotion.getBuy() &&
                promotionStock >= promotionAppliedQuantity + promotion.getPromotionUnit()) {
            return promotion.getGet();
        }
        return 0;
    }

    private int calculateRegularQuantity(int remainingQuantity, int additionalQuantity,
                                         int promotionStock, int promotionAppliedQuantity) {
        if (additionalQuantity > 0) {
            return 0;
        }

        if (remainingQuantity <= 0) {
            return 0;
        }

        int availablePromotionStock = promotionStock - promotionAppliedQuantity;

        if (remainingQuantity <= availablePromotionStock) {
            return remainingQuantity;
        }

        return remainingQuantity;
    }
}