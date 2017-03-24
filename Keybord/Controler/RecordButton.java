package Keybord.Controler;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import Keybord.Model.*;
import Keybord.View.*;

public class RecordButton implements ActionListener
{
	private SynthModel synthModel;
	private OptionPanel optionPanel;
	private JButton recordB,playB,saveB,exportB;
    private JLabel time;
	public RecordButton(SynthModel model,OptionPanel optionPanel)
	{
		this.synthModel = model;
		this.optionPanel = optionPanel;
		this.recordB = this.optionPanel.getAllButton()[0];
		this.saveB = this.optionPanel.getAllButton()[1];
		this.playB = this.optionPanel.getAllButton()[2];
        this.exportB = this.optionPanel.getAllButton()[3];
        this.time = this.optionPanel.getLabel();
        //this.time.setText("00:00");
	}
	public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            boolean record = false;
            if (button.equals(this.recordB)) {
                record = this.synthModel.isRecording();
                if (!record) {
                    // add a program change right at the beginning of 
                    // the track for the current instrument
                    this.synthModel.startRecord();

                    this.recordB.setText("Stop");
                    playB.setEnabled(false);
                    saveB.setEnabled(false);
                } else {
                    this.synthModel.stopRecord();
                    this.recordB.setText("Record");
                    playB.setEnabled(true);
                    saveB.setEnabled(true);
                } 
            } else if (button.equals(playB)) {
                if (playB.getText().startsWith("Play")) {
                    try {
                        this.synthModel.playSequence();
                    } catch (Exception ex) { ex.printStackTrace(); }
                    playB.setText("Stop");
                    this.recordB.setEnabled(false);
                } else {
                    this.synthModel.pauseSequence();
                    playB.setText("Play");
                    this.recordB.setEnabled(true);
                } 
            } else if (button.equals(saveB)) {
                try {
                    File file = new File(System.getProperty("user.dir"));
                    JFileChooser fc = new JFileChooser(file);
                    fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        public boolean accept(File f) {
                            if (f.isDirectory()) {
                                return true;
                            }
                            return false;
                        }
                        public String getDescription() {
                            return "Save as .mid file.";
                        }
                    });
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        this.synthModel.saveMidiFile(fc.getSelectedFile());
                    }
                }catch (SecurityException ex) { 
                    //JavaSound.showInfoDialog();
                    ex.printStackTrace();
                } catch (Exception ex) { 
                    ex.printStackTrace();
                }
            }
            else if(button.equals(exportB))
            {
                //System.out.println("export");
                try{
                 File file = new File(System.getProperty("user.dir"));
                    JFileChooser fc = new JFileChooser(file);
                    fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        public boolean accept(File f) {
                            if (f.isDirectory()) {
                                return true;
                            }
                            return false;
                        }
                        public String getDescription() {
                            return "Save as .wav file.";
                        }
                    });
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        this.synthModel.saveWavFile(fc.getSelectedFile());
                    }
                } catch (SecurityException ex) { 
                    //JavaSound.showInfoDialog();
                    ex.printStackTrace();
                } catch (Exception ex) { 
                    ex.printStackTrace();
                }
            }
        }
}