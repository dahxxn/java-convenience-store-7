package store.view;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import store.domain.Inventory.Inventory;
import store.domain.products.Product;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String CURRENT_PRODUCTS_MESSAGE = "현재 보유하고 있는 상품입니다.\n";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.KOREA);


    public static void print(String message) {
        System.out.println(message);
    }


    public static void println() {
        System.out.println();
    }

    public void printWelcome() {
        print(WELCOME_MESSAGE);
        print(CURRENT_PRODUCTS_MESSAGE);
    }

    public void printInventory(Inventory inventory) {
        Map<String, List<Product>> groupedProducts = inventory.getGroupedProducts();

        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            printProductGroup(entry.getValue());
        }
        println();
    }

    private void printProductGroup(List<Product> products) {
        boolean hasPromotionProduct = products.stream()
                .anyMatch(Product::hasPromotion);

        if (hasPromotionProduct) {
            products.stream()
                    .filter(Product::hasPromotion)
                    .forEach(this::printProduct);

            List<Product> regularProducts = products.stream()
                    .filter(product -> !product.hasPromotion())
                    .toList();

            if (regularProducts.isEmpty()) {
                Product promoProduct = products.get(0);
                printOutOfStockProduct(promoProduct.getName(), promoProduct.getPrice());
            } else {
                regularProducts.forEach(this::printProduct);
            }
        } else {
            products.forEach(this::printProduct);
        }
    }

    private void printProduct(Product product) {
        String quantityText = getQuantityText(product.getQuantity());
        String promotionText = getPromotionText(product);

        System.out.printf("- %s %s원 %s%s%n",
                product.getName(),
                formatNumber(product.getPrice()),
                quantityText,
                promotionText);
    }

    private void printOutOfStockProduct(String name, int price) {
        System.out.printf("- %s %s원 %s%n",
                name,
                formatNumber(price),
                OUT_OF_STOCK);
    }

    private String getQuantityText(int quantity) {
        if (quantity == 0) {
            return OUT_OF_STOCK;
        }
        return formatNumber(quantity) + "개";
    }

    private String getPromotionText(Product product) {
        if (product.hasPromotion()) {
            return " " + product.getPromotion().getName();
        }
        return "";
    }

    private String formatNumber(int number) {
        return NUMBER_FORMAT.format(number);
    }

}