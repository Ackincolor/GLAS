package Keybord.Controler;

import java.awt.event.*;
import Keybord.View.*;
import Keybord.Model.*;
public class PitchModGraphControlMouse implements MouseListener
{
	private PitchModGraph v;
  	private SynthModel model;
	public PitchModGraphControlMouse(PitchModGraph v,SynthModel model)
	{
		this.model = model;
		this.v = v;
	}
	public void mouseClicked(MouseEvent e)
    {
    	if(e.getButton() == MouseEvent.BUTTON3)
    	{
    		this.v.setPitch(8192);
		    this.v.setMod(64);
		    //on applique les filtre au model
		    this.model.setEffect(8192,0,false);
		    this.model.setEffect(0,1,false);
    	}
    }
    public void mouseEntered(MouseEvent e){
    }
    public void mouseExited(MouseEvent e){
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
}