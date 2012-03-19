package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.*;

import client.model.UserModel;

public class CheckListCellRenderer extends JPanel implements ListCellRenderer{ 
    private ListCellRenderer delegate; 
    private ListSelectionModel selectionModel; 
    private JCheckBox checkBox = new JCheckBox();
    Random random;
    int red;
    int blue;
    int green;
    Color lol;
    Icon normal;
 
    public CheckListCellRenderer(ListCellRenderer renderer, ListSelectionModel selectionModel){ 
        this.delegate = renderer; 
        this.selectionModel = selectionModel; 
        setLayout(new BorderLayout()); 
        setOpaque(false); 
        checkBox.setOpaque(true);
        
        random = new Random();
        red = 255;
        blue = 0;
        green = 0;
        
        initComponents();
        
    } 
 
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
    	
    	UserModel model = (UserModel)value;
    	//value = model;
        Component renderer = delegate.getListCellRendererComponent(list, model, index, isSelected, cellHasFocus); 
        checkBox.setSelected(selectionModel.isSelectedIndex(index));
        
        	
        removeAll(); 
        add(checkBox, BorderLayout.WEST); 
        add(renderer, BorderLayout.CENTER);
        
        return this; 
    } 
    
    public void initComponents(){
    	
    	//Icon normal = UIManager.getIcon("CheckBox.icon");
        checkBox.setOpaque(true);
        Icon normal;
    	Icon selected = new MyCheckBoxIcon(Color.white);
    	
    	//if (false)
    		lol = new Color(red, green, blue);
    	//if (true)
    		//lol = new Color(green);
    	//if (false)
    		//lol = new Color(blue);
    	
    	normal = new MyCheckBoxIcon(lol);
		checkBox = new JCheckBox(normal);
    	checkBox.setSelected(true);
    	checkBox.setSelectedIcon(selected);
    	checkBox.setBackground(Color.white);
    	checkBox.setEnabled(true);
    }

	

    
} 
	
