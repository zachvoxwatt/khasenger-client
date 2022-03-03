package ui.main.functions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class MouseDetector implements MouseListener
{
	private boolean mouseIsIn = false;
	private int mx, my;
	
	private JComponent target;
	
	public MouseDetector(JComponent jc) { this.target = jc; }
	
	@Override
	public void mouseEntered(MouseEvent e) { this.mx = e.getX(); this.my = e.getY(); }
	@Override
	public void mouseExited(MouseEvent e) { this.mouseIsIn = false; }
	
	//no use
	@Override
	public void mouseClicked(MouseEvent e) { }
	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }
	
	public boolean isMouseInside() 
	{
		if (this.mx >= 0 && this.mx <= this.target.getPreferredSize().getWidth() 
		&& this.my >= 0 && this.my <= this.target.getPreferredSize().getHeight()) this.mouseIsIn = true;
		
		else this.mouseIsIn = false;

		return this.mouseIsIn;
	}
}