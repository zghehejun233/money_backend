package site.surui.web.common.util;

public class SQLUtil {
    public static String buildRegex(String src) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        char[] tokens = src.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append("^.*");
        for (char token : tokens) {
            if (token != ' ') {
                sb.append("(?=.*").append(token).append(")");
            }
        }
        sb.append(".*$");

        return sb.toString();
    }
}
