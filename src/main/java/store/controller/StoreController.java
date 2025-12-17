package store.controller;

import java.util.List;
import store.domain.Inventory.Inventory;
import store.domain.shopping.ShoppingItem;
import store.view.InputValidator;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;
    private final InputValidator inputValidator;
    private final Inventory inventory;

    public StoreController(Inventory inventory) {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.inputValidator = new InputValidator();
        this.inventory = inventory;
    }

    public void run() {
        displayWelcomeAndInventory();

        do {
            List<ShoppingItem> shoppingItems = getPurchaseInput();
            System.out.println("\n구매 상품: " + shoppingItems.size() + "개");
//            for (ShoppingItem item : shoppingItems) {
//                System.out.println("- " + item.getProductName() + ": " + item.getQuantity() + "개");
//            }
        } while (askContinueShopping());
    }

    private void displayWelcomeAndInventory() {
        outputView.printWelcome();
        outputView.printInventory(inventory);
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