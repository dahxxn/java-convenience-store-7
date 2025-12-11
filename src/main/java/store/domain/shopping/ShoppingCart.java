package store.domain.shopping;

import java.util.HashMap;

public class ShoppingCart {
    HashMap<String, Integer> shoppingBag;
    HashMap<String, Integer> freeProducts;

    public ShoppingCart(HashMap<String, Integer> shoppingBag) {
        this.shoppingBag = shoppingBag;
        this.freeProducts = new HashMap<>();
    }

    public void checkShoppingBag() {

        for (String key : shoppingBag.keySet()) {
            int quantity = shoppingBag.get(key);

            System.out.printf("%s - %d \n", key, quantity);
        }

    }

}
