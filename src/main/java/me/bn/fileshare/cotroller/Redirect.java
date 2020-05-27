package me.bn.fileshare.cotroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class Redirect {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${clientId}")
    private String clientId;

    @GetMapping("onedriver")
    public ModelAndView setToken() {
        ModelAndView modelAndView = new ModelAndView();
        String onedriveToken = redisTemplate.opsForValue().get("onedrive");
        if (StringUtils.isEmpty(onedriveToken)) {


            String scope = "files.readwrite.all";
//        String clientId = "a67433c0-386f-421d-9c48-f5825bb63ba7";
            String token = "token";
            String redirectUri = "http://localhost:8080/getToken";
            String url = "redirect:https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=" + clientId + "&scope=" + scope +
                    "&response_type=" + token + "&redirect_uri=" + redirectUri;
            modelAndView.setViewName(url);
            return modelAndView;
        } else {
            Map param = new HashMap();
            param.put("access_token", onedriveToken);
            return getToken(param);
        }
    }

    @GetMapping("getToken")
    public ModelAndView getToken(@RequestParam Map<String, String> param) {
        List<Map<String, String>> fileNameList = new ArrayList<>();
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
            redisTemplate.opsForValue().set("onedrive", param.get("access_token"));
            ResponseEntity<HashMap> result = template.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), HashMap.class);
            ArrayList<Map<String, String>> valueList = (ArrayList) result.getBody().get("value");
            String driverId = valueList.get(0).get("id");
            request.getSession().setAttribute("driverId", driverId);
            url = "https://graph.microsoft.com/v1.0/drives/" + driverId + "/root/children";
            ResponseEntity<HashMap> rootDir = template.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), HashMap.class);
            ArrayList<LinkedHashMap<String, Object>> fileMap = (ArrayList<LinkedHashMap<String, Object>>) rootDir.getBody().get("value");

          /*  System.out.println(rootDir.getBody());
            url= "https://graph.microsoft.com/v1.0//me/drive/items/016Z33MC57UQL5YFCJ7RGYH5RKO6XNQLER";

            ResponseEntity<HashMap> itemResult = template.exchange(url, HttpMethod.GET,new HttpEntity<>(httpHeaders), HashMap.class);
            System.out.println(itemResult.getBody());
            ArrayList<LinkedHashMap> fileList= (ArrayList<LinkedHashMap>) itemResult.getBody().get("value");
*/
            for (LinkedHashMap linkedHashMap : fileMap) {
                Map<String, String> respMap = new HashMap(2);
                if (linkedHashMap.containsKey("@microsoft.graph.downloadUrl")) {
                    respMap.put("url", String.valueOf(linkedHashMap.get("@microsoft.graph.downloadUrl")));

                } else {
                    respMap.put("url", "dir?id=" + linkedHashMap.get("id"));

                }
                respMap.put("fileName", String.valueOf(linkedHashMap.get("name")));
                fileNameList.add(respMap);


            }

            modelAndView.addObject("fileNameList", fileNameList);
            modelAndView.setViewName("index");
            return modelAndView;
        }

    }

    @GetMapping("dir")
    public ModelAndView dir(@RequestParam("id") String id) {
        List<Map<String, String>> fileNameList = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView();
        String onedriveToken = redisTemplate.opsForValue().get("onedrive");
        String driverId = (String) request.getSession().getAttribute("driverId");
        String url = "https://graph.microsoft.com/v1.0/drives/" + driverId + "/items/" + id + "/children";
        HttpHeaders httpHeaders = new HttpHeaders();
        RestTemplate template = new RestTemplate();
        httpHeaders.setBearerAuth(onedriveToken);
        ResponseEntity<HashMap> itemResult = template.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), HashMap.class);
        System.out.println(itemResult.getBody());
        ArrayList<LinkedHashMap> fileList = (ArrayList<LinkedHashMap>) itemResult.getBody().get("value");

        for (LinkedHashMap linkedHashMap : fileList) {
            Map<String, String> respMap = new HashMap(2);
            if (linkedHashMap.containsKey("@microsoft.graph.downloadUrl")) {
                respMap.put("url", String.valueOf(linkedHashMap.get("@microsoft.graph.downloadUrl")));

            } else {
                respMap.put("url", "dir?id=" + linkedHashMap.get("id"));

            }
            respMap.put("fileName", String.valueOf(linkedHashMap.get("name")));
            fileNameList.add(respMap);

        }

        modelAndView.addObject("fileNameList", fileNameList);
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
