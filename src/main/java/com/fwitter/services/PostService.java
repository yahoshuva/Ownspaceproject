//package com.fwitter.services;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fwitter.dto.CreatePostDTO;
//import com.fwitter.dto.CreateReplyDTO;
//import com.fwitter.exceptions.PostDoesNotExistException;
//import com.fwitter.exceptions.UnableToCreatePostException;
//import com.fwitter.models.ApplicationUser;
//import com.fwitter.models.Audience;
//import com.fwitter.models.Image;
//import com.fwitter.models.NotificationType;
//import com.fwitter.models.Poll;
//import com.fwitter.models.PollChoice;
//import com.fwitter.models.Post;
//import com.fwitter.models.ReplyRestriction;
//import com.fwitter.repositories.PostRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//@Transactional
//public class PostService {
//    private final PostRepository postRepo;
//    private final ImageService imageService;
//    private final PollService pollService;
//    private final TokenService tokenService;
//    private final UserService userService;
//    private final NotificationService notificationService;
//
//    @Autowired
//    public PostService(PostRepository postRepo, ImageService imageService, PollService pollService,
//            TokenService tokenService, UserService userService, NotificationService notificationService) {
//        this.postRepo = postRepo;
//        this.imageService = imageService;
//        this.pollService = pollService;
//        this.tokenService = tokenService;
//        this.userService = userService;
//        this.notificationService = notificationService;
//    }
//
//    public Post createPost(CreatePostDTO dto) {
//        System.out.println(dto);
//
//        Image savedGif;
//
//        // If true, there is a single gif from tenor
//        if (dto.getImages() != null && dto.getImages().size() > 0) {
//            System.out.println(dto.getImages());
//            List<Image> gifList = dto.getImages();
//            Image gif = gifList.get(0);
//            gif.setImagePath(gif.getImageURL());
//
//            savedGif = imageService.saveGifFromPost(gif);
//            gifList.remove(0);
//            gifList.add(savedGif);
//            dto.setImages(gifList);
//        }
//
//        // If true, there is a Poll that needs to be created
//        Poll savedPoll = null;
//        // System.out.println(dto);
//        if (dto.getPoll() != null) {
//            Poll p = new Poll();
//            p.setEndTime(dto.getPoll().getEndTime());
//            p.setChoices(new ArrayList<>());
//            savedPoll = pollService.generatePoll(p);
//            List<PollChoice> pollChoices = new ArrayList<PollChoice>();
//            List<PollChoice> choices = dto.getPoll().getChoices();
//            for (int i = 0; i < choices.size(); i++) {
//                PollChoice choice = choices.get(i);
//                choice.setPoll(savedPoll);
//                choice = pollService.generateChoice(choice);
//                pollChoices.add(choice);
//            }
//
//            savedPoll.setChoices(pollChoices);
//            savedPoll = pollService.generatePoll(savedPoll);
//
//            System.out.println(savedPoll);
//
//        }
//
//        Post p = new Post();
//        p.setContent(dto.getContent());
//
//        String[] words = dto.getContent().split(" ");
//        List<ApplicationUser> mentionedUsers = new ArrayList<>();
//        for (int i = 0; i < words.length; i++) {
//            if (words[i].startsWith("@")) {
//                try {
//                    String username = words[i].substring(1).replaceAll("[^a-zA-Z0-9]*$", "");
//                    ApplicationUser mentionedUser = userService.getUserByUsername(username);
//                    mentionedUsers.add(mentionedUser);
//                } catch (Exception e) {
//
//                }
//            }
//        }
//
//        if (dto.getScheduled()) {
//            p.setPostedDate(dto.getScheduledDate());
//        } else {
//            p.setPostedDate(LocalDateTime.now());
//            System.out.println(LocalDateTime.now());
//        }
//        p.setAuthor(dto.getAuthor());
//        p.setReplies(dto.getReplies());
//        p.setScheduled(dto.getScheduled());
//        p.setScheduledDate(dto.getScheduledDate());
//        p.setAudience(dto.getAudience());
//        p.setReplyRestriction(dto.getReplyRestriction());
//        p.setImages(dto.getImages());
//        p.setPoll(savedPoll);
//
//        try {
//            Post posted = postRepo.save(p);
//            notificationService.createAndSendPostNotifications(posted);
//            mentionedUsers.forEach((mentionUser) -> {
//                notificationService.createAndSendNotification(NotificationType.MENTION, mentionUser, posted.getAuthor(),
//                        posted, null);
//            });
//            return posted;
//        } catch (Exception e) {
//            // TODO: Setup custom exception
//            throw new UnableToCreatePostException();
//        }
//
//    }
//
//    public Post createMediaPost(String post, List<MultipartFile> files) {
//        CreatePostDTO dto = new CreatePostDTO();
//
//        try {
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule());
//            dto = objectMapper.readValue(post, CreatePostDTO.class);
//
//            Post p = new Post();
//            p.setContent(dto.getContent());
//            String[] words = dto.getContent().split(" ");
//            List<ApplicationUser> mentionedUsers = new ArrayList<>();
//            for (int i = 0; i < words.length; i++) {
//                if (words[i].startsWith("@")) {
//                    try {
//                        String username = words[i].substring(1).replaceAll("[^a-zA-Z0-9]*$", "");
//                        ApplicationUser mentionedUser = userService.getUserByUsername(username);
//                        mentionedUsers.add(mentionedUser);
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
//            if (dto.getScheduled()) {
//                p.setPostedDate(dto.getScheduledDate());
//            } else {
//                p.setPostedDate(LocalDateTime.now());
//            }
//            p.setAuthor(dto.getAuthor());
//            p.setReplies(dto.getReplies());
//            p.setScheduled(dto.getScheduled());
//            p.setScheduledDate(dto.getScheduledDate());
//            p.setAudience(dto.getAudience());
//            p.setReplyRestriction(dto.getReplyRestriction());
//
//            // Upload the images that got passed
//            List<Image> postImages = new ArrayList<>();
//
//            for (int i = 0; i < files.size(); i++) {
//                Image postImage = imageService.uploadImage(files.get(i), "post");
//                postImages.add(postImage);
//            }
//
//            p.setImages(postImages);
//
//            Post posted = postRepo.save(p);
//            notificationService.createAndSendPostNotifications(posted);
//            mentionedUsers.forEach((mentionUser) -> {
//                notificationService.createAndSendNotification(NotificationType.MENTION, mentionUser, posted.getAuthor(),
//                        posted, null);
//            });
//            return posted;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            // throw new UnableToCreatePostException();
//            return null;
//        }
//
//    }
//
//    public Post createReply(CreateReplyDTO dto) {
//
//        System.out.println("Create reply dto: " + dto);
//
//        CreatePostDTO postDTO = new CreatePostDTO(
//                dto.getReplyContent(),
//                dto.getAuthor(),
//                new HashSet<>(),
//                dto.getImages(),
//                dto.getScheduled(),
//                dto.getScheduledDate(),
//                Audience.EVERYONE,
//                ReplyRestriction.EVERYONE,
//                dto.getPoll());
//
//        Post reply = createPost(postDTO);
//        reply.setReply(true);
//        reply.setReplyTo(dto.getOriginalPost());
//
//        Post original = postRepo.findById(dto.getOriginalPost()).orElseThrow(UnableToCreatePostException::new);
//        Set<Post> originalPostReplies = original.getReplies();
//        originalPostReplies.add(reply);
//        original.setReplies(originalPostReplies);
//
//        postRepo.save(original);
//
//        Post savedReply = postRepo.save(reply);
//        notificationService.createAndSendNotification(NotificationType.REPLY, original.getAuthor(),
//                savedReply.getAuthor(), original, savedReply);
//        return savedReply;
//    }
//
//    public Post createReplyWithMedia(String reply, List<MultipartFile> files) {
//        CreateReplyDTO dto = new CreateReplyDTO();
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            dto = mapper.readValue(reply, CreateReplyDTO.class);
//            mapper.registerModule(new JavaTimeModule());
//
//            CreatePostDTO postDTO = new CreatePostDTO(
//                    dto.getReplyContent(),
//                    dto.getAuthor(),
//                    new HashSet<>(),
//                    dto.getImages(),
//                    dto.getScheduled(),
//                    dto.getScheduledDate(),
//                    Audience.EVERYONE,
//                    ReplyRestriction.EVERYONE,
//                    dto.getPoll());
//
//            Post replyPost = createPost(postDTO);
//            replyPost.setReply(true);
//            replyPost.setReplyTo(dto.getOriginalPost());
//
//            Post original = postRepo.findById(dto.getOriginalPost()).orElseThrow(UnableToCreatePostException::new);
//            Set<Post> originalPostReplies = original.getReplies();
//            originalPostReplies.add(replyPost);
//            original.setReplies(originalPostReplies);
//
//            postRepo.save(original);
//
//            // Upload the images that got passed
//            List<Image> postImages = new ArrayList<>();
//
//            for (int i = 0; i < files.size(); i++) {
//                Image postImage = imageService.uploadImage(files.get(i), "post");
//                postImages.add(postImage);
//            }
//
//            replyPost.setImages(postImages);
//
//            Post savedReply = postRepo.save(replyPost);
//            notificationService.createAndSendNotification(NotificationType.REPLY, original.getAuthor(),
//                    savedReply.getAuthor(), original, savedReply);
//            return savedReply;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new UnableToCreatePostException();
//        }
//    }
//
//    public Post repost(Integer postId, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userService.getUserByUsername(username);
//
//        Post post = postRepo.findById(postId).orElseThrow(PostDoesNotExistException::new);
//
//        Set<ApplicationUser> reposts = post.getReposts();
//        if (reposts.contains(user)) {
//            reposts = reposts.stream().filter(u -> u.getUserId() != user.getUserId()).collect(Collectors.toSet());
//        } else {
//            reposts.add(user);
//
//        }
//        post.setReposts(reposts);
//        notificationService.createAndSendNotification(NotificationType.REPOST, post.getAuthor(), user, post, null);
//        return postRepo.save(post);
//    }
//    public Post like(Integer postId, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userService.getUserByUsername(username);
//
//        Post post = postRepo.findById(postId).orElseThrow(PostDoesNotExistException::new);
//
//        if (post.getLikes().contains(user)) {
//            post.getLikes().remove(user);  // Unlike
//        } else {
//            post.getLikes().add(user);  // Like
//        }
//
//        notificationService.createAndSendNotification(NotificationType.LIKE, post.getAuthor(), user, post, null);
//        return postRepo.save(post);
//    }
//
//    public Post bookmark(Integer postId, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userService.getUserByUsername(username);
//
//        Post post = postRepo.findById(postId).orElseThrow(PostDoesNotExistException::new);
//
//        if (post.getBookmarks().contains(user)) {
//            post.getBookmarks().remove(user);  // Unbookmark
//        } else {
//            post.getBookmarks().add(user);  // Bookmark
//        }
//
//        notificationService.createAndSendNotification(NotificationType.BOOKMARK, post.getAuthor(), user, post, null);
//        return postRepo.save(post);
//    }
//
//
// /*   public Post like(Integer postId, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userService.getUserByUsername(username);
//
//        Post post = postRepo.findById(postId).orElseThrow(PostDoesNotExistException::new);
//
//        Set<ApplicationUser> likes = post.getLikes();
//        if (likes.contains(user)) {
//            likes = likes.stream().filter(u -> u.getUserId() != user.getUserId()).collect(Collectors.toSet());
//        } else {
//            likes.add(user);
//        }
//        post.setLikes(likes);
//
//        notificationService.createAndSendNotification(NotificationType.LIKE, post.getAuthor(), user, post, null);
//        return postRepo.save(post);
//    }
//
//    public Post bookmark(Integer postId, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userService.getUserByUsername(username);
//
//        Post post = postRepo.findById(postId).orElseThrow(PostDoesNotExistException::new);
//
//        Set<ApplicationUser> bookmarks = post.getBookmarks();
//        if (bookmarks.contains(user)) {
//            bookmarks = bookmarks.stream().filter(u -> u.getUserId() != user.getUserId()).collect(Collectors.toSet());
//        } else {
//            bookmarks.add(user);
//        }
//        post.setBookmarks(bookmarks);
//
//        notificationService.createAndSendNotification(NotificationType.BOOKMARK, post.getAuthor(), user, post, null);
//        return postRepo.save(post);
//    }
//*/
//    public Post viewPost(Integer postId, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userService.getUserByUsername(username);
//
//        Post post = postRepo.findById(postId).orElseThrow(PostDoesNotExistException::new);
//
//        Set<ApplicationUser> views = post.getViews();
//
//        if (views.contains(user))
//            return post;
//
//        views.add(user);
//
//        post.setViews(views);
//        try {
//            return postRepo.save(post);
//        } catch (Exception e) {
//            return post;
//        }
//
//    }
//
//    public List<Post> viewPosts(List<Integer> postIds, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userService.getUserByUsername(username);
//
//        List<Post> posts = postRepo.findByPostIdIn(postIds).orElse(new ArrayList<Post>());
//
//        List<Post> postsToUpdate = posts.stream()
//                .filter(post -> !post.getViews().contains(user))
//                .map(post -> {
//                    Set<ApplicationUser> views = post.getViews();
//                    views.add(user);
//                    post.setViews(views);
//                    return post;
//                })
//                .toList();
//
//        List<Post> updatedPosts = postRepo.saveAll(postsToUpdate);
//
//        posts.removeAll(updatedPosts);
//        posts.addAll(updatedPosts);
//
//        Collections.sort(posts);
//
//        return posts;
//    }
//
//    public List<Post> getAllPosts() {
//        return postRepo.findAll();
//    }
//
//    public Post getPostById(Integer id) {
//        // TODO: setup custom exception for posts that dont exist
//        return postRepo.findById(id).orElseThrow(PostDoesNotExistException::new);
//    }
//
//    public List<Post> getAllPostsByAuthor(ApplicationUser author) {
//        Set<Post> usersPosts = postRepo.findByAuthor(author).orElse(new HashSet<>());
//        List<Post> userPostsList = new ArrayList<>(usersPosts);
//
//        Collections.sort(userPostsList);
//
//        return userPostsList;
//    }
//
//    public List<Post> getUsersLikes(Integer userId) {
//        return postRepo.getUserLikes(userId);
//    }
//
//    // public Page<Post> getAllPostsByAuthors(Set<ApplicationUser> authors,
//    // LocalDateTime sessionStart, Integer page){
//    // // Get the next 100 posts starting on page specified in the request
//    // Pageable pageable = PageRequest.of(page, 100,
//    // Sort.by("postedDate").descending());
//    // return postRepo.findPostsByAuthors(authors, sessionStart, pageable);
//    // }
//
//    public Page<Post> getFeedPage(Integer userId, LocalDateTime sessionStart, Integer page) {
//        Pageable pageable = PageRequest.of(page, 100);
//        System.out.println(sessionStart);
//        return postRepo.findFeedPosts(userId, sessionStart, pageable);
//    }
//
//    public void deletePost(Post p) {
//        postRepo.delete(p);
//    }
//}




