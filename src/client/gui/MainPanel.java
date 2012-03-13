package client.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import client.gui.week.WeekView;


public class MainPanel extends JPanel {

	private static final long serialVersionUID = -6453034572305492442L;


	public MainPanel() {
		super();
		
		JTabbedPane optionTabbedPane = new JTabbedPane();
		JTabbedPane calendarTabbedPane = new JTabbedPane();
		
		System.out.println("her");
		HovedPanel hp = new HovedPanel();
		optionTabbedPane.addTab("Hoved", hp); //TODO
		optionTabbedPane.addTab("Andre", new JPanel()); //TODO
		optionTabbedPane.addTab("Varsler(0)", new JPanel()); //TODO
		
		calendarTabbedPane.addTab("Uke", new WeekView());
		calendarTabbedPane.addTab("Måned", new JPanel()); //TODO
		
		this.add(optionTabbedPane);
		this.add(calendarTabbedPane);
	}
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Kalender");
		
		JPanel content = new MainPanel();
		frame.setContentPane(content);
		
		frame.pack();
		frame.setVisible(true);
	}

}
