package nl.uwv.otod.otod_portal.controller.handler;

import org.apache.poi.ss.usermodel.Cell;

public interface CellHandler<T> {
	public T getValue(Cell cell);
}
