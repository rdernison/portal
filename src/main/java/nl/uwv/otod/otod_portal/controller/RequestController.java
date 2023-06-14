package nl.uwv.otod.otod_portal.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import nl.uwv.otod.otod_portal.converter.PersonConverter;
import nl.uwv.otod.otod_portal.converter.ServerRequestConverter;
import nl.uwv.otod.otod_portal.converter.TransientToPersistentConverter;
import nl.uwv.otod.otod_portal.model.DiskRequest;
import nl.uwv.otod.otod_portal.model.Middleware;
import nl.uwv.otod.otod_portal.model.MiddlewareRequest;
import nl.uwv.otod.otod_portal.model.Person;
import nl.uwv.otod.otod_portal.model.PersonDto;
import nl.uwv.otod.otod_portal.model.Request;
import nl.uwv.otod.otod_portal.model.ServerRequest;
import nl.uwv.otod.otod_portal.model.User;
import nl.uwv.otod.otod_portal.security.IAuthenticationFacade;
import nl.uwv.otod.otod_portal.service.CostCenterService;
import nl.uwv.otod.otod_portal.service.DiskRequestService;
import nl.uwv.otod.otod_portal.service.MiddlewareRequestService;
import nl.uwv.otod.otod_portal.service.MiddlewareService;
import nl.uwv.otod.otod_portal.service.OsService;
import nl.uwv.otod.otod_portal.service.PersonService;
import nl.uwv.otod.otod_portal.service.RequestService;
import nl.uwv.otod.otod_portal.service.ServerRequestService;
import nl.uwv.otod.otod_portal.service.StorageService;
import nl.uwv.otod.otod_portal.service.UserService;
import nl.uwv.otod.otod_portal.util.SettingsUtil;

// hooguit 7 schijven per server

@Controller
@RequestMapping("/request")
@Log4j2
public class RequestController {

	private static final String SHOW_REQUESTS = "showRequests";

	private static final String SHOW_REQUEST = "showRequest";

	private static final String SHOW_MY_REQUESTS = "showMyRequests";

	private static final String REQUEST_ENVIRONMENT = "requestEnvironment";

	private static final String SHOW_SAVED_REQUEST = "showSavedRequest";

	private static final String EDIT_REQUEST = "editRequest";

	private static final String REQUEST_ENVIRONMENT2 = "requestEnvironment2";

	private static final String REQUEST = "request";

	private static final String ADMINS = "admins";

	@Autowired
	private RequestService requestService;

	@Autowired
	private OsService osService;

	@Autowired
	private MiddlewareService middlewareService;

	@Autowired
	private UserService userService;

	private final StorageService storageService;

	@Autowired
	private PersonService personService;

	@Autowired
	private CostCenterService costCenterService;

	@Autowired
	private MiddlewareRequestService middlewareRequestService;
	
	@Autowired
	private IAuthenticationFacade authenticationFacade;
	
	@Autowired
	private ServerRequestService serverRequestService;
	
	@Autowired
	private DiskRequestService  diskRequestService;

	@Autowired
	public RequestController(StorageService storageService) {
		this.storageService = storageService;
	}

	@RequestMapping("/mine")
	public String getMyRequests(Model model) {
		log.info("Gettting my requests");
		var userOpt = getUserFromPrincipal();
		if (userOpt.isPresent()) {
			var user = userOpt.get();
			log.info("User: email {}", user.getEmailAddress());
			var myRequests = requestService.getMyRequests(user);
			model.addAttribute("myRequests", myRequests);
		}
		return SHOW_MY_REQUESTS;
	}
	
	private Optional<User> getUserFromPrincipal() {
		var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		var username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername()
				: principal.toString();
		var userOpt = userService.getByUsername(username);
		log.info("user name: {}", username);

		return userOpt;
	}

	@RequestMapping("/all")
	public String getRequest(Model model) {
		log.info("Getting all requests");
		var requests = requestService.getSubmitted();
		model.addAttribute("allRequests", requests);
		return SHOW_REQUESTS;
	}

