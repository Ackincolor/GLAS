package Keybord.View;

import javax.swing.*;
import java.awt.*;
import Keybord.Controler.*;
import Keybord.Model.*;

public class PitchModGraph extends JComponent
{
	//on staock les valeur entre 0 et 127
	private int mod,pitch;
	final int HEIGHT=160;

	public PitchModGraph(SynthModel synthModel)
	{
		this.mod = HEIGHT/2;
		this.pitch =HEIGHT/2;
		this.setPreferredSize(new Dimension(HEIGHT,HEIGHT));
		this.addMouseMotionListener(new PitchModGraphControl(this,synthModel));
		this.addMouseListener(new PitchModGraphControlMouse(this,synthModel));
	}
	public void setMod(int v)
	{
		this.mod=(v*this.getHeight())/127;
		this.repaint();
	}
	public void setModMinus(int v)
	{
		this.mod = ((127+v)*HEIGHT)/254;
		this.repaint();
	}
	public void setModPlus(int v)
	{
		this.mod = ((127-v)*HEIGHT)/254;
		this.repaint();
	}
	public void setPitch(int v)
	{
		this.pitch = (v*HEIGHT)/16384;
		this.repaint();
	}
	//on dessine au coordonées (pitch,mod);
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D secondPinceau = (Graphics2D)g.create();
	    if (this.isOpaque()) {
	      secondPinceau.setColor(this.getBackground());
	      secondPinceau.fillRect(0, 0, HEIGHT, HEIGHT);
	    }

	    secondPinceau.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON); 
		secondPinceau.setColor(Color.BLACK);
		secondPinceau.fillRect(0,0,HEIGHT,HEIGHT);
		//on dessine la grille

		secondPinceau.setColor(Color.GRAY);
		for(int i=0;i<HEIGHT;i+=10)
		{
			for(int j = 0;j<HEIGHT;j+=10)
			{
				secondPinceau.drawLine(0,i,HEIGHT,i);
				secondPinceau.drawLine(i,0,i,HEIGHT);
			}
		}
		//couleur pour ligne du milieu
		secondPinceau.setColor(Color.BLUE);
		secondPinceau.drawLine(0,HEIGHT/2,HEIGHT,HEIGHT/2);
		secondPinceau.drawLine(HEIGHT/2,0,HEIGHT/2,HEIGHT);
		
		//ensuite on dessine le cercle
		secondPinceau.setColor(Color.RED);
		secondPinceau.fillArc(this.pitch-5,this.mod-5,10,10,0,360);
	}
}