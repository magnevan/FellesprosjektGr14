package client.gui;

import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

public class AvtalePanel extends JPanel {
	
	private final JTextField tittelText;
	private final JDateChooser dateChooser;
	private final JTimePicker fromTime, toTime;
	private final JComboBox moteromComboBox;
	private final JTextField moteromText;
	
	public AvtalePanel() {
		super(new VerticalLayout(5,SwingConstants.LEFT));

		//Tittel
		this.add(new JLabel("Tittel:"));
		tittelText = new JTextField(25);
		this.add(tittelText);
		
		//Tid
		this.add(new JLabel("Tid"));
		JPanel tidPanel = new JPanel();
		dateChooser = new JDateChooser(Calendar.getInstance().getTime(), "dd. MMMM YYYY");
		dateChooser.setPreferredSize(new Dimension(130,20));
		tidPanel.add(dateChooser);
		
		fromTime = new JTimePicker("08:00");
		toTime = new JTimePicker("09:00");
		
		tidPanel.add(fromTime);
		tidPanel.add(new JLabel(" - "));
		tidPanel.add(toTime);
		
		this.add(tidPanel);
		
		//Moterom
		this.add(new JLabel("Møterom"));
		JPanel moteromPanel = new JPanel();
		moteromComboBox = new JComboBox(new String[]{"","moterom1","moterom2","moterom3","moterom4"});
		moteromText = new JDefaultTextField(15, "Skriv møteplass...");
		moteromPanel.add(moteromComboBox);
		moteromPanel.add(moteromText);
		
		this.add(moteromPanel);
		
	}
	
//	public static void main(String[] args) {
//		JFrame frame = new JFrame("test");
//		
//		JPanel content = new AvtalePanel();
//		frame.setContentPane(content);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		frame.pack();
//		frame.setVisible(true);
//	}

}
