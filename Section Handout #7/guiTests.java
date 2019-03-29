import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class guiTests extends ConsoleProgram {
	public void init() {
		//setFont("Courier-24");	
		
		add(new JButton("Hi"), EAST);
		add(new JButton("CS106A"), NORTH);
		add(new JButton("Basket Weaving 101"), SOUTH);
		
		nameField = new JTextField(10);
		add(new JLabel("Name"), SOUTH);
		add(nameField, SOUTH);
		nameField.addActionListener(this);
		addActionListeners();
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Hi")) {
			println("Hello there!");
		}else if(cmd.equals("CS106A")) {
			println("CS106A rocks!");
		}else if(cmd.equals("Basket Weaving 101")) {
			println("Basket Weaving does not rock!");
		}
		
		if(e.getSource() == nameField) {
			println("Hello, " + nameField.getText());
		}
	}
	
	/* instance vars */
	private JTextField nameField;
}
