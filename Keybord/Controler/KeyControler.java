package Keybord.Controler;
import java.awt.event.*;
import javax.swing.*;
import Keybord.View.*;
import Keybord.Model.*;
import javax.sound.midi.*;

import java.util.*;
public class KeyControler extends MouseAdapter
{
	private PianoView vue;
	private ArrayList<Key> blanches,noires;
	private int x,y;
	private Key current = null;
	private SynthModel synthModel;
	private JCheckBox mouseOverPiano;
/**
*	Constructeur de KeyControler
*
* 	@param vue Vue du piano
*	@param blanches Liste des touches blanches
*	@param noires Liste des touches noires
*	@param synthModel Model du Clavier Virtuel
*	@param mousOverPiano JCheckBox pour savoir si on active ou non le MouseOver
*/
	public KeyControler(PianoView vue,ArrayList<Key> blanches,ArrayList<Key> noires,SynthModel synthModel,JCheckBox mouseOverPiano)
	{
		this.vue = vue;
		this.blanches = blanches;
		this.noires = noires;
		this.synthModel = synthModel;
		this.mouseOverPiano = mouseOverPiano;
	}
	public void mouseClicked(MouseEvent e)
	{	
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
		/*
		if(this.current!=null)
			this.synthModel.noteOffModel(this.current.getNum());
		this.current=null;
		System.out.println("exited :"+this.synthModel.getIntInstru());*/
	}
	public void mousePressed(MouseEvent e)
	{
		x=e.getX();
		y=e.getY();
		this.current=getKey(x,y);
		if(this.current!=null)
		{
			this.synthModel.noteOnModel(this.current.getNum());
			this.current.setOn();
			this.vue.repaint();

		}
	}
	public void mouseReleased(MouseEvent e)
	{
		if(this.current!=null)
		{
			this.synthModel.noteOffModel(this.current.getNum());
			this.current.setOff();
			this.vue.repaint();
		}		
	}
	public void mouseDragged(MouseEvent e)
	{}
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if(this.mouseOverPiano.isSelected())
		{
			Key key = getKey(e.getX(),e.getY());		
			if (this.current != null && this.current != key) {
                this.synthModel.noteOffModel(this.current.getNum());
                this.current.setOff();
            } 
            if (key != null && this.current != key) {
                this.synthModel.noteOnModel(key.getNum());
                key.setOn();
            }
            this.current = key;
            this.vue.getParent().repaint();
		}
	}
	private Key getKey(int x,int y)
	{
		for(Key note : this.noires)
		{
			if(note.contains(x,y))
			{
				return note;
			}
		}
		for(Key note : this.blanches)
		{
			if(note.contains(x,y))
			{
				return note;
			}
		}
		return null;
	}
}
