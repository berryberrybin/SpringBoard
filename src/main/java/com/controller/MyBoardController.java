package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        List<String> list = new ArrayList<>();
        File f = new File("fileDB/"+title+".txt");
        list.add();
        list.add("B");
        list.add("C");
        return list;
    }
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public void read() throws Exception {
    }
}
