package com.fwitter.dto;


import com.fwitter.models.ApplicationUser;

public class UserDTO {
    private String username;
    private String profilePicture;
    private String bio;

    public UserDTO(ApplicationUser user) {
        this.username = user.getUsername();
        this.profilePicture = user.getProfilePicture() != null ? user.getProfilePicture().getImageUrl() : null;
        this.bio = user.getBio();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