	@RequestMapping("/id/{id}")
	public String getRequestById(Model model, @PathVariable long id) {
		log.info("Getting request: {}", id);
		var requestOpt = requestService.get(id);
		if (requestOpt.isPresent()) {
			var request = requestOpt.get();
			log.info("Got request: {}", request.getApplicationName());
			log.info("otod project name: {}", request.getOtodProjectName());
			log.info("application name: {}", request.getApplicationName());
			log.info("title: {}", request.getTitle());
			var devServersJson = request.getDevServerRequestsJson();
			var testServersJson = request.getTestServerRequestsJson();
			var serverRequestConverter = new ServerRequestConverter();
			var personConverter = new PersonConverter();
			try {
				var devServers = serverRequestConverter.convertJsonToServerRequests(devServersJson);
				var testServers = serverRequestConverter.convertJsonToServerRequests(testServersJson);
//				request.setDevServers(devServers);
//				request.setTestServers(testServers);
				var devUsersJson = request.getDevUsersJson();
				var testUsersJson = request.getTestUsersJson();
				var devUsers = personConverter.convertJsonToPeople(devUsersJson);
				var testUsers = personConverter.convertJsonToPeople(testUsersJson);
//				request.setDevUsers(devUsers);
//				request.setTestUsers(testUsers);
				model.addAttribute(REQUEST, request);
				
				log.info("Found {} dev servers and {} test servers", request.getDevServers().size(), request.getTestServers().size());
			} catch (JsonProcessingException e) {
				log.error("Error converting json to server reqeusts: {}", e.toString());
			}
		}
		return SHOW_REQUEST;
	}

	private List<Person> convertJsonToUsers(String json) {
		var users = new ArrayList<Person>();
		return users;
	}

	@PostMapping("/")
	public String edit(/* @Valid */ Request request, BindingResult bindingResult, RedirectAttributes redirAttrs,
			Model model) {
		log.info("post: /request");
		PersonDto developerDto = new PersonDto();
		developerDto.setPeople(personService.getDevPeopleByRequestId(request.getId()));
		model.addAttribute("devUserDto", developerDto);
		PersonDto testerDto = new PersonDto();
		developerDto.setPeople(personService.getTestPeopleByRequestId(request.getId()));
		model.addAttribute("testUserDto", testerDto);
		model.addAttribute("allCostCenters", costCenterService.getByPkunOrS());
		if (bindingResult.hasErrors()) {
			redirAttrs.addFlashAttribute("errorMessage", "The submitted data has errors.");
			model.addAttribute("errorMessage", "The submitted data has errors.");
			return REQUEST_ENVIRONMENT;
		} else {
			requestService.save(request);
			model.addAttribute("request", request);
			redirAttrs.addFlashAttribute("successMessage", "Request saved successfully!");
			model.addAttribute("successMessage", "Request saved successfully!");
		}

		return "redirect:/request/" + request.getId();
	}

	@RequestMapping("/new")
	public String requestEnvironment(Model model) {
		log.info("Creating new request");
		try {
			var request = new Request();//initRequest();
			
			add2DevUsers(request);
			log.info("Going to add two dev servers to the request");
			add2DevServers(request);
			add2TestUsers(request);
			add2TestServers(request);
			setModelAttributes(model);
			var userOpt = getUserFromPrincipal();
			if (userOpt.isPresent()) {
				var user = userOpt.get();
				log.info("Found user: full name={}, email address={}", user.getFullName(), user.getEmailAddress());
				request.setEmailAddressServiceRequester(user.getEmailAddress());
				request.setNameServiceRequester(user.getFullName());
			}
			model.addAttribute("request", request);
			
		} catch (NumberFormatException e) {
			log.error("Error initializing request: {}", e.toString());
		} catch (Exception e) {
			log.error("Some other error has occurred in requestEnvironment: {}", e.toString());
			System.out.println("=== 666 ===");
			e.printStackTrace();
		}
		return REQUEST_ENVIRONMENT;
	}

	private void add2DevUsers(Request request) {
		for (int i = 0; i < 2; i++) {
			var p = new Person();
			request.getDevUsers().add(p);
		}
	}
	
	private void add2DevServers(Request request) {
		log.info("Adding two dev servers");
		for (var i = 0; i < 2; i++) {
			var sr = new ServerRequest();
			log.info("Making dev server");
			sr.setDisks(makeDiskRequests());
			sr.setMiddlewareRequests(makeMiddlewareRequests());
			log.info("Adding it to request");
			request.getDevServers().add(sr);
		}
	}
	
	private void add2TestUsers(Request request) {
		for (var i = 0; i < 2; i++) {
			var p = new Person();
			request.getTestUsers().add(p);
		}
	}

	private void add2TestServers(Request request) {
		for (int i = 0; i < 2; i++) {
			var sr = new ServerRequest();
			sr.setDisks(makeDiskRequests());
			sr.setMiddlewareRequests(makeMiddlewareRequests());
			request.getTestServers().add(sr);
		}
	}
	
