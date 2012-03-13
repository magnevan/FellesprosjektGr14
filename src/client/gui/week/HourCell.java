package client.gui.week;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

public class HourCell extends JPanel {

	private static final long serialVersionUID = -7714516649231902177L;
	public final int WIDTH, HEIGHT;
	
	private final int hour;
	
	/**
	 * @author Magne
	 */
	public HourCell(int hour, int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.hour = hour;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, WIDTH, HEIGHT);
		
		g2d.setColor(Color.GRAY);
		
		Stroke dotted = new BasicStroke(1.0f, // line width
			      /* cap style */BasicStroke.CAP_BUTT,
			      /* join style, miter limit */BasicStroke.JOIN_BEVEL, 1.0f,
			      /* the dash pattern */new float[] { 5.0f, 3.0f, 5.0f, 3.0f },
			      /* the dash phase */0.0f); /* on 8, off 3, on 2, off 3 */
		
		g2d.setStroke(dotted);
		g2d.drawLine(1, HEIGHT/2, WIDTH-1, HEIGHT/2);
	}
	
	public int getHour() {return hour;}
}
