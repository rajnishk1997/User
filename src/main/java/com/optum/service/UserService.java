package com.optum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optum.dao.PermissionDao;
import com.optum.dao.ReqRes;
import com.optum.dao.RoleDao;
import com.optum.dao.RolePermissionDao;
import com.optum.dao.UserDao;
import com.optum.dao.UserRoleDao;
import com.optum.entity.JwtResponse;
import com.optum.entity.Permission;
import com.optum.entity.RegistrationResponse;
import com.optum.entity.Role;
import com.optum.entity.RolePermission;
import com.optum.entity.User;
import com.optum.entity.UserRole;
import com.optum.exception.UserRegistrationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
//	@Value("${permissions}")
//    private String permissionsName;

	private static final Logger logger = LogManager.getLogger(UserService.class);

	@Autowired
	private PermissionDao permissionRepository;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private RolePermissionDao rolePermissionDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

//    @Transactional
//    public void initPermissions() {
//		 List<String> permissionNames = Arrays.asList(permissionsName.split(", "));
//	        for (String permissionName : permissionNames) {
//	            Permission permission = permissionRepository.findByPermissionName(permissionName);
//	            if (permission == null) {
//	                permission = new Permission(permissionName);
//	                permissionRepository.save(permission);
//	            }
//	        }
//		
//	}

	@Transactional
	public void initRoleAndUser() {
		// Role 1- Admin
		Role adminRole = new Role();
		adminRole.setRoleName("Admin");
		adminRole.setRoleDescription("Admin role");
		roleDao.save(adminRole);
		// dynamically fetch all permissions from DB and assign them to Admin Role:
		List<Permission> permissions = permissionRepository.findAll();
		adminRole.setPermissions(new HashSet<>(permissions));
		roleDao.save(adminRole);

		// User 1 - Admin User
		User adminUser = new User();
		adminUser.setUserName("admin");
		adminUser.setUserFirstName("admin1");
		adminUser.setUserPassword(getEncodedPassword("admin"));
		adminUser.setUserLastName("Sharma");
		adminUser.setUserMobile("9656789056");
		adminUser.setUserEmail("admin@gmail.com");
		adminUser.setUserDesignation("CFO");
		adminUser.setUserEmployeeId("E1941945");
		Set<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);
		adminUser.setRoles(adminRoles);
		userDao.save(adminUser);
		
//		System.out.println("Roles:");
//		adminUser.getRoles().forEach(role -> {
//		    System.out.println("- " + role.getRoleName());
//		    System.out.println("Permissions:");
//		    role.getPermissions().forEach(permission -> System.out.println("  - " + permission.getPermissionName()));
//		});

		// Now Create User-Role
		// Step 2: Retrieve the permissions associated with the Admin role
		List<Permission> permissions1 = permissionRepository.findAll();

		// Step 3: Create a new UserRole object
		UserRole userRole = new UserRole();

		// Step 4: Set the necessary fields for the UserRole object
		 userRole.setRole(adminRole); // Set the Role object directly
		    userRole.setUser(adminUser); // Set the User object directly

		// Step 5: Save the UserRole object using UserRoleDao
		userRoleDao.save(userRole);

		// Now Create RolePermission entries
