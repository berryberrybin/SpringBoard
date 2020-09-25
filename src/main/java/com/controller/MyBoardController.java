package com.controller;

import com.board.dto.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
    public List<Param> titleList() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Param> paramList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:log4jdbc:mysql://localhost:3306/insight?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
            conn = DriverManager.getConnection(url, "root", "sb09130504@@");

            String sql = "SELECT * FROM board ";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Param param = new Param();
                int index = rs.getInt("_index");
                param.setIndex(index);
                String title = rs.getString("title");
                param.setTitle(title);
                String writer = rs.getString("writer");
                param.setWriter(writer);
                String update_time = rs.getString("update_time");
                param.setUpdateTime(update_time);
                paramList.add(param);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error : " + e.getMessage());
        } catch (ClassNotFoundException e1) {
            System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return paramList;
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public Param read(@RequestParam(value = "_index", defaultValue = "0") int index) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet resultSet = null;
        PreparedStatement pstmt = null;
        Param param = new Param();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:log4jdbc:mysql://localhost:3306/insight?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
            conn = DriverManager.getConnection(url, "root", "sb09130504@@");
            String sql2 = "UPDATE board SET hit_count = hit_count+ 1 WHERE _index = " + index;
            pstmt = conn.prepareStatement(sql2);
            pstmt.executeUpdate();

            String sql = "SELECT title, contents, writer, update_time,hit_count FROM board WHERE _index = " + index;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String title = rs.getString("title");
                param.setTitle(title);
                String contents = rs.getString("contents");
                param.setContents(contents);
                String writer = rs.getString("writer");
                param.setWriter(writer);
                String update_time = rs.getString("update_time");
                param.setUpdateTime(update_time);
                int hitCount = rs.getInt("hit_count");
                param.setHitCount(hitCount);
                System.out.println(title + " " + contents + " " + writer + " " + update_time);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error : " + e.getMessage());
        } catch (ClassNotFoundException e1) {
            System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return param;
    }

    @RequestMapping(value = "/board", method = RequestMethod.DELETE)
    public String delete(@RequestParam int index) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:log4jdbc:mysql://localhost:3306/insight?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
            conn = DriverManager.getConnection(url, "root", "sb09130504@@");

            String sql = " DELETE From board WHERE _index = " + index;

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("SQL Error : " + e.getMessage());
        } catch (ClassNotFoundException e1) {
            System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return "success";
    }

    @RequestMapping(value = "/board", method = RequestMethod.PUT)
    public String update(@RequestBody Param param) throws Exception {
        int index = param.getIndex();
        String title = param.getTitle();
        String contents = param.getContents();
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:log4jdbc:mysql://localhost:3306/insight?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
            conn = DriverManager.getConnection(url, "root", "sb09130504@@");
            String sql = "update board set title=?,contents=?where _index=" + index;
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, title);
            pstmt.setString(2, contents);
            pstmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Error" + e.getMessage());
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }
}