	private List<DiskRequest> makeDiskRequests() {
		var diskRequests = new ArrayList<DiskRequest>();
		
		for (int i = 0; i < 4; i++) {
			var diskRequest = new DiskRequest();
			diskRequest.setMountPoint("");
			diskRequest.setSize(0);
			diskRequests.add(diskRequest);
		}
		return diskRequests;
	}
	
	private List<MiddlewareRequest> makeMiddlewareRequests() {
		var middlewareRequests = new ArrayList<MiddlewareRequest>();
		for (var i = 0; i < 3; i++) {
			var mr = new MiddlewareRequest();
			middlewareRequests.add(mr);
		}
		return middlewareRequests;
	}

	private void setModelAttributesOnRequest(Model model, Request request) {
		log.info("Setting model attributeds on request");
		var devServers = (Set<ServerRequest>) model.getAttribute("devServers");
		var testServers = (Set<ServerRequest>) model.getAttribute("testServers");
//		request.setTransientDevServers(devServers);
//		request.setTransientTestServers(testServers);
	}

	private void setModelAttributes(Model model) {
		log.info("setting model attributes");
		var devUserDto = initDevUserDto();
		model.addAttribute("devUserDto", devUserDto);
		var testUserDto = initTestUserDto();
		model.addAttribute("testUserDto", testUserDto);
		model.addAttribute("setAssignee", false);
		model.addAttribute("oses", osService.getEnabled());
		model.addAttribute("middleware", middlewareService.getEnabled());
//		model.addAttribute("devDisks", new ArrayList<DiskRequest>());
//		model.addAttribute("testDisks", new ArrayList<DiskRequest>());
		var allCostCenters = costCenterService.getByPkunOrS();
		log.info("Found {} cost centers", allCostCenters.size());
		if (allCostCenters.size() > 0) {
			log.info("First cost center: {}", allCostCenters.get(0).getCode());
		}
		model.addAttribute("allCostCenters", allCostCenters);
		log.info("Added cost centers");
		model.addAttribute("transientDevServers", new HashSet<ServerRequest>());
		log.info("Added dev servers");
		model.addAttribute("transientTestServers", new HashSet<ServerRequest>());
		log.info("Added test servers");
	}

	private PersonDto initDevUserDto() {
		log.info("Init dev user dto");
		var devUsers = new PersonDto();
		var robert = new Person();
		robert.setGroupName("dev");
		robert.setRole("developer");
		robert.setUserId("rko016");
		var userList = new ArrayList<Person>();
		userList.add(robert);
		devUsers.setPeople(userList);
		return devUsers;
	}

	private PersonDto initTestUserDto() {
		log.info("Init test user dto");
		var testUsers = new PersonDto();
		var sieto = new Person();
		sieto.setGroupName("test");
		sieto.setRole("tester");
		sieto.setUserId("sri075");
		var userList = new ArrayList<Person>();
		userList.add(sieto);
		testUsers.setPeople(userList);
		return testUsers;
	}

	
	private void initMiddleware(ServerRequest server, String environment) {
		log.info("Init middleware");
		var mw = new HashSet<MiddlewareRequest>();
		var allMiddleware = middlewareService.getEnabled();
		Middleware middleware = null;
		var middlewareIt = allMiddleware.iterator();
		if (middlewareIt.hasNext()) {
			middleware = middlewareIt.next();
		}
		log.info("Got middleware ? {}", middleware == null ? "no" : middleware.getId());
		var middlewareRequestList = new ArrayList<MiddlewareRequest>();
		/* TODO make this dynamic
		for (int i = 0; i < 4; i++) { // TODO read from settings
			var middlewareRequest = new MiddlewareRequest();
			if (middlewareRequest.getMiddleware() == null) {
				log.info("no middleware in request, adding empty one");
				middlewareRequest.setMiddleware(middleware);
//				middlewareRequestService.save(middlewareRequest);
				middlewareRequestList.add(middlewareRequest);
			}
			log.info("Adding middleware to request for environment: {}", environment);
			mw.add(middlewareRequest);
		}*/
//		server.setTransientMiddleware(mw);

//		server.setMiddleware(middleware);

		server.setMiddlewareRequests(middlewareRequestList);

	}

