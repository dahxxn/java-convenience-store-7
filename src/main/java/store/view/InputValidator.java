package store.view;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.Inventory.Inventory;
import store.domain.shopping.ShoppingItem;
import store.error.BusinessException;
import store.error.ErrorCode;

public class InputValidator {
    private static final Pattern PURCHASE_PATTERN = Pattern.compile("\\[([가-힣a-zA-Z]+)-(\\d+)\\]");
    private static final String YES = "Y";
    private static final String NO = "N";

    public List<ShoppingItem> validateAndParsePurchaseInput(String input, Inventory inventory) {
        if (input == null || input.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        List<ShoppingItem> items = parsePurchaseInput(input);
        validateProducts(items, inventory);

        return items;
    }

    private List<ShoppingItem> parsePurchaseInput(String input) {
        List<ShoppingItem> items = new ArrayList<>();
        Matcher matcher = PURCHASE_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        matcher.reset();
        while (matcher.find()) {
            String productName = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));

            if (quantity <= 0) {
                throw new BusinessException(ErrorCode.INVALID_INPUT);
            }

            items.add(new ShoppingItem(productName, quantity));
        }

        return items;
    }

    private void validateProducts(List<ShoppingItem> items, Inventory inventory) {
        for (ShoppingItem item : items) {
            validateProductExists(item.getProductName(), inventory);
            validateSufficientStock(item.getProductName(), item.getQuantity(), inventory);
        }
    }

    private void validateProductExists(String productName, Inventory inventory) {
        if (!inventory.hasProduct(productName)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    private void validateSufficientStock(String productName, int quantity, Inventory inventory) {
        int totalQuantity = inventory.getTotalQuantity(productName);
        if (totalQuantity < quantity) {
            throw new BusinessException(ErrorCode.BUY_COUNT_EXCEED_STOCK);
        }
    }

    public boolean validateYesOrNo(String input) {
        if (input == null || (!input.equals(YES) && !input.equals(NO))) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        return input.equals(YES);
    }
}