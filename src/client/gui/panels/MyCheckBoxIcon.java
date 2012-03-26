package client.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.*;

public class MyCheckBoxIcon implements Icon {



	private Color color;

	private int width, height;



	MyCheckBoxIcon(Color iconColor) {

		color = iconColor;

		Icon icon = UIManager.getIcon("CheckBox.icon");

		width = icon.getIconWidth();

		height = icon.getIconHeight();

}



	public void paintIcon(Component c, Graphics g, int x, int y) {

		g.setColor(color);

		g.fillRect(x, y, width, height);

}



	public int getIconWidth() {

		return width;

}



	public int getIconHeight() {

		return height;

}


}

