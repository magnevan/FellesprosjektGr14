package client.gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextArea;

public class JDefaultTextArea extends JTextArea implements FocusListener {
	
	private static final long serialVersionUID = -8018332784929190232L;
	
	private final String defaultText;
	
	public JDefaultTextArea(String defaultText, int rows, int cols) {
		
		super(rows, cols);
		
		this.defaultText = defaultText;
		this.setText(defaultText);
		this.setForeground(Color.GRAY);
		
		this.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().equals(defaultText)) {
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
	
}
