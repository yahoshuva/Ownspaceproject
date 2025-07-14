package com.fwitter.controllers;

import java.util.LinkedHashMap;
import java.util.Set;

import com.fwitter.dto.UpdateProfileRequest;
import com.fwitter.exceptions.FollowException;
import com.fwitter.exceptions.UnableToResolvePhotoException;
import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.services.ImageService;
import com.fwitter.services.NotificationService;
import com.fwitter.services.TokenService;
import com.fwitter.services.UserService;
import com.google.common.net.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	
	private final UserService userService;
	private final TokenService tokenService;
	private final ImageService imageService;
    private final NotificationService notificationService;
	
	@Autowired
	public UserController(UserService userService, TokenService tokenService, ImageService imageService, NotificationService notificationService) {
		this.tokenService = tokenService;
		this.userService = userService;
		this.imageService = imageService;
        this.notificationService = notificationService;
	}

	@GetMapping("/profile/{username}")
	public ApplicationUser getUserByUsername(@PathVariable("username") String username){
		return userService.getUserByUsername(username);
	}
	
	@GetMapping("/verify")
	public ApplicationUser verifyIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
		String username = tokenService.getUsernameFromToken(token);
		
		return userService.getUserByUsername(username);
	}
	
	@PostMapping("/pfp")
	public ApplicationUser uploadProfilePicture(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("image") MultipartFile file) throws UnableToSavePhotoException{
		
		String username = tokenService.getUsernameFromToken(token);
		
		return userService.setProfileOrBannerPicture(username, file, "pfp");
	}
	
	@PostMapping("/banner")
	public ApplicationUser uploadBannerPicture(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("image") MultipartFile file) throws UnableToSavePhotoException{
		String username = tokenService.getUsernameFromToken(token);
		return userService.setProfileOrBannerPicture(username, file, "bnr");
	}

	@PostMapping("/organization")
	public ResponseEntity<byte[]> setUserOrganization(@RequestPart("user") String user, @RequestPart("file") MultipartFile file, @RequestPart("organization") String organization) throws UnableToResolvePhotoException {
		byte[] org = userService.setUserOrganization(user, file, organization);

		return ResponseEntity.ok(org);
	}
	
	@PutMapping("/")
	public ApplicationUser updateUser(@RequestBody UpdateProfileRequest body) throws UnableToSavePhotoException {
		return userService.updateUserProfile(body);
	}
	
	
	@ExceptionHandler({FollowException.class})
	public ResponseEntity<String> handleFollowException(){
		return new ResponseEntity<String>("Users cannot follow themselves", HttpStatus.FORBIDDEN);
	}
//
//	@PutMapping("/follow")
//	public Set<ApplicationUser> followUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody LinkedHashMap<String, String> body) throws FollowException{
//		String loggedInUser = tokenService.getUsernameFromToken(token);
//		String followedUser = body.get("followedUser");
//
//        ApplicationUser user = userService.followUser(loggedInUser, followedUser);
//		ApplicationUser followed = user.getFollowing().stream().filter(u -> u.getUsername().equals(followedUser)).findFirst().orElse(null);
//
//
//		notificationService.createAndSendFollowNotification(followed,user);
//
//		return user.getFollowing();
//	}

	@PutMapping("/follow")
	public Set<ApplicationUser> followUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody LinkedHashMap<String, String> body) throws FollowException {
		String loggedInUsername = tokenService.getUsernameFromToken(token);
		String followedUsername = body.get("followedUser");

		// ✅ Fetch the followed user directly and validate
		ApplicationUser followed = userService.getUserByUsername(followedUsername);
		if (followed == null) {
			throw new FollowException();
		}

		ApplicationUser loggedInUser = userService.followUser(loggedInUsername, followedUsername);

		// ✅ Send notification with valid user objects
		notificationService.createAndSendFollowNotification(followed, loggedInUser);

		return loggedInUser.getFollowing();
	}


	@GetMapping("/following/{username}")
	public Set<ApplicationUser> getFollowingList(@PathVariable("username") String username){
		return userService.retrieveFollowingList(username);
	}
	
	@GetMapping("/followers/{username}")
	public Set<ApplicationUser> getFollowersList(@PathVariable("username") String username){
		return userService.retrieveFollowersList(username);
	}
}
