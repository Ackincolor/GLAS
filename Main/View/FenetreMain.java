package Main.View;
import Main.Controler.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.html.*;
import java.util.*;

import java.io.File;

public class FenetreMain extends JFrame
{
	private WidgetAudio[] tableauWidgetAudio = new WidgetAudio[4];
	private GridBagConstraints c;
	private ListeWidget listeView;
	public FenetreMain(String s)
	{
		super(s);
		this.setSize(900,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel background = new JPanel();
        background.setOpaque(true);
        background.setLayout(new GridBagLayout());
        background.setSize(900,700);

        
		//changement du gestionnaire de mise en page.
		this.c = new GridBagConstraints();
		/*menuBar();
		coloneGauche();
		vueMilieu();
		vueBas();*/
        TreeMap<String, JPanel> panels = new TreeMap<String, JPanel>();

        panels.put("Background",background);
        
        Onde onde = new Onde();
        panels.put("Onde",onde);
        
        MidiView wave = new MidiView();
        JScrollPane scroll = new JScrollPane(wave,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panels.put("Wave",wave);
       
        AccueilView accueil = new AccueilView();
        accueil.draw(null);
        panels.put("Accueil",accueil);
        //panel avec CardLayout
        JPanel soundView = new JPanel();
        soundView.setLayout(new CardLayout());

        soundView.add(onde,"V1");
        soundView.add(scroll,"V2");
        soundView.add(accueil,"V3");

        ((CardLayout)soundView.getLayout()).show(soundView,"V3");
        panels.put("Soundview",soundView);
        background.add(soundView,onde.constraints);

        Playback menu = new Playback();
        background.add(menu,menu.constraints);
        panels.put("Playback", menu);
        
        Sliders effets = new Sliders();
        background.add(effets,effets.constraints);
        panels.put("Sliders", effets);
        
        this.add(background);
        panels.put("Background",background);
        
        this.setJMenuBar(new Menu(panels));
		this.setVisible(true);

		//wave.draw();
		//menu.setSound(wave.getSound());
	}
	private void vueBas()
	{
		//vue en bas
		JPanel bottomSide = new BotSide();
		bottomSide.setBackground(Color.BLACK);
		this.c.fill = GridBagConstraints.HORIZONTAL;
		this.c.weightx = 1;
		this.c.weighty = 0;
		this.c.gridwidth = 3;
		this.c.gridheight = 1;
		this.c.gridx = 1;
		this.c.gridy = 4;
		this.add(bottomSide,this.c);
		//set visible
	}
	private void vueMilieu()
	{
		//vue du milieu
		this.listeView = new ListeWidget();
		listeView.setLayout(new GridLayout(4,0));
		listeView.setBackground(Color.RED);
		this.c.fill = GridBagConstraints.BOTH;
		this.c.weightx = 0;
		this.c.weighty =1;
		this.c.gridwidth = 4;
		this.c.gridheight = 2;
		this.c.gridx = 1;
		this.c.gridy = 1;
		this.add(listeView,this.c);
	}
	private void coloneGauche()
	{
		//colone de gauche
		JPanel leftColone = new LeftSide(this);
		leftColone.setBackground(Color.BLUE);
		this.c.fill = GridBagConstraints.VERTICAL;
		this.c.weightx = 0;
		this.c.weighty = 1;
		this.c.gridwidth = 1;
		this.c.gridheight = 5;
		this.c.gridx = 0;
		this.c.gridy = 1;
		//ajout de la colone
		this.add(leftColone,this.c);
	}
	private void menuBar()
	{
		//ajout de la bar de menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFiles = new JMenu("Files");
		JMenu menuEdit = new JMenu("Edit");		
		JMenu menuNew = new JMenu("New");

		JMenuItem wave= new JMenuItem("Wave");
		JMenuItem onde = new JMenuItem("Onde"); 
		JMenuItem piano = new JMenuItem("Piano");
		JMenuItem rythmBox = new JMenuItem("RythmBox");

		menuEdit.add(wave);
		menuEdit.add(onde);

		menuNew.add(piano);
		menuNew.add(rythmBox);
		MenuControl menuControl = new MenuControl();

		wave.addActionListener(menuControl);
		onde.addActionListener(menuControl);
		piano.addActionListener(menuControl);
		rythmBox.addActionListener(menuControl);
		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuFiles);
		menuBar.add(menuEdit);
		menuBar.add(menuNew);
		menuBar.add(menuHelp);
		//debut des contraintes
		this.c.fill = GridBagConstraints.HORIZONTAL;
		this.c.weightx = 1;
		this.c.weighty = 0;
		this.c.gridwidth = 5;
		this.c.gridheight = 1;
		this.c.gridx = 0;
		this.c.gridy = 0;
		//Fin des contraintes
		//ajout de la bar de menu
		this.add(menuBar,this.c);
	}
	public ListeWidget getListeView()
	{
		return this.listeView;
	}
	public void addListeView()
	{
		//ouverture du pop-up fichier 

		//ajout
		for(int i=0;i<tableauWidgetAudio.length;i++)
		{
			if(tableauWidgetAudio[i]==null )
			{
				this.tableauWidgetAudio[i]=new WidgetAudio("String");
				this.listeView.addWidget(tableauWidgetAudio[i]);
				this.revalidate();
				return;
			}
		}
		System.out.println("espace plein");
	}
	public void removeListeView(int value)
	{
		if(value<4 || value >=0)
		{
			this.listeView.removeAll();
			this.tableauWidgetAudio[value]=null;
		}
		else
		{
			System.out.println("indice invalide");
		}
	}
	public int getSelected()
	{
		return 2;
	}
}