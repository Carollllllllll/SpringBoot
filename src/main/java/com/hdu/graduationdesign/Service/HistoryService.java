package com.hdu.graduationdesign.Service;

import com.hdu.graduationdesign.Pojo.History;

import java.util.List;

public interface HistoryService {
    List<History> getHistory();

    void deleteHistory(Integer id);

    void addHistory(History history);
}