	private List<DiskRequest> initDisks(int numDisksPerServer) {
		log.info("Initializing {} disks", numDisksPerServer);
		var disks = new ArrayList<DiskRequest>();
		for (int i = 0; i < numDisksPerServer; i++) {
			var disk = new DiskRequest();
			log.info("Initializing disk");
			disks.add(disk);
		}
		log.info("Created {} disks", disks.size());
		return disks;
	}
	
	@PostMapping("/addDevUser")
	public String addDevUser(Request request) {
		log.info("Before adding dev user 1a - {} {} {} requst = {}", request.getDevUsers().size(), request.getProjectName(), request.getDevUsers(), request);
		requestService.addDevUser(request);
		log.info("After adding dev user 1b - {} {} {} request = {}", request.getDevUsers().size(), request.getProjectName(), request.getDevUsers(), request);
		return "requestEnvironment :: devUsers";
	}

	@PostMapping("/addDevServer")
	public String addDevServer(Request request) {
		log.info("Before adding dev server 1a - {} {}", request.getDevServers().size(), request.getProjectName());
		requestService.addDevServer(request);
		log.info("After adding dev server 1b - {} {}", request.getDevServers().size(), request.getProjectName());
		var devServers = request.getDevServers();
		var lastDevServer = devServers.get(devServers.size() - 1);
		serverRequestService.save(lastDevServer);
		return "requestEnvironment :: devServers";
	}

	@PostMapping("removeDevUser")
	public String removeDevUser(Request request) {
		log.info("Removing dev user");
//		requestService.removeDevUser(request); TODO
		return "requestEnvironment"; //"requestEnvironment :: devUsers"; // returning the updated section
	}

	@PostMapping("addTestUser")
	public String addTestUser(Request request) {
		log.info("Adding test user for project {} 1a - {} {} {}", request.getProjectName(), request.getTestUsers().size(), request, request.getTestUsers());
		requestService.addTestUser(request);
		log.info("After adding test user 1b - {} {} {}", request.getTestUsers().size(), request, request.getTestUsers());
		return "requestEnvironment :: testUsers";//"requestEnvironment :: testUsers"; // returning the updated section
	}

	@PostMapping("addTestServer")
	public String addTestServer(Request request) {
		log.info("Adding test server for project {} 1a - {} {} {}", request.getProjectName(), request.getTestUsers().size(), request, request.getTestUsers());
		requestService.addTestServer(request);
		log.info("After adding test server 1b - {} {} {}", request.getTestUsers().size(), request, request.getTestUsers());
		return "requestEnvironment :: testServers"; //"requestEnvironment :: testServers"; // returning the updated section
	}

	@PostMapping("removeTestUser")
	public String removeTestUser(Request request) {
		log.info("Removing test user");
		return "requestEnvironment :: testUsers"; // returning the updated section
	}

