import java.util.HashMap;
import java.util.Map;

public class JsonMapper {

    // Status
    public static final int INIT = 0;
    public static final int KEY = 1;
    public static final int VALUE = 2;
    public static final int END = 3;

    private int status;
    private String input;
    private int index = 0;
    private int length = 0;

    /**
     * Constructor
     * @param str input string
     */
    public JsonMapper(String str) {
        if (str != null) {
            str = str.trim();
            input = str;
            length = input.length();
        }
    }

    /**
     * Loops through the given string input
     * @return HashMap<String, Object>
     */
    public Map parse() {
        if (input == null || input.trim().length() == 0) {
            return null;
        }

        status = INIT;

        HashMap<String, Object> map = new HashMap<>();
        String key = null;
        Object value = null;

        while (true) {
            char ch = input.charAt(index);
            switch (ch) {
                case '{':
                    status = KEY;
                    key = null;
                    value = null;
                    break;
                case '}':
                    status = END;
                    key = null;
                    value = null;
                    break;
                case '\\':
                    break;
                case ' ':
                    break;
                case '"':
                    if (status == KEY) {
                        key = getString();
                    } else {
                        value = getValue();
                    }
                    break;
                case ':':
                    index++;
                    value = getValue();
                    break;
                case ',':
                    key = null;
                    value = null;
                    status = KEY;
                    break;
                default:
                    break;
            }

            if (key != null && value != null) {
                map.put(key, value);
            }

            index++;

            if (index >= length || status == END) {
                break;
            }
        }

        return map;
    }

    /**
     * Get String value
     * @return String
     */
    private String getString() {
        if (input == null || index >= length) {
            return null;
        }

        if (input.charAt(index) == '"') {
            index++;
        }

        StringBuilder sb = new StringBuilder("");
        while (input.charAt(index) != '"' && index < length) {
            char ch = input.charAt(index);
            if (ch != '\\') {
                sb.append(ch);
            }
            index++;
        }

        return sb.toString();
    }

    /**
     * Get Number
     * @return Integer / Double as value
     */
    private Object getNumber() {
        StringBuilder sb = new StringBuilder("");
        while (index < length) {
            char ch = input.charAt(index);
            if ((ch >= '0' && ch <= '9') || ch == '.') {
                sb.append(ch);
            } else {
                break;
            }
            index++;
        }

        String number = sb.toString();

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            try {
                return Double.parseDouble(number);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get value
     * @return String / Boolean / Integer / Double object as value
     */
    private Object getValue() {
        final int STRING = 1;
        final int TRUE = 2;
        final int FALSE = 3;
        final int NUMBER = 4;
        int status = 0;

        if (index >= input.length()) {
            return null;
        }

        char ch;

        while (index < input.length()) {
            ch = input.charAt(index);
            if (ch == '"') {
                status = STRING;
                break;
            }
            if (ch >= '0' && ch <= '9') {
                status = NUMBER;
                break;
            }
            if (ch == 't') {
                status = TRUE;
                while (index < length && ch != ' ' && ch != ',' && ch != '}' && ch != '\\') {
                    index++;
                    ch = input.charAt(index);
                }
                break;
            }
            if (ch == 'f') {
                status = FALSE;
                while (index < length && ch != ' ' && ch != ',' && ch != '}' && ch != '\\') {
                    index++;
                    ch = input.charAt(index);
                }
                break;
            }
            index++;
        }

        if (status == STRING) {
            return getString();
        }

        if (status == TRUE) {
           return true;
        }

        if (status == FALSE) {
           return false;
        }

        if (status == NUMBER) {
            return getNumber();
        }

        return null;
    }

}
