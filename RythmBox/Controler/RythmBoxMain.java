package RythmBox.Controler;
import RythmBox.View.*;
import javax.swing.*;
public class RythmBoxMain implements Runnable
{
	public void run()
	{
		JFrame f = new JFrame();
		FenetreRythmBox f2 = new FenetreRythmBox(f);
		f.add(f2);
		f.setSize(640,400);
		f.setVisible(true);
	}
}