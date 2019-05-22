package RythmBox.Model;

import java.util.Vector;
import java.awt.*;
import javax.sound.midi.*;
public class RythmModel
{
	private Vector data;
	private Synthesizer synth;
	private Sequence sequence;
	private Sequencer sequencer;
	private MidiChannel[] midiChannel;
	private MidiChannel currentChannel;
	private int currentInstru = 0;
	private float tempo;
	private boolean isPlaying = false;
	private Thread t;
	public RythmModel()
	{
    	try
		{
			this.synth = MidiSystem.getSynthesizer();
			this.sequence = new Sequence(Sequence.PPQ, 10);
			this.synth.open();
			this.sequencer = MidiSystem.getSequencer();
			
			//this.ctrlMIDI = new ControlerMIDI(this);
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			return;
		}
		if(this.synth!=null)
		{
			this.midiChannel = this.synth.getChannels();
			this.synth.loadInstrument(synth.getDefaultSoundbank().getInstruments()[currentInstru]);
			//channel 9 = drum kit
			this.currentChannel = this.midiChannel[9];
		}
		this.tempo = 120;
		this.t = new Thread(){
			public void run()
			{
				while(true)
				{
					playingBMP();
				}
			}
		};
	}
	public void setTempoInBPM(float v)
	{
		this.tempo = v;
		System.out.println("Tempo:"+v);
	}
	public void setIsPlayingToogle()
	{
		this.isPlaying = !this.isPlaying;
		if(this.isPlaying)
		{
			this.t = new Thread(){
			public void run()
			{
				while(true)
				{
					playingBMP();
				}
			}
		};
        	this.t.start();
		}
    	else
    		this.t.interrupt();
	}
	public void close()
	{
		this.t.interrupt();
		this.isPlaying = false;
		System.out.println("closing");
	}
	public void playingBMP()
	{
		int note = 42;
		int velocity = 127;		/*while(true)
		{
			
			long time = System.currentTimeMillis();
			if(time>=prochain && this.isPlaying)
			{
				//on joue le son
				this.currentChannel.noteOn(note,velocity);
				System.out.println("BMP!!!"+this.tempo);
				prochain = time+(long)(1000*(1/(this.tempo/60)));
				//Thread.sleep(proc);
				this.currentChannel.noteOff(note,velocity);
			}
			//System.out.println("infini");

		}*/
		try{
				if(this.isPlaying)
				{
					this.currentChannel.noteOn(note,velocity);
					Thread.sleep((long)((60*1000/(this.tempo))));
					System.out.println("play");

				}
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
	}
}