package com.fwitter.services;

import com.fwitter.exceptions.PostDoesNotExistException;
import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.models.*;
import com.fwitter.repositories.CommentRepository;
import com.fwitter.repositories.PostRepository;
import com.fwitter.repositories.UserRepository;
import com.google.cloud.vision.v1.Image;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;



import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class PostService {

    private UserRepository userRepository;

    private PostRepository postRepository;

    private CommentRepository commentRepository;

    private ImageService imageService;

    private TokenService tokenService; // Injected Token Service


    private CategoryService categoryService;


    private NotificationService notificationService;

    private UserService userService;

    @Autowired
    public PostService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, ImageService imageService, TokenService tokenService, NotificationService notificationService,CategoryService categoryService,UserService userService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.imageService = imageService;
        this.tokenService = tokenService;
        this.notificationService = notificationService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);




    private boolean scanImageForNudity(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();
        ByteString imgBytes = ByteString.copyFrom(imageBytes);
        Image image = Image.newBuilder().setContent(imgBytes).build();

        List<String> detectedLabels = new ArrayList<>();

        try (ImageAnnotatorClient visionClient = ImageAnnotatorClient.create()) {
            Feature safeSearchFeature = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
            Feature labelDetectionFeature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();

            List<Feature> features = List.of(safeSearchFeature, labelDetectionFeature);

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addAllFeatures(features)
                    .setImage(image)
                    .build();

            BatchAnnotateImagesResponse response = visionClient.batchAnnotateImages(List.of(request));

            if (response.getResponsesList().isEmpty()) {
                return false; // No response from API, assume safe
            }

            AnnotateImageResponse imageResponse = response.getResponsesList().get(0);

            SafeSearchAnnotation safeSearch = imageResponse.getSafeSearchAnnotation();
            if (safeSearch != null) {
                if (safeSearch.getAdult() == Likelihood.LIKELY || safeSearch.getAdult() == Likelihood.VERY_LIKELY) {
                    return true;
                }
            }

            for (EntityAnnotation label : imageResponse.getLabelAnnotationsList()) {
                detectedLabels.add(label.getDescription());
                if (isNudityLabel(label.getDescription())) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return false;
    }

    private boolean isNudityLabel(String label) {
        List<String> nudityLabels = List.of(
                "Lingerie", "Undergarment", "Nudity", "Fetish model",
                "Swimsuit", "Bikini", "Adult", "Sexy"
        );
        return nudityLabels.contains(label);
    }

//    public Post createPost(String title, String content, MultipartFile image)
//            throws UnableToSavePhotoException, IOException {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null ||
//                !authentication.isAuthenticated() ||
//                authentication instanceof AnonymousAuthenticationToken) {
//            throw new RuntimeException("User not authenticated");
//        }
//
//        String username = authentication.getName();
//
//        ApplicationUser author = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//      //  boolean nudityDetected = scanImageForNudity(image);
//
//       // if (nudityDetected) {
////            throw new UnableToSavePhotoException("Please don't upload these types of images.");
//       //     throw new UnableToSavePhotoException();
//
//
//       // }
//
//        String imagePath = imageService.uploadPostImage(image).getImageUrl();
//
//
//        Post post = new Post();
//        post.setTitle(title);
//        post.setContent(content);
//        post.setImagePath(imagePath);
//        post.setCreatedAt(LocalDateTime.now());
//        post.setAuthor(author);
//
//        System.out.println("Saving post by: " + author.getUsername());
//
//
//        try{
//
//            Post posted = postRepository.save(post);
//            notificationService.createAndSendPostNotifications(posted);
//
//            return posted;
//
//        }catch(Exception e){
//            throw new UnableToSavePhotoException();
//
//        }
//     //   return postRepository.save(post);
//
//    }



public Post createPost(String title, String content, MultipartFile image, List<String> categoryNames)
        throws UnableToSavePhotoException, IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null ||
            !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
        throw new RuntimeException("User not authenticated");
    }

    String username = authentication.getName();

    ApplicationUser author = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    String imagePath = imageService.uploadPostImage(image).getImageUrl();

    Post post = new Post();
    post.setTitle(title);
    post.setContent(content);
    post.setImagePath(imagePath);
    post.setCreatedAt(LocalDateTime.now());
    post.setAuthor(author);

    // Convert category names to Category entities
    Set<Category> categories = categoryNames.stream()
            .map(categoryService::getOrCreate)
            .collect(Collectors.toSet());

    post.setCategories(categories);

    System.out.println("Saving post by: " + author.getUsername());

    try {
        Post posted = postRepository.save(post);
        notificationService.createAndSendPostNotifications(posted);
        return posted;
    } catch (Exception e) {
        throw new UnableToSavePhotoException();
    }
}

