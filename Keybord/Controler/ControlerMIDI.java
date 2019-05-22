package Keybord.Controler;

import Keybord.Model.*;
import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
    
public class ControlerMIDI
{
    private SynthModel synth;
    private MidiDevice device;
    private PrintStream console;
    public ControlerMIDI(SynthModel synth)
    {
        //affichage des messages dans la console.
        this.console = new PrintStream(new TextAreaOutputStream(synth.getConsole()));
        //System.setOut(console);
        this.synth = synth;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {
            this.device = MidiSystem.getMidiDevice(infos[i]);
            //obtention des instruments midi
            this.console.println(infos[i]);
            List<Transmitter> transmitters = device.getTransmitters();

            for(int j = 0; j<transmitters.size();j++) {
                transmitters.get(j).setReceiver(
                        new DumpReceiver(synth,this.console)
                );
            }

            Transmitter trans = device.getTransmitter();
            trans.setReceiver(new DumpReceiver(synth,this.console));

            //ouverture
            this.device.open();
            System.out.println(device.getDeviceInfo()+": ouvert");


        } catch (MidiUnavailableException e) {}
        }
    }
    public void close()
    {
        this.device.close();
        System.out.println("Piano fermé");
    }
    public class DumpReceiver implements  Receiver
{

    public long seByteCount = 0;
    public long smByteCount = 0;
    public long seCount = 0;
    public long smCount = 0;

    private final String[]       sm_astrKeyNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    private final String[]       sm_astrKeySignatures = {"Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#"};
    private final String[]       SYSTEM_MESSAGE_TEXT =
    {
        "System Exclusive ",
        "MTC Quarter Frame: ",
        "Song Position: ",
        "Song Select: ",
        "Undefined",
        "Undefined",
        "Tune Request",
        "End of SysEx (should not be in ShortMessage!)",
        "Timing clock",
        "Undefined",
        "Start",
        "Continue",
        "Stop",
        "Undefined",
        "Active Sensing",
        "System Reset"
    };

    private final String[]       QUARTER_FRAME_MESSAGE_TEXT =
    {
        "frame count LS: ",
        "frame count MS: ",
        "seconds count LS: ",
        "seconds count MS: ",
        "minutes count LS: ",
        "minutes count MS: ",
        "hours count LS: ",
        "hours count MS: "
    };

    private final String[]       FRAME_TYPE_TEXT =
    {
        "24 frames/second",
        "25 frames/second",
        "30 frames/second (drop)",
        "30 frames/second (non-drop)",
    };

    private boolean         m_bDebug;
    private boolean         m_bPrintTimeStampAsTicks;

    private SynthModel synth;

    private PrintStream console;


    public DumpReceiver(SynthModel synth,PrintStream console)
    {
        this.synth = synth;
        m_bPrintTimeStampAsTicks = true;
        this.console = console;
    }



    public void close()
    {
    }



    public void send(MidiMessage message, long lTimeStamp)
    {
        boolean isConsole = true;
        long debut = System.nanoTime();
        String  strMessage = null;
        if (message instanceof ShortMessage)
        {
            strMessage = decodeMessage((ShortMessage) message);
        }
        else if (message instanceof SysexMessage)
        {
            strMessage = decodeMessage((SysexMessage) message);
        }
        else if (message instanceof MetaMessage)
        {
            strMessage = decodeMessage((MetaMessage) message);
        }
        else
        {
            strMessage = "unknown message type";
        }
        String  strTimeStamp = null;
        if (m_bPrintTimeStampAsTicks)
        {
            strTimeStamp = "tick " + lTimeStamp + ": ";
        }
        else
        {
            if (lTimeStamp == -1L)
            {
                strTimeStamp = "timestamp [unknown]: ";
            }
            else
            {
                strTimeStamp = "timestamp " + lTimeStamp + " us: ";
            }
        }
        if(strMessage!=null && isConsole)
        {
            this.console.println(strTimeStamp + strMessage);

        }
        long fin = System.nanoTime();
        //this.console.println("latence ="+(fin-debut)/ 1000000+"ms");
    }



