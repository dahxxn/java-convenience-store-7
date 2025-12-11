package store.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import store.error.BusinessException;
import store.error.ErrorCode;

public class ReadMdFile {
    public static List<String> readMdFile(String fileName) {
        try (InputStream is = ReadMdFile.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new BusinessException(ErrorCode.MD_FILE_NOT_FOUND);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            int count = 0;

            List<String> fileContents = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                count++;
                if (count == 1) {
                    continue;
                }
                fileContents.add(line);
            }

            return fileContents;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.MD_FILE_READE_ERROR);
        }
    }

}
