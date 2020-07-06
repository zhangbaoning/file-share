package me.bn.fileshare.util;

import org.apache.tomcat.util.buf.HexUtils;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

public class CoolapkUtil {
    public static String getToken() {
        String one = "token://com.coolapk.market/c67ef5943784d09750dcfbb31020f0ab?";
        Long nowTime = new Date().getTime() / 1000;
        //Long nowTime = 1591716031L;
        MessageDigest md = null;
        try {

            md = MessageDigest.getInstance("md5");

            String md5hex = HexUtils.toHexString(md.digest(String.valueOf(nowTime).getBytes()));

            String str = one + md5hex + "$" + "8513efac-09ea-3709-b214-95b366f1a185" + "&com.coolapk.market";

            byte[] base64 = Base64.getEncoder().encode(str.getBytes());

            String md5a = HexUtils.toHexString(md.digest(base64));
            return md5a + "8513efac-09ea-3709-b214-95b366f1a1850x" + Long.toHexString(nowTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
return null;
    }
}
