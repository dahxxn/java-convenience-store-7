package store.controller;

import java.util.List;
import store.domain.products.Products;
import store.domain.promotions.Promotions;
import store.util.ReadMdFile;
import store.view.OutputView;

public class ConvenienceSystem {
    Promotions promotions;
    Products products;

    public void run() {
        initSystem();
        process();
    }

    public void initSystem() {
        ReadMdFile reader = new ReadMdFile();
        List<String> rawPromotionContents = reader.readMdFile("promotions.md");
        List<String> rawProductsContents = reader.readMdFile("products.md");

        promotions = new Promotions(rawPromotionContents);
        products = new Products(rawProductsContents, promotions);
    }

    public void process() {
        boolean keepGoing = true;
        while (keepGoing) {
            OutputView.print("안녕하세요. W편의점입니다.");
            OutputView.print(products.getCurrentProductStatus());
            keepGoing = false;
        }
    }
}
