package store.error;


import java.util.function.Supplier;
import store.view.OutputView;

public class GlobalExceptionHandler {

    public static <T> T inputUntilSuccess(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (BusinessException e) {
                OutputView.print(e.getMessage());
            }
        }
    }

    public static void protect(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            OutputView.print(ErrorCode.INTERNAL_ERROR.message());
        }
    }
}