//		for (Permission permission : permissions) {
//			RolePermission rolePermission = new RolePermission();
//			rolePermission.setRoleId(adminRole.getRoleRid()); // Using Role ID
//			rolePermission.setPermissionId(permission.getPermissionRid());
//			rolePermissionDao.save(rolePermission);
//		}
		
		 for (Permission permission : permissions) {
		        RolePermission rolePermission = new RolePermission();
		        rolePermission.setRole(adminRole); // Set the Role object directly
		        rolePermission.setPermission(permission); // Set the Permission object directly
		        rolePermissionDao.save(rolePermission);
		    }

		// Role 2- User
		// Find existing permissions by name
		List<String> permissionNames = Arrays.asList("USER MANAGEMENT", "ACCESS MANAGEMENT");
		List<Permission> existingPermissions = permissionRepository.findAllByPermissionNameIn(permissionNames);

		// Create a user role
		Role userRole1 = new Role();
		userRole1.setRoleName("User");
		userRole1.setRoleDescription("Default role for newly created record");

		// Initialize the permissions set in the user role
		userRole1.setPermissions(new HashSet<>());

		// Filter out existing permissions that are not already associated with the user
		// role to avoid duplicates
		Set<Permission> existingPermissionsToAssociate = existingPermissions.stream()
				.filter(permission -> !userRole1.getPermissions().contains(permission)).collect(Collectors.toSet());

		// Associate the filtered permissions with the user role
		userRole1.getPermissions().addAll(existingPermissionsToAssociate);

		// Save the user role
		roleDao.save(userRole1);

		// User 1 - Admin User
//        User adminUser2 = new User();
//        adminUser2.setUserFirstName("admin1");
//        adminUser2.setUserPassword(getEncodedPassword("admin1"));
//        adminUser2.setUserLastName("Sharma");
//        adminUser2.setUserMobile("9656559056");
//        adminUser2.setUserEmail("admin1@gmail.com");
//        adminUser2.setUserDesignation("CFO");
//        adminUser2.setUserEmployeeId("E1941943");
//        Set<Role> adminRoles1 = new HashSet<>();
//        adminRoles1.add(adminRole);
//        adminUser2.setRoles(adminRoles1);
//        userDao.save(adminUser2);

