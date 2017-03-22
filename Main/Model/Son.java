package Main.Model;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.util.Vector;
import java.awt.geom.Line2D;

public class Son implements Runnable
{
	private final int BUFL = 128000;
	private File soundfile;
	private AudioInputStream stream;
	private AudioFormat format;
	private SourceDataLine source;
    
    private boolean play;
    private boolean stop;

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
                 y_new = (double) (h * (128 - my_byte) / 256);
            } else if(format.getSampleSizeInBits() == 16){
                 my_byte = (byte) (audioData[idx]>>8);
                 y_new = (double) (h * (128 - my_byte) / 256);
            }
            else
            {
            	 my_byte=(byte)(audioData[idx]>>16);
                 y_new = (double) (h * (128 - my_byte) / 256);
            }
            
            nbLine++;
            lines.add(new Line2D.Double(x, y_last, x, y_new));
            y_last = y_new;
        }
        System.out.println("nombre ligne = "+nbLine+" nb16="+nb16+" nb8="+nb8+" sampleSizeinBits="+format.getSampleSizeInBits());
        return lines;
    }
    /*teste de lecture avec Clip*/
    /*
    public void run()
    {
        this.play = true;
        this.stop = false;
        try
        {
            Line.Info linfo = new Line.Info(Clip.class);
            Line line = AudioSystem.getLine(linfo);
            Clip clip = (Clip) line;
            //clip.addLineListener(this);
            clip.open(this.stream);
            clip.start();
        }catch(LineUnavailableException e)
        {
            System.out.println("Line unavailable");
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }*/
    
	public void run()
	{
        this.play = true;
        this.stop = false;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.format);
		try
		{
            if(this.source != null)
            {
                this.source.close();
            }
			this.source = (SourceDataLine) AudioSystem.getLine(info);
			this.source.open(format);
			
		}
		catch(LineUnavailableException e)
		{
			System.out.println("Line unavailable");
		}

		this.source.start();

		int bytes = 0;

		byte[] buf = new byte[BUFL];
		while(bytes != -1 && this.play)
		{	
			try
			{
				bytes = stream.read(buf, 0, buf.length);
			}
			catch(IOException e)
			{
				System.out.println("Problème de lecture!");
			}
			if(bytes >= 0)
			{
				int byteswritten = this.source.write(buf, 0, bytes);
                System.out.println("envoie du flux");
			}
		}
	
        if(stop)
        {
            this.source.drain();
        }
        
	}
    
    public void arreterSon()
    {
        this.play = false;
        this.stop = true;
    }
    
    public void pauseSon()
    {
        this.play = false;
        this.stop = false;
    }

}
