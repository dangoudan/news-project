package com.kenji.controller.user;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class FileController {

    @RequestMapping("/getPic")
    public void getPicture(String path, HttpServletResponse response) throws Exception {
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream,outputStream);
    }

}
