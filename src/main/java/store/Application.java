package store;

import java.util.List;
import store.domain.products.Product;
import store.domain.promotions.Promotion;
import store.util.ProductLoader;
import store.util.PromotionLoader;

public class Application {
    public static void main(String[] args) {
        PromotionLoader promotionLoader = new PromotionLoader();
        List<Promotion> promotions = promotionLoader.loadFile();

        ProductLoader productLoader = new ProductLoader();
        List<Product> products = productLoader.loadFile(promotions);

    }

}