//    public Post like(Integer postId, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(PostDoesNotExistException::new);
//
//        Set<ApplicationUser> likes = post.getLikes();
//        if (likes.contains(user)) {
//            likes.removeIf(u -> u.equals(user)); // Fix: Use .equals()
//        } else {
//            likes.add(user);
//        }
//        post.setLikes(likes);
//
//
//
//
//      return postRepository.save(post);
//    }

    public Post like(Integer postId, String token) {
        String username = tokenService.getUsernameFromToken(token);
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        Set<ApplicationUser> likes = post.getLikes();
        if (likes.contains(user)) {
            likes.remove(user); // Like already exists; remove it
        } else {
            likes.add(user); // Add like
            // Send notification for new like
            notificationService.createAndSendNotification(
                    NotificationType.LIKE,
                    post.getAuthor(), // recipient (post author)
                    user,             // actionUser (the user who liked the post)
                    post,null               // the post that was liked
            );
        }
        post.setLikes(likes);

        return postRepository.save(post);
    }


//    public Post bookmark(Integer postId, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(PostDoesNotExistException::new);
//
//        Set<ApplicationUser> bookmarks = post.getBookmarks();
//        if (bookmarks.contains(user)) {
//            bookmarks.removeIf(u -> u.equals(user)); // Fix: Use .equals()
//        } else {
//            bookmarks.add(user);
//        }
//        post.setBookmarks(bookmarks);
//
//        return postRepository.save(post);
//    }

    public Post bookmark(Integer postId, String token) {
        String username = tokenService.getUsernameFromToken(token);
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        Set<ApplicationUser> bookmarks = post.getBookmarks();
        if (bookmarks.contains(user)) {
            bookmarks.remove(user); // Bookmark already exists; remove it
        } else {
            bookmarks.add(user); // Add bookmark
            // Send notification for new bookmark
            notificationService.createAndSendNotification(
                    NotificationType.BOOKMARK,
                    post.getAuthor(),  // recipient (post author)
                    user,              // actionUser (the user who bookmarked the post)
                    post,null                // the post that was bookmarked
            );
        }
        post.setBookmarks(bookmarks);

        return postRepository.save(post);
    }

