package Main.Model;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.util.Vector;
import java.awt.geom.Line2D;
import Main.View.*;
import Main.Controler.*;


public class Son implements Runnable
{
	private final int BUFL = 128000;
	private File soundfile;
	private AudioInputStream stream;
	private AudioFormat format;
	private SourceDataLine source;
    private Clip clip;
    private long framePosition =0;
    private boolean play;
    private boolean stop;
    private Control[] controls;
    private Cursor cursor;
    private ClipControler clipcontroler;
    
	public Son(File f)
	{
		soundfile = f;
		try
		{
			this.stream = AudioSystem.getAudioInputStream(soundfile);
			this.format = stream.getFormat();
		}
		catch(Exception e)
		{
			System.out.println("Ce type de fichier n'est pas supporté.");
		}
	}

	public Vector createWaveForm(Dimension d) {
		byte[] audioBytes = null;
		Vector lines = new Vector();
        lines.removeAllElements();  // clear the old vector

        //AudioFormat format = this.stream.getFormat();
        if (audioBytes == null) {
            try {
                audioBytes = new byte[
                    (int) (this.stream.getFrameLength() 
                    * this.format.getFrameSize())];
                this.stream.read(audioBytes);
            } catch (Exception ex) { 
                ex.printStackTrace();
                return null; 
            }
        }

        //Dimension d = getSize();
        int w = d.width;
        int h = d.height-15;
        int[] audioData = null;
        int nb16=0,nb8=0;
        if (format.getSampleSizeInBits() == 16) {
             int longueurSample = audioBytes.length / 2;
             System.out.println("longueur="+longueurSample);
             audioData = new int[longueurSample];
             if (format.isBigEndian()) {
                for (int i = 0; i < longueurSample; i++) {

                     int MSB = (int) audioBytes[2*i];
                     
                     int LSB = (int) audioBytes[2*i+1];
                     audioData[i] = MSB << 8 | (255 & LSB);
                     nb16++;
                 }
             } else {
                 for (int i = 0; i < longueurSample; i++) {
                     
                     int LSB = (int) audioBytes[2*i];
                     
                     int MSB = (int) audioBytes[2*i+1];
                     audioData[i] = MSB << 8 | (255 & LSB);
                     nb16++;
                 }
             }
         } else if (format.getSampleSizeInBits() == 8) {
             int longueurSample = audioBytes.length;
             audioData = new int[longueurSample];
             System.out.println("longueur"+longueurSample);
             if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                 for (int i = 0; i < audioBytes.length; i++) {
                     audioData[i] = audioBytes[i];
                     nb8++;
                 }
             } else {
                 for (int i = 0; i < audioBytes.length; i++) {
                     audioData[i] = audioBytes[i] - 128;
                     nb8++;
                 }
             }
        }else if (format.getSampleSizeInBits() == 24) {
             int longueurSample = audioBytes.length / 3;
             System.out.println("longueur"+longueurSample);
             audioData = new int[longueurSample];
             if (format.isBigEndian()) {
                for (int i = 0; i < longueurSample; i++) {
                    int o1,o2,o3;
                    o3= audioBytes[3*i];
                    o2= audioBytes[3*i+1];
                    o1= audioBytes[3*i+2];
                    audioData[i] = o3<<16 | o2 << 8 |o1;
                 }
             } else {
                 for (int i = 0; i < longueurSample; i++) {
                     int o1,o2,o3;
                    o3= audioBytes[3*i+2];
                    o2= audioBytes[3*i+1];
                    o1= audioBytes[3*i];
                    audioData[i] = o3<<16 | o2 << 8 |o1;
                 }
             }
         }
           
        int frames_per_pixel = audioBytes.length / format.getFrameSize()/w;
        byte my_byte = 0;
        double y_last = 0,y_new=0;
        int nbLine=0;
        int numChannels = format.getChannels();
        for (double x = 0; x < w && audioData != null; x++) {
            int idx = (int) (frames_per_pixel * numChannels * x);
            if (format.getSampleSizeInBits() == 8) {
                 my_byte = (byte) audioData[idx];
            } else if(format.getSampleSizeInBits() == 16){
                 my_byte = (byte) (audioData[idx]>>8);
            }
            else
            {
            	 my_byte=(byte)(audioData[idx]>>16);
            }
            y_new = (double) (h * (128 - my_byte) / 256);
            nbLine++;
            lines.add(new Line2D.Double(x, y_last, x, y_new));
            y_last = y_new;
        }
        System.out.println("nombre ligne = "+nbLine+" nb16="+nb16+" nb8="+nb8+" sampleSizeinBits="+format.getSampleSizeInBits());
        return lines;
    }
    /*teste de lecture avec Clip*/
    
    public void run()
    {
        this.play = true;
        this.stop = false;
        try
        {
            Line.Info linfo = new Line.Info(Clip.class);
            Line line = AudioSystem.getLine(linfo);
            this.clip = (Clip) line;
            //clip.addLineListener(this);
            this.clip.open(this.stream);
            clip.setMicrosecondPosition(this.framePosition);
            this.clip.start();
        }catch(LineUnavailableException e)
        {
            System.out.println("Line unavailable");
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public File getFile()
    {
        return this.soundfile;
    }
    public Clip getClip()
    {
        return this.clip;
    }
    
    public Control[] getControls()
    {
        if(this.clip==null)
        {
            try
            {
                openSound();

            }catch(LineUnavailableException e)
            {
                System.out.println("Line unavailable");
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return this.controls;
    }
    
    public void openSound() throws LineUnavailableException, IOException
    {
        Line.Info linfo = new Line.Info(Clip.class);
        Line line = AudioSystem.getLine(linfo);
        this.clip = (Clip) line;
        //clip.addLineListener(this);
        this.clip.open(this.stream);
        this.controls = this.clip.getControls();
        this.framePosition= 0;
    }
    
    public void playSound()
    {
        try
        {
            if(this.clip==null)
                openSound();
            if(this.clipcontroler==null)
                this.clipcontroler = new ClipControler(this.cursor,this.clip.getMicrosecondLength());
            this.clip.setMicrosecondPosition(this.framePosition);
            this.clip.start();
            this.clipcontroler.setRunning(false);
            this.clipcontroler.setRunning(true);
            (new Thread(this.clipcontroler)).start();
            
        }catch(LineUnavailableException e)
        {
            System.out.println("Line unavailable");
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void arreterSon()
    {
        this.play = false;
        this.stop = true;
        this.clip.stop();
        this.clip.drain();
        this.framePosition =0;
        this.cursor.changeCursor(0);
        this.clipcontroler.setRunning(false);
    }
    
    public void pauseSon()
    {
        this.framePosition = this.clip.getMicrosecondPosition();
        this.clip.stop();
        this.clip.drain();
        this.play = false;
        this.stop = false;
        this.clipcontroler.setRunning(false);
    }
    
    public void changerEffet(Control c, int value)
    {
        
        if(this.clip.isControlSupported(c.getType()))
        {
            System.out.println("Here hopefully");

            if(c instanceof FloatControl)
            {
                ((FloatControl)this.clip.getControl(c.getType())).setValue((float)value);
                this.clip.start();
            }
            else if(c instanceof BooleanControl)
            {
                ((BooleanControl)this.clip.getControl(c.getType())).setValue((new Integer(value)).equals(1));
            }
        } 
        
        /*FloatControl gainControl =
        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
        clip.start();*/
    }

    public void setCursor(Cursor c)
    {
        this.cursor = c;
    }
    
}
