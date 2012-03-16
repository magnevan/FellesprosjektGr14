package client.gui.panels;

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
    } 
    
 
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){ 
        Component renderer = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
        checkBox.setSelected(selectionModel.isSelectedIndex(index)); 
        
        Color color = new Color(255, 0, 0);
        checkBox.setBackground(color);
        setEnabled(checkBox.isEnabled());
        
        removeAll(); 
        add(checkBox, BorderLayout.WEST); 
        add(renderer, BorderLayout.CENTER); 
        return this; 
    } 
    
    public void setColor(){
	    Random color = new Random();
	    int red = color.nextInt(256);
	    int green = color.nextInt(256);
	    int blue = color.nextInt(256);
	    Color backDrop = new Color(red, green, blue);
	    checkBox.setBackground(backDrop);
	    
	    }
    
    
}
