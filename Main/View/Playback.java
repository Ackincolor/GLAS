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

    private JButton stop,pause,play,piano;
    private boolean everUsed=false;
    
 
    public Playback()
    {
        setLayout(new GridBagLayout());
        
        JSlider volume = new JSlider(SwingConstants.HORIZONTAL);
        
        this.stop = new JButton(new ImageIcon("Icons/stop.png"));
        this.pause = new JButton(new ImageIcon("Icons/pause.png"));
        this.play = new JButton(new ImageIcon("Icons/play.png"));
        this.piano = new JButton(new ImageIcon("Icons/piano.png"));

        this.stop.setName(Playback.STOP);
        this.pause.setName(Playback.PAUSE);
        this.play.setName(Playback.PLAY);
        
        this.stop.setOpaque(false);
        this.pause.setOpaque(false);
        this.play.setOpaque(false);
        this.piano.setOpaque(false);

        this.piano.addActionListener(new AfficherPianoControler());
        
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 0;
        this.add(stop,c);
        c.gridx = 1;
        this.add(pause,c);
        c.gridx = 2;
        this.add(play,c);
        c.gridwidth = 2;
        c.gridx = 3;
        this.add(piano,c);
        /*c.gridwidth = 6;
        c.gridheight = 1;
        c.gridx=3;
        add(volume,c);
        c.gridwidth = 2;
        c.gridy = 2;
        add(sauvegarde,c);
        c.gridx = 5;
        add(ouvrir,c);
        c.gridx = 7;
        add(piano,c);*/
        
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
        if(this.playcontroler==null)
            this.playcontroler = new PlaybackControl(son);
        else
            this.playcontroler.setSon(son);
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
