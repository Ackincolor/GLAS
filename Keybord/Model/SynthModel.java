package Keybord.Model;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Keybord.Controler.*;
import Keybord.View.*;

import javax.swing.JTable;
import javax.swing.JTextArea;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat;
import com.sun.media.sound.AudioSynthesizer;
/**
*	Cette class represente le model d'un clavier.
*	Il peut jouer des notes,les enregistrer au format MIDI et exporter au format WAVE.
*
*/
public class SynthModel 
{
	private ControlerMIDI ctrlMIDI;
	private KeyControlerClavier keyControlerClavier = null;
	private PianoView pianoView = null;
	private OptionPanel optionPanel = null;
	private Synthesizer synth;
	private Sequence sequence;
	private MidiChannel[] midiChannel;
	private MidiChannel currentChannel;
	private Instrument[] instruments;
	private JTable listeInstruJTable;
	private JTextArea console;
	private int currentInstru = 0;
	private int channel = 0;
	private int velocity = 100;
	//variable pour enregistrer la sequence midi
	private Sequencer sequencer;
	private boolean enregistrement = false;
	private Track enregistrementSequence;
	private long debutRecord;
	private long startTime;
	private int octave = -1;
	final int PROGRAM = 192;
    final int NOTEON = 144;
    final int NOTEOFF = 128;
    final int PITCHBEND = 224;
    final int REVERB = 91;
    final int ATTACKTIME = 73;
    final int RELEASE = 72;
    final int ON = 0, OFF = 1;
    final int PAN = 10;
    final int BALANCE = 8;
    final int MODULATION = 1;
    final int MODULATION1 = 1;
    final int MODULATION2 = 2;
    final int TIMBRE = 71;
    final int CHVOL = 39;
    final int SUSTAIN = 64;
    private boolean isSustain = false; 
    /**
    *	Constructeur du Model
    */
	public SynthModel()
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
			this.instruments = synth.getDefaultSoundbank().getInstruments();
			this.synth.loadInstrument(instruments[currentInstru]);
			this.currentChannel = this.midiChannel[1];
		}
	}
	 /**
    *	setKeyControlerClavier
    *
    *	permet de definir le Controleur CLavier
	*
    *	@param kC le controler (KeyControlerClavier)
    *
    */
	public void setKeyControlerClavier(KeyControlerClavier kC)
	{
		this.keyControlerClavier = kC;
	}
	/**
	*	setPianoView 
	*
	*	Permet de faire reference a la vue du piano
	*
	*	@param p Vue du piano
	*/
	public void setPianoView(PianoView p)
	{
		this.pianoView = p;
	}
	/**
	*	setTextArea
	*
	*	Permet de faire reference a la zone de texte utilisépour la console.
	*
	*	@param t JTextArea qui est la console
	*/
	public void setTextArea(JTextArea t)
	{
		this.console = t;
		this.ctrlMIDI = new ControlerMIDI(this);
	}
	/**
	*	getConsole
	*
	*	Getter pour la console	
	*
	*
	*/
	public JTextArea getConsole()
	{
		return this.console;
	}
	/**
	*	setTable
	*
	*	fait reference a la liste des instruments.	
	*
	*	@param l JTable liste des instruments.
	*/
	public void setTable(JTable l)
	{
		this.listeInstruJTable = l;
	}
	/**
	*	getMidiChannel
	*
	*	Getter pour les Channels	
	*
	*
	*/
	public MidiChannel[] getMidiChannel()
	{
		return this.midiChannel;
	}
	/**
	*	getSynth
	*
	*	Getter pour le Synthesizer
	*
	*
	*/
	public Synthesizer getSynth()
	{
		return this.synth;
	}
	/**
	*	setVelocity
	*
	*	Setter pour la velocité
	*
	*	Aucun effet si un Clavier Physique est utilisé
*
*		@param velo int Veolictée
	*/
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
            ajoutEvenement(NOTEON, note,this.velocity);
        }
	}
	public void noteOffModel(int note)
	{
		this.currentChannel.noteOff(note,this.velocity);
		if (this.isRecording()) {
            ajoutEvenement(NOTEOFF, note,this.velocity);
        }
	}
	//Version pour clavier réel:
	public void noteOnModel(int note,int velocity)
	{
		if(note<120 && note >=0)
		{
			this.currentChannel.noteOn(note,velocity);
			if (this.isRecording()) {
	            ajoutEvenement(NOTEON, note,velocity);
	        }
	        if(this.keyControlerClavier!=null && this.pianoView != null)
	        {
	        	this.keyControlerClavier.getKey(note).setOn();
	        	this.pianoView.repaint();
	        }
	    }else
	    {
	    	System.out.println(note + "/"+ velocity);
	    }
	}
	public void noteOnModelC(int channel,int note,int velocity)
	{
		//System.out.println("Channel = "+channel);
		if(note<120 && note >=0)
		{
			this.midiChannel[channel].noteOn(note,velocity);
			if (this.isRecording()) {
	            ajoutEvenement(channel,NOTEON, note,velocity);
	        }
	        if(this.keyControlerClavier!=null && this.pianoView != null)
	        {
	        	this.keyControlerClavier.getKey(note).setOn();
	        	this.pianoView.repaint();
	        }
	    }else
	    {
	    	System.out.println(note + "/"+ velocity);
	    }
	}
	public void noteOffModel(int note,int velocity)
	{
		if(note<120 && note >=0)
		{
			this.currentChannel.noteOff(note,velocity);
			if (this.isRecording()) {
	            ajoutEvenement(NOTEOFF, note,velocity);
	        }
	        if(this.keyControlerClavier!=null  && this.pianoView != null)
	        {
	        	this.keyControlerClavier.getKey(note).setOff();
	        	this.pianoView.repaint();
	        }
	    }else
	    {
	    	System.out.println(note + "/"+ velocity);
	    	
	    }
	}
	public void noteOffModelC(int channel,int note,int velocity)
	{
		if(note<120 && note >=0)
		{
			this.midiChannel[channel].noteOff(note,velocity);
			if (this.isRecording()) {
	            ajoutEvenement(channel,NOTEOFF, note,velocity);
	        }
	        if(this.keyControlerClavier!=null  && this.pianoView != null)
	        {
	        	this.keyControlerClavier.getKey(note).setOff();
	        	this.pianoView.repaint();
	        }
	    }else
	    {
	    	System.out.println(note + "/"+ velocity);
	    	
	    }
	}
	public void setEffect(int value,int effet,boolean isPhysique)
	{
		//System.out.println("seteffect");
		if(this.optionPanel==null)
			this.optionPanel = this.pianoView.getOptionPanel();
		//Modulation haute
		if (effet==PITCHBEND)
		{
			if(isPhysique)
			{
				this.optionPanel.getPitchModGraph().setPitch(value);
				this.currentChannel.setPitchBend(value);
			}
			else
			{
				this.currentChannel.setPitchBend(value);
			}
			//System.out.println("pitch :"+value );
			if (this.isRecording()) {
				//valeur dans un tableau de Byte pour MSB et LSB
	            ajoutEvenement(PITCHBEND, value>>7,value&0x7f);
	        }
		}
		else if(effet==MODULATION1)
		{
	        if(isPhysique)
			{
				this.optionPanel.getPitchModGraph().setModPlus(value);		
				this.currentChannel.controlChange(MODULATION,value);
			}
			else
			{
				this.currentChannel.controlChange(MODULATION,value);
			}
			//System.out.println("Mod :"+value );
			if (this.isRecording()) {
	            ajoutEvenementProg(MODULATION, value);
	        }
		}
		//Modulation basse
		else if(effet==MODULATION2)
		{
			if(isPhysique)
			{
				this.optionPanel.getPitchModGraph().setModMinus(value);		
				this.currentChannel.controlChange(MODULATION,value);
			}
			else
			{
				this.currentChannel.controlChange(MODULATION,value);
			}
			//System.out.println("Mod :"+value );
			if (this.isRecording()) {
	            ajoutEvenementProg(MODULATION, value);
	        }
		}
		//Attack time
		else if(effet==ATTACKTIME)
		{
			//System.out.println("AttackTime/"+isPhysique+"/"+value);
			this.currentChannel.controlChange(ATTACKTIME,value);
			if(isPhysique)
			this.optionPanel.getAttackSlider().setValue(value);
			if (this.isRecording()) {
	            ajoutEvenementProg(ATTACKTIME, value);
	        }
		}
		//Release 
		else if(effet==RELEASE)
		{
			this.currentChannel.controlChange(RELEASE ,value);
			if(isPhysique)
			this.optionPanel.getReleaseSlider().setValue(value);
			if (this.isRecording()) {
	            ajoutEvenementProg(RELEASE , value);
	        }
		}
		//PAN
		/*
		else if(effet==PAN)
		{
			this.currentChannel.controlChange(PAN,value);
			if (this.isRecording()) {
	            ajoutEvenementProg(PAN, value);
	        }
		}*/
		//timbre
		else if(effet==TIMBRE)
		{
			this.currentChannel.controlChange(TIMBRE,value);
			if (this.isRecording()) {
	            ajoutEvenementProg(TIMBRE, value);
	        }
		}
		//pan
		else if(effet==PAN)
		{
			if(isPhysique)
			this.optionPanel.getPanSlider().setValue(value);
			this.currentChannel.controlChange(PAN,value);
			if (this.isRecording()) {
	            ajoutEvenementProg(PAN, value);
	        }
		}
		else if(effet==REVERB)
		{
			if(isPhysique)
			this.optionPanel.getReverbeSlider().setValue(value);
			//this.currentChannel.setPitchBend(value);
			this.currentChannel.controlChange(REVERB,value);
			//System.out.println(value);
			if (this.isRecording()) {
	            ajoutEvenementProg(REVERB, value);
	        }
		}
		else if(effet==SUSTAIN)
		{
			this.isSustain = !this.isSustain;
			System.out.println("Sustain"+this.isSustain);
			this.currentChannel.controlChange(SUSTAIN,value);
			//System.out.println(value);
			if (this.isRecording()) {
	            ajoutEvenementProg(SUSTAIN, value);
	        }
		}
		else
		{
			System.out.println("Effet non pris en charge par le system");
			System.out.println("effect : "+effet+" value :"+value);
		}
		//System.out.println("value = "+value);

		
	}
	public MidiChannel getCurrentChannel()
	{
		return this.currentChannel;
	}
	public int getCurrentChannelInt()
	{
		return this.channel;
	}
	public void setChannel(int c)
	{
		this.channel = c;
		this.currentChannel = this.midiChannel[c];
	}
	public void setOctave(int x)
	{
		this.octave = x;
	}
	public int getOctave()
	{
		return this.octave;
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
                	ajoutEvenement(PROGRAM, numInstru,0);
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
	public void setInstrumentKorg(int numInstru)
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
				this.listeInstruJTable.changeSelection(numInstru%8,numInstru/8,false,false);
				this.synth.loadInstrument(this.instruments[numInstru]);
				this.currentChannel.programChange(numInstru);
				if (this.isRecording()) {
                	ajoutEvenement(PROGRAM, numInstru,0);
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
		if(this.enregistrementSequence!=null)
		{
			this.sequence.deleteTrack(this.enregistrementSequence);
		}
		this.enregistrementSequence = this.sequence.createTrack();
		this.startTime = System.currentTimeMillis();
		this.enregistrement = true;
		ajoutEvenement(PROGRAM,this.currentInstru,0);
	}
	public void stopRecord()
	{
		ajoutEvenement(PROGRAM,this.currentInstru,0x2F);
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
	public void ajoutEvenementProg(int effet, int value) {
        try {
        	ShortMessage message = new ShortMessage();
            long millis = System.currentTimeMillis() - this.startTime;
            long tick = millis * this.sequence.getResolution() / 500;
            message.setMessage(ShortMessage.CONTROL_CHANGE, (byte)effet, value);
            System.out.println(value);
            MidiEvent event = new MidiEvent(message, tick);
            enregistrementSequence.add(event);
        } catch (Exception ex) { 
        	ex.printStackTrace();
        }
    }
	public void ajoutEvenement(int type, int num,int velocity) {
        try {
        	ShortMessage message = new ShortMessage();
            long millis = System.currentTimeMillis() - this.startTime;
            long tick = millis * this.sequence.getResolution() / 500;
            //message.setMessage(type+this.channel, num, velocity);
            message.setMessage(type, num, velocity); 
            MidiEvent event = new MidiEvent(message, tick);
            enregistrementSequence.add(event);
        } catch (Exception ex) { 
        	ex.printStackTrace();
        }
    }
    public void ajoutEvenement(int channel,int type, int num,int velocity) {
        try {
        	ShortMessage message = new ShortMessage();
            long millis = System.currentTimeMillis() - this.startTime;
            long tick = millis * this.sequence.getResolution() / 500;
            //message.setMessage(type+this.channel, num, velocity);
            message.setMessage(type,channel, num, velocity); 
            MidiEvent event = new MidiEvent(message, tick);
            enregistrementSequence.add(event);
        } catch (Exception ex) { 
        	ex.printStackTrace();
        }
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
	        // On ouvre le stream
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
	public void close()
	{
		this.synth.close();
		this.ctrlMIDI.close();
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