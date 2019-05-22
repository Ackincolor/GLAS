package Main.Controler;
import Main.View.Cursor;
import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;

public class ClipControler implements Runnable
{
    private Cursor cursor;
    private boolean running;
    private int time;
    
    public ClipControler(Cursor c, float length)
    {
        this.cursor = c;
        this.running = true;
        int width = c.getParent().getWidth();
        float microlength = (float)length*0.001f;
        this.time = (int) (microlength/(float)width);
    }
    
    public void setRunning(boolean b)
    {
        this.running = b;
    }
    
    public void run()
    {
        try
        {
            while(this.running)
            {
                Thread.sleep(this.time);
                this.cursor.increment();
            }

        }
        catch(InterruptedException e)
        {
            //System.out.println("interrupted!");
        }
    }
}