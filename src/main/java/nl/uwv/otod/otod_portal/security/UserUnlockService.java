package nl.uwv.otod.otod_portal.security;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.persistence.UserDao;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserUnlockService {

	private final UserDao userDao;
	
//	@Scheduled(fixedRate = 5000)
	public void unlockAccounts() {
		log.debug("Unlock accounts");
		
		var lockedUsers = userDao.findAllByAccountNonLockedAndLastModifiedDateIsBefore(false, Timestamp.valueOf(LocalDateTime.now().minusSeconds(30)));
		
		if (lockedUsers.size() > 0) {
			log.info("Locked accounts found");
			lockedUsers.forEach(user -> user.setAccountNonLocked(true));
			userDao.saveAll(lockedUsers);
		}
	}
}
