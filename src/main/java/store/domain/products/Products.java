package store.domain.products;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import store.domain.promotions.PromotionName;
import store.domain.promotions.Promotions;
import store.error.BusinessException;
import store.error.ErrorCode;

public class Products {
    private HashMap<ProductName, ProductInfo> products;
    private LinkedHashSet<String> productNameSet;
    private HashMap<ProductName, ProductInfo> promotionProducts;

    private static final String PRODUCT_INFO_FORMAT = "- %s %,d원 %s %s\n";


    public Products(List<String> rawProductsContents, Promotions promotions) {
        products = new HashMap<>();
        promotionProducts = new HashMap<>();
        productNameSet = new LinkedHashSet<>();

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
                    productNameSet.add(name);
                    continue;
                }

                promotionProducts.put(productname, productInfo);
                if (!productNameSet.contains(name)) {
                    products.put(productname, new ProductInfo(price, "0", "null", promotions));
                }
                productNameSet.add(name);
            } catch (IndexOutOfBoundsException e) {
                throw new BusinessException(ErrorCode.PRODUCT_FORMAT_ERROR);
            }
        }
    }

    public String getCurrentProductStatus() {
        StringBuilder status = new StringBuilder();

        status.append("현재 보유하고 있는 상품입니다.\n\n");

        for (String name : productNameSet) {
            ProductName promotionProductName = getPromotionProductName(name);
            if (promotionProductName != null) {
                status.append(getPromotionProductStatus(promotionProductName));
            }

            ProductName productName = getProductName(name);
            if (productName != null) {
                status.append(getProductStatus(productName));
                continue;
            }
        }

        status.append("\n");

        return status.toString();
    }

    public boolean isContain(String productName) {
        return productNameSet.contains(productName);
    }

    public boolean isCoverageStock(int quantity, String name) {
        return totalStock(name) >= quantity;
    }

    public boolean isPromotionProduct(String productName, Promotions promotions) {
        ProductName promotionProductName = getPromotionProductName(productName);
        if (promotionProductName != null) {

            ProductInfo productInfo = promotionProducts.get(promotionProductName);
            PromotionName promotionName = productInfo.promotionName;

            if (promotions.isAvailablePromotion(promotionName)) {
                return true;
            }
        }
        return false;
    }

    public PromotionName getPromotionName(String productName) {
        ProductName promotionProductName = getPromotionProductName(productName);
        if (promotionProductName != null) {

            ProductInfo productInfo = promotionProducts.get(promotionProductName);
            return productInfo.promotionName;
        }
        return null;
    }

    public int checkPromotionStock(String name, int getQuantity) {
        ProductName promotionProductName = getPromotionProductName(name);
        ProductInfo productInfo = promotionProducts.get(promotionProductName);
        return getQuantity - productInfo.stock;
    }

    private int totalStock(String name) {
        ProductName promotionProductName = getPromotionProductName(name);
        ProductName productName = getProductName(name);

        int totalStock = 0;

        if (promotionProductName != null) {
            totalStock += promotionProducts.get(promotionProductName).stock;
        }
        if (productName != null) {
            totalStock += products.get(productName).stock;
        }

        return totalStock;
    }

    private String getProductStatus(ProductName productName) {
        ProductInfo productInfo = products.get(productName);
        return PRODUCT_INFO_FORMAT.formatted(productName.getName(), productInfo.price,
                productInfo.stock == 0 ? "재고없음" : productInfo.stock + "개", "");
    }

    private String getPromotionProductStatus(ProductName productName) {
        ProductInfo productInfo = promotionProducts.get(productName);
        return PRODUCT_INFO_FORMAT.formatted(productName.getName(), productInfo.price,
                productInfo.stock == 0 ? "재고없음" : productInfo.stock + "개",
                productInfo.promotionName.getName());
    }


    private ProductName getProductName(String productName) {
        for (ProductName productName1 : products.keySet()) {
            if (productName1.getName().equals(productName)) {
                return productName1;
            }
        }
        return null;
    }

    private ProductName getPromotionProductName(String productName) {
        for (ProductName productName1 : promotionProducts.keySet()) {
            if (productName1.getName().equals(productName)) {
                return productName1;
            }
        }
        return null;
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
