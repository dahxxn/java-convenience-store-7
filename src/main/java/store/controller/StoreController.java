package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.domain.Inventory.Inventory;
import store.domain.promotions.PromotionResult;
import store.domain.receipt.Receipt;
import store.domain.shopping.ShoppingItem;
import store.service.PurchaseService;
import store.view.InputValidator;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final InputValidator inputValidator;
    private final PurchaseService purchaseService;
    private final Inventory inventory;

    public StoreController(Inventory inventory) {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.inputValidator = new InputValidator();
        this.inventory = inventory;
        this.purchaseService = new PurchaseService(inventory);
    }

    public void run() {
        do {
            displayWelcomeAndInventory();
            processPurchase();
        } while (askContinueShopping());
    }

    private void displayWelcomeAndInventory() {
        outputView.printWelcome();
        outputView.printInventory(inventory);
    }

    private void processPurchase() {
        List<ShoppingItem> shoppingItems = getPurchaseInput();
        List<PromotionResult> promotionResults = new ArrayList<>();

        for (ShoppingItem item : shoppingItems) {
            PromotionResult result = processPromotionForItem(item);
            promotionResults.add(result);
        }

        boolean applyMembership = askMembership();
        Receipt receipt = purchaseService.createReceipt(shoppingItems, promotionResults, applyMembership);
        outputView.printReceipt(receipt);
        purchaseService.processPayment(shoppingItems);
    }

    private PromotionResult processPromotionForItem(ShoppingItem item) {
        PromotionResult result = purchaseService.getPromotionResult(item);

        if (result.hasAdditionalPromotion()) {
            boolean addMore = askAddPromotionItem(item.getProductName());
            if (addMore) {
                item.increaseQuantity(result.getAdditionalQuantity());
                return purchaseService.getPromotionResult(item);
            }
        }

        if (result.hasRegularPriceItems()) {
            boolean buyRegular = askBuyAtRegularPrice(item.getProductName(), result.getRegularQuantity());
            if (!buyRegular) {
                item.setQuantity(item.getQuantity() - result.getRegularQuantity());
                return purchaseService.getPromotionResult(item);
            }
        }

        return result;
    }

    private boolean askMembership() {
        while (true) {
            try {
                String input = inputView.readMembershipInput();
                return inputValidator.validateYesOrNo(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean askAddPromotionItem(String productName) {
        while (true) {
            try {
                String input = inputView.readPromotionAddInput(productName);
                return inputValidator.validateYesOrNo(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean askBuyAtRegularPrice(String productName, int quantity) {
        while (true) {
            try {
                String input = inputView.readRegularPriceInput(productName, quantity);
                return inputValidator.validateYesOrNo(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<ShoppingItem> getPurchaseInput() {
        while (true) {
            try {
                String input = inputView.readPurchaseInput();
                return inputValidator.validateAndParsePurchaseInput(input, inventory);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean askContinueShopping() {
        while (true) {
            try {
                String input = inputView.readContinueShoppingInput();
                return inputValidator.validateYesOrNo(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}