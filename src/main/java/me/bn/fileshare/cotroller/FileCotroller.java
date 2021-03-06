package me.bn.fileshare.cotroller;

import me.bn.fileshare.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class FileCotroller {
    @Value("${upload-file-path}")
    private String uploadFilePath;
    @Autowired
    FileService service;
    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String upload(@RequestParam("uploadfile")MultipartFile file,@RequestParam Map<String,String> paramMap){

        String filePath= uploadFilePath+File.separator+(paramMap.size()>0?paramMap.get("filePath"):"");
        File pathFile= new File(filePath);
        if (!pathFile.exists()){
            pathFile.mkdirs();
        }
        System.out.println("文件上传路径"+filePath);
        Path path = Paths.get(filePath+File.separator+file.getOriginalFilename());
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
    public ModelAndView index(@Nullable String listPath){
        ModelAndView modelAndView = new ModelAndView();
        boolean isPublic = true;
        List<Map<String,String>> fileNameList = new ArrayList<>();
        if (StringUtils.isEmpty(listPath)){
            listPath = uploadFilePath;
        }
        File file = new File(listPath);
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if ("isPublic".equals(cookie.getName())){
                    isPublic = "true".equals(cookie.getValue());
                }
            }
            if (isPublic){
                for (File listFile : Objects.requireNonNull(file.listFiles())) {
                    Map<String,String> respMap = new HashMap(2);

                    respMap.put("fileName",String.valueOf(listFile.getName()));
                        respMap.put("url","/download?fileName="+listFile.getAbsolutePath());
                        fileNameList.add(respMap);
                }
                request.getSession().setAttribute("filePath",file.getAbsolutePath());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("fileNameList",fileNameList);
        modelAndView.setViewName("index");
        return modelAndView;
    }
    @GetMapping("/download")
    public Object download(@RequestParam("fileName") String fileName){
        File file = new File(fileName);
        if (file.isDirectory()){
           return index(fileName);

        }
        InputStreamResource resource = null;
        HttpHeaders headers = new HttpHeaders();
        try {
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.setContentDispositionFormData("attachment", new String(file.getName().getBytes(), StandardCharsets.ISO_8859_1));

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
    @GetMapping("/img")
    public ModelAndView img(){
        String path = (String) request.getSession().getAttribute("filePath");
        File uploadFile = new File(uploadFilePath);
        File file = new File(path);

        List list = new ArrayList();
        List preList = new ArrayList();
        for (File listFile : file.listFiles()) {
            if (listFile.isFile()&&(listFile.getName().endsWith("png")||listFile.getName().endsWith("jpg")||listFile.getName().endsWith("jpeg"))){
                list.add(listFile.getPath().replace(uploadFile.getAbsolutePath(),""));
                preList.add( listFile.getPath().replace(uploadFile.getAbsolutePath(),""));

            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("fileList",list);
        modelAndView.addObject("preList",preList);
        modelAndView.setViewName("photowall");
        return modelAndView;
    }
}