//        User user = new User();
//        user.setUserFirstName("raj");
//        user.setUserPassword(getEncodedPassword("raj123"));
//        user.setUserLastName("sharma");
//        Set<Role> userRoles = new HashSet<>();
//        userRoles.add(userRole);
//        user.setRoles(userRoles);
//        userDao.save(user);
	}

	@Transactional
	public RegistrationResponse<User> registerNewUser(User user, Set<Permission> providedPermissions) {
		try {

//			if (userDao.existsByUserMobile(user.getUserMobile())) {
//				throw new IllegalArgumentException("User already exists with mobile number: " + user.getUserMobile());
//			}
			if (userDao.existsByUserEmail(user.getUserEmail())) {
				throw new IllegalArgumentException("User already exists with email: " + user.getUserEmail());
			}
			// Null checks
			if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
				throw new IllegalArgumentException("Invalid user data");
			}

			// Retrieve existing roles from the database
			Set<Role> userRoles1 = new HashSet<>();
			Role userRole = new Role();

			for (Role role : user.getRoles()) {
				// Find the existing role by name
				Optional<Role> existingRoleOptional = roleDao.findByRoleName(role.getRoleName());
				if (existingRoleOptional.isPresent()) {
					Role existingRole = existingRoleOptional.get();
					// Use the existing role from the database and associate it with the user
					// Set permissions for thse user role
					if (providedPermissions != null && !providedPermissions.isEmpty()) {
						// Only add the provided permissions to the user role
						userRole.setPermissions(new HashSet<>(providedPermissions));
					}
					userRoles1.add(existingRole);
				} else {
					throw new IllegalArgumentException("Role '" + role.getRoleName() + "' not found");
				}
			}
			// Set the roles for the user
			user.setRoles(userRoles1);

			// Set encoded password for the user

			String firstName = user.getUserFirstName();
			String middleName = user.getUserMiddleName();
			String lastName = user.getUserLastName();

			//String generatedPassword = generateSystemPassword(firstName, lastName);
			String generatedPassword = generateBCryptPassword(firstName, lastName);
			String generatedUserName = generateUserName(firstName, lastName);
			String userFullName = createFullName(firstName, middleName, lastName);
			// Hash the password using BCrypt
		 //   String hashedPassword = new BCryptPasswordEncoder().encode(generatedPassword);

			// Set the generated password for the user
			user.setUserPassword(generatedPassword);
			user.setUserName(generatedUserName);
			user.setUserFullName(userFullName);

			// Also Map User and Role with UserRole Table:
			// Set roles for the user
			Set<Role> userRoles = new HashSet<>();
			for (Role role : user.getRoles()) {
				Optional<Role> existingRoleOptional = roleDao.findByRoleName(role.getRoleName());
				if (existingRoleOptional.isPresent()) {
					Role existingRole = existingRoleOptional.get();
					// Add the existing role to the user's roles
					userRoles.add(existingRole);
				} else {
					throw new IllegalArgumentException("Role '" + role.getRoleName() + "' not found");
				}
			}
			user.setRoles(userRoles);

			// Set created date, created by, modified date, and modified by
			Date currentDate = new Date();
			user.setCreatedDate(currentDate);
			user.setCreatedBy(user.getCurrentUserId());

			// Save the user with the modified roles
			User savedUser = userDao.save(user);
			
			// Map User's u_rid and Role's r_rid with UserRole Table
			for (Role role : userRoles) {
			    UserRole userRole1 = new UserRole();
			    userRole1.setUser(savedUser); // Set the User object directly
			    userRole1.setRole(role); // Set the Role object directly
			    userRole.setCreatedDate(new Date()); // Set the current date
			    userRole.setCreatedBy(user.getCurrentUserId()); // Set the current user ID
			    
			    // Save userRole to your database
			    userRoleDao.save(userRole1); // Assuming userRoleDao is your data access object for UserRole
			}

			// Concatenate role names
			StringBuilder roleNamesBuilder = new StringBuilder();
			for (Role role : user.getRoles()) {
				if (roleNamesBuilder.length() > 0) {
					roleNamesBuilder.append(", ");
				}
				roleNamesBuilder.append(role.getRoleName());
			}
			String roleNames = roleNamesBuilder.toString();
			user.setRoleNames(roleNames);

			// Save the user with the modified roles
			userDao.save(user);
			// Create and return the registration response
			return new RegistrationResponse<>(HttpStatus.CREATED.value(), null, "Successfully Registered User",
					savedUser.getUserName(), savedUser.getUserPassword() // Do not return the password
			);
		} catch (IllegalArgumentException e) {
			// Log the error
			logger.error("Error registering new user: " + e.getMessage());
			// Rethrow the exception
			throw e;
		} catch (Exception e) {
			// Log the error
			logger.error("An error occurred while registering the user", e);
			// Wrap the exception in a custom application exception and rethrow
			throw new UserRegistrationException("An error occurred while registering the user", e);
		}

	}

	public String generateUserName(String firstName, String lastName) {
		// Validate input parameters
		if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
			throw new IllegalArgumentException("First name and last name cannot be null or empty.");
		}

		// Generate a random number between 1000 and 9999
		Random random = new Random();
		int randomNumber = random.nextInt(9000) + 1000; // To ensure it's a 4-digit number

		// Concatenate the first name, last name, and random number to form the username
		String userName = firstName.toLowerCase() + lastName.toLowerCase() + randomNumber;
		return userName;
	}

	String generateSystemPassword(String firstName, String lastName) {
		// Check if last name is null or empty
		if (lastName == null || lastName.isEmpty()) {
			throw new IllegalArgumentException("Last name cannot be null or empty.");
		}

		// Generate a random number between 0 and 9999
		Random random = new Random();
		int randomNumber = random.nextInt(1000);

		// Choose a random symbol from a predefined set
		String symbols = "!@#$%^&*()_-+=<>?/[]{},.";
		char randomSymbol = symbols.charAt(random.nextInt(symbols.length()));

		// Concatenate the parts to form the password
		String generatedPassword = firstName + lastName + randomNumber + randomSymbol;

		return generatedPassword;
	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public String createFullName(String firstName, String middleName, String lastName) {
		StringBuilder fullName = new StringBuilder();

		if (firstName != null) {
			fullName.append(firstName);
		}

		if (middleName != null) {
			if (fullName.length() > 0) {
				fullName.append(" ");
			}
			fullName.append(middleName);
		}

		if (lastName != null) {
			if (fullName.length() > 0) {
				fullName.append(" ");
			}
			fullName.append(lastName);
		}

		return fullName.toString();
	}

	public Optional<User> updateUserByUsername(String userName, User updatedUser, Set<String> newRoleNames) {
	    try {
	        Optional<User> optionalUser = userDao.findByUserName(userName);
	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();
	            // Update other user fields if needed
	            user.setUserFirstName(updatedUser.getUserFirstName());
	            user.setUserLastName(updatedUser.getUserLastName());
	            user.setUserEmail(updatedUser.getUserEmail());
	            user.setUserDesignation(updatedUser.getUserDesignation());

	            int userId = user.getUserRid();

	            // Update roles
	            updateUserRoles(user, newRoleNames);

	            // Save the updated user
	            User savedUser = userDao.save(user);
	            return Optional.of(savedUser);
	        } else {
	            return Optional.empty(); // User not found
	        }
	    } catch (Exception e) {
	        // Log the exception or handle it appropriately
	        e.printStackTrace();
	        return Optional.empty(); // Return empty indicating failure
	    }
	}

	private void updateUserRoles(User user, Set<String> newRoleNames) {
	    Set<Role> currentRoles = user.getRoles();
	    Set<String> currentRoleNames = currentRoles.stream()
	            .map(Role::getRoleName)
	            .collect(Collectors.toSet());

	    // Determine roles to be added
	    Set<String> rolesToAdd = newRoleNames.stream()
	            .filter(roleName -> !currentRoleNames.contains(roleName))
	            .collect(Collectors.toSet());

	    // Determine roles to be removed
	    Set<String> rolesToRemove = currentRoleNames.stream()
	            .filter(roleName -> !newRoleNames.contains(roleName))
	            .collect(Collectors.toSet());

	    // Add new roles to the user
	    for (String roleName : rolesToAdd) {
	        Optional<Role> roleOptional = roleDao.findByRoleName(roleName);
	        roleOptional.ifPresent(role -> {
	            // Assuming you have a method addRole in the User class to add a role
	            user.addRole(role);
	            // Append the role name to roleNames with comma separation
	            if (user.getRoleNames() != null && !user.getRoleNames().isEmpty()) {
	                user.setRoleNames(user.getRoleNames() + ", " + roleName);
	            } else {
	                user.setRoleNames(roleName);
	            }

	            // Create UserRole entity and save it
	            UserRole userRole = new UserRole();
	            userRole.setUser(user);
	            userRole.setRole(role);
	            userRole.setCreatedDate(new Date());
	            userRole.setCreatedBy(user.getCurrentUserId());
	            userRoleDao.save(userRole);
	        });
	    }

	    // Remove old roles from the user
	    for (String roleName : rolesToRemove) {
	        currentRoles.removeIf(role -> role.getRoleName().equals(roleName));
	        // Remove the role name from roleNames
	        if (user.getRoleNames() != null && !user.getRoleNames().isEmpty()) {
	            String updatedRoleNames = user.getRoleNames().replace(roleName, "").replaceFirst(",\\s*,", ",");
	            user.setRoleNames(updatedRoleNames);
	        }
	        // Remove UserRole entity associated with the removed role
	        UserRole userRoleToRemove = userRoleDao.findByUserAndRole(user, roleDao.findByRoleName(roleName).orElse(null));
	        if (userRoleToRemove != null) {
	            userRoleDao.delete(userRoleToRemove);
	        }
	    }
	}

	public Optional<User> updateUserByUserId(Integer userId, User updatedUser) {
		Optional<User> optionalUser = userDao.findByUserRid(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// Update other user fields if needed
			user.setUserFirstName(updatedUser.getUserFirstName());
			user.setUserLastName(updatedUser.getUserLastName());
			user.setUserMiddleName(updatedUser.getUserMiddleName());
			user.setUserMobile(updatedUser.getUserMobile());
			user.setUserEmail(updatedUser.getUserEmail());
			user.setUserDesignation(updatedUser.getUserDesignation());

			// Fetch the existing roles for the user
			Set<Role> existingRoles = user.getRoles();

			// Fetch the existing role from the database by name
			Role updatedRole = updatedUser.getRoles().iterator().next(); // Assuming only one role is updated

			// Check if the updated role already exists in the user's roles
			boolean roleExists = existingRoles.stream()
					.anyMatch(role -> role.getRoleName().equals(updatedRole.getRoleName()));

			if (roleExists) {
				// Update the user's roles with the existing ones
				user.setRoles(existingRoles);
				userDao.save(user);
				return Optional.of(user);
			} else {
				// Role does not exist in the user's roles, return empty Optional
				return Optional.empty();
			}
		} else {
			return Optional.empty(); // User not found
		}
	}

	public Optional<ReqRes> deleteUserByUsername(String userName) {
		Optional<User> optionalUser = userDao.findByUserName(userName);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// Delete associated roles from user_role table
			user.setRoles(null); // Remove all roles from the user
			userDao.save(user); // Save the user without roles, which will cascade the deletion of associated
								// roles
			userDao.delete(optionalUser.get());
			return Optional.of(new ReqRes(HttpStatus.OK.value(), "", "User deleted successfully"));
		} else {
			return Optional.of(new ReqRes(HttpStatus.NOT_FOUND.value(), "User not found", ""));
		}
	}

	public Optional<ReqRes> deleteUserByUserId(Integer userId) {
		Optional<User> optionalUser = userDao.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// Delete associated roles from user_role table
			user.setRoles(null); // Remove all roles from the user
			userDao.save(user); // Save the user without roles, which will cascade the deletion of associated
								// roles
			userDao.delete(user);
			return Optional.of(new ReqRes(HttpStatus.OK.value(), "", "User deleted successfully"));
		} else {
			return Optional.of(new ReqRes(HttpStatus.NOT_FOUND.value(), "User not found", ""));
		}
	}

	public List<User> getAllUsers() {
		try {
			return userDao.findAll();
		} catch (Exception e) {
			// Log the exception or handle it as needed
			return Collections.emptyList(); // Return an empty list in case of an error
		}
	}

	public List<User> searchUsersByKeyword(String keyword) {
		return userDao
				.findByUserFirstNameContainingIgnoreCaseOrUserMiddleNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCase(
						keyword, keyword, keyword);

	}

	public List<User> findByUserName(String userName) {
		Optional<User> userOptional = userDao.findByUserName(userName);
		return userOptional.map(Collections::singletonList).orElseGet(Collections::emptyList);
	}

	public List<User> findByUserFirstName(String userFirstName) {
		return userDao.findByUserFirstName(userFirstName);
	}

//	public User getUserByUsername(String userName) {
//		Optional<User> optionalUser = userDao.findByUserName(userName);
//        return optionalUser.orElse(null);
//	}

	public User getUserByNames(String name) {
		Optional<User> optionalUser = userDao
				.findByUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCaseOrUserMiddleNameContainingIgnoreCase(
						name, name, name);
		return optionalUser.orElse(null);
	}

	public List<User> findByUserLastName(String userLastName) {
		// TODO Auto-generated method stub
		return userDao.findByUserLastName(userLastName);
	}

	@Transactional
    public void saveUserWithRoles(User user,Set<Role> roles) {
        user.setRoles(roles); // Associate roles with the user
        userDao.save(user); // Persist the user along with associated roles
    }
	
	private String generateBCryptPassword(String firstName, String lastName) {
	    // Generate a random password using the user's first name and last name
	    String rawPassword = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase() + "123";

	    // Hash the password using BCrypt
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    return passwordEncoder.encode(rawPassword);
	}

	public User getUserByUsername(String username) {
		return userDao.findByUserName(username).orElse(null);
	}
}
