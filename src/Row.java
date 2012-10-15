import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class Row extends JLabel implements ListCellRenderer{
	private int percent = -1;
	private boolean isSelected = false;
	private int index = 0;
	
	public Row(){
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
		setText(value.toString());
		setPreferredSize(new Dimension(375, 22));
		setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
		
		this.index = index;
		
		if(isSelected || cellHasFocus){
			setForeground(Color.white);
			
			this.isSelected = isSelected;
		}
		else{
			setForeground(Color.black);
		}
		
		if(value.toString().charAt(value.toString().length() - 1) == '%'){
			percent = Integer.parseInt(value.toString().split(" ")[value.toString().split(" ").length - 1].split("%")[0]);
		}
		
		return this;
	}
	
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		setOpaque(false);
		int height = getHeight();
		
		if(index % 2 == 1){
			g2d.setColor(new Color(230, 230, 230));
			g2d.fillRect(0, 0, 375, height);
		}
		
		if(isSelected){
			int width = 375;
			
			g2d.setColor(new Color(0, 150, 255));
			g2d.fillRect(0, 0, width, height);
		}
		
		if(percent != -1){
			g2d.setColor(Color.white);
			g2d.fillRect(250, height / 2 - 7, 100, 12);
			g2d.setColor(new Color(57, 183, 0));
			
			int width = (int) (100 * ((double) percent / 100));
			g2d.fillRect(250, height / 2 - 7, width, 12);
			
			g2d.drawRect(250, height / 2 - 7, 100, 12);
		}
		
		super.paintComponent(g);
		percent = -1;
		isSelected = false;
	}
}