package client.gui.panels;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ModifiedCheckBoxDemo extends JFrame {

	//....

	JCheckBox checkBox;

	//....

	public ModifiedCheckBoxDemo () {

	//....

	initComponents();

	pack();

	}



	private void initComponents() {

	//...

	Icon normal = UIManager.getIcon("CheckBox.icon");

	Icon selected = new MyCheckBoxIcon(Color.white);

	checkBox = new JCheckBox("Test", normal);

	checkBox.setSelectedIcon(selected);

	this.getContentPane().add(checkBox);

	}



	public static void main(String[] args) {
		
		new ModifiedCheckBoxDemo().setVisible(true);


	}
}

