package com.example.otd.controller;

import com.example.otd.dto.*;
import com.example.otd.rest.Level;
import com.example.otd.rest.LevelRanking;
import com.example.otd.rest.User;
import com.example.otd.rest.UserRanking;
import com.example.otd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping("/restapi/otd")
public class RestapiController {

    private final String link_pref="static/images/stages/clearlevel";
    private final String link_postf=".png";

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    public User userInfo(@PathVariable String userId) {
        UserLevel userLevel = userService.getUser(userId);
        User user = new User();

        if(userLevel==null){
            user.setResponse("ERROR : User Not Found");
            return user;
        }

        int userRank = userService.getUserRanking(userId);
        int totalUsers = userService.getTotalUsers();
        user.setResponse("SUCCESS");
        user.setId(userLevel.getId());
        user.setRank(userRank);
        user.setUsers(totalUsers);
        user.setLevel(userLevel.getCurrentLevel());
        user.setDiamonds(userLevel.getTotalDia());

        List<Map<String, String>> records = new ArrayList<>();

        UserRecords userRecords = userService.getUserRecords(userId);
        UserDiamond userDiamond = userService.getUserDiamond(userId);
        int mem_cnt = -1;
        try{
            for(Field field : userRecords.getClass().getDeclaredFields()){
                mem_cnt++;
                if(mem_cnt==0) continue;
                LinkedHashMap<String, String> record = new LinkedHashMap<>();
                field.setAccessible(true);
                Object value = field.get(userRecords);
                record.put("level"+mem_cnt, value.toString());
                records.add(record);
            }
            mem_cnt = -1;
            for(Field field : userDiamond.getClass().getDeclaredFields()){
                mem_cnt++;
                if(mem_cnt==0) continue;
                field.setAccessible(true);
                Object value = field.get(userDiamond);
                records.get(mem_cnt-1).put("reward_diamond", value.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        user.setRecords(records);
        return user;
    }

    @GetMapping("/levels")
    public List<Level> levels(HttpServletRequest request) {
        String s_start=request.getParameter("start");
        String s_end=request.getParameter("end");
        if(s_start==null) s_start="1";
        if(s_end==null) s_end="40";
        int start = Integer.parseInt(s_start);
        int end = Integer.parseInt(s_end);
        List<Level> levelList = new ArrayList<>();
        List<Stage> stageList = userService.getStages(start-1, end-start+1);
        for(int i=0;i<stageList.size();i++){
            Stage stage = stageList.get(i);
            Level level = new Level();
            level.setResponse("SUCCESS");
            level.setLevel(stage.getLevel());
            level.setMinimapImg(link_pref+stage.getLevel()+link_postf);
            level.setRecord4(stage.getRecord4());
            level.setRecord3(stage.getRecord3());
            level.setRecord2(stage.getRecord2());
            level.setRecord1(stage.getRecord1());
            level.setDifficulty(stage.getDifficulty());
            levelList.add(level);
        }
        return levelList;
    }

    @GetMapping("/ranking/user")
    public List<UserRanking> userRanking(){
        List<UserRanking> userRankingList = new ArrayList<>();
        List<UserLevel> userLevelList = userService.getAllUserTotal(100);
        for(int i=0;i<userLevelList.size();i++){
            UserLevel userLevel = userLevelList.get(i);
            UserRanking userRanking = new UserRanking();
            userRanking.setResponse("SUCCESS");
            userRanking.setRank(i+1);
            userRanking.setId(userLevel.getId());
            userRanking.setCurrentLevel(userLevel.getCurrentLevel());
            userRanking.setTotalDia(userLevel.getTotalDia());
            userRankingList.add(userRanking);
        }
        return userRankingList;
    }

    @GetMapping("/ranking/level")
    public List<LevelRanking> levelRanking(){
        List<LevelRanking> levelRankingList = new ArrayList<>();
        for(int i=1;i<=40;i++){
            LevelRanking levelRanking = new LevelRanking();
            BestRecord bestRecord = userService.getBestRecord(i);
            levelRanking.setResponse("SUCCESS");
            levelRanking.setLevel(i);
            levelRanking.setId(bestRecord.getId());
            levelRanking.setBestRecord(bestRecord.getBestRecord());
            if(levelRanking.getBestRecord().equals("--:--:--")) levelRanking.setId(null);
            levelRankingList.add(levelRanking);
        }
        return levelRankingList;
    }
}
