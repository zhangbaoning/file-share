package me.bn.fileshare.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FileServiceTest {
@Autowired
FileService service;
    @Test
    void save() {
//        service.save("1111",true);
        
        String str = "20150117_114204.jpg  20150503_090816.jpg      20180522_110007.jpg  DSC_9791.jpg " +
                "20150117_121204.jpg  20150503_090833.jpg      20180522_110159.jpg  DSC_9802.jpg " +
                "20150118_112519.jpg  20150503_091723.jpg      20180522_110203.jpg  DSC_9803.jpg " +
                "20150118_112800.jpg  20150503_091735.jpg      20181014_103952.jpg  DSC_9804.jpg " +
                "20150118_112816.jpg  20150503_113859.jpg      20181014_104053.jpg  DSC_9805.jpg " +
                "20150118_114324.jpg  20150503_113904.jpg      20181014_121526.jpg  DSC_9807.jpg " +
                "20150118_114349.jpg  20150503_113915.jpg      20181014_150304.jpg  DSC_9809.jpg " +
                "20150118_114636.jpg  20150503_124501.jpg      20181014_150346.jpg  DSC_9810.jpg " +
                "20150118_120200.jpg  20150503_150533_HDR.jpg  20181023_211526.jpg  DSC_9811.jpg " +
                "20150118_130409.jpg  20150503_155905.jpg      20181023_211623.jpg  IMG_20180522_105431.jpg " +
                "20150118_133018.jpg  20160520_180330.jpg      20181023_212552.jpg  IMG_20181219_210945.jpg " +
                "20150118_162340.jpg  20160524_083332.jpg      20190107_205124.jpg  IMG_20190331_165641.jpg " +
                "20150502_113731.jpg  20160524_083410.jpg      20190704_212227.jpg  Screenshot_20190107-210044_WeChat.jpg " +
                "20150502_140009.jpg  20160904_075625.jpg      20191120_211755.jpg  Screenshot_20190921-173224_Alipay.jpg " +
                "20150502_141120.jpg  20160908_085915.jpg      20191124_142325.jpg  Screenshot_20190921-173347_Alipay.jpg " +
                "20150502_142304.jpg  20170304_131331_001.jpg  20191201_144639.jpg  Screenshot_20191112-114532.jpg " +
                "20150502_143333.jpg  20180522_104642.jpg      20200526_190902.jpg  Screenshot_20191115-224829_WeChat.jpg " +
                "20150502_143340.jpg  20180522_105810.jpg      20200526_190907.jpg " +
                "20150502_144836.jpg  20180522_105813.jpg      CAM00032.jpg " +
                "20150503_084214.jpg  20180522_110003.jpg      DSC_9790.jpg";
        for (String s : str.split(" ")) {
            if (!StringUtils.isEmpty(s)){
                System.out.println(s);
                //service.save(s,false,"127.0.0.1");
            }
        }
    }
}