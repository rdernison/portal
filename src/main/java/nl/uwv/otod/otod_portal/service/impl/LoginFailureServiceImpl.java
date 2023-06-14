package nl.uwv.otod.otod_portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.uwv.otod.otod_portal.model.LoginFailure;
import nl.uwv.otod.otod_portal.persistence.LoginFailureRepository;
import nl.uwv.otod.otod_portal.service.LoginFailureService;

@Service
public class LoginFailureServiceImpl implements LoginFailureService {

	@Autowired
	private LoginFailureRepository loginFailureRepository;

	@Override
	public List<LoginFailure> getAll() {
		return loginFailureRepository.findAll();
	}

	@Override
	public List<LoginFailure> getAllOrderedByCreatedDateDesc() {
		return loginFailureRepository.findAllByOrderByCreatedDateDesc();
	}
}
