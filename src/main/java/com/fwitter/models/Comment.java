
package com.fwitter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates unique IDs
	private Integer id;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private ApplicationUser author;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	@JsonBackReference  // Prevents infinite recursion

	private Post post;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ApplicationUser getAuthor() {
		return author;
	}

	public void setAuthor(ApplicationUser author) {
		this.author = author;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}




