package store.domain.products;

import store.domain.promotions.PromotionName;
import store.domain.promotions.Promotions;
import store.util.Parser;

public class ProductInfo {
    int price;
    int stock;
    PromotionName promotionName;

    public ProductInfo(String price, String stock, String promotionName, Promotions promotions) {
        this.price = Parser.toInt(price);
        this.stock = Parser.toInt(stock);

        if (promotionName.equals("null")) {
            this.promotionName = null;
            return;
        }
        this.promotionName = promotions.getPromotionName(promotionName);
    }
}
