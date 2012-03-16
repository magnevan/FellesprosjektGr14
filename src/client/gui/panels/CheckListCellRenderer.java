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
        
        initComponents();
    	//pack();
        
        setOpaque(false); 
        checkBox.setOpaque(true);
    } 
    
 
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){ 
        Component renderer = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
        checkBox.setSelected(selectionModel.isSelectedIndex(index)); 
        
        removeAll(); 
        add(checkBox, BorderLayout.WEST); 
        add(renderer, BorderLayout.CENTER); 
        return this; 
    } 
    
	private void initComponents() {

	//...

	Icon normal = UIManager.getIcon("CheckBox.icon");

	Icon selected = new MyCheckBoxIcon(Color.white);

	checkBox = new JCheckBox(normal);

	checkBox.setSelectedIcon(selected);
	
	checkBox.setBackground(Color.white);

	}
    
    
}
