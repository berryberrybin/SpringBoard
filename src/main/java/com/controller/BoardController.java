package com.controller;
import java.util.List;
import com.board.dto.BoardDto;
import com.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @RequestMapping("/board/openBoardList.do")
    public ModelAndView openBoardList() throws Exception{
        ModelAndView mv = new ModelAndView();
        mv.setViewName("boardList");

        List<BoardDto> list = boardService.selectBoardList();
        mv.addObject("list", list);
        System.out.println("S");
        return mv;
    }
}
