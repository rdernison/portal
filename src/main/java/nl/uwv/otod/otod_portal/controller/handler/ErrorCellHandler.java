package nl.uwv.otod.otod_portal.controller.handler;

import org.apache.poi.ss.usermodel.Cell;

public class ErrorCellHandler implements CellHandler<Byte> {

	@Override
	public Byte getValue(Cell cell) {
		return cell.getErrorCellValue();
	}

}
