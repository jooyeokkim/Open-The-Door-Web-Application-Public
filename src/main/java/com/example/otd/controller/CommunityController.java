package com.example.otd.controller;

import com.example.otd.dto.Comment;
import com.example.otd.dto.UserDiamond;
import com.example.otd.dto.UserLevel;
import com.example.otd.dto.UserRecords;
import com.example.otd.rest.User;
import com.example.otd.service.SentimentService;
import com.example.otd.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
@RequestMapping("/community")
public class CommunityController {
    private final String URL_PREF = "community";
    private final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    @Autowired
    UserService userService;

    @Autowired
    SentimentService sentimentService;


    @GetMapping("/write")
    public String write(){
        return URL_PREF+"/write";
    }


    // Write or Edit comment
    @PostMapping("/save")
    public String save(HttpServletRequest request){
        Object commentId=request.getParameter("commentId");
        Object name=request.getParameter("name");
        Object password=request.getParameter("password");
        Object content=request.getParameter("content");

        if(name.equals("")) name="익명";
        if(password.equals("")) password="1234";
        if(content.equals("")) return "error/empty_content";

        String sentimentJsonStr = sentimentService.getSentimentJsonStr(content.toString());
        if(sentimentJsonStr.equals("error")) return "error/api_server";
        JSONObject sentimentJson = new JSONObject(sentimentJsonStr);
        JSONObject document = (JSONObject) sentimentJson.get("document");
        String sentiment = document.get("sentiment").toString();
        JSONObject confidence = (JSONObject) document.get("confidence");
        Float percent = Float.parseFloat(confidence.get(sentiment).toString());
        if(percent<50) sentiment="neutral";

        if(request.getParameter("commentId")==null)
            userService.saveComment(name.toString(), password.toString(), sentiment, percent,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)), content.toString());
        else {
            userService.editComment(name.toString(), password.toString(), sentiment, percent,
                    content.toString(), commentId.toString());
        }
        return "alarm/check_"+sentiment;
    }


    @GetMapping("/forum/{forumType}")
    public String forum(@PathVariable String forumType, HttpServletRequest request, Model model) {
        String sentiment = forumType.toLowerCase();
        Object opage = request.getParameter("page");
        if(opage==null) opage = 1;
        List<Comment> commentList = userService.getComments(sentiment, (Integer.parseInt(opage.toString())-1)*10);
        model.addAttribute("commentList", commentList);
        model.addAttribute("currentPage", opage.toString());

        if(sentiment.equals("negative")){
            model.addAttribute("forum", "Negative");
            model.addAttribute("isNegative", true);
            model.addAttribute("borderColor", "danger");
        }
        else if(sentiment.equals("neutral")){
            model.addAttribute("forum", "Neutral");
            model.addAttribute("isNeutral", true);
            model.addAttribute("borderColor", "secondary");
        }
        else{
            model.addAttribute("forum", "Positive");
            model.addAttribute("isPositive", true);
            model.addAttribute("borderColor", "primary");
        }
        return URL_PREF+"/forum";
    }



    // Edit comment
    @GetMapping("/edit/pwcheck/{commentId}")
    public String editPwcheck(@PathVariable String commentId, Model model){
        model.addAttribute("commentId", commentId);
        model.addAttribute("action","/community/edit/write");
        return URL_PREF+"/pw";
    }


    @PostMapping("/edit/write")
    public String editWrite(HttpServletRequest request, Model model){
        Object commentId = request.getParameter("commentId");
        Object inputPassword = request.getParameter("password");
        Comment comment = userService.getComment(commentId.toString());
        if(!inputPassword.toString().equals(comment.getPassword())) return "error/incorrect_password";
        model.addAttribute("comment", comment);
        return URL_PREF+"/edit";
    }



    // Delete comment
    @GetMapping("/delete/pwcheck/{commentId}")
    public String deletePwcheck(@PathVariable String commentId, Model model){
        model.addAttribute("commentId", commentId);
        model.addAttribute("action","/community/delete/execute");
        return URL_PREF+"/pw";
    }


    @PostMapping("/delete/execute")
    public String deleteExecute(HttpServletRequest request, Model model){
        Object commentId = request.getParameter("commentId");
        Object inputPassword = request.getParameter("password");
        Comment comment = userService.getComment(commentId.toString());
        if(!inputPassword.toString().equals(comment.getPassword())) return "error/incorrect_password";
        userService.removeComment(commentId.toString());
        return "alarm/delete_complete";
    }
}

