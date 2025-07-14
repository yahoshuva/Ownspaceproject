package com.fwitter.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fwitter.dto.CellDTO;
import com.fwitter.dto.UserDTO;
import com.fwitter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import com.fwitter.dto.FindUsernameDTO;
import com.fwitter.dto.PasswordCodeDTO;
import com.fwitter.exceptions.EmailAlreadyTakenException;
import com.fwitter.exceptions.EmailFailedToSendException;
import com.fwitter.exceptions.IncorrectVerificationCodeException;
import com.fwitter.exceptions.InvalidCredentialsException;
import com.fwitter.exceptions.UserDoesNotExistException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.LoginResponse;
import com.fwitter.models.RegistrationObject;
import com.fwitter.services.MailService;
import com.fwitter.services.TokenService;
import com.fwitter.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
	
	private final UserService userService;
	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	private final MailService emailService;
	private final UserRepository userRepo;
	
	@Autowired
	public AuthenticationController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager, MailService emailService,
	UserRepository userRepo) {
		this.userService = userService;
		this.tokenService = tokenService;
		this.authenticationManager = authenticationManager;
		this.emailService = emailService;
		this.userRepo = userRepo;
	}
	
	@ExceptionHandler({EmailAlreadyTakenException.class})
	public ResponseEntity<String> handleEmailTaken(){
		return new ResponseEntity<String>("The email you provided is already in use", HttpStatus.CONFLICT);
	}
	
	// go to http://localhost:8000/auth/register
	@PostMapping("/register")
	public ApplicationUser registerUser(@RequestBody RegistrationObject ro) {
		
		return userService.registerUser(ro);
	}
	
	@ExceptionHandler({UserDoesNotExistException.class})
	public ResponseEntity<String> handleUserDoesntExist(){
		return new ResponseEntity<String>("The user you are looking  no exist", HttpStatus.NOT_FOUND);
	}

	// AuthenticationController.java or a new UserController.java
	@GetMapping("/users/cells")
	public List<CellDTO> getAllAllocatedCells() {
		List<ApplicationUser> users = userRepo.findAll();
		return users.stream()
				.filter(u -> u.getCellRow() != null && u.getCellCol() != null)
				.map(u -> new CellDTO(u.getCellRow(), u.getCellCol(), u.getUsername()))
				.collect(Collectors.toList());
	}

//	@GetMapping("/users/cells")
//	public List<CellDTO> getAllAllocatedCells() {
//		List<ApplicationUser> users = userRepo.findAllAllocatedUsers();
//		return users.stream()
//				.map(user -> new CellDTO(user.getCellRow(), user.getCellCol(), new UserDTO(user)))
//				.collect(Collectors.toList());
//	}


	@GetMapping("/users/me/cell")
	public CellDTO getCurrentUserCell(Authentication authentication) {
		String username = authentication.getName();
		ApplicationUser user = userService.getUserByUsername(username);
		return new CellDTO(user.getCellRow(), user.getCellCol(), user.getUsername());
	}


//	@GetMapping("/users/cells")
//	public List<CellDTO> getAllAllocatedCells() {
//		List<ApplicationUser> users = userRepo.findAll();
//		return users.stream()
//				.map(user -> new CellDTO(user.getCellRow(), user.getCellCol(), new UserDTO(user)))  // Convert to UserDTO
//				.collect(Collectors.toList());
//	}
//
//	@GetMapping("/users/me/cell")
//	public CellDTO getCurrentUserCell(Authentication authentication) {
//		String username = authentication.getName();
//		ApplicationUser user = userService.getUserByUsername(username);
//		return new CellDTO(user.getCellRow(), user.getCellCol(), new UserDTO(user));  // Convert to UserDTO
//	}



	@PutMapping("/update/phone")
	public ApplicationUser updatePhoneNumber(@RequestBody LinkedHashMap<String, String> body) {
		
		String username = body.get("username");
		String phone = body.get("phone");
		
		ApplicationUser user = userService.getUserByUsername(username);
		
		user.setPhone(phone);
		
		return userService.updateUser(user);
		
	}
	
	@ExceptionHandler({EmailFailedToSendException.class})
	public ResponseEntity<String> handleFailedEmail(){
		return new ResponseEntity<String>("Email failed to send, try again in a moment", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/email/code")
	public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String, String> body){
		
		userService.generateEmailVerification(body.get("username"));
		
		return new ResponseEntity<String>("Verification code generated, email sent", HttpStatus.OK);
	}
	
	@ExceptionHandler({IncorrectVerificationCodeException.class})
	public ResponseEntity<String> incorrectCodeHandler(){
		return new ResponseEntity<String>("The code provided does not match the users code", HttpStatus.CONFLICT);
	}
	
	@PostMapping("/email/verify")
	public ApplicationUser verifyEmail(@RequestBody LinkedHashMap<String, String> body) {
		
		Long code = Long.parseLong(body.get("code"));
		
		String username = body.get("username");
		
		return userService.verifyEmail(username, code);
		
	}
	
	@PutMapping("/update/password")
	public ApplicationUser updatePassword(@RequestBody LinkedHashMap<String, String> body) {
		String username = body.get("username");
		String password = body.get("password");
		
		return userService.setPassword(username, password);
	}
	
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LinkedHashMap<String, String> body) throws InvalidCredentialsException{
		
		String username = body.get("username");
		String password = body.get("password");
		
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));
			
			String token = tokenService.generateToken(auth);
			return new LoginResponse(userService.getUserByUsername(username), token);
			
		} catch(AuthenticationException e) {
			throw new InvalidCredentialsException();
		}
	}
	
	@ExceptionHandler({InvalidCredentialsException.class})
	public ResponseEntity<String> handleInvalidCredentials(){
		return new ResponseEntity<String>("Invalide credentials", HttpStatus.FORBIDDEN);
	}
	
	@PostMapping("/find")
	public ResponseEntity<String> verifyUsername(@RequestBody FindUsernameDTO credential){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		String username = userService.verifyUsername(credential);
		return new ResponseEntity<String>(username, HttpStatus.OK);
	}
	
	@PostMapping("/identifiers")
	public FindUsernameDTO findIdentifiers(@RequestBody FindUsernameDTO credential) {
		ApplicationUser user = userService.getUsersEmailAndPhone(credential);
		return new FindUsernameDTO(user.getEmail(), user.getPhone(), user.getUsername());
	}
	
	@PostMapping("/password/code")
	public ResponseEntity<String> retrievePasswordCode(@RequestBody PasswordCodeDTO body) throws EmailFailedToSendException {
		String email = body.getEmail();
		int code = body.getCode();
		emailService.sendEmail(email, "Your password reset code", ""+code);
		return new ResponseEntity<String>("Code sent successfully", HttpStatus.OK);
	}

	// In AuthenticationController.java
	//newly added methods
	@GetMapping("/users/{username}")
	public ResponseEntity<ApplicationUser> getUserByUsername(@PathVariable String username) {
		ApplicationUser user = userService.getUserByUsername(username);
		return ResponseEntity.ok(user);
	}

}























