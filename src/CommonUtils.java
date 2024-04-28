package src;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    /**
     * Removes all newline characters in a given String.
     * @param s The String to be collapsed
     * @return String with all newline characters removed.
     */
    public static String collapseNewLine(String s) {
        Pattern pattern = Pattern.compile("\n");
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll("");
    }
}
