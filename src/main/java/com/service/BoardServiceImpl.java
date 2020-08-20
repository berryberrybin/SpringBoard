package com.service;

import com.board.dto.BoardDto;
import com.board.dto.BoardFileDto;
import com.cache.LRUCache;
import com.common.FileUtils;
import com.mapper.BoardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
//@Transactional

public class BoardServiceImpl implements BoardService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<BoardDto> selectBoardList() throws Exception {
        return boardMapper.selectBoardList();
    }

    @Override
    public void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {

        boardMapper.insertBoard(board);
        List<BoardFileDto> fileList = fileUtils.parseFileInfo(board.getBoardIdx(), multipartHttpServletRequest);
        log.info("FileList size: " + fileList.size());
        if (!CollectionUtils.isEmpty(fileList)) {
            boardMapper.insertBoardFileList(fileList);
        }

        /*if(!ObjectUtils.isEmpty(multipartHttpServletRequest)){
            Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
            String name;
            while(iterator.hasNext()){
                name = iterator.next();
                log.debug("file tag name:"+name);
                List<MultipartFile> list = multipartHttpServletRequest.getFiles(name);
                for(MultipartFile multipartFile : list){
                    log.debug("start file information");
                    log.debug("file name :"+multipartFile.getOriginalFilename());
                    log.debug("file size"+multipartFile.getSize());
                    log.debug("file content type"+multipartFile.getContentType());
                    log.debug("end file information.\n");
                }

            }
        }*/
    }

    @Override
    public BoardDto selectBoardDetail(int boardIdx) throws Exception {
        boardMapper.updateHitCount(boardIdx);
        BoardDto board;
        LRUCache lruCache = new LRUCache(boardIdx);
        if (!lruCache.containsKey(boardIdx)) {
            board = boardMapper.selectBoardDetail(boardIdx);
            lruCache.put(boardIdx, board);
        } else {
            board = lruCache.get(boardIdx);
        }
        List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
        board.setFileList(fileList);

        boardMapper.updateHitCount(boardIdx);
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
