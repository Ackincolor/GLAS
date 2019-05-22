package Main.Controler;
import java.awt.event.*;
import Keybord.Controler.*;
public class AfficherPianoControler implements ActionListener
{
	public AfficherPianoControler()
	{

	}
	public void actionPerformed(ActionEvent e) 
	{ 
    	//afficher la fenetre du piano
    	javax.swing.SwingUtilities.invokeLater(new KeybordMain());
	}
}