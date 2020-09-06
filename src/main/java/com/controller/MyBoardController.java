package com.controller;


import com.board.dto.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/my")
public class MyBoardController {

    @RequestMapping(value = "/board", method = RequestMethod.POST)
    public String write(@RequestBody Param param) throws Exception {
        String title = param.getTitle();
        String contents = param.getContents();
        String writer = param.getWriter();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:log4jdbc:mysql://localhost:3306/insight?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
            conn = DriverManager.getConnection(url, "root", "sb09130504@@");

            String sql = "INSERT INTO board(title,contents,writer, update_time) VALUES(?,?,?,NOW())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, contents);
            pstmt.setString(3, writer);

            int count = pstmt.executeUpdate();
            if (count == 0) {
                System.out.println("데이터 입력 실패");
            } else {
                System.out.println("데이터 입력 성공");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패");
        } catch (SQLException e) {
            System.out.println("에러 " + e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "success";
    }

    @RequestMapping(value = "/boards", method = RequestMethod.GET)
    public List<String> titleList() throws Exception {
        List<String> fileNameList = new ArrayList<>();
        String fileName = "fileDB/";
        for (File info : new File(fileName).listFiles()) {
            if (info.isFile()) {
                fileNameList.add(info.getName());
            }
        }
        return fileNameList;
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public String read(HttpServletRequest httpServletRequest) throws Exception {
        String title = httpServletRequest.getParameter("title");
        File f = new File("fileDB/" + title + ".txt");
        if (!f.exists()) {
            return "Not found";
        }
        String contents = "";
        FileReader fileReader = new FileReader(f);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = null;
        while ((line = reader.readLine()) != null) {
            contents = contents + line + "\n";
        }
        reader.close();
        return contents;
    }

    @RequestMapping(value = "/board", method = RequestMethod.DELETE)
    public String delete(HttpServletRequest httpServletRequest) throws Exception {
        String title = httpServletRequest.getParameter("title");
        File f = new File("fileDB/" + title + ".txt");
        if (!f.exists()) {
            return "Not found";
        } else {
            f.delete();
        }
        return "success";
    }


}