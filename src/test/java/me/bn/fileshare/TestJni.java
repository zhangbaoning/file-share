package me.bn.fileshare;

import javax.naming.Context;

public class TestJni {
    public static native String getAS(Context context, String str);

    static {
        System.load("/Users/baoning/IdeaProjects/file-share/src/main/resources/libnative-lib.so");
        //System.loadLibrary("native-lib");
    }
    public static void main(String[] args) {
getAS(null,null);
    }
}
