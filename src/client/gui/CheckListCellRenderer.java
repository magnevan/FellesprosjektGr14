package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.*;

import client.model.UserModel;

public class CheckListCellRenderer extends JPanel implements ListCellRenderer{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = -226231049907500676L;
	private ListCellRenderer delegate; 
    private ListSelectionModel selectionModel; 
    private JCheckBox checkBox = new JCheckBox();
    Color lol;
    Icon normal;
 
    public CheckListCellRenderer(ListCellRenderer renderer, ListSelectionModel selectionModel){ 
        this.delegate = renderer; 
        this.selectionModel = selectionModel; 
        setLayout(new BorderLayout()); 
        setOpaque(false); 
        checkBox.setOpaque(true);
        
        //initComponents();
        
    } 
 
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
    	
    	UserModel model = (UserModel)value;
    	checkBox.setOpaque(true);
        Icon normal;
       // Color color = model.getColor();
        normal = new MyCheckBoxIcon(model.getColor());
    	checkBox = new JCheckBox(normal);
      	Icon selected = new MyCheckBoxIcon(Color.white);
  		
      	checkBox.setSelected(true);
      	checkBox.setSelectedIcon(selected);
      	checkBox.setBackground(Color.white);
      	checkBox.setEnabled(true);
    	
        Component renderer = delegate.getListCellRendererComponent(list, model, index, isSelected, cellHasFocus); 
        checkBox.setSelected(selectionModel.isSelectedIndex(index));
        	
        removeAll(); 
        add(checkBox, BorderLayout.WEST); 
        add(renderer, BorderLayout.CENTER);
        
        return this; 
    } 
    
    public void initComponents(){
    	
    	//Icon normal = UIManager.getIcon("CheckBox.icon");
    
       
    }
    
} 
	
