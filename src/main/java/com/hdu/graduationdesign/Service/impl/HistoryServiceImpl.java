package com.hdu.graduationdesign.Service.impl;

import com.hdu.graduationdesign.Mapper.HistoryMapper;
import com.hdu.graduationdesign.Pojo.History;
import com.hdu.graduationdesign.Service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public List<History> getHistory() {
        return historyMapper.getHistory();
    }

    @Override
    public void deleteHistory(Integer id) {
        historyMapper.deleteHistory(id);
    }

    @Override
    public void addHistory(History history) {
        historyMapper.addHistory(history);
    }

}
