package nl.uwv.otod.otod_portal.controller.handler;

import org.apache.poi.ss.usermodel.Cell;

public class NumericCellHandler implements CellHandler<Double> {

	
	@Override
	public Double getValue(Cell cell) {
		System.out.println("Numeric cell: " + cell.getNumericCellValue());
		return cell.getNumericCellValue();
	}

}
