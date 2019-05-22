package Keybord.View;
import Keybord.Model.*;
import Keybord.Controler.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.sound.midi.*;
public class ListeInstru extends JPanel
{
	private Instrument[] listeInstru;
	private JTable table;
	private int nRows = 8;
	private String names[] = { 
           "Piano", "Percutions", "Orgue", "Guitare", 
           "Bass", "Corde", "Ensemble", "Vent", 
           "Reed", "Pipe", "Synth Lead", "Pad",
           "Synth Effects", "Ethnic", "Percution", "Effets" };
           private int nCols = names.length;
	public ListeInstru(SynthModel synthModel)
	{
		this.listeInstru = synthModel.getAllInstru();
		TableModel dataModel = new AbstractTableModel() {
                public int getColumnCount() { return nCols; }
                public int getRowCount() { return nRows;}
                public Object getValueAt(int r, int c) { 
                    if (listeInstru != null) {
                        return listeInstru[c*nRows+r].getName();
                    } else {
                        return Integer.toString(c*nRows+r);
                    }
                }
                public String getColumnName(int c) { 
                    return names[c];
                }
                public Class getColumnClass(int c) {
                    return getValueAt(0, c).getClass();
                }
                public boolean isCellEditable(int r, int c) {return false;}
                public void setValueAt(Object obj, int r, int c) {}
            };
    	
        this.table = new JTable(dataModel);
        this.table.setCellSelectionEnabled(true);
        this.add(this.table);
        ListSelectionModel listSelectionModel = this.table.getSelectionModel();        
		listSelectionModel.addListSelectionListener(new ControleurTableSelection(synthModel,this.table));
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getSelectionModel().addListSelectionListener(new ControleurTableSelection(synthModel,this.table));
        synthModel.setTable(this.table);
	}
    public void setFocus(PianoView v)
    {
        this.table.addMouseListener(new FocusPiano(v));
    }
    public JTable getJTable()
    {
        return this.table;
    }
}