package nl.uwv.otod.otod_portal.controller.handler;

import org.apache.poi.ss.usermodel.Cell;

public class NoneCellHandler implements CellHandler<String> {

	@Override
	public String getValue(Cell cell) {
		System.out.println("None cell: ");
		return null;
	}

}
