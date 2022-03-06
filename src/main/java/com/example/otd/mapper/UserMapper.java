package com.example.otd.mapper;

import com.example.otd.dto.*;
import com.example.otd.rest.UserRanking;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user_total ORDER BY CURRENTLEVEL DESC , TOTALDIA DESC LIMIT ${limit}")
    List<UserLevel> findAllUserTotal(@Param("limit") int limit);

    @Select("SELECT * FROM user_total WHERE ID='${userId}'")
    UserLevel findUserTotal(@Param("userId") String userId);

    @Select("SELECT R.ranking\n" +
            "FROM\n" +
            "(SELECT ID, CURRENTLEVEL, TOTALDIA, RANK() OVER (ORDER BY CURRENTLEVEL DESC, TOTALDIA DESC) AS RANKING\n" +
            "FROM user_total) AS R WHERE ID='${userId}'")
    int findUserRanking(@Param("userId") String userId);

    @Select("SELECT COUNT(*) FROM user_total")
    int findTotalUsers();

    @Select("SELECT * FROM user_map_records WHERE ID='${userId}'")
    UserRecords findUserRecords(@Param("userId") String userId);

    @Select("SELECT * FROM user_map_dia WHERE ID='${userId}'")
    UserDiamond findUserDiamond(@Param("userId") String userId);

    @Select("SELECT R.ID, R.LEVEL${levelId} BESTRECORD\n" +
            "FROM user_map_records AS R\n" +
            "JOIN user_map_records_float AS RF ON R.ID = RF.ID\n" +
            "ORDER BY RF.LEVEL${levelId}\n" +
            "LIMIT 1")
    List<BestRecord> findBestUserMapRecord(@Param("levelId") int levelId);

    @Select("SELECT level, record4, record3, record2, record1, difficulty, bg_color\n" +
            "FROM stages s\n" +
            "JOIN colors c ON s.difficulty_id=c.id\n" +
            "ORDER BY level\n" +
            "LIMIT ${start}, ${count}")
    List<Stage> findStages(@Param("start") int start, @Param("count") int count);

    @Select("SELECT * FROM comment WHERE sentiment='${sentiment}' ORDER BY datetime DESC LIMIT ${start}, 20")
    List<Comment> findComments(@Param("sentiment") String sentiment, @Param("start") int start);

    @Select("SELECT * FROM comment WHERE id='${commentId}'")
    Comment findComment(@Param("commentId") String commentId);

    @Insert("INSERT INTO comment(name, password, sentiment, percent, datetime, content)\n" +
            "VALUE ('${name}', '${password}', '${sentiment}', ${percent}, '${datetime}', '${content}')")
    void insertComment(@Param("name") String name, @Param("password") String password, @Param("sentiment") String sentiment,
                       @Param("percent") float percent, @Param("datetime") String datetime, @Param("content") String content);

    @Update("UPDATE comment SET name='${name}', password='${password}', sentiment='${sentiment}',\n" +
            "percent=${percent}, content='${content}' WHERE id='${commentId}'")
    void updateComment(@Param("name") String name, @Param("password") String password, @Param("sentiment") String sentiment,
                       @Param("percent") float percent, @Param("content") String content, @Param("commentId") String commentId);

    @Delete("DELETE FROM comment WHERE id='${commentId}'")
    void deleteComment(@Param("commentId") String commentId);
}
