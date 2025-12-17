package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String PURCHASE_INPUT_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP_INPUT_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String CONTINUE_SHOPPING_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String PROMOTION_ADD_MESSAGE = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String REGULAR_PRICE_MESSAGE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";

    public String readPurchaseInput() {
        System.out.println(PURCHASE_INPUT_MESSAGE);
        return Console.readLine();
    }

    public String readMembershipInput() {
        System.out.println(MEMBERSHIP_INPUT_MESSAGE);
        return Console.readLine();
    }

    public String readContinueShoppingInput() {
        System.out.println(CONTINUE_SHOPPING_MESSAGE);
        return Console.readLine();
    }

    public String readPromotionAddInput(String productName) {
        System.out.println(String.format(PROMOTION_ADD_MESSAGE, productName));
        return Console.readLine();
    }

    public String readRegularPriceInput(String productName, int quantity) {
        System.out.println(String.format(REGULAR_PRICE_MESSAGE, productName, quantity));
        return Console.readLine();
    }
}