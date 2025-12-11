package store.domain.promotions;

import java.time.LocalDateTime;
import store.util.Parser;


public class PromotionInfo {
    int buyCnt;
    int getCnt;
    LocalDateTime startDate;
    LocalDateTime endDate;

    public PromotionInfo(String buyCnt, String getCnt, String startDate, String endDate) {
        this.buyCnt = Parser.toInt(buyCnt);
        this.getCnt = Parser.toInt(getCnt);
        this.startDate = Parser.toLocalDateTime(startDate);
        this.endDate = Parser.toLocalDateTime(endDate);
    }
}
