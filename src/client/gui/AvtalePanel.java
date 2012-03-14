package client.gui;

import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

public class AvtalePanel extends JPanel {
	
	private final JTextField tittelText;
	private final JDateChooser dateChooser;
	private final JTimePicker fromTime, toTime;
//	private final JComboBox moteromComboBox;
//	private final JTextField moteromText;
	
	public AvtalePanel() {
		super(new VerticalLayout(5,SwingConstants.LEFT));

		//Tittel
		this.add(new JLabel("Tittel:"));
		tittelText = new JTextField(20);
		this.add(tittelText);
		
		//Tid
		this.add(new JLabel("Tid"));
		JPanel tidPanel = new JPanel();
		dateChooser = new JDateChooser(Calendar.getInstance().getTime(), "dd. MMMM YYYY");
		dateChooser.setPreferredSize(new Dimension(130,20));
		tidPanel.add(dateChooser);
		
		toTime = new JTimePicker();
		fromTime = new JTimePicker();
		
		tidPanel.add(fromTime);
		tidPanel.add(new JLabel(" - "));
		tidPanel.add(toTime);
		
		
		this.add(tidPanel);
		
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("test");
		
		JPanel content = new AvtalePanel();
		frame.setContentPane(content);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
	}

}
