package Main.Controler;
import java.awt.event.*;
import RythmBox.Controler.*;
public class AfficherRythmeControler implements ActionListener
{
	public AfficherRythmeControler()
	{

	}
	public void actionPerformed(ActionEvent e) 
	{ 
    	//afficher la fenetre du Rythme
    	javax.swing.SwingUtilities.invokeLater(new RythmBoxMain());
	}
}