package me.bn.fileshare;

public class Test1 {
    public static void main(String[] args) {
        String str = "10\t未婚\n" +
                "20\t已婚\n" +
                "21\t初婚\n" +
                "22\t再婚\n" +
                "23\t复婚\n" +
                "30\t丧偶\n" +
                "40\t离婚\n" +
                "90\t其他";
        String[] keyValue = str.split("\n");
        StringBuilder stringBuilder = new StringBuilder("[{\"name\": \"00\", \"desc\": \"请选择\"},");
        for (String s : keyValue) {
           String[] out = s.split("\t");
           stringBuilder.append("{\"name\": \""+out[0]+"\", \"desc\": \""+out[1]+"\"},") ;
        }
        stringBuilder.append(" ]");
        System.out.println(stringBuilder.toString().replaceAll(" ",""));
    }
}
