package me.bn.fileshare.cotroller;

import org.apache.catalina.util.URLEncoder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WbController {
    @GetMapping("authorize")
    public RedirectView authorize() {
        Map paramMap = new HashMap<>();

       /* App Key：
        1701871125

        App Secret：
        9063c24f8aea0144fdcee58d7bdad873
        client_id 	true 	string 	申请应用时分配的AppKey。
        redirect_uri 	true 	string 	授权回调地址，站外应用需与设置的回调地址一致，站内应用需填写canvas page的地址。
        scope 	false 	string 	申请scope权限所需参数，可一次申请多个scope权限，用逗号分隔。使用文档
        state 	false 	string 	用于保持请求和回调的状态，在回调时，会在Query Parameter中回传该参数。开发者可以用这个参数验证请求有效性，也可以记录用户请求授权页前的位置。这个参数可用于防止跨站请求伪造（CSRF）攻击
        display 	false 	string 	授权页面的终端类型，取值见下面的说明。
        forcelogin 	false 	boolean 	是否强制用户重新登录，true：是，false：否。默认false。
        language 	false 	string 	授权页语言，缺省为中文简体版，en为英文版。英文版测试中，开发者任何意见可反馈至 @微博API*/
        paramMap.put("client_id", "1701871125");
        paramMap.put("redirect_uri", "http://127.0.0.1:8080/token");
        RedirectView redirectView = new RedirectView("https://api.weibo.com/oauth2/authorize?client_id=1701871125&redirect_uri=http://127.0.0.1:8080/token", false, true, true);
        //redirectView.setAttributesMap(paramMap);

        return redirectView;
    }

    @GetMapping("token")
    public void token(@RequestParam Map<String,String> map) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        paramMap.put("client_id", Collections.singletonList("1701871125"));
        paramMap.put("client_secret", Collections.singletonList("9063c24f8aea0144fdcee58d7bdad873"));
        paramMap.put("grant_type", Collections.singletonList("authorization_code"));
        paramMap.put("code", Collections.singletonList(map.get("code")));
        paramMap.put("redirect_uri", Collections.singletonList("http://127.0.0.1:8080/token"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity httpEntity = new HttpEntity(paramMap,headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity("https://api.weibo.com/oauth2/access_token",httpEntity,Map.class);
        System.out.println(responseEntity.getBody());

            share((String) responseEntity.getBody().get("access_token"));
    }
    public static String conver2HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(Long.toString(b[i] & 0xff, 2));
        }
        return result.toString().substring(0, result.length() - 1);
    }
    @GetMapping("share")
    public void share(String access_token){
        RestTemplate restTemplate = new RestTemplate();
        byte[] readByte=null;
        String readLine = "";
        StringBuilder sb = new StringBuilder();
        /*try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream("/Users/baoning/Pictures/1/123.png"))) {
            while ((readByte=dataInputStream.readByte())!=-1){
                sb.append(readByte);
            }

        }catch (Exception e){

        }*/
        try(FileInputStream fileReader = new FileInputStream("/Users/baoning/Pictures/1/456.jpeg")){
             readByte= new byte[fileReader.available()];
            fileReader.read(readByte);
        }catch (Exception e){
        }
        FileSystemResource resource = new FileSystemResource(new File("/Users/baoning/Pictures/1/456.jpeg"));
        StringBuilder stringBuilder = new StringBuilder("https://www.dinging.cn\n");
        URLEncoder encoder = new URLEncoder();
        ByteArrayResource byteArrayResource = new ByteArrayResource(readByte);
       String postTxt =  encoder.encode(stringBuilder.toString(), Charset.defaultCharset());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.put("access_token", Collections.singletonList(access_token));
        paramMap.put("status", Collections.singletonList(stringBuilder.toString()));
        try {
            paramMap.put("pic", Collections.singletonList(resource));
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity httpEntity = new HttpEntity(paramMap,headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                "https://api.weibo.com/2/statuses/share.json",httpEntity,Map.class);
        System.out.println(responseEntity.getBody());
    }
}
