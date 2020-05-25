package me.bn.fileshare.cotroller;

import me.bn.fileshare.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class FileCotroller {
    @Value("${upload-file-path}")
    private String uploadFilePath;
    @Autowired
    FileService service;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/upload")
    public String upload(@RequestParam("uploadfile") MultipartFile file){
        Path path = Paths.get(uploadFilePath+File.separator+file.getOriginalFilename());
        try {
            file.transferTo(path);
            System.out.println(request.getLocalAddr());
            System.out.println(request.getRemoteHost());
            System.out.println(request.getRemoteAddr());
            service.save(file.getOriginalFilename(),false,request.getRemoteHost());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "index";
    }
//    @GetMapping("/")
//    public String index(){
//        return "index";
//    }
    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        boolean isPublic = true;
        List<String> fileNameList = new ArrayList<>();
        File file = new File(uploadFilePath);
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if ("isPublic".equals(cookie.getName())){
                    isPublic = "true".equals(cookie.getValue())?true:false;
                }
            }
            if (isPublic){
                for (File listFile : file.listFiles()) {
                    fileNameList.add(listFile.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("fileNameList",fileNameList);
        modelAndView.setViewName("index");
        return modelAndView;
    }
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileName") String fileName){
        File file = new File(uploadFilePath+File.separator+fileName);
        InputStreamResource resource = null;
        HttpHeaders headers = new HttpHeaders();
        try {
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.setContentDispositionFormData("attachment", new String(fileName.getBytes(),"iso8859-1"));

            resource = new InputStreamResource(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(resource);
    }
}
