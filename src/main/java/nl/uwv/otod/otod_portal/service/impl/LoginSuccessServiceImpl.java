package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.LoginSuccess;
import nl.uwv.otod.otod_portal.persistence.LoginSuccessRepository;
import nl.uwv.otod.otod_portal.service.LoginSuccessService;

@Service
public class LoginSuccessServiceImpl implements LoginSuccessService {

	@Autowired
	private LoginSuccessRepository loginSuccessDao;
	
	@Override
	public List<LoginSuccess> getAll() {
		return loginSuccessDao.findFirst25ByOrderByCreationDateDesc();//findAllByOrderByCreationDateDesc();
	}

}
