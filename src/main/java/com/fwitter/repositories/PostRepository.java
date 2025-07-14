//package com.fwitter.repositories;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import com.fwitter.models.ApplicationUser;
//import com.fwitter.models.Post;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface PostRepository extends JpaRepository<Post, Integer>{
//
//	String FEED_QUERY = "select * from\n" +
//			"\t(select * from posts where author_id = :id\n" +
//			"\tunion\n" +
//			"\tselect p.post_id, p.audience, p.content, p.posted_date, p.is_reply, p.reply_restriction, p.reply_to, p.scheduled,\n" +
//			"\t\tp.scheduled_date, p.author_id, p.poll_id\n" +
//			"\tfrom posts p\n" +
//			"\tinner join post_repost_junction prj\n" +
//			"\ton p.post_id = prj.post_id where prj.user_id\n" +
//			"\tin (select u.user_id as following_id\n" +
//			"\t\tfrom users u\n" +
//			"\t\tinner join following\n" +
//			"\t\ton u.user_id = following.following_id where following.user_id = :id and not following.following_id = :id)\n" +
//			"\tunion\n" +
//			"\tselect p.post_id, p.audience, p.content, p.posted_date, p.is_reply, p.reply_restriction, p.reply_to, p.scheduled,\n" +
//			"\t\tp.scheduled_date, p.author_id, p.poll_id\n" +
//			"\tfrom posts p\n" +
//			"\twhere p.author_id in (select u.user_id as following_id\n" +
//			"\t\t\tfrom users u\n" +
//			"\t\t\tinner join following\n" +
//			"\t\t\ton u.user_id = following.following_id where following.user_id = :id and not following.following_id = :id)\n" +
//			"\t\t\t) as p where p.posted_date <= :session_start order by p.posted_date desc";
//
//	Optional<Set<Post>> findByAuthor(ApplicationUser author);
//
//	@Query(nativeQuery = true, value = FEED_QUERY,
//			countQuery = "select count(*) from (" + FEED_QUERY + ")"
//		)
//	public Page<Post> findFeedPosts(@Param("id") Integer id, @Param("session_start") LocalDateTime sessionDate, Pageable pageable);
//
//    Optional<List<Post>> findByPostIdIn(List<Integer> posts);
//
//    @Query(
//			nativeQuery = true,
//			value = "select p.post_id, p.audience, p.content, p.posted_date, p.is_reply, p.reply_restriction, p.reply_to, p.scheduled, p.scheduled_date, p.author_id, p.poll_id " +
//					"from posts p " +
//					"join post_likes_junction plj on p.post_id = plj.post_id " +
//					"where plj.user_id = :userId"
//	)
//    List<Post> getUserLikes(Integer userId);
//}


package  com.fwitter.repositories;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> { // Use Integer if postId is Integer

	@Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
	List<Post> findAllByOrderByCreatedAtDesc();
	Optional<Set<Post>> findByAuthor(ApplicationUser author);

	//Optional<Post> findById(Integer id);  // Returns Optional<Post> instead of Post
	Optional<List<Post>> findByPostIdIn(List<Integer> posts);

}
