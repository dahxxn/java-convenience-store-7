package store.controller;

import java.util.List;
import store.domain.products.Products;
import store.domain.promotions.Promotions;
import store.util.ReadMdFile;

public class ConvenienceSystem {
    public void run() {
        ReadMdFile reader = new ReadMdFile();
        List<String> rawPromotionContents = reader.readMdFile("promotions.md");
        List<String> rawProductsContents = reader.readMdFile("products.md");

        Promotions promotions = new Promotions(rawPromotionContents);
        Products products = new Products(rawProductsContents, promotions);
    }
}
