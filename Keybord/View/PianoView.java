package Keybord.View;
import Keybord.Model.*;
import Keybord.Controler.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class PianoView extends JPanel
{
	final int hauteur = 100;
	final int largeur = 22;
	private ArrayList<Key> notesBlanche = new ArrayList<Key>();
	private ArrayList<Key> notesNoire = new ArrayList<Key>();
    private SynthModel synthModel;
    private OptionPanel optnPanel;
    private String[] sm_astrKeyNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	public PianoView(SynthModel synthModel,OptionPanel optnPanel)
	{
        this.synthModel = synthModel;
        this.optnPanel = optnPanel;
		this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(70*largeur, hauteur+1));
        int noteDepart = 0;
        KeyControler keyControler = new KeyControler(this,this.notesBlanche,this.notesNoire,this.synthModel,this.optnPanel.getCheckMouseOver());
        //envoi du controler au model pour changer les touche de couleur.
        KeyControlerClavier kc = new KeyControlerClavier(this,this.synthModel,this.notesBlanche,this.notesNoire);
        this.synthModel.setKeyControlerClavier(kc);
        this.synthModel.setPianoView(this);
        this.addKeyListener(kc);
        this.setFocusable(true);
        int blancheOctave[] = { 0, 2, 4, 5, 7, 9, 11 };
        for (int i = 0, x = 0; i < 10; i++) {
            for (int j = 0; j < 7; j++, x += largeur) {
                int numeroTouche = i * 12 + blancheOctave[j] + noteDepart;
                notesBlanche.add(new Key(x, 0, largeur, hauteur, numeroTouche));
                //System.out.println(numeroTouche);
            }
        }
        for (int i = 0, x = 0; i < 10; i++, x += largeur) {
            int numeroTouche = i * 12 + noteDepart;
            notesNoire.add(new Key((x += largeur)-5-2, 0, largeur/2+4, hauteur/2, numeroTouche+1));
            notesNoire.add(new Key((x += largeur)-5-2, 0, largeur/2+4, hauteur/2, numeroTouche+3));
            x += largeur;
            notesNoire.add(new Key((x += largeur)-5-2, 0, largeur/2+4, hauteur/2, numeroTouche+6));
            notesNoire.add(new Key((x += largeur)-5-2, 0, largeur/2+4, hauteur/2, numeroTouche+8));
            notesNoire.add(new Key((x += largeur)-5-2, 0, largeur/2+4, hauteur/2, numeroTouche+10));
        }
        this.addMouseListener(keyControler);
        this.addMouseMotionListener(keyControler);
        FocusPiano focusPiano = new FocusPiano(this);  
        this.optnPanel.addMouseListener(focusPiano);
        this.optnPanel.getAllButton()[0].addMouseListener(focusPiano);
        this.optnPanel.getAllButton()[1].addMouseListener(focusPiano); 
        this.optnPanel.getAllButton()[2].addMouseListener(focusPiano);            
	}
    public OptionPanel getOptionPanel()
    {
        return this.optnPanel;
    }
	public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = this.getSize();

            g2.setBackground(this.getBackground());
            g2.clearRect(0, 0, d.width, d.height);

            g2.setColor(Color.white);
            g2.fillRect(0, 0, 70*largeur, hauteur);

            for (int i = 0; i < notesBlanche.size(); i++) {
                Key key = (Key) notesBlanche.get(i);
                if (key.isNoteOn()) {
                    g2.setColor(Color.GREEN);
                    g2.fill(key);
                }
                g2.setColor(Color.black);
                g2.draw(key);
                g2.drawString(this.sm_astrKeyNames[key.getNum()%12],key.posX+5,key.posY+90);
            }
            for (int i = 0; i < notesNoire.size(); i++) {
                Key key = (Key) notesNoire.get(i);
                if (key.isNoteOn()) {
                    g2.setColor(Color.GREEN);
                    g2.fill(key);
                    g2.setColor(Color.black);
                    g2.draw(key);
                } else {
                    g2.setColor(Color.black);
                    g2.fill(key);
                }
                g2.setColor(Color.WHITE);
                g2.drawString(this.sm_astrKeyNames[key.getNum()%12],key.posX,key.posY+20);
            }
        }
}