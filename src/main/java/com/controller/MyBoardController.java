package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MyBoardController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String openBoardList() throws Exception {
        return "Hello";
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String write(HttpServletRequest httpServletRequest) throws Exception {
        String title = httpServletRequest.getParameter("title");
        String contents = httpServletRequest.getParameter("contents");
        File file = new File("fileDB/"+title+".txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<String> titleList() throws Exception {
        List<String> fileNameList = new ArrayList<>();
        String fileName = "fileDB/" ;
        for (File info : new File(fileName).listFiles()) {
            if (info.isFile()) {
                fileNameList.add(info.getName());
            }
        }
        return fileNameList;
    }
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public String read(HttpServletRequest httpServletRequest) throws Exception {
        String title = httpServletRequest.getParameter("title");
        String fileName = "fileDB/"+title+".txt";
        FileReader fileReader = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = null;
        String contents="";
        while((line = reader.readLine())!=null){
            contents += line;
        }
        reader.close();
        return contents;
    }
}
