package me.bn.fileshare;

import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
public class TokenTest {
    public static void main(String[] args) {
        String one = "token://com.coolapk.market/c67ef5943784d09750dcfbb31020f0ab?";
        Long nowTime = new Date().getTime()/1000;
        //Long nowTime = 1591716031L;
        MessageDigest md = null;
        try {

            md = MessageDigest.getInstance("md5");

            String md5hex= HexUtils.toHexString(md.digest(String.valueOf(nowTime).getBytes()))   ;

            String str= one+md5hex+"$"+"8513efac-09ea-3709-b214-95b366f1a185"+"&com.coolapk.market";

            byte[] base64 = Base64.getEncoder().encode(str.getBytes());
            System.out.println(new String(md.digest(base64)));
//token://com.coolapk.market/c67ef5943784d09750dcfbb31020f0ab?40056a4de3dc2a59112c0b557731b3f1$8513efac-09ea-3709-b214-95b366f1a185&com.coolapk.market
            System.out.println(str);
String md5a = HexUtils.toHexString(md.digest(base64));
            System.out.println(md5a+"8513efac-09ea-3709-b214-95b366f1a1850x"+Long.toHexString(nowTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 通过md5计算摘要,返回一个字节数组

    }
    @Test
    public void test(){
        String string ="237123faac89ca21485a1b86bddd5358a3fb7dc6-6c82-3d8f-aa75-c3c950691b580x5edf9fea";
       String dateStr =  String.valueOf(new Date().getTime()/1000);
        System.out.println("dateStr"+dateStr);
        System.out.println(dateStr.length());
        System.out.println(Long.toHexString( new Date().getTime()));
        int hexlength = HexUtils.toHexString(dateStr.getBytes()).length();
        System.out.println(hexlength);

        Long l = Long.parseLong("5edf9fea",16);
        System.out.println(new Date(l).toString());
    }
    @Test
    public void hex(){
        int i = 1591716031 ;
       String hex=  Integer.toHexString(i);
        System.out.println("0x"+hex);
        MessageDigest md = null;
        try {

            md = MessageDigest.getInstance("md5");
        String md5hex= HexUtils.toHexString(md.digest(String.valueOf(i).getBytes()))   ;
            System.out.println(md5hex);
        }catch (Exception e){

        }
    }

}
