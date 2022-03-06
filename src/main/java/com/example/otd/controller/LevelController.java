package com.example.otd.controller;

import com.example.otd.dto.Stage;
import com.example.otd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/levels")
public class LevelController {

    @Autowired
    UserService userService;

    @GetMapping("/firststage")
    public String firststage(Model model){
        List<Stage> stageList = userService.getStages(0, 10);
        model.addAttribute("stageList", stageList);
        model.addAttribute("title", "First Stage (1 ~ 10)");
        model.addAttribute("table_type","success");
        return "levels/stages";
    }

    @GetMapping("/secondstage")
    public String secondstage(Model model){
        List<Stage> stageList = userService.getStages(10, 10);
        model.addAttribute("stageList", stageList);
        model.addAttribute("title", "Second Stage (11 ~ 20)");
        model.addAttribute("table_type","warning");
        return "levels/stages";
    }

    @GetMapping("/thirdstage")
    public String thirdstage(Model model){
        List<Stage> stageList = userService.getStages(20, 10);
        model.addAttribute("stageList", stageList);
        model.addAttribute("title", "Third Stage (21 ~ 30)");
        model.addAttribute("table_type","info");
        return "levels/stages";
    }

    @GetMapping("/fourthstage")
    public String fourthstage(Model model){
        List<Stage> stageList = userService.getStages(30, 10);
        model.addAttribute("stageList", stageList);
        model.addAttribute("title", "Fourth Stage (31 ~ 40)");
        model.addAttribute("table_type","danger");
        return "levels/stages";
    }
}
