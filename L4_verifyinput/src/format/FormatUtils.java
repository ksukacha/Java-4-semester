package format;

import java.util.regex.Pattern;

public class FormatUtils {
    public static boolean isCorrectFormat(String input, String selectedFormat) {
        switch(selectedFormat){
            case "Natural":
                return Pattern.matches("[1-9]\\d*", input);
            case "Integer":
                return Pattern.matches("0|(-?[1-9]\\d*)", input);
            case "Floating point":
                return Pattern.matches("-?((((\\d+\\.\\d*)|(\\d*\\.\\d+)))|"
                    +"(((\\d+\\.\\d*)|(\\d*\\.\\d+)|(\\d+\\.?\\d*))e-?\\d+))", input);

            case "Date":
                return Pattern.matches("(((0[1-9]|[12][0-9]|30)\\.(04|06|09|11))|" +
                    "((0[1-9]|[12][0-9]|30|31)\\.(01|03|05|07|08|10|12))|"+
                    "((0[1-9]|[12][0-9])\\.02))\\.[1-9]\\d{3}", input);
            case "Time":
                return Pattern.matches("(([01]\\d)|(2[0-3])):[0-5]\\d:[0-5]\\d", input);
            case "E-mail":
                return Pattern.matches("([a-z]|[A-Z]|[0-9])([_.-]|[a-z]|[A-Z]|[0-9])*@([a-z]|[0-9])+\\.(by|ru|org|com)", input);
            default:
                return false;
        }
    }
}
