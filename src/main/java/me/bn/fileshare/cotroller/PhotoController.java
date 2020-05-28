package me.bn.fileshare.cotroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Controller
public class PhotoController {
    @GetMapping("photo")
    public ModelAndView wall(){
        ModelAndView modelAndView = new ModelAndView();
        File file = new File("D:\\temp\\");
        List list = new ArrayList<String>();
        for (File listFile : file.listFiles()) {
            if (listFile.getName().contains("jpg")){
               list.add("img/"+listFile.getName()) ;
            }
        }
        modelAndView.setViewName("photowall");
        modelAndView.addObject("fileList",list);
        return modelAndView;
    }
}
