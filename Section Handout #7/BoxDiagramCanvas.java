import acm.graphics.*;

public class BoxDiagramCanvas extends GCanvas {
	/** Box dimensions */
	private static final double BOX_WIDTH = 120;
	private static final double BOX_HEIGHT = 50;
	
	public GRect createRect() {
		rect = new GRect(BOX_WIDTH, BOX_HEIGHT);
		rect.setFilled(false);		
		return rect;
	}
	
	public GRect rect;
}
