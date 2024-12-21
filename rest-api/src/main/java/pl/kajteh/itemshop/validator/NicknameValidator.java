package pl.kajteh.itemshop.validator;

import java.util.regex.Pattern;

public class NicknameValidator {

    private static final String NICKNAME_REGEX = "^[a-zA-Z0-9_\\-]{3,16}$";

    public static boolean isValidNickname(String nickname) {
        return nickname != null && Pattern.matches(NICKNAME_REGEX, nickname);
    }
}
