package ui_component;

public class Cell {

	public Cell upCell = null, downCell = null, leftCell = null, rightCell = null;
	public boolean upBoolean = false, downBoolean = false, leftBoolean = false, rightBoolean = false;
	boolean isVisiteddfs = false;// added
	boolean isVisitedbfs = false;// added
	int x, y;
}