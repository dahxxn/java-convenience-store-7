package store.view;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import store.domain.Inventory.Inventory;
import store.domain.products.Product;
import store.domain.receipt.Receipt;

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

    public void printReceipt(Receipt receipt) {
        System.out.println("\n==============W 편의점================");
        printPurchaseDetails(receipt);
        printGiftDetails(receipt);
        printAmountDetails(receipt);
    }

    private void printPurchaseDetails(Receipt receipt) {
        System.out.println("상품명\t\t수량\t금액");

        for (Map.Entry<String, Receipt.PurchaseDetail> entry : receipt.getPurchaseDetails().entrySet()) {
            String productName = entry.getKey();
            Receipt.PurchaseDetail detail = entry.getValue();

            System.out.printf("%s\t\t%d\t%s%n",
                    productName,
                    detail.getQuantity(),
                    formatNumber(detail.getTotalPrice()));
        }
    }

    private void printGiftDetails(Receipt receipt) {
        if (receipt.getGiftDetails().isEmpty()) {
            return;
        }

        System.out.println("=============증정===============");
        for (Map.Entry<String, Integer> entry : receipt.getGiftDetails().entrySet()) {
            System.out.printf("%s\t\t%d%n", entry.getKey(), entry.getValue());
        }
    }

    private void printAmountDetails(Receipt receipt) {
        System.out.println("====================================");
        System.out.printf("총구매액\t\t%d\t%s%n",
                receipt.getTotalQuantity(),
                formatNumber(receipt.getTotalAmount()));
        System.out.printf("행사할인\t\t\t-%s%n",
                formatNumber(receipt.getPromotionDiscount()));
        System.out.printf("멤버십할인\t\t\t-%s%n",
                formatNumber(receipt.getMembershipDiscount()));
        System.out.printf("내실돈\t\t\t %s%n",
                formatNumber(receipt.getFinalAmount()));
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