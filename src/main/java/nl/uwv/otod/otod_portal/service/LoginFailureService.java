package nl.uwv.otod.otod_portal.service;

import java.util.List;

import nl.uwv.otod.otod_portal.model.LoginFailure;

public interface LoginFailureService {
	List<LoginFailure> getAll();
	List<LoginFailure> getAllOrderedByCreatedDateDesc();
}
