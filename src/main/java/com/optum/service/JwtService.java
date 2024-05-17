package com.optum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.optum.dao.PermissionDao;
import com.optum.dao.RolePermissionDao;
import com.optum.dao.UserDao;
import com.optum.entity.JwtRequest;
import com.optum.entity.JwtResponse;
import com.optum.entity.Permission;
import com.optum.entity.Role;
import com.optum.entity.RolePermission;
import com.optum.entity.User;
import com.optum.util.JwtUtil;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private RolePermissionDao rolePermissionRepository;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private PermissionDao permissionRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userName, userPassword);

        UserDetails userDetails = loadUserByUsername(userName);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);
        
        User user = userDao.findByUserName(userName).orElseThrow(() -> new RuntimeException("User not found"));

     // Eagerly fetch roles along with user data
     user.getRoles().size();

     Set<Role> roles = user.getRoles();
     Set<Permission> permissions = new HashSet<>();

     // Iterate over user's roles
     for (Role role : roles) {
         // Check if the role has permissions
         if (role.getPermissions() != null) {
             permissions.addAll(role.getPermissions());
         }
     }
        
        System.out.println("Roles:");
        user.getRoles().forEach(role -> {
		    System.out.println("- " + role.getRoleName());
		    System.out.println("Permissions:");
		    role.getPermissions().forEach(permission -> System.out.println("  - " + permission.getPermissionName()));
		});

        return new JwtResponse(201, null, "Successfully Logged In", newGeneratedToken, roles, permissions, user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUserName(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
