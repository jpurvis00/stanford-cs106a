import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import java.awt.Color;


public class RandomCircles extends GraphicsProgram 	{

	/** Number of balls to be drawn on screen. */
	private static final int NUMB_BALLS = 10;
	
	/** Circle can not have a radius smaller than 5. */
	private static final double MIN_RADIUS = 5;
	
	/** Circle can not have a radius bigger than 50. */
	private static final double MAX_RADIUS = 50;
	
	public void run() {
	
		drawCircle();
	}
	
	/** Draw a circle on the canvas */
	private void drawCircle() {
		double windWidth = getWidth();
		double windHeight = getHeight();
		
		for(int i = 0; i < NUMB_BALLS; i++) {
			Color circleColor = rgen.nextColor();
			double radius = rgen.nextDouble(MIN_RADIUS, MAX_RADIUS);
			double x = rgen.nextDouble(0, windWidth - 2 * radius);
			double y = rgen.nextDouble(0, windHeight - 2 * radius);
			GOval circle = new GOval(x, y, radius * 2, radius * 2);
			circle.setFilled(true);
			circle.setColor(circleColor);
			add(circle);
		}
	}
	
	/* Private instance variable */
	private RandomGenerator rgen = RandomGenerator.getInstance();
}
