
package com.fwitter.repositories;


import com.fwitter.models.Comment;
import com.fwitter.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findByPost(Post post);
}

