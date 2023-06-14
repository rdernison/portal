package nl.uwv.otod.otod_portal.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.uwv.otod.otod_portal.model.PasswordResetToken;

@Repository
public interface PasswordTokenRepository extends CrudRepository<PasswordResetToken, Long> {

	public PasswordResetToken findByToken(String token);
}
