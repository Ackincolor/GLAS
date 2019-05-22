package Keybord.View;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class GraphicsSon extends JComponent
{
	private int attack,hauteur,release;
	public GraphicsSon()
	{
		super();
		this.attack = release = 64;
		this.hauteur =50;
		this.setPreferredSize(new Dimension(280,100));

	}
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D secondPinceau = (Graphics2D)g.create();
	    if (this.isOpaque()) {
	      secondPinceau.setColor(this.getBackground());
	      secondPinceau.fillRect(0, 0, this.getWidth(), this.getHeight()-1);
	    }

	    secondPinceau.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON); 
		secondPinceau.setColor(Color.BLACK);
		secondPinceau.fillRect(0,0,this.getWidth(),this.getHeight());

		secondPinceau.setColor(Color.YELLOW);
		//debut Graphic (0,0) donc 0 et -this.height;
		// ensuit on va de (0,-this.height) a (attack,hauteur)
		//secondPinceau.drawLine(5,this.getHeight()-1-1,this.attack,this.hauteur);
		//puis (attack,hauteur) vers (release+attack,-this.height)
		//secondPinceau.drawLine(this.attack,this.hauteur,5+this.release+this.attack,this.getHeight()-1);
		//dessin avec courbe de Bezier
		Path2D.Double path = new Path2D.Double();
		path.moveTo(5,this.getHeight()-1);
		path.curveTo(this.attack/2.0,this.getHeight()-1,this.attack/2.0,this.hauteur,this.attack,this.hauteur);
		path.curveTo((this.release/2.0)+this.attack,this.hauteur,(this.release/2.0)+this.attack,this.getHeight()-1,5+this.release+this.attack,this.getHeight()-1);
		secondPinceau.draw(path);
	}
	public void setAttack(int a)
	{
		this.attack = a+10;
	}
	public void sethauteur(int a)
	{
		this.hauteur = a+10;
	}
	public void setRelease(int a)
	{
		this.release = a+10;
	}
}