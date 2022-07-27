package stage.agencedirectserver.utils;

import java.util.Date;

public class ExpireDateUtil {
    public static Date getExpireDate(int years) {
        Date expireDate = new Date();
        expireDate.setYear(expireDate.getYear() + years);
        return expireDate;
    }
}
