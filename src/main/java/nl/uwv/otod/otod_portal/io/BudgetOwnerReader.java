package nl.uwv.otod.otod_portal.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BudgetOwnerReader {

	public List<String> readBudgetOwners() throws UnsupportedEncodingException, IOException {
		var budgetOwners = new ArrayList<String>();
		try (var in = getClass().getResourceAsStream("/latest-import/budgethouder.txt");
				var br = new BufferedReader(new InputStreamReader(in, "utf-8"))) {
			var line = "";
			while ((line = br.readLine()) != null) {
				budgetOwners.add(line);
			}
			
		}
		
		return budgetOwners;
	}
}
