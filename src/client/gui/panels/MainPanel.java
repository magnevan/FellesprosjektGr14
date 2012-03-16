package client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import client.gui.week.WeekView;


public class MainPanel extends JPanel {

	private static final long serialVersionUID = -6453034572305492442L;


	public MainPanel() {
		super(new BorderLayout());
		
		JTabbedPane optionTabbedPane = new JTabbedPane();
		JTabbedPane calendarTabbedPane = new JTabbedPane();
		
		AvtalePanel ap = new AvtalePanel();
		HovedPanel hp = new HovedPanel();
		VarselPanel vp = new VarselPanel();
		AndrePanel akp = new AndrePanel();
		
		optionTabbedPane.addTab("Hoved", ap); //TODO
		optionTabbedPane.addTab("Andre Kalendre", akp); //TODO
		optionTabbedPane.addTab("Varsler(0)", vp); //TODO
		
		calendarTabbedPane.addTab("Uke", new WeekView());
		calendarTabbedPane.addTab("Måned", new JPanel()); //TODO
		
		//TODO This should probably be done in a better manner
		optionTabbedPane.setPreferredSize(new Dimension(300,calendarTabbedPane.getPreferredSize().height));
		
		this.add(optionTabbedPane,BorderLayout.CENTER);
		this.add(calendarTabbedPane, BorderLayout.EAST);
	}
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Kalender");
		
		JPanel content = new MainPanel();
		frame.setContentPane(content);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
	}

}