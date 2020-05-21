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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileCotroller {
    @Value("${upload-file-path}")
    private String uploadFilePath;
    @Autowired
    FileService service;
    @PostMapping("/upload")
    public String upload(@RequestParam("uploadfile") MultipartFile file){
        Path path = Paths.get(uploadFilePath+File.separator+file.getOriginalFilename());
        List<String> fileNameList = new ArrayList<>();
        try {
            file.transferTo(path);

            for (File listFile : path.getParent().toFile().listFiles()) {
                fileNameList.add(listFile.getName());
            }
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

        List<String> fileNameList = new ArrayList<>();
        File file = new File(uploadFilePath);
        try {

            for (File listFile : file.listFiles()) {
                fileNameList.add(listFile.getName());
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
