/* Example of creating a console not using acm.jar */

import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class JFrameTest {

	public static void main(String[] args) {
		JFrame frame = new JFrame("JFrame test");
		frame.setSize(300, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//placeComponents(frame);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
				
		JPanel checkBoxPanel = new JPanel();
		
		JCheckBox check = new JCheckBox("Hello");
		checkBoxPanel.add(check);
		check.setSelected(true);
		JCheckBox bye = new JCheckBox("Bye");
		checkBoxPanel.add(bye);
		
		mainPanel.add(checkBoxPanel);
		frame.add(mainPanel, BorderLayout.PAGE_END);
		frame.setVisible(true);
	}

	/* private static void placeComponents(JFrame frame) {
		frame.setLayout(null);

		JLabel userLabel = new JLabel("User");
		userLabel.setBounds(10, 10, 80, 25);
		frame.add(userLabel);

		JTextField userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		frame.add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		frame.add(passwordLabel);

		JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		frame.add(passwordText);

		JButton loginButton = new JButton("login");
		loginButton.setBounds(10, 80, 80, 25);
		frame.add(loginButton);

		JButton registerButton = new JButton("register");
		registerButton.setBounds(180, 80, 80, 25);
		frame.add(registerButton);

		//ActionListener loginButtonListener = new LoginButtonListener();
		//loginButton.addActionListener(loginButtonListener);
	} */
}
