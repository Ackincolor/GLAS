package Keybord.Controler;
import Keybord.View.*;
import java.awt.event.*;
public class FocusPiano implements MouseListener
{
	private PianoView vue;
	public FocusPiano(PianoView vue)
	{
		this.vue = vue;
	}
    public void mouseClicked(MouseEvent e)
    {
    	this.vue.requestFocus();
    }
    public void mouseEntered(MouseEvent e){
        this.vue.requestFocus();
    }
    public void mouseExited(MouseEvent e){
     this.vue.requestFocus();   
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
}