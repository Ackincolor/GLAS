package Keybord.View;

import javax.swing.*;
import java.awt.Rectangle;
import java.awt.Graphics;
import javax.sound.midi.*;
public class Key extends Rectangle
{
	private int num;
	private int longueur = 400;
	private boolean on = false;
	public int posX,posY,largeur,hauteur;
	public Key(int posX,int posY,int largeur,int hauteur,int num)
	{
		super(posX,posY,largeur,hauteur);
		//this.getGraphics().drawString(this.sm_astrKeyNames[num%12],posX+largeur/2,posY+hauteur/2);
		this.num = num;
		this.posX = posX;
		this.posY = posY;
		this.largeur = largeur;
		this.hauteur = hauteur;
	}
	public boolean isNoteOn()
	{
		//test si touche cliquée 
		return this.on;
	}
	public int getNum()
	{
		return this.num;
	}
	public void setOn()
	{
		this.on = true;
		//this.mc[channel].noteOn(this.num,this.longueur);
	}
	public void setOff()
	{
		this.on=false;
		//this.mc[channel].noteOff(this.num,this.longueur);
	}
	
}