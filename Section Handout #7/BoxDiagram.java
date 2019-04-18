import acm.program.*;
import java.awt.event.*;
import javax.swing.*;
import acm.graphics.*;


public class BoxDiagram extends ConsoleProgram {
	
	/** Box dimensions */
	//private static final double BOX_WIDTH = 120;
	//private static final double BOX_HEIGHT = 50;
	
	public void init() {
		nameField = new JTextField(10);
		add(new JLabel("Name"), SOUTH);
		add(nameField, SOUTH);
		
		add(new JButton("Add"), SOUTH);
		add(new JButton("Remove"), SOUTH);
		add(new JButton("Clear"), SOUTH);
		
		nameField.addActionListener(this);
		addActionListeners();
		
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Add")) {
			//add(canvas.createRect());
			add(new GRect(100, 100, 50, 100));
			println("Add");
		}
		else if(cmd.equals("Remove"))
			println("Remove");
		else if(cmd.equals("Clear"))
			println("Clear");
		
		if(e.getSource() == nameField) {
			println(nameField.getText());
		}
			
	}
	
	//private GRect createRect() {
		//rect = new GRect(BOX_WIDTH, BOX_HEIGHT);
		//rect.setFilled(false);		
		//return rect;
	//}
	
	/* Private instance vars */
	private JTextField nameField;
	//private GRect rect;
	private BoxDiagramCanvas canvas;
}
