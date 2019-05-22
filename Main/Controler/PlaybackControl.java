package Main.Controler;
import Main.Model.*;
import Main.View.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.JButton;

public class PlaybackControl implements ActionListener
{
	private Son son;
    private Thread t;
    public PlaybackControl()
    {}
    public PlaybackControl(Son son)
    {
      this.son = son;  
    }

	@Override
	public void actionPerformed(ActionEvent e)
	{
        if(((JButton)e.getSource()).getName().equals(Playback.PLAY))
        {
            if(this.son != null)
            {
                this.son.playSound();
            }
            else{System.out.println("sonNull");}
            System.out.println("play");

        }
        else if(((JButton)e.getSource()).getName().equals(Playback.PAUSE))
        {
            if(this.son != null)
            {
                this.son.pauseSon();
            }
            System.out.println("pause");

        }
        else if(((JButton)e.getSource()).getName().equals(Playback.STOP))
        {
            if(this.son != null)
            {
                this.son.arreterSon();
            }
            System.out.println("stop");

        }
	}
    public void closeThread()
    {
        this.t.interrupt();
    }
	public void setSon(Son s)
	{	
        this.son = s;
	}
}
