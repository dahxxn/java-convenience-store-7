package store;

import java.util.List;
import store.domain.Inventory.Inventory;
import store.domain.products.Product;
import store.domain.promotions.Promotion;
import store.util.ProductLoader;
import store.util.PromotionLoader;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        PromotionLoader promotionLoader = new PromotionLoader();
        List<Promotion> promotions = promotionLoader.loadFile();

        ProductLoader productLoader = new ProductLoader();
        List<Product> products = productLoader.loadFile(promotions);

        Inventory inventory = new Inventory(products);

        OutputView outputView = new OutputView();
        outputView.printWelcome();
        outputView.printInventory(inventory);

    }

}
