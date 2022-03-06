package com.example.otd.controller;

import com.example.otd.dto.BestRecord;
import com.example.otd.dto.UserLevel;
import com.example.otd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping(value={"/", "/home"})
    public String home(){
        return "home";
    }

    @GetMapping("/restapi")
    public String restapi(){
        return "restapi";
    }

    @GetMapping("/ranking")
    public String ranking(Model model){
        ArrayList<Map<String, String>> levelRanking = new ArrayList<>();
        List<UserLevel> userLevelList = userService.getAllUserTotal(50);
        for(int i=0;i<userLevelList.size();i++){
            UserLevel userLevel = userLevelList.get(i);
            Map<String,String> user = new HashMap<>();
            user.put("rank",Integer.toString(i+1));
            user.put("id", userLevel.getId());
            user.put("currentLevel", Integer.toString(userLevel.getCurrentLevel()));
            user.put("totalDia", Integer.toString(userLevel.getTotalDia()));
            levelRanking.add(user);
        }
        model.addAttribute("levelRanking",levelRanking);

        ArrayList<Map<String, String>> recordRanking = new ArrayList<>();
        for(int i=1;i<=40;i++){
            Map<String,String> record = new HashMap<>();
            record.put("level",Integer.toString(i));
            BestRecord bestRecord = userService.getBestRecord(i);
            record.put("id", bestRecord.getId());
            if(bestRecord.getBestRecord().equals("--:--:--")) record.put("id","-");
            record.put("bestRecord", bestRecord.getBestRecord());
            recordRanking.add(record);
        }
        model.addAttribute("recordRanking",recordRanking);
        return "ranking";
    }
}
