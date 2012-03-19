package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.*;


public class CheckListCellRenderer extends JPanel implements ListCellRenderer{ 
    private ListCellRenderer delegate; 
    private ListSelectionModel selectionModel; 
    private JCheckBox checkBox = new JCheckBox(); 
 
    public CheckListCellRenderer(ListCellRenderer renderer, ListSelectionModel selectionModel){ 
        this.delegate = renderer; 
        this.selectionModel = selectionModel; 
        setLayout(new BorderLayout()); 
        setOpaque(false); 
        checkBox.setOpaque(true);
        
       initComponents();
        
        
    } 
 
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){ 
        Component renderer = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
        checkBox.setSelected(selectionModel.isSelectedIndex(index)); 
        
        //initComponents();
        
        removeAll(); 
        add(checkBox, BorderLayout.WEST); 
        add(renderer, BorderLayout.CENTER); 
        return this; 
    } 
    
    public void initComponents(){
    	
    	  //Icon normal = UIManager.getIcon("CheckBox.icon");
        checkBox.setOpaque(true);
        Random random = new Random();
        int red = random.nextInt(256);
        int blue = random.nextInt(256);
        int green = random.nextInt(256);
        Color lol = new Color(red, green, blue);
        Icon normal = new MyCheckBoxIcon(lol);
    	Icon selected = new MyCheckBoxIcon(Color.white);
    	checkBox = new JCheckBox(normal);
    	checkBox.setSelected(true);
    	checkBox.setSelectedIcon(selected);
    	checkBox.setBackground(Color.white);
    	checkBox.setEnabled(true);
    	//add(checkBox);
    	
    }
    
} 
	
