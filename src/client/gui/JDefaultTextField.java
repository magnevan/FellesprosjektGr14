package client.gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * Works like a normal text field, but when the textfield is empty, the default text is displayed
 * @author Magne
 *
 */
public class JDefaultTextField extends JTextField implements FocusListener {
	
	private static final long serialVersionUID = 1L;
	
	private final String defaultText;
	
	public JDefaultTextField(String defaultText, int columns) {
		super(columns);
		
		this.defaultText = defaultText;
		this.setText(defaultText);
		this.setForeground(Color.GRAY);
		
		this.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().equals("")) {
			this.setText("");
			this.setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().equals("")) {
			this.setText(defaultText);
			this.setForeground(Color.GRAY);
		}
	}
	
	@Override
	public String getText() {
		if (super.getText().equals(defaultText)) 
			return "";
		
		return super.getText();
	}
	
}
