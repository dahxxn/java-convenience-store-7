package store.error;


public enum ErrorCode {

    INTERNAL_ERROR("예기치 못한 오류가 발생했습니다."),
    INVALID_INPUT("유효하지 않은 입력값입니다."),
    EMPTY_INPUT("입력이 비어있습니다."),
    NUMBER_FORMAT("숫자 형식이 올바르지 않습니다."),
    OUT_OF_RANGE("입력 값이 허용 범위를 벗어났습니다."),

    MD_FILE_READE_ERROR("MD 파일을 읽는 중에 예기치 못한 오류가 발생했습니다."),
    MD_FILE_NOT_FOUND("해당 MD 파일을 찾을 수 없습니다."),
    DATE_TIME_FORMAT_ERROR("잘못된 날짜 형식입니다."),
    PROMOTION_FORMAT_ERROR("프로모션 정보가 잘못된 형식입니다."),
    PROMOTION_DUPLICATE("프로모션은 중복으로 존재할 수 없습니다."),

    ;

    private final String message;
    private final String header = "[ERROR] ";

    ErrorCode(String message) {
        this.message = message;
    }

    public String message() {
        return header + message;
    }
}
