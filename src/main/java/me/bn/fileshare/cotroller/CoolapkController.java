package me.bn.fileshare.cotroller;

import me.bn.fileshare.util.CoolapkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CoolapkController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("getPic")
    public String getPic() {
        ModelAndView modelAndView = new ModelAndView();
        HttpHeaders httpHeaders = new HttpHeaders();
        List picUrlList = new ArrayList();

       /* User-Agent: Dalvik/2.1.0 (Linux; U; Android 6.0.1; MuMu Build/V417IR) (#Build; Android; MuMu; V417IR release-keys; 6.0.1) +CoolMarket/10.2.1-2005201
        X-Requested-With: XMLHttpRequest
        X-Sdk-Int: 23
        X-Sdk-Locale: zh-CN
        X-App-Id: com.coolapk.market
        X-App-Token:cbe56aa2e358f41d6b47144323359bcb8513efac-09ea-3709-b214-95b366f1a1850x5ee0330f
        X-App-Version: 10.2.1
        X-App-Code: 2005201
        X-Api-Version: 10
        X-App-Device: 11UdNByOkl2byRmbBByOlNXYlRXZOByO0cjO1MjOEVkO3IjOwAjO4ADI7AyO0AzNyQTMyYzN2kzN4ADMgsTOzgTZzcjM4MGN3UDO3E2M
        X-Dark-Mode: 0*/
        httpHeaders.set("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0.1; MuMu Build/V417IR) (#Build; Android; MuMu; V417IR release-keys; 6.0.1) +CoolMarket/10.2.1-2005201");
        httpHeaders.set("X-Requested-With", "XMLHttpRequest");
        httpHeaders.set("X-Sdk-Int", "zh-CN");
        httpHeaders.set("X-App-Id", "com.coolapk.market");
        httpHeaders.set("X-App-Version", "10.2.1");
        httpHeaders.set("X-App-Code", "2005201");
        httpHeaders.set("X-Api-Version", "10");
        httpHeaders.set("X-App-Device", "11UdNByOkl2byRmbBByOlNXYlRXZOByO0cjO1MjOEVkO3IjOwAjO4ADI7AyO0AzNyQTMyYzN2kzN4ADMgsTOzgTZzcjM4MGN3UDO3E2M");
        httpHeaders.set("X-Dark-Mode", "0*");
        httpHeaders.set("X-App-Token", CoolapkUtil.getToken());
        RestTemplate restTemplate = new RestTemplate();
        try (
                PrintWriter writer = new PrintWriter(new FileWriter("/Users/baoning/Pictures/1/down"));
        ) {

            ResponseEntity resultMap = restTemplate.exchange("https://api.coolapk.com/v6/page/dataList?url=/feed/coolPictureList?listType=fullScreen&dataListType=staggered&title=热门&subTitle=&page=1", HttpMethod.GET,
                    new HttpEntity<>(httpHeaders), HashMap.class);
            ArrayList<Map> picList = (ArrayList<Map>) ((Map<Object, Object>) resultMap.getBody()).get("data");
            for (Map map : picList) {
                List<String> urlList = (List) map.get("picArr");
                for (String s : urlList) {
                    writer.println(s);
                }

            }
            logger.info("生成的链接为", picUrlList.toString());
        } catch (IOException e) {
            logger.error("文件读取错误",e);
        }
        modelAndView.addObject("fileList", picUrlList);
        modelAndView.setViewName("photowall");
        return "index";
    }
}
