package store.domain.promotions;

import java.util.HashMap;
import java.util.List;
import store.error.BusinessException;
import store.error.ErrorCode;

public class Promotions {
    private HashMap<PromotionName, PromotionInfo> promotions;

    public Promotions(List<String> rawPromotionsContents) {
        promotions = new HashMap<>();

        for (String rawPromotionInfo : rawPromotionsContents) {
            try {
                String[] infos = rawPromotionInfo.split(",");

                String name = infos[0].trim();
                checkPromotionExist(name);

                String buyCnt = infos[1].trim();
                String getCnt = infos[2].trim();
                String startDate = infos[3].trim();
                String endDate = infos[4].trim();

                PromotionName promotionName = new PromotionName(name);
                PromotionInfo promotionInfo = new PromotionInfo(buyCnt, getCnt, startDate, endDate);

                promotions.put(promotionName, promotionInfo);
            } catch (IndexOutOfBoundsException e) {
                throw new BusinessException(ErrorCode.PROMOTION_FORMAT_ERROR);
            }
        }
    }

    private void checkPromotionExist(String name) {
        if (promotions.containsKey(new PromotionName(name))) {
            throw new BusinessException(ErrorCode.PROMOTION_DUPLICATE);
        }
    }

    public void checkPromotions() {
        for (PromotionName promotionName : promotions.keySet()) {
            PromotionInfo promotionInfo = promotions.get(promotionName);
            System.out.printf("%s, %d, %d, %s, %s \n", promotionName.name, promotionInfo.buyCnt,
                    promotionInfo.getCnt,
                    promotionInfo.startDate, promotionInfo.endDate);
        }
    }
}
