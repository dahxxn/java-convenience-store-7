package store;

import java.util.List;
import store.controller.StoreController;
import store.domain.Inventory.Inventory;
import store.domain.products.Product;
import store.domain.promotions.Promotion;
import store.util.ProductLoader;
import store.util.PromotionLoader;

public class Application {
    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    public void run() {
        Inventory inventory = loadInventory();
        StoreController controller = new StoreController(inventory);
        controller.run();
    }

    private Inventory loadInventory() {
        PromotionLoader promotionLoader = new PromotionLoader();
        List<Promotion> promotions = promotionLoader.loadFile();

        ProductLoader productLoader = new ProductLoader();
        List<Product> products = productLoader.loadFile(promotions);

        return new Inventory(products);
    }

}
