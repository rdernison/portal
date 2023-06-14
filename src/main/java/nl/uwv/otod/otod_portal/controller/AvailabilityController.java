package nl.uwv.otod.otod_portal.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import nl.uwv.otod.otod_portal.util.SettingsUtil;

@Controller
@RequestMapping("/availableIpAddresses")
public class AvailabilityController {

	private static final String SHOW_AVAILABLE_IP_ADDRESSES = "showAvailableIpAddresses";
	
	private static final String SHOW_AVAILABLE_IP_ADDRESSES_PAGED = "showAvailableIpAddressesPaged";

	private static Logger logger = LogManager.getLogger();
	
	@RequestMapping("")
	public String getAvailableIpAddresses(Model model) throws IOException {
		logger.info("Getting available ip addresses");
		var availableIpAddresses = readAvailableIpAddresses();
		model.addAttribute("availableIpAddresses", availableIpAddresses);
		return SHOW_AVAILABLE_IP_ADDRESSES;
	}
	
	private List<String> readAvailableIpAddresses() throws IOException {
		var availableIpAddressesPathName = SettingsUtil.readSetting(SettingsUtil.AVAILABLE_IP_ADDRESSES_PATH);
		var availableIpAddressesPath = Paths.get(availableIpAddressesPathName);
		var availableIpAddresses = Files.readAllLines(availableIpAddressesPath);
		return availableIpAddresses;
	}
	
/*
    @RequestMapping(value = "/listBooks", method = RequestMethod.GET)
    public String listBooks(
      Model model, 
      @RequestParam("page") Optional<Integer> page, 
      @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Book> bookPage = bookService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("bookPage", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "listBooks.html";
    }
  */
	
	@RequestMapping("/paged")
	public String getAvailableIpAddressesPaged(Model model, 
		      @RequestParam("page") Optional<Integer> page, 
		      @RequestParam("size") Optional<Integer> size) throws IOException {
		
		int currentPage = page.orElse(1);
        int pageSize = size.orElse(20);
logger.info("Getting available ip addresses paged; current page = {}, pageSize = {}", currentPage, pageSize);
        Page<String> availableIpAddressPage = readAvailableIpAddressesPaged(PageRequest.of(currentPage - 1, pageSize));//bookService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("ipAddressPage", availableIpAddressPage);

        int totalPages = availableIpAddressPage.getTotalPages();
        if (totalPages > 0) {
            var pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
		return SHOW_AVAILABLE_IP_ADDRESSES_PAGED;
		
	}

private Page<String> readAvailableIpAddressesPaged(PageRequest of) throws IOException {
	var availableIpAddresses = readAvailableIpAddresses();
	var pageSize = of.getPageSize();
	var pageNumber = of.getPageNumber();
	var availableIpAddressesPaged = paginate(availableIpAddresses, pageSize, pageNumber);
	var page = new PageImpl<String>(availableIpAddressesPaged, of, availableIpAddresses.size());
	return page;
}

private List<String> paginate(List<String> availableIpAddresses, int pageSize, int pageNumber) {
	var page = new ArrayList<String>();
	var startIndex = (pageNumber ) * pageSize;
	var endIndex = startIndex + pageSize;
	for (var i = startIndex; i < endIndex && i < availableIpAddresses.size(); i++) {
		page.add(availableIpAddresses.get(i));
	}
	return page;
}
    
}
