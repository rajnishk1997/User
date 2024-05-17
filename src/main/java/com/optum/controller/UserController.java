package com.optum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import org.modelmapper.ModelMapper;

import com.optum.dao.ReqRes;
import com.optum.dto.request.UserRequestDTO;
import com.optum.entity.*;
import com.optum.service.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

//    @PostConstruct   
//    public void initPermissions() {
//    	userService.initPermissions();
//    }

	@PostConstruct // PostConstruct as I wish to run this code once the compilation is done.
	public void initRoleAndUser() {
		userService.initRoleAndUser();
	}

	@PostMapping({ "/registerNewUser" })
	// @PreAuthorize("hasRole('Admin')")
	public ResponseEntity<RegistrationResponse<User>> registerNewUser(@RequestBody UserRequestDTO userRequestDTO) {
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				String username = ((UserDetails) principal).getUsername();
				System.out.println("Current logged in user: " + username);
			} else {
				System.out.println("No authenticated user found");
			}

			// Map UserRequestDTO to User entity
			User user = modelMapper.map(userRequestDTO, User.class);
			Set<Permission> permissions = new HashSet<>();

			for (Role role : user.getRoles()) {
				Collection<Permission> rolePermissions = role.getPermissions();
				if (rolePermissions != null && !rolePermissions.isEmpty()) {
					permissions.addAll(role.getPermissions());
				}
			}
			RegistrationResponse<User> registeredUser = userService.registerNewUser(user, permissions);

			// Populate the RegistrationResponse object
			RegistrationResponse<User> response = new RegistrationResponse<>(HttpStatus.CREATED.value(), "",
					"User registered successfully", registeredUser.getUserName(), registeredUser.getPassword() 
			);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			// Specific handling for IllegalArgumentException
			RegistrationResponse<User> response = new RegistrationResponse<>(HttpStatus.BAD_REQUEST.value(),
					"Bad Request", e.getMessage(), null, null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} catch (Exception e) {
			// Generic handling for other exceptions
			RegistrationResponse<User> response = new RegistrationResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Internal Server Error", "An error occurred while registering the user", null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// Method to match useCases:
	@GetMapping("/admin/get-all-users-case")
	public ResponseEntity<ResponseWrapper<List<User>>> getAllUsersCases(@RequestParam(required = true) String keyword) {
		try {
			List<User> userList;
			if (keyword.isEmpty()) {
				userList = userService.getAllUsers();
			} else {
				userList = userService.searchUsersByKeyword(keyword);
			}

			ReqRes reqRes;
			if (userList.isEmpty()) {
				reqRes = new ReqRes(HttpStatus.NOT_FOUND.value(), "Users not found", "No users found in the database");
			} else {
				reqRes = new ReqRes(HttpStatus.OK.value(), null, "Users retrieved successfully");
			}
			return ResponseEntity.ok(new ResponseWrapper<>(userList, reqRes));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/admin/update/{userName}")
	public ResponseEntity<ResponseWrapper<User>> updateUser(@PathVariable String userName,
			@RequestBody UserRequestDTO userRequestDTO) {
		try {
			// Map UserRequestDTO to User entity
			User updatedUser = modelMapper.map(userRequestDTO, User.class);
			ReqRes reqRes;
	        Set<String> newRoleNames = updatedUser.getRoles().stream()
	                .map(Role::getRoleName)
	                .collect(Collectors.toSet());
			Optional<User> optionalUser = userService.updateUserByUsername(userName, updatedUser, newRoleNames);
			User user = null;
			if (optionalUser.isPresent()) {
				user = optionalUser.get();
				reqRes = new ReqRes(HttpStatus.OK.value(), "", "User updated successfully");
			} else {
				reqRes = new ReqRes(HttpStatus.NOT_FOUND.value(), "User not found", "");
			}
			// Construct the ResponseWrapper and return ResponseEntity
			ResponseWrapper<User> responseWrapper = new ResponseWrapper<>(user, reqRes);
			return ResponseEntity.ok(responseWrapper);
		} catch (Exception e) {
			// Handle exception and return internal server error response
			ReqRes reqRes = new ReqRes(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
					"An error occurred while updating the user");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/admin/delete/{userName}")
	//@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<ResponseWrapper<ReqRes>> deleteUser(@PathVariable String userName) {
		try {
			java.util.Optional<ReqRes> optionalReqRes = userService.deleteUserByUsername(userName);
			if (optionalReqRes.isPresent()) {
				ReqRes reqRes = optionalReqRes.get();
				return ResponseEntity.ok(new ResponseWrapper<>(reqRes, reqRes));
			} else {
				ReqRes reqRes = new ReqRes(HttpStatus.NOT_FOUND.value(), "User not found", "");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(reqRes, reqRes));
			}
		} catch (Exception e) {
			ReqRes reqRes = new ReqRes(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
					"An error occurred while deleting the user");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(reqRes, reqRes));
		}
	}
	
    @Transactional
	 @PostMapping("/save")
	    public ResponseEntity<String> saveUserWithRoles(@RequestBody Map<String, Object> requestBody) {
	        try {
	            User user = (User) requestBody.get("user");
	            List<Role> roleList = (List<Role>) requestBody.get("roles");
	         // Convert List<Role> to Set<Role>
	          Set<Role> roles = new HashSet<>(roleList);
	            userService.saveUserWithRoles(user, roles);
	            return ResponseEntity.ok("User saved successfully with roles.");
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to save user with roles: " + ex.getMessage());
	        }
	    }

	// Method to match exact user
	@GetMapping("/admin/get-all-users")
	public ResponseEntity<ResponseWrapper<List<User>>> getAllUsers(@RequestParam(required = false) String userName,
			@RequestParam(required = false) String userFirstName) {
		try {
			List<User> userList;
			if (userName != null) {
				userList = userService.findByUserName(userName);
			} else if (userFirstName != null) {
				userList = userService.findByUserFirstName(userFirstName);
			} else {
				userList = userService.getAllUsers();
			}

			ReqRes reqRes;
			if (userList.isEmpty()) {
				reqRes = new ReqRes(HttpStatus.NOT_FOUND.value(), "Users not found", "No users found in the database");
			} else {
				reqRes = new ReqRes(HttpStatus.OK.value(), null, "Users retrieved successfully");
			}
			return ResponseEntity.ok(new ResponseWrapper<>(userList, reqRes));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	 @GetMapping("get-user-details/{username}")
	    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
	        User user = userService.getUserByUsername(username);
	        if (user != null) {
	            return ResponseEntity.ok(user);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }

	@GetMapping({ "/forAdmin" })
	@PreAuthorize("hasRole('Admin')")
	public String forAdmin() {
		return "This URL is only accessible to the admin";
	}

	@GetMapping({ "/forUser" })
	@PreAuthorize("hasRole('User')")
	public String forUser() {
		return "This URL is only accessible to the user";
	}

}