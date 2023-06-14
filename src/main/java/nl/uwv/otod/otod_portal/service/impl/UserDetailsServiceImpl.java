package nl.uwv.otod.otod_portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.persistence.UserDao;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userDao.findByUsernameIgnoreCase(username)
		        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		var userDetails = UserDetailsImpl.build(user);
		return userDetails;
	}

}
