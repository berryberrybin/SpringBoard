package com.cache;

import com.board.dto.BoardDto;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache extends LinkedHashMap<Integer, BoardDto> {
    private int capacity;

    public LRUCache(int capacity){
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }
    public BoardDto get(int key){
        return super.getOrDefault(key,null); // 값이 존재 하지 않을 경우 null 반환
    }
    public void put(int key, BoardDto value){
        super.put(key,value);
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer,BoardDto> eldest){
        return size()>capacity;
    }

}
