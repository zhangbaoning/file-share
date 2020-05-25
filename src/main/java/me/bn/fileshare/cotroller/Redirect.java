package me.bn.fileshare.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class Redirect {
    @Autowired
    private HttpServletRequest request;

    @GetMapping("onedriver")
    public String setToken() {
        String scope = "files.readwrite.all";
        String clientId = "19ef04dc-2fb4-4fc9-87c8-7017f133cfbc";
        String token = "token";
        String redirectUri = "http://localhost:8080/getToken";
        return "redirect:https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=" + clientId + "&scope=" + scope +
                "&response_type=" + token + "&redirect_uri=" + redirectUri;
    }

    @GetMapping("getToken")
    public ModelAndView getToken(@RequestParam Map<String, String> param) {
        List<Map<String,String>> fileNameList = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView();


        if (param.size() == 0) {
            System.out.println(param);
            System.out.println(request.getPathInfo());
            modelAndView.setViewName("onedriver");
            return modelAndView;
        } else {
            String url = "https://graph.microsoft.com/v1.0/me/drives";
            HttpHeaders httpHeaders = new HttpHeaders();
            RestTemplate template = new RestTemplate();
           httpHeaders.setBearerAuth(param.get("access_token"));
            ResponseEntity<HashMap> result = template.exchange(url, HttpMethod.GET,new HttpEntity<>(httpHeaders), HashMap.class);
            System.out.println(result.getBody().get("id"));
            url= "https://graph.microsoft.com/v1.0//me/drive/items/016Z33MC57UQL5YFCJ7RGYH5RKO6XNQLER";

            ResponseEntity<HashMap> itemResult = template.exchange(url, HttpMethod.GET,new HttpEntity<>(httpHeaders), HashMap.class);
            System.out.println(itemResult.getBody());
            ArrayList<LinkedHashMap> fileList= (ArrayList<LinkedHashMap>) itemResult.getBody().get("value");

            for (LinkedHashMap linkedHashMap : fileList) {
                Map<String,String> respMap = new HashMap(2);
                respMap.put("url",String.valueOf(linkedHashMap.get("webUrl")));
                respMap.put("fileName",String.valueOf(linkedHashMap.get("webUrl")));
                fileNameList.add(respMap);
            }

            modelAndView.addObject("fileNameList",fileNameList);
            modelAndView.setViewName("index");
            return modelAndView;
        }

    }

}
