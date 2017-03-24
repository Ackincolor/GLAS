package Main.View;
import java.awt.*;
import javax.swing.*;
import Main.Controler.*;
import Main.Model.*;
import java.io.*;

public class Playback extends JPanel
{
    public GridBagConstraints constraints;
	private PlaybackControl playcontroler;
    
    public static final String PLAY = "\u25B6";
    public static final String PAUSE = "\u23F8";
    public static final String STOP = "\u25B4";

    private JButton stop,pause,play;
    private boolean everUsed=false;
    
 
    public Playback()
    {
        setLayout(new GridBagLayout());
        
        JSlider volume = new JSlider(SwingConstants.HORIZONTAL);
        
        this.stop = new JButton(Playback.STOP);
        this.pause = new JButton(Playback.PAUSE);
        this.play = new JButton(Playback.PLAY);
        
        this.stop.setOpaque(false);
        this.pause.setOpaque(false);
        this.play.setOpaque(false);
        
        JButton sauvegarde = new JButton("Sauvegarder");
        JButton ouvrir = new JButton("Ouvrir");
        JButton piano = new JButton("Piano");
        piano.addActionListener(new AfficherPianoControler());
        
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 0;
        add(stop,c);
        c.gridx = 1;
        add(pause,c);
        c.gridx = 2;
        add(play,c);
        c.gridwidth = 6;
        c.gridheight = 1;
        c.gridx=3;
        add(volume,c);
        c.gridwidth = 2;
        c.gridy = 2;
        add(sauvegarde,c);
        c.gridx = 5;
        add(ouvrir,c);
        c.gridx = 7;
        add(piano,c);
        
        this.constraints = new GridBagConstraints();
        this.constraints.gridy = 4;
        this.constraints.gridx = 0;
        this.constraints.gridwidth = 4;
        this.constraints.gridheight = 1;
        this.constraints.anchor = GridBagConstraints.LAST_LINE_START;
        this.constraints.insets = new Insets(2,2,2,2);

    }
    public void setSound(Son son)
    {
        this.playcontroler = new PlaybackControl(new Son(son.getFile()));
        if(!this.everUsed)
        {
            this.play.addActionListener(playcontroler);
            this.stop.addActionListener(playcontroler);
            this.pause.addActionListener(playcontroler);
        }
        this.everUsed = true;
    }
	public PlaybackControl getPlayController()
	{
		return this.playcontroler;
	}

}
