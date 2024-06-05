package ca.sheridancollege.machmark.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.machmark.database.DatabaseAccess;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Lazy
	private DatabaseAccess da;

	//This is to load the user and find the user by using the username
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		ca.sheridancollege.machmark.beans.User user = da.findUserAccount(username);

		//If the user is not found the console prints that the user is not found
		if (user == null) {
			System.out.println("User not found:" + username);
			throw new UsernameNotFoundException("User " + username + " was not found in the database");

		}

		// Get a list of roles for that user
		List<String> roleNameList = da.getRolesById(user.getUserId());

		//This gets a list of granted authority and collects the role
		List<GrantedAuthority> grantList = new ArrayList<>();
		if (roleNameList != null) {
			for (String role : roleNameList) {
				grantList.add(new SimpleGrantedAuthority(role));
			}
		}
		//This finally collects all the user information and returns it as userDetails for the security config to use
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(), grantList);
		return userDetails;
	}

}
