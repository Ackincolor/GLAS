package Keybord.Controler;

import Keybord.View.*;
import javax.swing.*;
/**
*
*
*/
public class KeybordMain implements Runnable
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new KeybordMain());
	}
	public void run()
	{
		FenetreKeybord fen = new FenetreKeybord();
		fen.setVisible(true);
	}
}