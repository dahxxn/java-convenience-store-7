package store.util;

import java.util.List;

public interface Loader {
    List<Object> loadFile();

    Object parse(String line);
}
