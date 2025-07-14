package com.fwitter.models;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity


@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "cell_row", "cell_col" })
})
public class ApplicationUser {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private Integer userId;

    @Column(name = "full_name")
    private String fullName;



	@Column(unique=true)
	private String email;

	private String phone;

	@Column(name="dob")
	private Date dateOfBirth;

	@Column(unique=true)
	private String username;

	@JsonIgnore
	private String password;

	private String bio;

	private String nickname;

    public Integer getCellRow() {
        return cellRow;
    }

    public void setCellRow(Integer cellRow) {
        this.cellRow = cellRow;
    }

    public Integer getCellCol() {
        return cellCol;
    }

    public void setCellCol(Integer cellCol) {
        this.cellCol = cellCol;
    }

    @Column(name = "cell_row")
    private Integer cellRow;

    @Column(name = "cell_col")
    private Integer cellCol;


    @Column(name="create_ts")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimestamp;



	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="profile_picture", referencedColumnName="image_id")
	private Image profilePicture;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="banner_picture", referencedColumnName="image_id")
	private Image bannerPicture;

	@Column(name="verified_account", nullable = true)
	private Boolean verifiedAccount;

	@Column(name="private_account", nullable=true)
	private Boolean privateAccount;

	@ManyToOne
	@JoinColumn(name="organization_id", nullable = true)
	private Image organization;

    @Column(name="business", nullable=true)
    private String business;

    @Column(name="location", nullable=true, length=30)
    private String location;

    @Column(name="website_url", nullable=true, length=120)
    private String websiteUrl;

    @Column(name="display_dob")
    private boolean displayBirthDate;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="following",
			joinColumns= {@JoinColumn(name="user_id")},
			inverseJoinColumns = {@JoinColumn(name="following_id")}
	)
	@JsonIgnore
	private Set<ApplicationUser> following;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="followers",
			joinColumns= {@JoinColumn(name="user_id")},
			inverseJoinColumns = {@JoinColumn(name="follower_id")}
	)
	@JsonIgnore
	private Set<ApplicationUser> followers;

	/* Security related */

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="user_role_junction",
			joinColumns = {@JoinColumn(name="user_id")},
			inverseJoinColumns = {@JoinColumn(name="role_id")}
	)
	private Set<Role> authorities;

	private Boolean enabled;

	@Column(nullable=true)
	@JsonIgnore
	private Long verification;

    @ManyToMany(mappedBy = "conversationUsers", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Conversation> conversations = new HashSet<>();

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }

    public ApplicationUser() {
        this.createTimestamp = LocalDateTime.now();
		this.authorities = new HashSet<>();
		this.following = new HashSet<>();
		this.followers = new HashSet<>();
		this.enabled = false;
        this.displayBirthDate = true;
	}

    public Integer getUserId() {
        return userId;
    }



    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }



    public String getPhone() {
        return phone;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }



    public Date getDateOfBirth() {
        return dateOfBirth;
    }



    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }



    public String getUsername() {
        return username;
    }



    public void setUsername(String username) {
        this.username = username;
    }



    public String getPassword() {
        return password;
    }



    public void setPassword(String password) {
        this.password = password;
    }



    public String getBio() {
        return bio;
    }



    public void setBio(String bio) {
        this.bio = bio;
    }



    public String getNickname() {
        return nickname;
    }



    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



    public LocalDateTime getCreateTimestamp() {
        return createTimestamp;
    }



    public void setCreateTimestamp(LocalDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
    }



    public Image getProfilePicture() {
        return profilePicture;
    }



    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }



    public Image getBannerPicture() {
        return bannerPicture;
    }



    public void setBannerPicture(Image bannerPicture) {
        this.bannerPicture = bannerPicture;
    }



    public Boolean getVerifiedAccount() {
        return verifiedAccount;
    }



    public void setVerifiedAccount(Boolean verifiedAccount) {
        this.verifiedAccount = verifiedAccount;
    }



    public Boolean getPrivateAccount() {
        return privateAccount;
    }



    public void setPrivateAccount(Boolean privateAccount) {
        this.privateAccount = privateAccount;
    }



    public Image getOrganization() {
        return organization;
    }



    public void setOrganization(Image organization) {
        this.organization = organization;
    }



    public String getBusiness() {
        return business;
    }



    public void setBusiness(String business) {
        this.business = business;
    }



    public String getLocation() {
        return location;
    }



    public void setLocation(String location) {
        this.location = location;
    }



    public String getWebsiteUrl() {
        return websiteUrl;
    }



    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }



    public boolean isDisplayBirthDate() {
        return displayBirthDate;
    }



    public void setDisplayBirthDate(boolean displayBirthDate) {
        this.displayBirthDate = displayBirthDate;
    }



    public Set<ApplicationUser> getFollowing() {
        return following;
    }



    public void setFollowing(Set<ApplicationUser> following) {
        this.following = following;
    }



    public Set<ApplicationUser> getFollowers() {
        return followers;
    }



    public void setFollowers(Set<ApplicationUser> followers) {
        this.followers = followers;
    }



    public Set<Role> getAuthorities() {
        return authorities;
    }



    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }



    public Boolean getEnabled() {
        return enabled;
    }



    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }



    public Long getVerification() {
        return verification;
    }



    public void setVerification(Long verification) {
        this.verification = verification;
    }


@Override
	public String toString() {
		return "ApplicationUser{" +
				"userId=" + userId +


                "fullName="+fullName+
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", dateOfBirth=" + dateOfBirth +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", bio='" + bio + '\'' +
				", nickname='" + nickname + '\'' +
				", profilePicture=" + profilePicture +
				", bannerPicture=" + bannerPicture +
				", verifiedAccount=" + verifiedAccount +
				", privateAccount=" + privateAccount +
				", organization=" + organization +
                ", business=" + business +
                ", location=" + location +
                ", websiteUrl=" + websiteUrl +
                ", displayBirthDate=" + displayBirthDate +
				", following=" + following.stream().map(followee -> followee.getUserId()).collect(Collectors.toList()) +
				", followers=" + followers.stream().map(follower -> follower.getUserId()).collect(Collectors.toList()) +
				", authorities=" + authorities +
				", enabled=" + enabled +
				", verification=" + verification +
                ", createTimestamp=" + createTimestamp +
				'}';
	}

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApplicationUser other = (ApplicationUser) obj;
        return Objects.equals(userId, other.userId);
    }
}
