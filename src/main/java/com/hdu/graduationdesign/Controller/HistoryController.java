package com.hdu.graduationdesign.Controller;

import com.hdu.graduationdesign.Pojo.History;
import com.hdu.graduationdesign.Service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/getHistory")
    public List<History> getHistory() {
        return historyService.getHistory();
    }

    @PostMapping("/deleteHistory/{id}")
    public void deleteHistory(@PathVariable Integer id) {
        historyService.deleteHistory(id);
    }

    @PostMapping("/addHistory")
    public void addHistory(@RequestBody String history) {
        History his = new History();
        String history1 = null;
        try {
            history1 = URLDecoder.decode(history, "UTF-8");
            history1 = history1.substring(0, history1.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        his.setHistory(history1);
        his.setOperateTime(LocalDateTime.now());
        historyService.addHistory(his);
    }

}
