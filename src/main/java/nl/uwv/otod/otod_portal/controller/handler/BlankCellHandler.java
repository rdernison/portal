package nl.uwv.otod.otod_portal.controller.handler;

import org.apache.poi.ss.usermodel.Cell;

public class BlankCellHandler implements CellHandler<String> {

	@Override
	public String getValue(Cell cell) {
		return "";
	}

}
