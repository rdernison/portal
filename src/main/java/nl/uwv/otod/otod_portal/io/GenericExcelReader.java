package nl.uwv.otod.otod_portal.io;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;

import nl.uwv.otod.otod_portal.controller.handler.BlankCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.CellHandler;
import nl.uwv.otod.otod_portal.controller.handler.ErrorCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.FormulaCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.NoneCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.NumericCellHandler;
import nl.uwv.otod.otod_portal.controller.handler.StringCellHandler;

public abstract class GenericExcelReader {

	protected static Map<CellType, CellHandler<?>> cellHandlers = new HashMap<>();
	static {
		cellHandlers.put(CellType._NONE, new NoneCellHandler());
		cellHandlers.put(CellType.BLANK, new BlankCellHandler());
		cellHandlers.put(CellType.ERROR, new ErrorCellHandler());
		cellHandlers.put(CellType.FORMULA, new FormulaCellHandler());
		cellHandlers.put(CellType.NUMERIC, new NumericCellHandler());
		cellHandlers.put(CellType.STRING, new StringCellHandler());
	}
}
