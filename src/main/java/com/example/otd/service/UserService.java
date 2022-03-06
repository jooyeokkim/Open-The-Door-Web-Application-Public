package com.example.otd.service;

import com.example.otd.dto.*;
import com.example.otd.mapper.UserMapper;
import com.example.otd.rest.UserRanking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 멤버 전체 정보 조회
    public List<UserLevel> getAllUserTotal(int limit) {
        List<UserLevel> userList = userMapper.findAllUserTotal(limit);
        return userList;
    }

    // 특정 유저 정보 조회
    public UserLevel getUser(String userId) {
        UserLevel user = userMapper.findUserTotal(userId);
        return user;
    }

    // 특정 유저 순위 조회
    public int getUserRanking(String userId) {
        return userMapper.findUserRanking(userId);
    }

    // 유저수 조회
    public int getTotalUsers(){
        return userMapper.findTotalUsers();
    }

    // 유저별 맵별 기록 조회
    public UserRecords getUserRecords(String userId){
        UserRecords userRecords = userMapper.findUserRecords(userId);
        return userRecords;
    }

    // 유저별 맵별 획득 다이아몬드 수 조회
    public UserDiamond getUserDiamond(String userId){
        UserDiamond userDiamond = userMapper.findUserDiamond(userId);
        return userDiamond;
    }

    // 맵별 최단 기록 조회
    public BestRecord getBestRecord(int levelId){
        List<BestRecord> bestRecord = userMapper.findBestUserMapRecord(levelId);
        return bestRecord.get(0);
    }

    // 전체 스테이지 조회
    public List<Stage> getStages(int start, int count) {
        List<Stage> stageList = userMapper.findStages(start, count);
        return stageList;
    }

    // 특정 감정에 해당하는 댓글들 조회
    public List<Comment> getComments(String sentiment, int start){
        List<Comment> commentList = userMapper.findComments(sentiment, start);
        return commentList;
    }

    // 특정 댓글 조회
    public Comment getComment(String commentId) { return userMapper.findComment(commentId); }

    // 댓글 저장
    public void saveComment(String name, String password, String sentiment, float percent, String datetime, String content){
        userMapper.insertComment(name, password, sentiment, percent, datetime, content);
    }

    //댓글 수정
    public void editComment(String name, String password, String sentiment, float percent, String content, String commentId){
        userMapper.updateComment(name, password, sentiment, percent, content, commentId);
    }

    //댓글 삭제
    public void removeComment(String commentId) { userMapper.deleteComment(commentId); }
}
