package com.fwitter.controllers;

import java.util.List;

import com.fwitter.models.ApplicationUser;
import com.fwitter.services.DiscoveryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discovery")
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @Autowired
    public DiscoveryController(DiscoveryService discoveryService){
        this.discoveryService = discoveryService;
    }

    @GetMapping("/users")
    public List<ApplicationUser> searchForUsers(@RequestParam String searchTerm){
        return discoveryService.searchForUsers(searchTerm);
    }
}




//import com.fwitter.models.ApplicationUser;
//import com.fwitter.services.DiscoveryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class DiscoveryController {
//
//    @Autowired
//    private DiscoveryService discoveryService;
//
//    // Endpoint to search for users by username
//    @GetMapping("/search")
//    public List<ApplicationUser> searchUsers(@RequestParam String username) {
//        return discoveryService.searchUsersByUsername(username);
//    }
//}