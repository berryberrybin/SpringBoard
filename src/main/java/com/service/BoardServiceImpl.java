package com.service;

import java.util.List;

import com.board.dto.BoardDto;
import com.cache.LRUCache;
import com.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@Transactional

public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<BoardDto> selectBoardList() throws Exception {
        return boardMapper.selectBoardList();
    }

    @Override
    public void insertBoard(BoardDto board) throws Exception {
        boardMapper.insertBoard(board);
    }

    @Override
    public BoardDto selectBoardDetail(int boardIdx) throws Exception {
        boardMapper.updateHitCount(boardIdx);
        BoardDto board ;
        LRUCache lruCache = new LRUCache(boardIdx);
        if(!lruCache.containsKey(boardIdx)){
            board = boardMapper.selectBoardDetail(boardIdx);
            lruCache.put(boardIdx, board);
        } else{
            board = lruCache.get(boardIdx);
        }
        return board;
    }

    @Override
    public void updateBoard(BoardDto board) throws Exception {
        boardMapper.updateBoard(board);
    }

    @Override
    public void deleteBoard(int boardIdx) throws Exception {
        boardMapper.deleteBoard(boardIdx);
    }



}
