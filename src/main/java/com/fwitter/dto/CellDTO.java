package com.fwitter.dto;

public class CellDTO {
    private Integer row;
    private Integer col;
    private String username;

    // Constructors, getters, setters...


    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CellDTO(Integer row, Integer col, String username) {
        this.row = row;
        this.col = col;
        this.username = username;
    }
}
//
//import com.fwitter.models.ApplicationUser;
//
//public class CellDTO {
//    private int row;
//    private int col;
//    private UserDTO user;  // Full user object
//
//    public CellDTO(int row, int col, ApplicationUser user) {
//        this.row = row;
//        this.col = col;
//        this.user = new UserDTO(user);  // Convert ApplicationUser to DTO
//    }
//
//    public int getRow() {
//        return row;
//    }
//
//    public void setRow(int row) {
//        this.row = row;
//    }
//
//    public int getCol() {
//        return col;
//    }
//
//    public void setCol(int col) {
//        this.col = col;
//    }
//
//    public UserDTO getUser() {
//        return user;
//    }
//
//    public void setUser(UserDTO user) {
//        this.user = user;
//    }
//
//    // Getters and setters
//}

//
//package com.fwitter.dto;
//import com.fwitter.dto.UserDTO;
//
//public class CellDTO {
//    private int row;
//    private int col;
//    private UserDTO user;  // Directly using UserDTO
//
//    public CellDTO(int row, int col, UserDTO user) {
//        this.row = row;
//        this.col = col;
//        this.user = user; // No conversion inside constructor
//    }
//
//    public int getRow() {
//        return row;
//    }
//
//    public void setRow(int row) {
//        this.row = row;
//    }
//
//    public int getCol() {
//        return col;
//    }
//
//    public void setCol(int col) {
//        this.col = col;
//    }
//
//    public UserDTO getUser() {
//        return user;
//    }
//
//    public void setUser(UserDTO user) {
//        this.user = user;
//    }
//}
