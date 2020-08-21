package com.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import com.board.dto.BoardDto;
import com.board.dto.BoardFileDto;
import com.service.BoardService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class BoardController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BoardService boardService;

    @RequestMapping(value = "/board/openBoardList.do", method = RequestMethod.GET)
    public ModelAndView openBoardList() throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("boardList");
        List<BoardDto> list = boardService.selectBoardList();
        mv.addObject("list", list);
        log.info("Call openBoardList. Size: " + list.size());
        return mv;
    }

    @RequestMapping(value = "/board/openBoardWrite.do", method = RequestMethod.GET)
    public String openBoardWrite() throws Exception {
        return "boardWrite";
    }

    @RequestMapping(value = "/board/insertBoard.do", method = RequestMethod.POST)
    public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        boardService.insertBoard(board, multipartHttpServletRequest);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping(value = "/board/openBoardDetail.do", method = RequestMethod.GET)
    public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception {
        ModelAndView mv = new ModelAndView("boardDetail");
        BoardDto board = boardService.selectBoardDetail(boardIdx);
        mv.addObject("board", board);

        return mv;
    }

    @RequestMapping(value = "/board/updateBoard.do", method = RequestMethod.POST)
    public String updateBoard(BoardDto board) throws Exception {
        boardService.updateBoard(board);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping(value = "/board/deleteBoard.do", method = RequestMethod.POST)
    public String deleteBoard(int boardIdx) throws Exception {
        boardService.deleteBoard(boardIdx);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/downloadBoardFile.do")
    public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx,
                                  HttpServletResponse response) throws Exception{
        BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);
        if(ObjectUtils.isEmpty(boardFile)==false){
            String fileName = boardFile.getOriginalFileName();
            byte[] files = FileUtils.readFileToByteArray(new File (boardFile.getStoredFilePath()));

            response.setContentType("application/octet-stream");
            response.setContentLength(files.length);
            response.setHeader("Content-Disposition","attachment;fileName=\""+
                    URLEncoder.encode(fileName,"UTF-8")+"\";");
            response.setHeader("Content-Transfer-Encoding","binary");
            response.getOutputStream().write(files);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }
}
