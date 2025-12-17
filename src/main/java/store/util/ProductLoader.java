package store.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.domain.products.Product;
import store.domain.promotions.Promotion;
import store.error.BusinessException;
import store.error.ErrorCode;

public class ProductLoader {
    private static final String PRODUCT_FILES_DIR = "src/main/resources/products.md";
    private static final String DELIMITER = ",";
    private static final String NULL_PROMOTION = "null";

    public List<Product> loadFile(List<Promotion> promotions) {
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCT_FILES_DIR))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                Product product = (Product) parse(line, promotions);
                products.add(product);
            }

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.MD_FILE_READE_ERROR);
        }

        return products;
    }

    public Object parse(String line, List<Promotion> promotions) {
        String[] parts = line.split(DELIMITER);

        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        String promotionName = parts[3];

        Promotion promotion = findPromotion(promotionName, promotions);

        return new Product(name, price, quantity, promotion);
    }

    private Promotion findPromotion(String promotionName, List<Promotion> promotions) {
        if (promotionName.equals(NULL_PROMOTION)) {
            return null;
        }

        return promotions.stream()
                .filter(promo -> promo.getName().equals(promotionName))
                .findFirst()
                .orElse(null);
    }
}
