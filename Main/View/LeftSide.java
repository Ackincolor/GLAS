package Main.View;
import Main.View.*;
import Main.Controler.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class LeftSide extends JPanel
{
	private JButton plus,moins,modif;
	private FenetreMain fen;
	public LeftSide(FenetreMain window)
	{
		this.fen = window;
		this.setLayout(new GridLayout(10,1));
		this.plus=new JButton("+");
		this.moins=new JButton("-");
		this.modif=new JButton("mod");
		ListenerButtonLeft controler = new ListenerButtonLeft(this.fen);
		this.plus.addActionListener(controler);
		this.moins.addActionListener(controler);
		this.add(this.plus);
		this.add(this.moins);
		this.add(this.modif);
	}
}