package store.util;


import store.error.BusinessException;
import store.error.ErrorCode;

public class Validator {

    public static void notEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.EMPTY_INPUT);
        }
    }

    public static void range(int number, int min, int max) {
        if (number < min || number > max) {
            throw new BusinessException(ErrorCode.OUT_OF_RANGE);
        }
    }
}