    public String decodeMessage(ShortMessage message)
    {
        String  strMessage = null;
        switch (message.getCommand())
        {
        case 0x80:
            //valeur pour note OFF
            //strMessage = "note Off " + getKeyName(message.getData1()) + " velocity: " + message.getData2();
            this.synth.noteOffModelC(message.getChannel()+1,message.getData1(),message.getData2());
            break;

        case 0x90:
        //valeur pour note On
            //strMessage = "note On " + getKeyName(message.getData1()) + " velocity: " + message.getData2();
            this.synth.noteOnModelC(message.getChannel()+1,message.getData1(),message.getData2());
            //pour certains clavier midi il renvoient defoi le meme message mais avec la velocité a 0.
            if(message.getData2()==0)
            {
                this.synth.noteOffModelC(message.getChannel()+1,message.getData1(),message.getData2());
            }
            break;

        case 0xa0:
            strMessage = "polyphonic key pressure " + getKeyName(message.getData1()) + " pressure: " + message.getData2();
            break;

        case 0xb0:
            strMessage = "control change " + message.getData1() + " value: " + message.getData2();
            this.synth.setEffect(message.getData2(),message.getData1(),true);
            break;
        //Changement instrumeznt
        case 0xc0:
            strMessage = "program change " + message.getData1();
            this.synth.setInstrumentKorg(message.getData1());
            break;

        case 0xd0:
            strMessage = "key pressure " + getKeyName(message.getData1()) + " pressure: " + message.getData2();
            break;

        case 0xe0:
            strMessage = "pitch wheel change " + get14bitValue(message.getData1(), message.getData2());
            this.synth.setEffect(get14bitValue(message.getData1(), message.getData2()),224,true);
            break;

        case 0xF0:
            strMessage = SYSTEM_MESSAGE_TEXT[message.getChannel()];
            switch (message.getChannel())
            {
            case 0x1:
                int nQType = (message.getData1() & 0x70) >> 4;
                int nQData = message.getData1() & 0x0F;
                if (nQType == 7)
                {
                    nQData = nQData & 0x1;
                }
                strMessage += QUARTER_FRAME_MESSAGE_TEXT[nQType] + nQData;
                if (nQType == 7)
                {
                    int nFrameType = (message.getData1() & 0x06) >> 1;
                    strMessage += ", frame type: " + FRAME_TYPE_TEXT[nFrameType];
                }
                break;

            case 0x2:
                strMessage += get14bitValue(message.getData1(), message.getData2());
                break;

            case 0x3:
                strMessage += message.getData1();
                break;
            }
            break;

        default:
            strMessage = "unknown message: status = " + message.getStatus() + ", byte1 = " + message.getData1() + ", byte2 = " + message.getData2();
            break;
        }
        if (message.getCommand() != 0xF0)
        {
            int nChannel = message.getChannel() + 1;
            /*if(this.synth.getCurrentChannelInt()!=nChannel)
                this.synth.setChannel(nChannel);*/
            String  strChannel = "channel " + nChannel + ": ";
            strMessage = strChannel + strMessage;
        }
        smCount++;
        smByteCount+=message.getLength();
        //supperssion de active sensing car c'est chiant
        if  (message.getCommand() == 0xF0 && message.getChannel()==14)
            return null;
        return "["+getHexString(message)+"] "+strMessage;
    }



    public String decodeMessage(SysexMessage message)
    {
        byte[]  abData = message.getData();
        String  strMessage = null;
        // this.console.println("sysex status: " + message.getStatus());
        if (message.getStatus() == SysexMessage.SYSTEM_EXCLUSIVE)
        {
            strMessage = "Sysex message: F0" + getHexString(abData);
        }
        else if (message.getStatus() == SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE)
        {
            strMessage = "Continued Sysex message F7" + getHexString(abData);
            seByteCount--; // do not count the F7
        }
        seByteCount += abData.length + 1;
        seCount++; // for the status byte
        return strMessage;
    }



