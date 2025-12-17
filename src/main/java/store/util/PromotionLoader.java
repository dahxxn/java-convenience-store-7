package store.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.promotions.Promotion;
import store.error.BusinessException;
import store.error.ErrorCode;

public class PromotionLoader {
    private static final String PROMOTION_FILES_PATH = "src/main/resources/promotions.md";
    private static final String DELIMITER = ",";

    public List<Promotion> loadFile() {
        List<Promotion> promotions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PROMOTION_FILES_PATH))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                Promotion promotion = parse(line);
                promotions.add(promotion);
            }

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.MD_FILE_READE_ERROR);
        }

        return promotions;
    }

    public Promotion parse(String line) {
        String[] parts = line.split(DELIMITER);

        String name = parts[0];
        int buy = Parser.toInt(parts[1]);
        int get = Parser.toInt(parts[2]);
        LocalDate startDate = LocalDate.parse(parts[3]);
        LocalDate endDate = LocalDate.parse(parts[4]);

        return new Promotion(name, buy, get, startDate, endDate);
    }
}
