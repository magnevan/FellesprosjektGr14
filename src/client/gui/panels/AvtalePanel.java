package client.gui.panels;

import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.gui.JDefaultTextArea;
import client.gui.JDefaultTextField;
import client.gui.JTimePicker;
import client.gui.VerticalLayout;

import com.toedter.calendar.JDateChooser;

public class AvtalePanel extends JPanel {
	
	private final JTextField tittelText;
	private final JDateChooser dateChooser;
	private final JTimePicker fromTime, toTime;
	private final JComboBox moteromComboBox;
	private final JTextField moteromText;
	private final JTextArea beskrivelseTextArea;
	final PersonLabel personLabel;
//	private final FilteredUserList filteredUserList;
	
	public AvtalePanel() {
		super(new VerticalLayout(5,SwingConstants.LEFT));
		
		//top
		JPanel topPanel = new JPanel();
		personLabel = new PersonLabel();
		topPanel.add(personLabel);
		
		this.add(topPanel);
		this.add(Box.createVerticalStrut(30));
		
		//Tittel
		this.add(new JLabel("Tittel:"));
		tittelText = new JTextField(26);
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
		this.add(new JLabel("M�terom"));
		JPanel moteromPanel = new JPanel();
		moteromComboBox = new JComboBox(new String[]{"","P15 rom 436","Torget","Hell","Oslo"});
		moteromText = new JDefaultTextField("Skriv m�teplass...", 15);
		moteromPanel.add(moteromComboBox);
		moteromPanel.add(moteromText);
		
		this.add(moteromPanel);
		
		//Beskrivelse
		this.add(new JLabel("Beskrivelse:"));
		beskrivelseTextArea = new JDefaultTextArea("Skriv inn beskrivelse...", 4, 26);
		beskrivelseTextArea.setLineWrap(true);
		this.add(beskrivelseTextArea);
		JScrollPane beskrivelseScroll = new JScrollPane(beskrivelseTextArea);
		beskrivelseScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(beskrivelseScroll);
		
		//Ansatte
		
		this.add(new JLabel("Ansatte:"));
		
//		filteredUserList = new FilteredUserList(new TestModel());
//		this.add(filteredUserList);
		
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
