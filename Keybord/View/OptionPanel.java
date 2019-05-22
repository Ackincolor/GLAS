package Keybord.View;
import javax.swing.*;
import java.awt.*;
import Keybord.View.*;
import Keybord.Model.*;
import Keybord.Controler.*;
public class OptionPanel extends JPanel
{
	private JSlider velocite,pan,reverbe,attack,release;
	private SynthModel synthModel;
	private JCheckBox mouseOverPiano;
	private JButton record,save,play,export;
	private JLabel time;
	private SelectOctave selectOctave;
	private ListeInstru listeInstru;
	private JScrollPane scroll;
	private JPanel panelSlider,panelPlayer;
	private GraphicsSon graphSon;
	private PitchModGraph pitchMod;
	private JTextArea console;
	public OptionPanel(SynthModel synthModel)
	{
		super();
		this.panelSlider = new JPanel();
		this.panelPlayer = new JPanel();

		this.setLayout(new GridLayout(1,2));
		this.synthModel = synthModel;
		//graphics du son
		this.graphSon = new GraphicsSon();
		this.graphSon.setSize(100,100);
		//slider 1
		this.velocite = createSlider("Velocit\u00e9 (sans clavier)",this,0,127,64,"0%","100%");
		this.velocite.addChangeListener(new SliderControler(this.synthModel,0,this.graphSon));
		//slider2
		this.pan = createSlider("pan n\u00b07",this,0,127,64,"left","right");
		this.pan.addChangeListener(new SliderControler(this.synthModel,10,this.graphSon));
		this.pitchMod = new PitchModGraph(synthModel);
		//slider3
		this.attack = createSlider("Attack n\u00b03",this,0,127,64,"0%","100%");
		this.attack.addChangeListener(new SliderControler(this.synthModel,73,this.graphSon));

		this.release = createSlider("Release n\u00b04",this,0,127,64,"0%","100%");
		this.release.addChangeListener(new SliderControler(this.synthModel,72,this.graphSon));
		//AttackTime
		this.reverbe = createSlider("Reverbe n\u00b08",this,0,127,64,"0%","100%");
		this.reverbe.addChangeListener(new SliderControler(this.synthModel,91,this.graphSon));
		//checkbox over
		this.mouseOverPiano = new JCheckBox("mouseOver", false);
		//enregistrement
		
		this.record = new JButton("Record");
		this.save = new JButton("Save");
		this.play = new JButton("Play");
		this.export = new JButton("Export");
		RecordButton listener = new RecordButton(this.synthModel,this);
		this.record.addActionListener(listener);
		this.save.addActionListener(listener);
		this.play.addActionListener(listener);
		this.export.addActionListener(listener);
		this.selectOctave = new SelectOctave(synthModel);
		this.panelPlayer.add(this.selectOctave);
		this.panelPlayer.add(this.mouseOverPiano);
		//this.add(this.velocite);
		this.panelPlayer.add(this.record);
		this.panelPlayer.add(this.play);
		this.panelPlayer.add(this.save);
		this.panelPlayer.add(this.export);
		this.time = new JLabel();
		this.panelPlayer.add(this.time);
		//ajout du Texte area pour la console.
		this.console = new JTextArea(10,50);
		this.console.setBackground(Color.BLACK);
		this.console.setForeground(Color.GREEN);
		this.panelPlayer.add(new JScrollPane(this.console,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		this.synthModel.setTextArea(this.console);

		this.listeInstru = new ListeInstru(this.synthModel);
		this.scroll = new JScrollPane(this.listeInstru,JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		Dimension tableSize = new Dimension(42*22, 160);
		scroll.setPreferredSize(tableSize);
		//this.add(new ListeInstru(this.synthModel.getAllInstru()));
		//this.add(scroll);
		this.panelSlider.add(this.graphSon);
		this.panelSlider.add(this.pitchMod);
		this.add(this.panelSlider);
		this.add(this.panelPlayer);
	}
	public JScrollPane getListe()
	{
		return this.scroll;
	}
	private JSlider createSlider(String name, JPanel p,int min,int max,int start,String left,String right) {

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel(left),BorderLayout.WEST);
		panel.add(new JLabel(name+":"),BorderLayout.NORTH);
		panel.add(new JLabel(right),BorderLayout.EAST);
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, start);
        //slider.addChangeListener(this);
        panel.add(slider,BorderLayout.SOUTH);
        panel.add(Box.createHorizontalStrut(5));
        this.panelSlider.add(panel);
        return slider;
    }
    public JButton[] getAllButton()
    {
    	JButton[] tab = new JButton[4];
    	tab[0]=this.record;
    	tab[1]=this.save;
    	tab[2]=this.play;
    	tab[3]=this.export;
    	return tab;
    }
    public void setFocus(PianoView v)
    {
    	this.addMouseListener(new FocusPiano(v));
    	this.listeInstru.addMouseListener(new FocusPiano(v));
    	this.listeInstru.setFocus(v);
    	this.selectOctave.setFocus(v);
    }
    public JLabel getLabel()
    {
    	return this.time;
    }
    public GraphicsSon getGraphicsSon()
    {
    	return this.graphSon;
    }
    public PitchModGraph getPitchModGraph()
    {
    	return this.pitchMod;
    }
    public JSlider getPanSlider()
    {
    	return this.pan;
    }
     public JSlider getReverbeSlider()
    {
    	return this.reverbe;
    }
    public JSlider getSliderVelocite()
    {
    	return this.velocite;
    }
    public JSlider getAttackSlider()
    {
    	return this.attack;
    }
    public JSlider getReleaseSlider()
    {
    	return this.release;
    }
    public JCheckBox getCheckMouseOver()
    {
    	return this.mouseOverPiano;
    }
}