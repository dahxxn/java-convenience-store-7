package store.error;


public enum ErrorCode {

    INTERNAL_ERROR("예기치 못한 오류가 발생했습니다."),
    INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요"),
    EMPTY_INPUT("입력이 비어있습니다."),
    NUMBER_FORMAT("숫자 형식이 올바르지 않습니다."),
    OUT_OF_RANGE("입력 값이 허용 범위를 벗어났습니다."),

    MD_FILE_READE_ERROR("MD 파일을 읽는 중에 예기치 못한 오류가 발생했습니다."),
    MD_FILE_NOT_FOUND("해당 MD 파일을 찾을 수 없습니다."),
    DATE_TIME_FORMAT_ERROR("잘못된 날짜 형식입니다."),
    PROMOTION_FORMAT_ERROR("프로모션 정보가 잘못된 형식입니다."),
    PROMOTION_DUPLICATE("프로모션은 중복으로 존재할 수 없습니다."),
    PRODUCT_FORMAT_ERROR("상품 정보가 잘못된 형식입니다."),
    PROMOTION_NOT_EXIST("해당 프로모션은 존재하지 않습니다."),

    INVALID_SHOPPING_INPUT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    BUY_COUNT_EXCEED_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    private final String message;
    private final String header = "[ERROR] ";

    ErrorCode(String message) {
        this.message = message;
    }

    public String message() {
        return header + message;
    }
}
