package me.bn.fileshare.cotroller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.templateparser.markup.HTMLTemplateParser;

import javax.swing.text.html.HTMLDocument;
import java.io.IOException;

@Controller
@RequestMapping("bing")
public class BingController {

    @GetMapping("img")
    public @ResponseBody String imgUrl() {
        String url = "";
        try {
            String bingUrl = "https://cn.bing.com/";
            Document document = Jsoup.connect(bingUrl).get();
            url = document.getElementById("bgLink").attr("href");
            if (!StringUtils.isEmpty(url)){
                url = bingUrl + url;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static void main(String[] args) {
        new BingController().imgUrl();
    }
}