//
//    public Comment addComment(Integer postId, String content, String token) {
//        String username = tokenService.getUsernameFromToken(token);
//        ApplicationUser user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(PostDoesNotExistException::new);
//
//        Comment comment = new Comment();
//        comment.setContent(content);
//        comment.setAuthor(user);
//        comment.setPost(post);
//        comment.setCreatedAt(LocalDateTime.now());
//
//        return commentRepository.save(comment);
//    }

    public Comment addComment(Integer postId, String content, String token) {
        // Retrieve username from the token and find the user
        String username = tokenService.getUsernameFromToken(token);
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Retrieve the post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        // Create and set up the new comment
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        // Save the comment to the database
        Comment savedComment = commentRepository.save(comment);

        // Send a notification to the post author
        notificationService.createAndSendNotification(
                NotificationType.COMMENT,
                post.getAuthor(),
                user,
                post, savedComment
        );

        return savedComment;
    }


    public List<ApplicationUser> getUsersLikes(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);
        return new ArrayList<>(post.getLikes()); // Convert Set to List
    }

    public List<Post> getAllPostsByAuthor(ApplicationUser author) {
        Set<Post> usersPosts = postRepository.findByAuthor(author).orElse(new HashSet<>());
        List<Post> userPostsList = new ArrayList<>(usersPosts);

        Collections.sort(userPostsList);

        return userPostsList;
    }


    public List<Comment> getCommentsByPostId(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostDoesNotExistException::new);

        return commentRepository.findByPost(post);
    }

    public void deleteComment(Integer commentId, String token) {
        String username = tokenService.getUsernameFromToken(token);
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().equals(user)) {
            throw new RuntimeException("Unauthorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    public Post viewPost(Integer postId, String token) {
        String username = tokenService.getUsernameFromToken(token);
        ApplicationUser user = userService.getUserByUsername(username);

        Post post = postRepository.findById(postId).orElseThrow(PostDoesNotExistException::new);

        Set<ApplicationUser> views = post.getViews();

        if (views.contains(user))
            return post;

        views.add(user);

        post.setViews(views);
        try {
            return postRepository.save(post);
        } catch (Exception e) {
            return post;
        }

    }

    public List<Post> viewPosts(List<Integer> postIds, String token) {
        String username = tokenService.getUsernameFromToken(token);
        ApplicationUser user = userService.getUserByUsername(username);

        List<Post> posts = postRepository.findByPostIdIn(postIds).orElse(new ArrayList<Post>());

        List<Post> postsToUpdate = posts.stream()
                .filter(post -> !post.getViews().contains(user))
                .map(post -> {
                    Set<ApplicationUser> views = post.getViews();
                    views.add(user);
                    post.setViews(views);
                    return post;
                })
                .collect(Collectors.toList());

        List<Post> updatedPosts = postRepository.saveAll(postsToUpdate);

        posts.removeAll(updatedPosts);
        posts.addAll(updatedPosts);

        Collections.sort(posts);

        return posts;
    }



    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc(); // Fetch all posts sorted by date
    }

//    public Optional<Post> getPostById(Integer postId) {
//        return postRepository.findById(postId);
//    }

//    public Post getPostById(Integer id) {
//        return postRepository.findById(id).orElseThrow(PostDoesNotExistException::new);
//    }

    public Post getPostById(Integer id) {
        return postRepository.findById(id).orElseThrow(PostDoesNotExistException::new);
    }


    public void deletePost(int postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postRepository.delete(post);
    }



}

