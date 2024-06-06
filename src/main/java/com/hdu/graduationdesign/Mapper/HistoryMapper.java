package com.hdu.graduationdesign.Mapper;

import com.hdu.graduationdesign.Pojo.History;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface HistoryMapper {

    @Select("select * from historyrecord.history")
    List<History> getHistory();

    @Delete("delete from historyrecord.history where id = #{id}")
    void deleteHistory(Integer id);

    @Insert("insert into historyrecord.history (history, operate_time) values (#{history}, #{operateTime})")
    void addHistory(History history);
}
