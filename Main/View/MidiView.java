package Main.View;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.io.*;

import java.util.ArrayList;

import javax.sound.midi.*;

public class MidiView extends JPanel implements SoundView
{
	public GridBagConstraints constraints;
	private int timeDecal;
	private FileInputStream file;
	private Sequencer sequencer;
	private Track[] track=null;
    private final int MINIMUCOUPLEX =20;
    private String[] sm_astrKeyNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public MidiView()
	{
		//contraintes
        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.BOTH;
        this.constraints.gridy = 0;
        this.constraints.gridx = 0;
        this.constraints.weightx = 5.0;
        this.constraints.weighty = 5.0;
        this.constraints.gridwidth = 4;
        this.constraints.gridheight = 4;
        this.constraints.insets = new Insets(2,2,2,2);

        ///this.setSize(new Dimension(1200,500));
		//ouverture du fichier Midi
        //ouverture de la sequence

	}
    public void draw(File f)
    {
        this.setBackground(Color.RED);
         try
        {
            this.sequencer = MidiSystem.getSequencer();
            this.sequencer.setSequence(new FileInputStream(f));
            //this.sequencer.open();            
            //this.sequencer.start();
        }catch(IOException e)
        {
            System.out.println("fichier introuvable");
        }catch(InvalidMidiDataException e)
        {
            System.out.println("Mauvais fichier Midi");
        }catch(MidiUnavailableException e)
        {
            System.out.println("Pas de midi ICI !!");
        }
        this.track = this.sequencer.getSequence().getTracks();
        this.repaint();
    }
	public void paintComponent(Graphics g)
	{

        ArrayList<CoupleNote> listeDebut = new ArrayList<CoupleNote>();
        ArrayList<CoupleNote> listeDebutEtFin = new ArrayList<CoupleNote>();
        if(this.track!=null)
        {
    		Graphics2D secondPinceau = (Graphics2D)g.create();
    	    if (this.isOpaque()) {
    	      secondPinceau.setColor(this.getBackground());
    	      secondPinceau.fillRect(0, 0, this.getWidth(), this.getHeight()-1);
    	    }

    	    secondPinceau.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

            secondPinceau.setColor(new Color(29, 91, 130));
    		secondPinceau.fillRect(0,0,this.getWidth(),this.getHeight());
    		// pour chaque Track on affiche des points.
    		secondPinceau.setColor(new Color(36, 176, 226));
            //this.setPreferedSize(new Dimension(this.track.length*MINIMUCOUPLEX,this.getHeight()));
            this.setSize(new Dimension((int)this.track[0].ticks()*20,800));
            this.setPreferredSize(new Dimension((int)this.track[0].ticks()*15,800));
            for(int i=0;i<this.track.length;i++)
            {
            	//System.out.println(this.track[i].ticks()+"pour "+i);
            	int y =0;
            	int x=0;
                int note = 0;
            	int xprime=0;
            	for(int j=0;j<this.track[i].size();j++)
            	{
                    if(this.track[i].ticks()>0)
            		  x = j*(int)(this.getWidth()/this.track[i].size());
                    else
                        x+=20;
                    //recuperation du message
                    MidiMessage msg = this.track[i].get(j).getMessage();
                    if(msg instanceof ShortMessage)
                    {
                        note = (int)((ShortMessage)msg).getData1();
                        y=note*this.getHeight()/120;
                        if(((ShortMessage)msg).getCommand()==0x80)
                        {
                            for(CoupleNote couple : listeDebut)
                            {
                                if(couple.getNote()==note)
                                {
                                    couple.setFin(x);
                                    //System.out.println("setFIN "+y);
                                }
                                listeDebutEtFin.add(couple);
                                //listeDebut.remove(couple);
                            }
                        }
                        else if(((ShortMessage)msg).getCommand()==0x90)
                        {
                            //si c'est note on
                            //on crée un objet CoupleNote
                            listeDebut.add(new CoupleNote(note,y,x));
                            /*
                            secondPinceau.setColor(Color.WHITE);
                            xprime = x+(10);
                            secondPinceau.drawLine(x,y,xprime,y);*/
                        }
                        else if(((ShortMessage)msg).getCommand()==0xb0)
                        {
                            secondPinceau.setColor(Color.RED);
                            secondPinceau.drawOval(x,y,2,2);
                        }
            	    }
                    else
                    {
                        secondPinceau.setColor(Color.RED);
                        secondPinceau.drawOval(x,y,2,2);
                    }
                }	
            }
            for(CoupleNote couple : listeDebutEtFin)
            {
                //secondPinceau.setColor(Color.WHITE);
                this.drawEvent(couple,secondPinceau);
                //secondPinceau.drawLine(coord[0],coord[1],coord[2],coord[3]);
            }
            //fin if
        }

	}
    private void drawEvent(CoupleNote couple,Graphics2D secondPinceau)
    {
        int[] coord = couple.getCoord();
        secondPinceau.setColor(new Color(36, 176, 226));
        secondPinceau.fillRoundRect(coord[0],coord[1],coord[2]-coord[0],15,15,15);
        secondPinceau.fillRoundRect(coord[0],coord[1],coord[2]-coord[0],15,15,15);
        secondPinceau.setColor(Color.BLACK);
        secondPinceau.drawString(this.sm_astrKeyNames[couple.getNote()%12],coord[0]+2,coord[1]+11);
    }
    private class CoupleNote
    {
        private int note,y,debut,fin;
        public CoupleNote(int note,int y,int debut)
        {
            this.y = y;
            this.note = note;
            this.debut = debut;
            this.fin=-1;
        }
        public void setFin(int fin)
        {
            if(this.fin==-1)
                this.fin = fin;
        }
        public int getNote()
        {
            return this.note;
        }
        public int[] getCoord()
        {
            int[] coord = {this.debut,this.y,this.fin,this.y};
            return coord;
        }
    }
}