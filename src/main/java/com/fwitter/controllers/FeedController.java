//package com.fwitter.controllers;
//
//import com.fwitter.dto.FeedPostDTO;
//import com.fwitter.dto.FeedRequestDTO;
//import com.fwitter.dto.FetchFeedReponseDTO;
//import com.fwitter.models.Post;
//import com.fwitter.services.FeedService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collections;
//import java.util.List;
//
//@RestController
//@RequestMapping("/feed")
//public class FeedController {
//
//    private final FeedService feedService;
//
//    @Autowired
//    public FeedController(FeedService feedService){
//        this.feedService = feedService;
//    }
//
//    @PostMapping
//    public FetchFeedReponseDTO getPostsForFeed(@RequestBody FeedRequestDTO feedRequest){
//        System.out.println(feedRequest);
//        return feedService.getFeedForUser(feedRequest.getUserId(), feedRequest.getSessionStart(), feedRequest.getPage());
//    }
//
//}
