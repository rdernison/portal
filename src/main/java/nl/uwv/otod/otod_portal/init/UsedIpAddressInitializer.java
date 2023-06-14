package nl.uwv.otod.otod_portal.init;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.uwv.otod.otod_portal.model.UsedIpAddress;
import nl.uwv.otod.otod_portal.service.UsedIpAddressService;

@Component
public class UsedIpAddressInitializer {
	@Autowired
	private UsedIpAddressService usedIpAddressService;
	
	public void initUsedIpAddresses() {
		Iterator<UsedIpAddress> usedIpAddresses = usedIpAddressService.getAll().iterator();
		if (!usedIpAddresses.hasNext()) {
			UsedIpAddress first = new UsedIpAddress();
			first.setAddress("192.168.70.1");
			usedIpAddressService.save(first);
			
			UsedIpAddress second = new UsedIpAddress();
			second.setAddress("192.168.70.2");
			usedIpAddressService.save(second);
		}
	}
	

}
