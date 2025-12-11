package store.view;


import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public static String readLine() {
        return Console.readLine().trim();
    }

    public static String read(String message) {
        OutputView.print(message);
        return readLine();
    }
}