	@PostMapping("/save")
	public String saveRequest(@ModelAttribute Request request, BindingResult bindingResult,
			@RequestParam("file") MultipartFile file, Model model) {
		log.info("Saving request: {} - file = {} file name='{}' empty? {}", request.getApplicationName(), file,
				file.getName(), file.isEmpty());
//		var transientToPersistentConverter = new TransientToPersistentConverter();
//		transientToPersistentConverter.convertTransientToPersistent(request);
		/*
		 * if (request.getTransientDevServers() == null ||
		 * request.getTransientDevServers().size() == 0) { Set<ServerRequest> devServers
		 * = (Set<ServerRequest>)model.getAttribute("devServers");
		 * request.setTransientDevServers(devServers); } if
		 * (request.getTransientTestServers() == null ||
		 * request.getTransientTestServers().size() == 0) { Set<ServerRequest>
		 * testServers = (Set<ServerRequest>)model.getAttribute("testServers");
		 * request.setTransientTestServers(testServers); }
		 */
		try {
			var employee = request.getEmployee();
			log.info("Request.Employee = {}", employee);
			var modelEmployee = model.getAttribute("employee");
			log.info("model.Employee = {}", modelEmployee);
			if (request.getApplicationName() != null && file != null && file.getName() != null
					&& file.getName().length() > 0 && !file.isEmpty()) {
				log.info("File not null, really saving");
				request.setRequestDate(LocalDate/* Time */.now());
				request.setDate(LocalDate/* Time */.now());

				try {
					var uploadPath = SettingsUtil.readSetting(SettingsUtil.FILE_UPLOAD_DIR);
					log.info("Upload dir = {}", uploadPath);
					log.info("Is file {} {} empty ? {}", file.getOriginalFilename(), file.getName(), file.isEmpty());
					request.setSignedContract(file.getOriginalFilename());
					log.info("project name 1 {}", request.getProjectName());
					log.info("application name 1 {}", request.getApplicationName());
					log.info("name service requester 1 {}", request.getNameServiceRequester());
					var currentUser = System.getProperty("user.name");
					log.info("Current user: {}", currentUser);
					model.addAttribute("request", request);
					log.info("Saving request - validating middleware");
//					var serverRequestConverter = new ServerRequestConverter();
					var devServerRequests = request.getDevServers();// (List<ServerRequest>)model.getAttribute("devServers");
					saveServerRequests(devServerRequests);
//					var devServersStr = serverRequestConverter.convertServerRequestsToJson(devServerRequests);
//					log.info("Dev servers: {} length: {}", devServersStr, devServersStr.length());
					var testServerRequests = request.getTestServers();// (List<ServerRequest>)model.getAttribute("testServers");
					saveServerRequests(testServerRequests);
//					var testServersStr = serverRequestConverter.convertServerRequestsToJson(testServerRequests);
//					log.info("test servers: {} len = {}", testServersStr, testServersStr.length());
//					request.setDevServerRequestsJson(devServersStr);
//					request.setTestServerRequestsJson(testServersStr);
					var devUsers = request.getDevUsers();
					saveUsers(devUsers);
					var testUsers = request.getTestUsers();
					saveUsers(testUsers);
//					var devUsersJson = convertUsersToJson(devUsers);
//					var testUsersJson = convertUsersToJson(testUsers);
//					request.setDevUsersJson(devUsersJson);
//					request.setTestUsersJson(testUsersJson);
					requestService.save(request);
					// log.info("Storing file: {}", file.getName());
					// log.info("File name from request: {}", request.getSignedContract());
					storageService.store(file);
					log.info("Stored file");
				} catch (IOException e) {
					log.error(" Error reading file upload path {}", e.toString());
				}
			}
		} catch (Exception e) {
			log.error("Some error occurred saving the request: {}", e.toString());
			System.out.println("== 1998 ===");
			e.printStackTrace();
		}

		var request2 = model.getAttribute("request");
		return SHOW_REQUEST;
	}

	private void saveUsers(List<Person> people) {
		
		for (var person : people) {
			personService.save(person);
		}
		
	}

	private void saveServerRequests(List<ServerRequest> serverRequests) {
		for (var serverRequest : serverRequests) {
			saveDiskRequests(serverRequest);
			saveMiddlewareRequests(serverRequest);
			serverRequestService.save(serverRequest);
		}
	}

	private void saveDiskRequests(ServerRequest serverRequest) {
		var diskRequests = serverRequest.getDisks();
		for (var diskRequest : diskRequests) {
			diskRequestService.save(diskRequest);
		}
	}

	private void saveMiddlewareRequests(ServerRequest serverRequest) {
		var middlewareRequests = serverRequest.getMiddlewareRequests();
		
		for (var middlewareRequest : middlewareRequests) {
			middlewareRequestService.save(middlewareRequest);
		}
		
	}

	private String convertUsersToJson(List<Person> users) {
		ObjectMapper om = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			om.writeValue(sw, users);
		} catch (IOException e) {
			log.error(e.toString());
			e.printStackTrace();
		}

