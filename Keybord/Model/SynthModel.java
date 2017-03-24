package Keybord.Model;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat;
import com.sun.media.sound.AudioSynthesizer;

public class SynthModel 
{
	private Synthesizer synth;
	private Sequence sequence;
	private MidiChannel[] midiChannel;
	private MidiChannel currentChannel;
	private Instrument[] instruments;
	private int currentInstru = 0;
	private int channel = 0;
	private int velocity = 100;
	//variable pour enregistrer la sequence midi
	private Sequencer sequencer;
	private boolean enregistrement = false;
	private Track enregistrementSequence;
	private long debutRecord;
	private long startTime;
	final int PROGRAM = 192;
    final int NOTEON = 144;
    final int NOTEOFF = 128;
    final int SUSTAIN = 64;
    final int REVERB = 91;
    final int ON = 0, OFF = 1;
	public SynthModel()
	{
		try
		{
			this.synth = MidiSystem.getSynthesizer();
			this.sequence = new Sequence(Sequence.PPQ, 10);
			this.synth.open();
			this.sequencer = MidiSystem.getSequencer();
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			return;
		}
		if(this.synth!=null)
		{
			this.midiChannel = this.synth.getChannels(); 
			this.instruments = synth.getDefaultSoundbank().getInstruments();
			this.synth.loadInstrument(instruments[currentInstru]);
			this.currentChannel = this.midiChannel[1];
		}
	}
	public MidiChannel[] getMidiChannel()
	{
		return this.midiChannel;
	}
	public Synthesizer getSynth()
	{
		return this.synth;
	}
	public void setVelocity(int velo)
	{
		this.velocity = velo;
	}
	public Instrument[] getAllInstru()
	{
		return this.instruments;
	}
	public int getIntInstru()
	{
		return this.currentInstru;
	}
	public void noteOnModel(int note)
	{
		this.currentChannel.noteOn(note,this.velocity);
		if (this.isRecording()) {
            ajoutEvenement(NOTEON, note);
        }
	}
	public void noteOffModel(int note)
	{
		this.currentChannel.noteOff(note,this.velocity);
		if (this.isRecording()) {
            ajoutEvenement(NOTEOFF, note);
        }
	}
	public MidiChannel getCurrentChannel()
	{
		return this.currentChannel;
	}
	public void setChannel(int c)
	{
		this.channel = c;
		this.currentChannel = this.midiChannel[c];
	}
	public void setInstrument(int numInstru)
	{
		if(numInstru<0 || numInstru>this.instruments.length)
		{
			System.out.println("Erreur  : Instrument inexistant");
			return;
		}
		else 
		{
			if(this.synth!=null)
			{
				this.synth.loadInstrument(this.instruments[numInstru]);
				this.currentChannel.programChange(numInstru);
				if (this.isRecording()) {
                	ajoutEvenement(PROGRAM, numInstru);
            }
				this.currentInstru = numInstru;
			}
			else
			{
				System.out.println("Erreur : Pas de synth");
				return;
			}
		}
	}
	public boolean isRecording()
	{
		return this.enregistrement;
	}
	public void startRecord()
	{
		this.enregistrementSequence = this.sequence.createTrack();
		this.startTime = System.currentTimeMillis();
		this.enregistrement = true;
		ajoutEvenement(PROGRAM,this.currentInstru);
	}
	public void stopRecord()
	{
		this.enregistrement = false;
	}
	public void playSequence()
	{
		try {
            this.sequencer.open();
            this.sequencer.setSequence(this.sequence);
        } catch (Exception ex) { ex.printStackTrace(); }
        this.sequencer.start();
	}
	public void pauseSequence()
	{
		this.sequencer.stop();
		this.sequencer.close();
	}
	public void ajoutEvenement(int type, int num) {
        ShortMessage message = new ShortMessage();
        try {
            long millis = System.currentTimeMillis() - this.startTime;
            long tick = millis * this.sequence.getResolution() / 500;
            message.setMessage(type+this.channel, num, this.velocity); 
            MidiEvent event = new MidiEvent(message, tick);
            enregistrementSequence.add(event);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    public void saveMidiFile(File file) {
    try {
        int[] fileTypes = MidiSystem.getMidiFileTypes(sequence);
        if (fileTypes.length == 0) {
            System.out.println("Can't save sequence");
        } else {
            if (MidiSystem.write(sequence, fileTypes[0], file) == -1) {
                throw new IOException("Problems writing to file");
            } 
        }
	    } catch (SecurityException ex) { 
	        //JavaSound.showInfoDialog();
	    } catch (Exception ex) { 
	        ex.printStackTrace(); 
	    }
	}
	public void saveWavFile(File file)
	{
		try{
			AudioSynthesizer synth2 = findAudioSynthesizer();
			System.out.println("debut Save");
			//AudioFormat format = new AudioFormat(96000, 24, 2, true, false);
			AudioFormat format = new AudioFormat(96000, 16, 2, true, false);
	        Map<String, Object> p = new HashMap<String, Object>();
	        p.put("interpolation", "sinc");
	        p.put("max polyphony", "1024");
	        AudioInputStream stream = synth2.openStream(format, p);

	        // Play Sequence into AudioSynthesizer Receiver.
	        double total = send(this.sequence, synth2.getReceiver());

	        // Calculate how long the WAVE file needs to be.
	        long len = (long) (stream.getFormat().getFrameRate() * (total + 4));
	        stream = new AudioInputStream(stream, stream.getFormat(), len);

	        // Write WAVE file to disk.
	        try{
	        	AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
	        }catch(IOException ex)
	        {
	        	ex.printStackTrace();
	        }
	    }catch(MidiUnavailableException e)
	    {
	    	e.printStackTrace();
	    }
	}
	private double send(Sequence seq, Receiver recv) 
	{
		float divtype = seq.getDivisionType();
		assert (seq.getDivisionType() == Sequence.PPQ);
		Track[] tracks = seq.getTracks();
		int[] trackspos = new int[tracks.length];
		int mpq = 500000;
		int seqres = seq.getResolution();
		long lasttick = 0;
		long curtime = 0;
		while (true) {
			MidiEvent selevent = null;
			int seltrack = -1;
			for (int i = 0; i < tracks.length; i++) {
				int trackpos = trackspos[i];
				Track track = tracks[i];
				if (trackpos < track.size()) {
					MidiEvent event = track.get(trackpos);
					if (selevent == null
							|| event.getTick() < selevent.getTick()) {
						selevent = event;
						seltrack = i;
					}
				}
			}
			if (seltrack == -1)
				break;
			trackspos[seltrack]++;
			long tick = selevent.getTick();
			if (divtype == Sequence.PPQ)
				curtime += ((tick - lasttick) * mpq) / seqres;
			else
				curtime = (long) ((tick * 1000000.0 * divtype) / seqres);
			lasttick = tick;
			MidiMessage msg = selevent.getMessage();
			if (msg instanceof MetaMessage) {
				if (divtype == Sequence.PPQ)
					if (((MetaMessage) msg).getType() == 0x51) {
						byte[] data = ((MetaMessage) msg).getData();
						mpq = ((data[0] & 0xff) << 16)
								| ((data[1] & 0xff) << 8) | (data[2] & 0xff);
					}
			} else {
				if (recv != null)
					recv.send(msg, curtime);
			}
		}
		return curtime / 1000000.0;
	}
	private AudioSynthesizer findAudioSynthesizer() throws MidiUnavailableException 
	{
		// First check if default synthesizer is AudioSynthesizer.
		Synthesizer synth = MidiSystem.getSynthesizer();
		if (synth instanceof AudioSynthesizer) {
			return (AudioSynthesizer)synth;
		}

		// If default synthesizer is not AudioSynthesizer, check others.
		MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < midiDeviceInfo.length; i++) {
			MidiDevice dev = MidiSystem.getMidiDevice(midiDeviceInfo[i]);
			if (dev instanceof AudioSynthesizer) {
				return (AudioSynthesizer) dev;
			}
		}

		// No AudioSynthesizer was found, return null.
		return null;
	}
}