package stage.agencedirectserver.utils;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

@Data
public class CodeAccessGenerator {
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase(Locale.ROOT);
    public static final String digits = "0123456789";
    public static final String symbols ="!@#$%&*()_+-=[]{}|;:,./<>?~";
    private String codeAccess;

    public CodeAccessGenerator() {
        this.codeAccess = RandomStringUtils.random(10, upper + lower + digits + symbols);
    }
    public static String generate() {
        return new CodeAccessGenerator().getCodeAccess();
    }
}
