package com.example.yizu.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by q on 2017/8/30.
 */

public class PhoneNumberConfirm {
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
