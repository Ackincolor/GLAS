package Main.Controler;
import Main.View.*;
public class GLAS implements Runnable
{
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new GLAS());
	}
	public void run()
	{
		FenetreMain f = new FenetreMain("GLAS");
		f.setVisible(true);
	}
}