    public String decodeMessage(MetaMessage message)
    {
        byte[]  abMessage = message.getMessage();
        byte[]  abData = message.getData();
        int nDataLength = message.getLength();
        String  strMessage = null;
        // this.console.println("data array length: " + abData.length);
        switch (message.getType())
        {
        case 0:
            int nSequenceNumber = ((abData[0] & 0xFF) << 8) | (abData[1] & 0xFF);
            strMessage = "Sequence Number: " + nSequenceNumber;
            break;

        case 1:
            String  strText = new String(abData);
            strMessage = "Text Event: " + strText;
            break;

        case 2:
            String  strCopyrightText = new String(abData);
            strMessage = "Copyright Notice: " + strCopyrightText;
            break;

        case 3:
            String  strTrackName = new String(abData);
            strMessage = "Sequence/Track Name: " +  strTrackName;
            break;

        case 4:
            String  strInstrumentName = new String(abData);
            strMessage = "Instrument Name: " + strInstrumentName;
            break;

        case 5:
            String  strLyrics = new String(abData);
            strMessage = "Lyric: " + strLyrics;
            break;

        case 6:
            String  strMarkerText = new String(abData);
            strMessage = "Marker: " + strMarkerText;
            break;

        case 7:
            String  strCuePointText = new String(abData);
            strMessage = "Cue Point: " + strCuePointText;
            break;

        case 0x20:
            int nChannelPrefix = abData[0] & 0xFF;
            strMessage = "MIDI Channel Prefix: " + nChannelPrefix;
            break;

        case 0x2F:
            strMessage = "End of Track";
            break;

        case 0x51:
            int nTempo = ((abData[0] & 0xFF) << 16)
                    | ((abData[1] & 0xFF) << 8)
                    | (abData[2] & 0xFF);           // tempo in microseconds per beat
            float bpm = convertTempo(nTempo);
            // truncate it to 2 digits after dot
            bpm = (float) (Math.round(bpm*100.0f)/100.0f);
            strMessage = "Set Tempo: "+bpm+" bpm";
            break;

        case 0x54:
            // this.console.println("data array length: " + abData.length);
            strMessage = "SMTPE Offset: "
                + (abData[0] & 0xFF) + ":"
                + (abData[1] & 0xFF) + ":"
                + (abData[2] & 0xFF) + "."
                + (abData[3] & 0xFF) + "."
                + (abData[4] & 0xFF);
            break;

        case 0x58:
            strMessage = "Time Signature: "
                + (abData[0] & 0xFF) + "/" + (1 << (abData[1] & 0xFF))
                + ", MIDI clocks per metronome tick: " + (abData[2] & 0xFF)
                + ", 1/32 per 24 MIDI clocks: " + (abData[3] & 0xFF);
            break;

        case 0x59:
            String  strGender = (abData[1] == 1) ? "minor" : "major";
            strMessage = "Key Signature: " + sm_astrKeySignatures[abData[0] + 7] + " " + strGender;
            break;

        case 0x7F:
            // TODO: decode vendor code, dump data in rows
            String  strDataDump = getHexString(abData);
            strMessage = "Sequencer-Specific Meta event: " + strDataDump;
            break;

        default:
            String  strUnknownDump = getHexString(abData);
            strMessage = "unknown Meta event: " + strUnknownDump;
            break;

        }
        return strMessage;
    }



    public String getKeyName(int nKeyNumber)
    {
        if (nKeyNumber > 127)
        {
            return "illegal value";
        }
        else
        {
            int nNote = nKeyNumber % 12;
            int nOctave = nKeyNumber / 12;
            return sm_astrKeyNames[nNote] + (nOctave - 1);
        }
    }


    public int get14bitValue(int nLowerPart, int nHigherPart)
    {
        return (nLowerPart & 0x7F) | ((nHigherPart & 0x7F) << 7);
    }



    private int signedByteToUnsigned(byte b)
    {
        return b & 0xFF;
    }

    // convert from microseconds per quarter note to beats per minute and vice versa
    private float convertTempo(float value) {
        if (value <= 0) {
            value = 0.1f;
        }
        return 60000000.0f / value;
    }



    private char hexDigits[] = 
       {'0', '1', '2', '3', 
        '4', '5', '6', '7', 
        '8', '9', 'A', 'B', 
        'C', 'D', 'E', 'F'};

    public String getHexString(byte[] aByte)
    {
        StringBuffer    sbuf = new StringBuffer(aByte.length * 3 + 2);
        for (int i = 0; i < aByte.length; i++)
        {
            sbuf.append(' ');
            sbuf.append(hexDigits[(aByte[i] & 0xF0) >> 4]);
            sbuf.append(hexDigits[aByte[i] & 0x0F]);
            /*byte  bhigh = (byte) ((aByte[i] &  0xf0) >> 4);
            sbuf.append((char) (bhigh > 9 ? bhigh + 'A' - 10: bhigh + '0'));
            byte    blow = (byte) (aByte[i] & 0x0f);
            sbuf.append((char) (blow > 9 ? blow + 'A' - 10: blow + '0'));*/
        }
        return new String(sbuf);
    }
    
    private String intToHex(int i) {
        return ""+hexDigits[(i & 0xF0) >> 4]
                 +hexDigits[i & 0x0F];
    }

    public String getHexString(ShortMessage sm)
    {
        // bug in J2SDK 1.4.1
        // return getHexString(sm.getMessage());
        int status = sm.getStatus();
        String res = intToHex(sm.getStatus());
        // if one-byte message, return
        switch (status) {
            case 0xF6:          // Tune Request
            case 0xF7:          // EOX
                // System real-time messages
            case 0xF8:          // Timing Clock
            case 0xF9:          // Undefined
            case 0xFA:          // Start
            case 0xFB:          // Continue
            case 0xFC:          // Stop
            case 0xFD:          // Undefined
            case 0xFE:          // Active Sensing
            case 0xFF: return res;
        }
        res += ' '+intToHex(sm.getData1());
        // if 2-byte message, return
        switch (status) {
            case 0xF1:          // MTC Quarter Frame
            case 0xF3:          // Song Select
                    return res;
        }
        switch (sm.getCommand()) {
            case 0xC0:
            case 0xD0:
                    return res;
        }
        // 3-byte messages left
        res += ' '+intToHex(sm.getData2());
        return res;
    }
}
}

