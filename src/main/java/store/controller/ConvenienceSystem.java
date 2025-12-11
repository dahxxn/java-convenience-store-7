package store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.products.Products;
import store.domain.promotions.Promotions;
import store.domain.shopping.ShoppingCart;
import store.error.BusinessException;
import store.error.ErrorCode;
import store.util.Parser;
import store.util.ReadMdFile;
import store.view.InputView;
import store.view.OutputView;

public class ConvenienceSystem {
    Promotions promotions;
    Products products;
    ShoppingCart shoppingCart;

    private static final String SHOPPING_REGEX = "^\\[(.+)-(\\d+)\\]$";

    public void run() {
        initSystem();
        process();
    }

    public void initSystem() {
        ReadMdFile reader = new ReadMdFile();
        List<String> rawPromotionContents = reader.readMdFile("promotions.md");
        List<String> rawProductsContents = reader.readMdFile("products.md");

        promotions = new Promotions(rawPromotionContents);
        products = new Products(rawProductsContents, promotions);
    }

    public void process() {
        boolean keepGoing = true;
        while (keepGoing) {
            OutputView.print("안녕하세요. W편의점입니다.");
            OutputView.print(products.getCurrentProductStatus());

            String rawShoppingList = InputView.read("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2], [감자칩-1])");
            HashMap<String, Integer> shoppingBag = settingShoppingBag(rawShoppingList);
            shoppingCart = new ShoppingCart(shoppingBag);

            keepGoing = false;
        }
    }

    private HashMap<String, Integer> settingShoppingBag(String rawShoppingList) {
        String[] shoppingList = rawShoppingList.split(",");
        HashMap<String, Integer> shoppingBag = new HashMap<>();

        for (String s : shoppingList) {
            s = s.trim();

            System.out.println(s);

            if (s.isEmpty()) {
                throw new BusinessException(ErrorCode.INVALID_SHOPPING_INPUT);
            }

            Pattern pattern = Pattern.compile(SHOPPING_REGEX);
            Matcher matcher = pattern.matcher(s);

            if (matcher.matches()) {
                String productName = matcher.group(1);
                if (!products.isContain(productName)) {
                    throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
                }

                int quantity = Parser.toInt(matcher.group(2));
                if (!products.isCoverageStock(quantity, productName)) {
                    throw new BusinessException(ErrorCode.BUY_COUNT_EXCEED_STOCK);
                }

                shoppingBag.put(productName, quantity);
                continue;
            }

            throw new BusinessException(ErrorCode.INVALID_SHOPPING_INPUT);


        }

        return shoppingBag;
    }
}
