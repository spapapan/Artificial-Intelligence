import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.neuroph.core.data.DataSet;

import neuralnet.NeuralNetwork;

public class Main extends JFrame{
 
    public Board board;
    public int difficulty = 60;
    public JLabel nnlabel;
    public JFrame nnframe;
    public Boolean onoffsound = true;
 
	
	public Main(Boolean AIPlayer, Boolean saveDataSet) {
 
		board = new Board(difficulty,AIPlayer, saveDataSet,onoffsound);
		add(board);
		setBar(this, board);
		
		this.setPreferredSize(new Dimension(710,730));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocation(500,200);
		this.setTitle("AI Snake 1.0");
		this.pack();
		this.setVisible(true); 

	}
	
	private void StartGame(Boolean AIPlayer, Boolean saveDataSet){

		this.remove(board);
		board = new Board(difficulty,AIPlayer,saveDataSet,onoffsound);
		setBar(this, board);
        this.add(board);
        board.revalidate();
        board.repaint();
        board.setFocusable(true);
        board.requestFocus();
 
	}
	
	private void LoadTrainPanel(){
		
		this.dispose();
		Main fr = new Main(false, false);
		board.SaveDataSet();
		
	    nnframe = new JFrame();
		nnframe.setPreferredSize(new Dimension(300,150));
		nnframe.setLocation(500,200);
		nnframe.setTitle("Train Neural Network");
		nnframe.pack();
		nnframe.setVisible(true); 
		
		JPanel nnpanel = new JPanel();
		nnpanel.setLayout(null);
		DataSet set = new DataSet(900,4);
		set = DataSet.load("dataset");
		if (set.size()!=0){
	        nnlabel = new JLabel("<html>    Training set size: " + Integer.toString(set.size()) + "<br>This will take a while ...<br> Why don't you play?</html> ");
		} else{
			nnlabel = new JLabel("Training set is zero size");
		}
		nnlabel.setBounds(40,0,300,100);
		nnpanel.add(nnlabel);
		nnframe.add(nnpanel);
		
		board.pauseGame();
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			 @Override
			  public void run() {
			TrainNeuralNetwork(fr);
			 }
		}, 1000);
		
	}
	
	private void TrainNeuralNetwork(JFrame fr){

 
		DataSet set = new DataSet(900,4);
		set = DataSet.load("dataset");
		if (set.size()!=0){
			TrainNeuralNet();
			nnlabel.setText("Neural Network trained!");
		}
		

		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			 @Override
			  public void run() {
				fr.dispose();
				nnframe.dispose();

			 }
		}, 2000);
		
	    timer = new Timer();
		timer.schedule(new TimerTask(){
			 @Override
			  public void run() {
				 new Main(false,false);

			 }
		}, 2000);
		
 
        
		DeleteDataSet();
	}
	
	// ------------ Train Neural Network ---------
	private void TrainNeuralNet(){
	    LinkedList<DataSet> setList = new LinkedList<DataSet>();
	    
		    DataSet set = new DataSet(900,4);
		    set = DataSet.load("dataset");
		    
		    if (set.size() >500){
		    	int lastindex=0;
		    	DataSet tempSet = new DataSet(900,4);
		    	for (int i=0; i<set.size(); i++){
		    		tempSet.addRow(set.getRowAt(i));
		    		if ((double)i/(double)500 == (int)((double)i/(double)500)){
		    			setList.add(tempSet);
		    			tempSet = new DataSet(900,4);
		    			lastindex = i;
		    		}
		    	}
		    	for (int i= lastindex; i< set.size(); i++){
		    		tempSet.addRow(set.getRowAt(i));
		    	}
		    	setList.add(tempSet);
		    	tempSet.clear();
		    	
		    } else{
		    	setList.add(set);
		    }
		    int c=0;
		    for (DataSet s: setList){

				NeuralNetwork nn = new NeuralNetwork();
				nn.TrainNeuralNetwork(s);

            
		    }
	}
	
	
	// ---------- Set Up Bar Elements ------------
	private void setBar(JFrame frame, Board board){
		
		JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Levels");
        JMenu file = new JMenu("File");
        JMenu settings = new JMenu("Settings");

        JMenu ai = new JMenu("Neural Network");
        ai.setMaximumSize(new Dimension(140,80));
        JMenuItem pause = new JMenuItem("Pause");
        pause.setMaximumSize(new Dimension(60,80));


 
        
        menuBar.add(file);
        menuBar.add(menu);
        menuBar.add(ai);
        menuBar.add(settings);
        menuBar.add(pause);
        
        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem medium = new JMenuItem("Medium");
        JMenuItem hard = new JMenuItem("Hard");
        JMenuItem extreme = new JMenuItem("Extreme");
        
        JMenuItem trainNN = new JMenuItem("Train Neural Network");
        JMenuItem aiplayer = new JMenuItem("AI Player");
        JMenuItem createSet = new JMenuItem("Create training set");
        
        JMenuItem start = new JMenuItem("New");
        JMenuItem about = new JMenuItem("About");
        JMenuItem exit = new JMenuItem("Exit");
        
        JMenuItem sound = new JMenuItem("Sound On/Off");
        JMenuItem grid = new JMenuItem("Show/Hide grid");
        
        start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	StartGame(false,false);
            }
        });
        
        sound.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.OnOffSound();
            	if (onoffsound){
            		onoffsound = false;
            	} else{
            		onoffsound = true;
            	}
            }
        });
        
        grid.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.PaintGrid();
            }
        });
        
        createSet.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.createDataSet();
            }
        });
        
        trainNN.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.SaveDataSet();
            	LoadTrainPanel();
            }
        });
        
        aiplayer.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	StartGame(true,false);
            }
        });
        

        easy.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	difficulty = 100;
            	StartGame(false,false);
            }
        });
        
        medium.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	difficulty = 60;
            	StartGame(false,false);
            }
        });
        
        hard.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	difficulty = 30;
            	StartGame(false,false);
            }
        });
        
        extreme.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	difficulty = 20;
            	StartGame(false,false);
            }
        });
        
        
        pause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.pauseGame();
            }
        });
        
        about.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					About();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        exit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });

        file.add(start);
        file.add(about);
        file.add(exit);
        
        menu.add(easy);
        menu.add(medium);
        menu.add(hard);
        menu.add(extreme);
        
        ai.add(aiplayer);
        ai.add(createSet);
        ai.add(trainNN);
        
        settings.add(sound);
        settings.add(grid);
        frame.setJMenuBar(menuBar);
	}
	
	
	// ------------ Delete Neural Network DataSet -----------
	public void DeleteDataSet(){
		File datafile = new File("dataset");
		if (datafile.exists()){
		     datafile.delete();
		}
	}
	
	// ------------ Create Neural Network ----------------
	private static void createNN(int lines,int hidden){
		
		File nnet = new File("SnakeNN.nnet");
		if (!nnet.exists()){
			NeuralNetwork nn = new NeuralNetwork();
			nn.CreateNN(lines, hidden);
		}

	}
	
	// --------------- About Panel -------------------
	private void About() throws URISyntaxException{
		
		
	    URI  uri = new URI("https://github.com/spapapan/Games/tree/master/Snake");
		
		JFrame aboutF = new JFrame();
		aboutF.setPreferredSize(new Dimension(410,450));
		aboutF.setLocation(400,500);
		aboutF.setTitle("About");
		aboutF.pack();
		aboutF.setVisible(true); 
		
		JPanel aboutP = new JPanel();
		aboutP.setLayout(null);
		
	 
		JLabel lb1 = new JLabel("AI Snake 1.0");
		lb1.setFont(new Font ("Calibri", Font.BOLD , 17));
        lb1.setBounds(140,10, 300,40);

        
		JLabel lb2 = new JLabel("<html>To train the snake you must first create a new<br>dataset from Neural Network/Create training set.<br> That will record your movements while you play</html>", SwingConstants.CENTER);
		lb2.setFont(new Font ("Calibri", 0 , 14));
        lb2.setBounds(2,2, 400,200);
        
		JLabel lb3 = new JLabel("<html>Teach the snake how to play by playing the game.</html>", SwingConstants.CENTER);
		lb3.setFont(new Font ("Calibri", 0 , 14));
		lb3.setBounds(2,50, 400,200);
		
		JLabel lb4 = new JLabel("<html>Train the snake to play by clicking Neural Network<br> /Train Neural Network (This will take a while).</html>", SwingConstants.CENTER);
		lb4.setFont(new Font ("Calibri", 0 , 14));
		lb4.setBounds(2,100, 400,200);
		
		JLabel lb5 = new JLabel("<html>There must be a sufficient number of training sets  <br>for the snake to be trained properly.</html>", SwingConstants.CENTER);
		lb5.setFont(new Font ("Calibri", 0 , 14));
		lb5.setBounds(2,150, 400,200);
		
		JLabel lb6 = new JLabel("<html><b>Programmer:</b> Stavros Papapantelis <br> <b>Email:</b> lucid.gr@gmail.com <br><b>Licence:</b> GNU GENERAL PUBLIC LICENSE</html>", SwingConstants.CENTER);
		lb6.setFont(new Font ("Calibri", 0 , 14));
		lb6.setBounds(2,230, 400,200);
		
		JLabel lb7 = new JLabel("<html><b>Source Code:</b> <font color='blue'>github</font></html>", SwingConstants.CENTER);
		lb7.setFont(new Font ("Calibri", 0 , 14));
		lb7.setBounds(2,265, 270,200);
		
        
		lb7.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	open(uri);
		    }
		});
        
		aboutP.add(lb1);
		aboutP.add(lb2);
		aboutP.add(lb3);
		aboutP.add(lb4);
		aboutP.add(lb5);
		aboutP.add(lb6);
		aboutP.add(lb7);
		
		aboutF.add(aboutP);
	}
	
	
	// -------------- Open URI -----------------
	private static void open(URI uri) {
		    if (Desktop.isDesktopSupported()) {
		      try {
		        Desktop.getDesktop().browse(uri);
		      } catch (IOException e) { /* TODO: error handling */ }
		    } else { /* TODO: error handling */ }
    }
 
	
	// ------------ Main Method --------------
	public static void main(String[] args) {
		
		createNN(30,50); // Create empty neural network if there is none  
		                 // with 30*30 inputs, 50 hidden neurons and 4 outputs
		
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
		 
		new Main(false,false);

		
		
	   
	}
 

}
