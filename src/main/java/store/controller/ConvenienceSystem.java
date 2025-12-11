package store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.products.Products;
import store.domain.promotions.PromotionName;
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
    HashMap<String, Integer> buyInfo;
    HashMap<String, Integer> getInfo;
    
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

        buyInfo = new HashMap<>();
        getInfo = new HashMap<>();
    }

    public void process() {
        boolean keepGoing = true;
        while (keepGoing) {
            OutputView.print("안녕하세요. W편의점입니다.");
            OutputView.print(products.getCurrentProductStatus());

            String rawShoppingList = InputView.read("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2], [감자칩-1])");
            HashMap<String, Integer> shoppingBag = settingShoppingBag(rawShoppingList);

            checkingPromotion(shoppingBag);
            checkBuyAndGetInfo();

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

    private void checkingPromotion(HashMap<String, Integer> shoppingBags) {
        for (String productName : shoppingBags.keySet()) {
            System.out.println(productName);
            if (products.isPromotionProduct(productName, promotions)) {
                System.out.println("프로모션 : " + promotions.getPromotionNameByProductName(productName));
                complyPromotion(productName, shoppingBags.get(productName));
                continue;
            }

            buyInfo.put(productName, shoppingBags.get(productName));
        }
    }

    private void complyPromotion(String name, int quantity) {
        PromotionName promotion = products.getPromotionName(name);
        int buyCnt = promotions.getBuyCnt(promotion);
        int getCnt = promotions.getGetCnt(promotion);

        int tempQuantity = quantity;
        int buyQuantity = 0;
        int getQuantity = 0;

        while (tempQuantity >= buyCnt) {
            tempQuantity -= buyCnt;
            buyQuantity += buyCnt;

            tempQuantity -= getCnt;
        }

        if (tempQuantity > 0) {
            buyQuantity += tempQuantity;
        } else if (tempQuantity < 0) {
            String yOrN = InputView.read(
                    "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)".formatted(name, (-1) * tempQuantity));

            yOrN = yOrN.trim();

            if (yOrN.equals("Y")) {
                getQuantity += getCnt;
            }
        }

        getQuantity += ((buyQuantity / buyCnt) * getCnt);

        int result = products.checkPromotionStock(name, getQuantity + buyQuantity);

        if (result > 0) {
            String yOrN = InputView.read("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)".formatted(name, result));

            yOrN = yOrN.trim();

            if (yOrN.equals("Y")) {
                buyQuantity += result;
            }
            getQuantity -= result;
        }

        buyInfo.put(name, buyQuantity);
        if (getQuantity > 0) {
            getInfo.put(name, getQuantity);
        }
    }

    public void checkBuyAndGetInfo() {
        System.out.println("구매");
        System.out.println(buyInfo);

        System.out.println("프로모션");
        System.out.println(getInfo);
    }

}
