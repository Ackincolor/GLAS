package Keybord.Controler;
import Keybord.Model.*;
import Keybord.View.*;
import java.util.*;
import java.awt.event.*;
public class KeyControlerClavier implements KeyListener
{
	private SynthModel synthModel;
	private PianoView vue;
	private boolean espace=false;
	private boolean ctrGauche=false;
	private boolean shift=false;
	private int current=0;
	private ArrayList<Key> notesBlanche = new ArrayList<Key>();
	private ArrayList<Key> notesNoire = new ArrayList<Key>();
	public KeyControlerClavier(PianoView vue,SynthModel synthModel,ArrayList<Key> notesBlanche,ArrayList<Key> notesNoire)
	{
		this.vue = vue;
		this.synthModel = synthModel;
		this.notesNoire = notesNoire;
		this.notesBlanche = notesBlanche;
	}
	public void keyPressed(KeyEvent e)
	{
		//System.out.println("appui");
		int touche=0;
		if(e.getKeyCode()==KeyEvent.VK_CONTROL)
			this.ctrGauche=true;
		else if(e.getKeyCode()==KeyEvent.VK_SHIFT)
			this.shift=true;
		else if(e.getKeyCode()==KeyEvent.VK_SPACE)
			this.espace=true;
		else
		{
			touche = getTouche(e);
			if(this.current!=touche)
			{
				if(touche!=-1)
				{
					this.synthModel.noteOnModel(touche);
					getKey(touche).setOn();
					this.vue.repaint();
				}
				this.current = touche;
			}
		}
	}
	public void keyReleased(KeyEvent e)
	{
		int touche=0;
		if(e.getKeyCode()==KeyEvent.VK_CONTROL)
		{
			this.ctrGauche=false;
		}
		else if(e.getKeyCode()==KeyEvent.VK_SHIFT)
		{
			this.shift=false;
		}
		else if(e.getKeyCode()==KeyEvent.VK_SPACE)
		{
			this.espace=false;
		}
		else
		{
			touche = getTouche(e);
			if(touche!=-1)
			{
				this.synthModel.noteOffModel(touche);
				getKey(touche).setOff();
				this.vue.repaint();
				this.current = 0;
			}
		}
	}
	public void setAllof()
	{
		//parcour de toute les touches pour setoff();
		for(Key key : this.notesBlanche)
			this.synthModel.noteOffModel(key.getNum());
		for(Key key : this.notesNoire)
			this.synthModel.noteOffModel(key.getNum());
		this.vue.repaint();
	}
	public int getNumeroNote(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_Q)
			return 0;
		else if(e.getKeyCode()==KeyEvent.VK_Z)
			return 1;
		else if(e.getKeyCode()==KeyEvent.VK_S)
			return 2;
		else if(e.getKeyCode()==KeyEvent.VK_E)
			return 3;
		else if(e.getKeyCode()==KeyEvent.VK_D)
			return 4;
		else if(e.getKeyCode()==KeyEvent.VK_F)
			return 5;
		else if(e.getKeyCode()==KeyEvent.VK_T)
			return 6;
		else if(e.getKeyCode()==KeyEvent.VK_G)
			return 7;
		else if(e.getKeyCode()==KeyEvent.VK_Y)
			return 8;
		else if(e.getKeyCode()==KeyEvent.VK_H)
			return 9;
		else if(e.getKeyCode()==KeyEvent.VK_U)
			return 10;
		else if(e.getKeyCode()==KeyEvent.VK_J)
			return 11;
		else return -1;
	}
	public int getTouche(KeyEvent e)
	{
		//on a besoin de : qzsedftgyhuj
		int touche=getNumeroNote(e);
		int octave = this.synthModel.getOctave();
		if(octave==-1)
		{
			if(touche!=-1)
			{//rien,Premier Octave
				if(!espace && !ctrGauche && !shift)
					touche+=24;
				//ctrGauche  = Deuxieme Octave
				else if(!espace && ctrGauche && !shift)
					touche+=36;
				//espace = Troisieme Octave
				else if(espace && !ctrGauche && !shift)
					touche+=48;
				//shift = Quatrieme Octave
				else if(!espace && !ctrGauche && shift)
					touche+=60;
				//ctrGauche +espace = 5eme Octave
				else if(espace && ctrGauche && !shift)
					touche+=72;
				//shift+espace = 6eme Octave
				else if(espace && !ctrGauche && shift)
					touche+=84;
				else
					System.out.println("Aucune Combinaison!");
				return touche;
			}
			else return -1;
		}
		else
		{
			return touche+(octave*12);
		}
	}
	public void keyTyped(KeyEvent e)
	{
		//System.out.println("touche");
	}
	public PianoView getView()
	{
		return this.vue;
	}
	public Key getKey(int num)
	{
		//System.out.println("num touche :"+num);
		for(Key note : this.notesNoire)
		{
			if(note.getNum()==num)
			{
				return note;
			}
		}
		for(Key note : this.notesBlanche)
		{
			if(note.getNum()==num)
			{
				return note;
			}
		}
		return null;
	}
}