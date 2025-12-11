package store.domain.promotions;

import java.time.LocalDate;
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
                if (checkPromotionExist(name)) {
                    throw new BusinessException(ErrorCode.PROMOTION_DUPLICATE);
                }

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

    public boolean checkPromotionExist(String name) {
        if (promotions.containsKey(new PromotionName(name))) {
            return true;
        }
        return false;

    }

    public PromotionName getPromotionNameByProductName(String name) {

        for (PromotionName promotionName : promotions.keySet()) {
            if (promotionName.getName().equals(name)) {
                return promotionName;
            }
        }
        return null;
    }

    public PromotionName getPromotionName(String name) {
        for (PromotionName promotionName : promotions.keySet()) {
            if (promotionName.name.equals(name)) {
                return promotionName;
            }
        }
        throw new BusinessException(ErrorCode.PROMOTION_NOT_EXIST);
    }

    public boolean isAvailablePromotion(PromotionName promotionName) {
        LocalDate endDate = LocalDate.from(promotions.get(promotionName).endDate);
        LocalDate startDate = LocalDate.from(promotions.get(promotionName).startDate);
        System.out.println(promotionName.getName() + "의 프로모션 유효기간 : " + startDate + " ~~ " + endDate + " ==> " + (
                endDate.isAfter(LocalDate.now()) && startDate.isBefore(LocalDate.now())));

        return endDate.isAfter(LocalDate.now()) && startDate.isBefore(LocalDate.now());
    }

    public int getBuyCnt(PromotionName promotionName) {
        return promotions.get(promotionName).buyCnt;
    }

    public int getGetCnt(PromotionName promotionName) {
        return promotions.get(promotionName).getCnt;
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