		return sw.toString();
	}

	private void setMiddlewareOnDevServers(Request request) {
		var devServers = request.getDevServers();
		for (var devServer : devServers) {
			log.info("Got a dev server");
			var devMiddleware = devServer.getMiddlewareRequests();
			if (devMiddleware == null) {
//				var devMiddlewareRequests = initMiddlewareRequests();
//				devServer.setMiddleware(devMiddlewareRequests);
			}
		}
	}

	private void setMiddlewareOnTestServers(Request request) {
		var testServers = request.getTestServers();
		for (var testServer : testServers) {
			log.info("Got a test server {}", testServer);
			var testMiddlewareRequests = testServer.getMiddlewareRequests();
			log.info("Test midleware {}", testMiddlewareRequests);
			if (testMiddlewareRequests == null) {
				var transientMiddlewareRequests = initMiddlewareRequests();

//				testServer.setTransientMiddleware(transientMiddlewareRequests);
//				testServer.setMiddleware(middleware);
			}

		}
	}
	/*
	 * private void nullifyMiddleware(Request request) {
	 * log.info("Nullifying middleware on request"); var devServers =
	 * request.getDevServers(); nullifyMiddleware(devServers);
	 * 
	 * var testServers = request.getTestServers(); nullifyMiddleware(testServers); }
	 * 
	 * private void nullifyMiddleware(Set<ServerRequest> servers) {
	 * log.info("Nullifying middleware on servers"); for (ServerRequest server :
	 * servers) { log.info("Nullifying middleware on server");
	 * server.setMiddleware(null); }
	 * 
	 * }
	 */

	private Set<MiddlewareRequest> initMiddlewareRequests() {
		log.info("Initializing test middleware request");
		var middlewareRequests = new HashSet<MiddlewareRequest>();
		var middlewareRequest = initTestMiddlewareRequest();
		middlewareRequests.add(middlewareRequest);
		return middlewareRequests;
	}

	private MiddlewareRequest initTestMiddlewareRequest() {
		log.info("Iniitializing test middleware request");
		var middlewareRequest = new MiddlewareRequest();
		var allMiddleware = middlewareService.getAll();
		var middlewareIt = allMiddleware.iterator();
		if (middlewareIt.hasNext()) {
			log.info("Found middleware adding it to middleware request");
			var middleware = middlewareIt.next();
			/* TODO check
			middlewareRequest.setMiddleware(middleware); */
		}
		return middlewareRequest;
	}

	@RequestMapping("/edit/{id}")
	public String editRequest(Model model, @PathVariable long id) {
		log.info("--- Editing request ---");
		log.info("Getting request by id: {}", id);
		var optRequest = requestService.get(id);
		log.info("Got an optional of a request");
		if (optRequest.isPresent()) {
			log.info("Request present");
			var request = optRequest.get();
			var admins = userService.getAdmins();
			var signedContract = request.getSignedContract();
			model.addAttribute("setAssignee", true);
			model.addAttribute(ADMINS, admins);
			var filename = request.getSignedContract();
			log.info("Adding link to uploaded document: {}", filename);
			model.addAttribute("adminContract", filename);
			log.info("Editing request: application name = {}", request.getApplicationName());
			log.info("Title: {}", request.getTitle());
			log.info("OToD project name: {}", request.getOtodProjectName());
			model.addAttribute("file", signedContract);
			log.info("--- updating developers ---");
			var developerDto = new PersonDto();
			developerDto.setPeople(personService.getDevPeopleByRequestId(request.getId()));
			for (Person dev : developerDto.getPeople()) {
				log.info("Found developer {}", dev.getUserId());
			}
			log.info("--- updating testers ---");
			var testerDto = new PersonDto();
			testerDto.setPeople(personService.getTestPeopleByRequestId(request.getId()));
			for (Person tester : testerDto.getPeople()) {
				log.info("Found tester {}", tester.getUserId());
			}

			model.addAttribute("devUserDto", developerDto);
			model.addAttribute("testUserDto", testerDto);
			model.addAttribute("file", request.getSignedContract());
			model.addAttribute("request", request);
		}
		return EDIT_REQUEST/* REQUEST_ENVIRONMENT */;
	}

	@RequestMapping("/editMine/{id}")
	public String editMyRequest(Model model, @PathVariable long id) {
		log.info("Getting request by id: {}", id);
		var optRequest = requestService.get(id);
		log.info("Got an optional of a request");
		if (optRequest.isPresent()) {
			log.info("Request present");
			var request = optRequest.get();
			log.info(" Found request: {}", request.getApplicationName());
			model.addAttribute("setAssignee", false);
			model.addAttribute("request", request);
			model.addAttribute("oses", osService.getAll());
			model.addAttribute("middleware", middlewareService.getAll());
		}
		return REQUEST_ENVIRONMENT2;
	}

	@RequestMapping("/submitted")
	public String getSubmittedRequests(Model model) {
		log.info("Getting submitte requests");
		var allRequests = requestService.getAll();
		var allRequestCount = 0;
		if (allRequests instanceof Collection<?>) {
			allRequestCount = ((Collection<Request>) allRequests).size();
		}

		var requests = requestService.getSubmitted();
		var requestCount = 0;
		if (requests instanceof Collection) {
			requestCount = ((Collection<Request>) requests).size();
		}

		log.info("Found {} requests and {} submitted requests", allRequestCount, requestCount);
		model.addAttribute("allRequests", requests);
		return SHOW_REQUESTS;
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		log.info("Showing uploaded file: {}", filename);
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
