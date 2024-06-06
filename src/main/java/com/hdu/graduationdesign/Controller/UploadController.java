package com.hdu.graduationdesign.Controller;

import com.hdu.graduationdesign.Utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@CrossOrigin
@RestController
public class UploadController {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("content") String content) throws Exception {
        // 处理文件和文件内容
        String ossUrl = aliOSSUtils.upload(file);

        return ossUrl;
    }

}
