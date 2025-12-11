package store.domain.products;

import java.util.HashMap;
import java.util.List;
import store.domain.promotions.Promotions;
import store.error.BusinessException;
import store.error.ErrorCode;

public class Products {
    private HashMap<ProductName, ProductInfo> products;
    private HashMap<ProductName, ProductInfo> promotionProducts;

    public Products(List<String> rawProductsContents, Promotions promotions) {
        products = new HashMap<>();
        promotionProducts = new HashMap<>();

        for (String rawPromotionInfo : rawProductsContents) {
            try {
                String[] infos = rawPromotionInfo.split(",");

                String name = infos[0].trim();
                String price = infos[1].trim();
                String stock = infos[2].trim();
                String promotionName = infos[3].trim();

                ProductName productname = new ProductName(name);
                ProductInfo productInfo = new ProductInfo(price, stock, promotionName, promotions);

                if (promotionName.equals("null")) {
                    products.put(productname, productInfo);
                    continue;
                }
                promotionProducts.put(productname, productInfo);

            } catch (IndexOutOfBoundsException e) {
                throw new BusinessException(ErrorCode.PRODUCT_FORMAT_ERROR);
            }
        }
    }

    public void checkProducts() {
        System.out.println("[일반 상품 재고]");

        for (ProductName productName : products.keySet()) {
            ProductInfo productInfo = products.get(productName);
            System.out.printf("%s, %d, %d, %s \n", productName.name,
                    productInfo.price,
                    productInfo.stock,
                    productInfo.promotionName != null ? productInfo.promotionName.getName() : "null");
        }

        System.out.println("[프로모션 상품 재고]");

        for (ProductName productName : promotionProducts.keySet()) {
            ProductInfo productInfo = promotionProducts.get(productName);
            System.out.printf("%s, %d, %d, %s \n", productName.name,
                    productInfo.price,
                    productInfo.stock,
                    productInfo.promotionName != null ? productInfo.promotionName.getName() : "null");
        }
    }
}
