package store.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import store.error.BusinessException;
import store.error.ErrorCode;

public class Parser {

    public static int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.NUMBER_FORMAT);
        }
    }

    public static LocalDateTime toLocalDateTime(String value) {
        try {
            LocalDate localDate = LocalDate.parse(value);
            return localDate.atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new BusinessException(ErrorCode.DATE_TIME_FORMAT_ERROR);
        }
    }
}