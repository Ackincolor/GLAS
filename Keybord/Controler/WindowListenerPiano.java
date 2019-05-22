package Keybord.Controler;

import java.awt.event.*;
import Keybord.View.*;

public class WindowListenerPiano implements WindowListener{
	private FenetreKeybord v;
	public WindowListenerPiano(FenetreKeybord v)
	{
		this.v = v;
	}
	public void windowActivated(WindowEvent e)
	{

	}
	public void windowClosed(WindowEvent e)
	{
		this.v.close();
	}
	public void windowClosing(WindowEvent e)
	{
		
	}
	public void windowDeactivated(WindowEvent e)
	{
		
	}
	public void windowDeiconified(WindowEvent e)
	{
		
	}
	public void windowIconified(WindowEvent e)
	{
		
	}
	public void windowOpened(WindowEvent e)
	{
		
	}
}