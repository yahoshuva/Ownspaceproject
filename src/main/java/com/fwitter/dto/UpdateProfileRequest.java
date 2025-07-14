package com.fwitter.dto;

public class UpdateProfileRequest{

    private Integer userId;
    private String nickname;
    private String bio;
    private String location;
    private String websiteUrl;
    private String profilePicture;
    private String bannerPicture;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(Integer userId, String nickname, String bio, String location, String websiteUrl, String profilePicture, String bannerPicture) {
        this.userId = userId;
        this.nickname = nickname;
        this.bio = bio;
        this.location = location;
        this.websiteUrl = websiteUrl;
        this.profilePicture = profilePicture;
        this.bannerPicture = bannerPicture;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBannerPicture() {
        return bannerPicture;
    }

    public void setBannerPicture(String bannerPicture) {
        this.bannerPicture = bannerPicture;
    }

    @Override
    public String toString() {
        return "UpdateProfileRequest{userId=" + userId + ", nickname=" + nickname + ", bio=" + bio + ", location="
                + location + ", websiteUrl=" + websiteUrl + ", profilePicture=" + profilePicture + ", bannerPicture="
                + bannerPicture + "}";
    }

}
