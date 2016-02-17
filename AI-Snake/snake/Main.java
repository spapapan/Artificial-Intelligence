package snake;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import neuralnet.Encog;

public class Main extends JFrame{
 
    public Board board;
    public int difficulty = 60;
    public JLabel nnlabel;
    public JFrame nnframe;
    public Boolean onoffsound = true;
 
	
	public Main(Boolean AIPlayer) {
 
		board = new Board(difficulty,AIPlayer,onoffsound);
		add(board);
		setBar(this, board);
		
		this.setPreferredSize(new Dimension(710,730));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocation(500,200);
		this.setTitle("AI Snake 1.0");
		this.pack();
		this.setVisible(true); 

	}
	
	private void StartGame(Boolean AIPlayer){

		this.remove(board);
		board = new Board(difficulty,AIPlayer,onoffsound);
		setBar(this, board);
        this.add(board);
        board.revalidate();
        board.repaint();
        board.setFocusable(true);
        board.requestFocus();
 
	}
	
	private void LoadTrainPanel(){
		
		this.dispose();
		Main fr = new Main(false);
		board.SaveDataSet();
		
	    nnframe = new JFrame();
		nnframe.setPreferredSize(new Dimension(300,150));
		nnframe.setLocation(500,200);
		nnframe.setTitle("Train Neural Network");
		nnframe.pack();
		nnframe.setVisible(true); 
		
		JPanel nnpanel = new JPanel();
		nnpanel.setLayout(null);

	    String line;
	    int totallines=0;
		try (BufferedReader br = new BufferedReader(new FileReader("dataset.txt"))) {
		    while ((line = br.readLine()) != null) {
		    	if (!line.equals("")){
		    		totallines++;
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (totallines!=0){
	        nnlabel = new JLabel("<html>    Training set size: " + totallines + "<br>This will take a while ...<br> Why don't you play?</html> ");
		} else{
			nnlabel = new JLabel("Training set is zero size");
		} 
		nnlabel.setBounds(40,0,300,100);
		nnpanel.add(nnlabel);
		
		nnframe.add(nnpanel);
		board.pauseGame();
		
		if (totallines != 0){
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			 @Override
			  public void run() {
			  TrainNeuralNetwork(fr);
			 }
		}, 1000);
		}
		
	}
	
	private void TrainNeuralNetwork(JFrame fr){
		
		Encog encog = new Encog();
		encog.TrainNeuralNetwork();
		nnlabel.setText("Neural Network trained!");

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
				 board.SaveDataSet();
				 new Main(false);

			 }
		}, 2000);

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
        JMenuItem saveSet = new JMenuItem("Save Training Set");
        
        JMenuItem start = new JMenuItem("New");
        JMenuItem about = new JMenuItem("About");
        JMenuItem exit = new JMenuItem("Exit");
        
        JMenuItem sound = new JMenuItem("Sound On/Off");
        JMenuItem grid = new JMenuItem("Show/Hide grid");
        
        start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.SaveDataSet();
            	StartGame(false);
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
        
        saveSet.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	board.SaveDataSet();
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
            	StartGame(true);
            }
        });
        

        easy.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	difficulty = 100;
            	StartGame(false);
            }
        });
        
        medium.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	difficulty = 60;
            	StartGame(false);
            }
        });
        
        hard.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	difficulty = 30;
            	StartGame(false);
            }
        });
        
        extreme.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	difficulty = 20;
            	StartGame(false);
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
        ai.add(saveSet);
        ai.add(trainNN);
        
        settings.add(sound);
        settings.add(grid);
        frame.setJMenuBar(menuBar);
	}
	
 
	// --------------- About Panel -------------------
	private void About() throws URISyntaxException{
		
		
	    URI  uri = new URI("https://github.com/spapapan/Artificial-Intelligence/tree/master/AI-Snake");
		
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

        
		JLabel lb3 = new JLabel("<html>Teach the snake how to play by playing the game.</html>", SwingConstants.CENTER);
		lb3.setFont(new Font ("Calibri", 0 , 14));
        lb3.setBounds(2,2, 400,200);
		
		JLabel lb4 = new JLabel("<html>Train the snake to play by clicking Neural Network<br> /Train Neural Network (This will take a while).</html>", SwingConstants.CENTER);
		lb4.setFont(new Font ("Calibri", 0 , 14));
		lb4.setBounds(2,50, 400,200);
		
		JLabel lb2 = new JLabel("<html>If you want to train the Snake another time you can<br>save the dataset from Neural Network/Save Training Set<br> </html>", SwingConstants.CENTER);
		lb2.setFont(new Font ("Calibri", 0 , 14));
		lb2.setBounds(2,100, 400,200);
		
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
	
	
	// ------------ Create Neural Network ----------------
	private static void createNN(int inputs,int hidden,int outputs){
		
		File nnet = new File("NeuralNetwork");
		if (!nnet.exists()){
			
			JFrame f = new JFrame();
			f.setPreferredSize(new Dimension(300,150));
			f.setLocation(800,500);
			f.setTitle("Wait a moment...");
			f.pack();
			f.setVisible(true); 
			
			JPanel p = new JPanel();
			p.setLayout(null);
			JLabel lb = new JLabel("Creating empty neural network...");
			lb.setBounds(20,30,250,40);
			p.add(lb);
			f.add(p);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask(){
				 @Override
				  public void run() {
					 Encog nn = new Encog();
					 nn.CreateNeuralNetwork(inputs, hidden, outputs);

				 }
			}, 3000);
			
			timer = new Timer();
			timer.schedule(new TimerTask(){
				 @Override
				  public void run() {
					 Encog nn = new Encog();
					 lb.setText("Neural network created!");
					 lb.setBounds(50,30,250,40);
				 }
			}, 3000);
			
			timer = new Timer();
			timer.schedule(new TimerTask(){
				 @Override
				  public void run() {
					 f.dispose();
					 new Main(false);
				 }
			}, 4000);
 
		    
		}
		else{
			 new Main(false);
		}

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
		
		createNN(900,450,4); // Create empty neural network if there is none              
		
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
	}
 

}
