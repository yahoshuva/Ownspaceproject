//package com.fwitter.controllers;
//
//import java.util.List;
//
//import com.fwitter.dto.CreatePostDTO;
//import com.fwitter.dto.CreateReplyDTO;
//import com.fwitter.dto.CreateViewsDTO;
//import com.fwitter.exceptions.PostDoesNotExistException;
//import com.fwitter.exceptions.UnableToCreatePostException;
//import com.fwitter.models.ApplicationUser;
//import com.fwitter.models.Post;
//import com.fwitter.services.PostService;
//import com.fwitter.services.UserService;
//import com.google.common.net.HttpHeaders;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/posts")
//public class PostController {
//
//	private final PostService postService;
//	private final UserService userService;
//
//	@Autowired
//	public PostController(PostService postService, UserService userService) {
//		this.postService = postService;
//		this.userService = userService;
//	}
//
//	@GetMapping("/")
//	public List<Post> getAllPosts(){
//		return postService.getAllPosts();
//	}
//
//	@ExceptionHandler({UnableToCreatePostException.class})
//	public ResponseEntity<String> handleUnableToCreatePost(){
//		return new ResponseEntity<String>("Unable to create post at this time", HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//
//	@PostMapping("/")
//	public Post createPost(@RequestBody CreatePostDTO postDTO) {
//		return postService.createPost(postDTO);
//	}
//
//	@PostMapping("/reply")
//	public Post createReply(@RequestBody CreateReplyDTO replyDTO){
//		return postService.createReply(replyDTO);
//	}
//
//	@PostMapping(value="/reply/media", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
//	public Post createMediaReply(@RequestPart("reply") String reply, @RequestPart("media") List<MultipartFile> files){
//		return postService.createReplyWithMedia(reply, files);
//	}
//
//	@PostMapping(value="/media", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
//	public Post createMediaPost(@RequestPart("post") String post, @RequestPart("media") List<MultipartFile> files) {
//		return postService.createMediaPost(post, files);
//	}
//
//	@ExceptionHandler({PostDoesNotExistException.class})
//	public ResponseEntity<String> handlePostDoesNotExist(){
//		return new ResponseEntity<String>("Post does not exist", HttpStatus.NOT_FOUND);
//	}
//
//	@GetMapping("/id/{id}")
//	public Post getPostById(@PathVariable("id") int id) {
//		return postService.getPostById(id);
//	}
//
//	@GetMapping("/author/{userId}")
//	public List<Post> getPostsByAuthor(@PathVariable("userId") Integer userId){
//		ApplicationUser author = userService.getUserById(userId);
//		return postService.getAllPostsByAuthor(author);
//	}
//
//	@PutMapping("/repost/{id}")
//	public Post repost(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
//		return postService.repost(id, token);
//	}
//
//	@PutMapping("/like/{id}")
//	public Post like(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
//		return postService.like(id, token);
//	}
//
//	@PutMapping("/bookmark/{id}")
//	public Post bookmark(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
//		return postService.bookmark(id, token);
//	}
//
//    @GetMapping("/likes/{id}")
//    public List<Post> getLikes(@PathVariable("id") Integer id){
//        return postService.getUsersLikes(id);
//    }
//
//    @PutMapping("/view/{id}")
//    public Post view(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
//        return postService.viewPost(id, token);
//    }
//
//    @PutMapping("/view/all")
//    public List<Post> viewPostsById(@RequestBody CreateViewsDTO views, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
//        return postService.viewPosts(views.getIds(), token);
//    }
//
//	@DeleteMapping("/")
//	public ResponseEntity<String> deletePost(@RequestBody Post p){
//		postService.deletePost(p);
//		return new ResponseEntity<String>("Post has been deleted", HttpStatus.OK);
//	}
//
//}
package com.fwitter.controllers;



import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fwitter.dto.CreateCommentDTO;
import com.fwitter.dto.CreateViewsDTO;
import com.fwitter.exceptions.PostDoesNotExistException;
import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Comment;
import com.fwitter.models.Post;
import com.fwitter.services.PostService;
import com.fwitter.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // Get all posts
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<Post> getPostById(@PathVariable("id") int id) {
//        return postService.getPostById(id)
//                .map(ResponseEntity::ok)
//                .orElseThrow(PostDoesNotExistException::new);
//    }


    @GetMapping("/id/{id}")
    public Post getPostById(@PathVariable("id") int id) {
        return postService.getPostById(id);
    }


    @GetMapping("/author/{userId}")
    public List<Post> getPostsByAuthor(@PathVariable("userId") Integer userId){
        ApplicationUser author = userService.getUserById(userId);
        return postService.getAllPostsByAuthor(author);
    }

    // Create a post
//    @PostMapping("/create")
//    public ResponseEntity<Post> createPost(
//            @RequestParam String title,
//            @RequestParam String content,
//            @RequestParam("image") MultipartFile image,
//    ) throws UnableToSavePhotoException, IOException {
//        return ResponseEntity.ok(postService.createPost(title, content, image));
//    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam("image") MultipartFile image,
            @RequestParam("categories") List<String> categories
    ) throws UnableToSavePhotoException, IOException {
        return ResponseEntity.ok(postService.createPost(title, content, image, categories));
    }


    // Like a post
    @PutMapping("/like/{id}")
    public ResponseEntity<Post> likePost(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(postService.like(id, token));
    }

    // Bookmark a post
    @PutMapping("/bookmark/{id}")
    public ResponseEntity<Post> bookmarkPost(@PathVariable("id") int id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(postService.bookmark(id, token));
    }

    // Get users who liked a post
    @GetMapping("/likes/{id}")
    public ResponseEntity<List<ApplicationUser>> getLikes(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(postService.getUsersLikes(id));
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") int postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Post has been deleted");
    }

    // **COMMENT-RELATED ENDPOINTS**

    // Add a comment to a post
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Integer postId,
            @RequestBody CreateCommentDTO commentDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Comment comment = postService.addComment(postId, commentDTO.content(), token);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }



    // Get all comments for a post
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Integer postId) {
        List<Comment> comments = postService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }



    // Delete a comment
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer commentId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        postService.deleteComment(commentId, token);
        return ResponseEntity.ok("Comment deleted successfully");
    }
    @PutMapping("/view/{id}")
    public Post view(@PathVariable("id") int id, @RequestHeader(com.google.common.net.HttpHeaders.AUTHORIZATION) String token){
        return postService.viewPost(id, token);
    }

    @PutMapping("/view/all")
    public List<Post> viewPostsById(@RequestBody CreateViewsDTO views, @RequestHeader(com.google.common.net.HttpHeaders.AUTHORIZATION) String token){
        return postService.viewPosts(views.getIds(), token);
    }

    // Exception Handlers
    @ExceptionHandler(PostDoesNotExistException.class)
    public ResponseEntity<String> handlePostDoesNotExist() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
    }

    @ExceptionHandler(UnableToSavePhotoException.class)
    public ResponseEntity<String> handleUnableToSavePhoto() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to create post at this time");
    }
}

