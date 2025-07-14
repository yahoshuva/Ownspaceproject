package com.fwitter.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.fwitter.dto.CellDTO;
import com.fwitter.dto.FindUsernameDTO;
import com.fwitter.dto.UpdateProfileRequest;
import com.fwitter.dto.UserDTO;
import com.fwitter.exceptions.*;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Image;
import com.fwitter.models.RegistrationObject;
import com.fwitter.models.Role;
import com.fwitter.repositories.RoleRepository;
import com.fwitter.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
//    private final GridService gridService;

    @Autowired
    public UserService(UserRepository userRepo, RoleRepository roleRepo, MailService mailService,
            PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;

    }

    public ApplicationUser getUserById(Integer userId) {
        return userRepo.findById(userId).orElseThrow(UserDoesNotExistException::new);
    }

    public List<ApplicationUser> getAllUserById(List<Integer> userIds) {
        return userRepo.findAllById(userIds);
    }

    public ApplicationUser getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            return userRepo.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public ApplicationUser updateUserProfile(UpdateProfileRequest body) throws UnableToSavePhotoException{
        ApplicationUser user = userRepo.findById(body.getUserId()).orElseThrow(UserDoesNotExistException::new);
        user.setNickname(body.getNickname());
        user.setBio(body.getBio());
        user.setLocation(body.getLocation());
        user.setWebsiteUrl(body.getWebsiteUrl());
        if(body.getProfilePicture() != null){
            Image pfp = imageService.updateImageFromBase64(body.getProfilePicture(), "pfp");
            user.setProfilePicture(pfp);
        }

        if(body.getBannerPicture() != null){
            Image banner = imageService.updateImageFromBase64(body.getBannerPicture(), "bnr");
            user.setBannerPicture(banner);
        }
        return userRepo.save(user);
    }

    public ApplicationUser registerUser(RegistrationObject ro) {

        ApplicationUser user = new ApplicationUser();

        user.setFullName(ro.getFullName());
        user.setEmail(ro.getEmail());
        user.setDateOfBirth(ro.getDob());

        String name = user.getFullName();

        boolean nameTaken = true;

        String tempName = "";

        while (nameTaken) {
            tempName = generateUsername(name);

            if (userRepo.findByUsername(tempName).isEmpty()) {
                nameTaken = false;
            }

        }

        user.setUsername(tempName);

        Set<Role> roles = user.getAuthorities();
        roles.add(roleRepo.findByAuthority("USER").get());
        user.setAuthorities(roles);

        Random random = new Random();
        int maxAttempts = 1000;
        boolean cellAssigned = false;

        // White cells are where (row + col) is ODD
        for(int attempts = 0; attempts < maxAttempts; attempts++) {
            int row = random.nextInt(100);
            int col = random.nextInt(100);

            // Check if it's a white cell (row + col is odd)
            if((row + col) % 2 != 0) {
                Optional<ApplicationUser> existing = userRepo.findByCellRowAndCellCol(row, col);
                if(existing.isEmpty()) {
                    user.setCellRow(row);
                    user.setCellCol(col);
                    cellAssigned = true;
                    break;
                }
            }
        }

        if(!cellAssigned) {
            throw new NoAvailableCellException("No available white cells");
        }

        try {
            return userRepo.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

//    public ApplicationUser registerUser(RegistrationObject ro) {
//        ApplicationUser user = new ApplicationUser();
//        user.setFullName(ro.getFullName());
//        user.setEmail(ro.getEmail());
//        user.setDateOfBirth(ro.getDob());
//
//        // ✅ Generate Unique Username with Fallback Counter
//        String tempName;
//        int counter = 0;
//        do {
//            tempName = generateUsername(user.getFullName()) + (counter > 0 ? counter : "");
//            counter++;
//            if (counter > 10) {
//                throw new RuntimeException("Unable to generate a unique username");
//            }
//        } while (userRepo.findByUsername(tempName).isPresent());
//        user.setUsername(tempName);
//
//        // ✅ Assign User Role with Exception Handling
//        Set<Role> roles = new HashSet<>();
//        Role userRole = roleRepo.findByAuthority("USER")
//                .orElseThrow(() -> new RuntimeException("Default USER role not found"));
//        roles.add(userRole);
//        user.setAuthorities(roles);
//
//        // ✅ Get an Available White Cell (Using `findRandomWhiteCell()`)
//        Optional<int[]> availableCell = userRepo.findRandomWhiteCell();
//        if (availableCell.isPresent()) {
//            int[] cell = availableCell.get();
//            user.setCellRow(cell[0]);
//            user.setCellCol(cell[1]);
//        } else {
//            throw new NoAvailableCellException("No available white cells");
//        }
//
//        // ✅ Save User with Proper Exception Handling
//        try {
//            return userRepo.save(user);
//        } catch (DataIntegrityViolationException e) {
//            throw new EmailAlreadyTakenException();
//        } catch (Exception e) {
//            throw new RuntimeException("User registration failed", e);
//        }
//    }



    @Transactional
    public List<CellDTO> getAllAllocatedCells() {
        return userRepo.findAll().stream()
                .filter(u -> u.getCellRow() != null && u.getCellCol() != null)
                .map(u -> new CellDTO(u.getCellRow(), u.getCellCol(), u.getUsername()))
                .collect(Collectors.toList());
    }

//    @Transactional
//    public List<CellDTO> getAllAllocatedCells() {
//        List<ApplicationUser> users = userRepo.findAll(); // Corrected method name
//        return users.stream()
//                .map(user -> new CellDTO(user.getCellRow(), user.getCellCol(), new UserDTO(user)))
//                .collect(Collectors.toList());
//    }


    public void generateEmailVerification(String username) {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        user.setVerification(generateVerificationNumber());

        try {
            mailService.sendEmail(user.getEmail(), "Your verification code",
                    "Here is your verification code: " + user.getVerification());
            userRepo.save(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new EmailFailedToSendException();
        }

        userRepo.save(user);
    }

    public ApplicationUser verifyEmail(String username, Long code) {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        if (code.equals(user.getVerification())) {
            user.setEnabled(true);
            user.setVerification(null);
            return userRepo.save(user);
        } else {
            throw new IncorrectVerificationCodeException();
        }

    }

    public ApplicationUser setPassword(String username, String password) {

        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        String encodedPassword = passwordEncoder.encode(password);

        user.setPassword(encodedPassword);

        return userRepo.save(user);
    }

    private String generateUsername(String name) {
        long generatedNumber = (long) Math.floor(Math.random() * 1_000);
        return name + generatedNumber;
    }

    private Long generateVerificationNumber() {
        return (long) Math.floor(Math.random() * 100_00);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = u.getAuthorities()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        UserDetails ud = new User(u.getUsername(), u.getPassword(), authorities);

        return ud;
    }

    public ApplicationUser setProfileOrBannerPicture(String username, MultipartFile file, String prefix)
            throws UnableToSavePhotoException {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        Image photo = imageService.uploadImage(file, prefix);

        try {
            if (prefix.equals("pfp")) {
                if (user.getProfilePicture() != null
                        && !user.getProfilePicture().getImageName().equals("defaultpfp.png")) {
                    Path p = Paths.get(user.getProfilePicture().getImagePath());
                    Files.deleteIfExists(p);
                }
                user.setProfilePicture(photo);
            } else {
                if (user.getBannerPicture() != null
                        && !user.getBannerPicture().getImageName().equals("defaultbnr.png")) {
                    Path p = Paths.get(user.getBannerPicture().getImagePath());
                    Files.deleteIfExists(p);
                }
                user.setBannerPicture(photo);
            }
        } catch (IOException e) {
            throw new UnableToSavePhotoException();
        }

        return userRepo.save(user);
    }

    public byte[] setUserOrganization(String username, MultipartFile file, String orgName)
            throws UnableToResolvePhotoException {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        Image orgImage = imageService.getImageByImageName(orgName)
                .orElseGet(() -> {
                    try {
                        return imageService.createOrganization(file, orgName);
                    } catch (UnableToSavePhotoException e) {
                        return null;
                    }
                });
        if (orgImage != null) {

            user.setOrganization(orgImage);
            userRepo.save(user);

            try {
                return Files.readAllBytes(new File(orgImage.getImagePath()).toPath());
            } catch (IOException e) {
                throw new UnableToResolvePhotoException();
            }

        } else {
            throw new UnableToResolvePhotoException("We were unable to find or save the organization photo");
        }
    }

    public ApplicationUser followUser(String user, String followee) throws FollowException {

        if (user.equals(followee))
            throw new FollowException();

        ApplicationUser loggedInUser = userRepo.findByUsername(user).orElseThrow(UserDoesNotExistException::new);

        Set<ApplicationUser> followingList = loggedInUser.getFollowing();

        ApplicationUser followedUser = userRepo.findByUsername(followee).orElseThrow(UserDoesNotExistException::new);

        Set<ApplicationUser> followersList = followedUser.getFollowers();

        if (followingList.contains(followedUser)) {
            followingList.remove(followedUser);
        } else {
            // Add the followed use to the following list
            followingList.add(followedUser);
        }
        loggedInUser.setFollowing(followingList);

        if (followersList.contains(loggedInUser)) {
            followersList.remove(loggedInUser);
        } else {
            // Add the current user to the follwer list of the folowee
            followersList.add(loggedInUser);
        }
        followedUser.setFollowers(followersList);

        // update both users
        userRepo.save(loggedInUser);
        userRepo.save(followedUser);

        return loggedInUser;
    }

    public Set<ApplicationUser> retrieveFollowingList(String username) {

        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        return user.getFollowing();
    }

    public Set<ApplicationUser> retrieveFollowersList(String username) {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        return user.getFollowers();
    }

    public String verifyUsername(FindUsernameDTO credential) {
        ApplicationUser user = userRepo
                .findByEmailOrPhoneOrUsername(credential.getEmail(), credential.getPhone(), credential.getUsername())
                .orElseThrow(UserDoesNotExistException::new);
        return user.getUsername();
    }

    public ApplicationUser getUsersEmailAndPhone(FindUsernameDTO credential) {
        return userRepo
                .findByEmailOrPhoneOrUsername(credential.getEmail(), credential.getPhone(), credential.getUsername())
                .orElseThrow(UserDoesNotExistException::new);
    }

}
