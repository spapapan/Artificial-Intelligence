package snake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.neuroph.core.data.DataSet;

import neuralnet.NeuralNetwork;

public class Board extends JPanel implements KeyListener{
	
	// --- Set Density of Grid ---
	public final int lines = 30;
	// ---------------------------

	NeuralNetwork neuralnet;
	DataSet NNset = new DataSet(lines*lines,4);
	Boolean AIplayer;
	
	int totalmoves=0;

	public final int width =  600;
	public final int height = 600;
	
	public Clip eatSound;
	public Clip impactSound;

	public Point p1 = new Point(0,0);
	public Point p2 = new Point(0,0);
	
	public Point target;
	
	public Boolean paintGrid = false;
	public Boolean pause = true;
	public Boolean createSet = false;
	public Boolean onoffsound = true;
	
	public Boolean pushbtn=true;
	
	public Dimension cube;
	public int cellX[][];
	public int cellY[][];
	public final int ymargin = 50;
	public final int xmargin = 50;
	
	public String direction = "up";
	public Boolean goBig = false;
	public Boolean canDraw = true;
	public Boolean startgame = false;
	public Boolean newgame = false;
	public Boolean saveDataSet = false;
	
	public JLabel lbscore;
	public int totalscore;
	public int score = 100;
	public int minus = 1;
	
	int difficulty = 100;
	
	public Timer timer;
	
	public int eatColor;
	
	LinkedList<Point> Snake = new LinkedList<Point>();
	
	LinkedList<double[]> NNinputList = new LinkedList<double[]>();
	LinkedList<double[]> NNoutputList = new LinkedList<double[]>();
	// Output: 0 : left, 1: right, 2: up, 3: down
	
	public Board(int diff, Boolean AIPlayer, Boolean saveSet, Boolean OnSound) {
		
    
		neuralnet = new NeuralNetwork();
		AIplayer = AIPlayer;
		saveDataSet = saveSet;
		loadDataSet();
		
		onoffsound = OnSound;
		
		eatSound = loadSound("eat");
		impactSound = loadSound("impact");
		eatColor=0;
		
		difficulty = diff;
		lbscore = new JLabel("Score : 0");
		lbscore.setFont(new Font("TimesRoman", Font.BOLD, 20));
		lbscore.setBounds(290,10,290,30);
		this.add(lbscore);
	        addKeyListener(this);
	        this.setFocusable(true);
	        this.requestFocus();
		
		startgame = true;
		newgame = true;
		
		initCoor();// Initialize coordinates
		initSnake();// Initialize Snake
		
		drawTarget();
	    repaint();
	    
	    StartGame();
	}
	
	
	
	
	public void Start(){

		startgame = true;
		newgame = true;
		
		initCoor();// Initialize coordinates
		initSnake();// Initialize Snake
		
		drawTarget();
	    repaint();
	    
	    StartGame();
	}
	
	// ---------------------- Start Timer ------------------
	public void StartGame(){
		
	
	    timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {

			    
				  if (AIplayer == false){
 
                    if (direction.equals("up")){
                    	Up();
                    }
                    else if(direction.equals("down")){
                    	Down();
                    }
                    else if(direction.equals("left")){
                    	Left();
                    }
                    else if(direction.equals("right")){
                    	Right();
                    }
                    pushbtn = true;
				  }
                  else{
                	  
            			double input[] = new double[lines*lines];
            			for (Point p : Snake){
            				input[(p.x)*lines + p.y] = 1; // Snake's position
            			}
            			input[(target.x)*lines + target.y]= 0.5; // Target's position 

            			int desition = neuralnet.AIResults(input);
            			if (desition == 0){
            				Left();
            			}
            			else if (desition == 1){
            				Right();
            			}
            			else if (desition == 2){
            				Up();
        
            			}
            			else if (desition == 3){
            				Down();
            			}
            	  }
			 
				  
				  
				  
		     }
	      }, difficulty, difficulty);
	 
	
	    
	}


	
	
	
	// ------------------------ Direction Decisions ----------------------------------
	
	public void Left(){
		
		Boolean wallturn = true;
		if (Snake.size()>2){
			if (Snake.get(Snake.size()-1).y - Snake.get(Snake.size()-2).y == -lines+1){
				wallturn= false;
			}
		}
		
		if (Snake.get(Snake.size()-1).y - Snake.get(Snake.size()-2).y != 1){
          if (wallturn){
			if (Snake.getLast().y != 0){
		                Snake.add(new Point(Snake.getLast().x,Snake.getLast().y-1));
			}
			else{
				Snake.add(new Point(Snake.getLast().x,lines-1));
			}
			if (!goBig){
				Snake.removeFirst();
			}
			goBig = false;
    		        checkImpact();
	                repaint();
			EatTarget();
			direction = "left";
			score = score - minus;
			
            // Create Dataset
			if (createSet){
			addTrainingSet(0);
			}

          }
		}

	}
	
	public void Right(){

		Boolean wallturn = true;
		if (Snake.size()>2){
			if (Snake.get(Snake.size()-1).y - Snake.get(Snake.size()-2).y == lines-1){
				wallturn= false;
			}
		}
		
		if (Snake.get(Snake.size()-1).y - Snake.get(Snake.size()-2).y != -1){
		  if (wallturn){
			if (Snake.getLast().y != lines-1){
		          Snake.add(new Point(Snake.getLast().x,Snake.getLast().y+1));
			}
			else{
		          Snake.add(new Point(Snake.getLast().x,0));
			}
			if (!goBig){
				Snake.removeFirst();
			}
			goBig = false;
    		        checkImpact();
	                repaint();
	  		EatTarget();
			direction = "right";
			score = score - minus;
			
            // Create Dataset
			if (createSet){
			addTrainingSet(1);
			}
		  }
		}

	}
	
	public void Up(){

		Boolean wallturn = true;
		if (Snake.size()>2){
			if (Snake.get(Snake.size()-1).x - Snake.get(Snake.size()-2).x == -lines+1){
				wallturn= false;
			}
		}
		
		if (Snake.get(Snake.size()-1).x - Snake.get(Snake.size()-2).x != 1){
		  if (wallturn){	
			if (Snake.getLast().x != 0){
		           Snake.add(new Point(Snake.getLast().x-1,Snake.getLast().y));
			}
			else{
		           Snake.add(new Point(lines-1,Snake.getLast().y));
			}
			if (!goBig){
			   Snake.removeFirst();
			}
			goBig = false;
    		        checkImpact();
	                repaint();
			EatTarget();
			direction = "up";
			score = score - minus;
			
            // Create Dataset
			if (createSet){
			addTrainingSet(2);
			}
		  }
		}

	}
	
	public void Down(){

		Boolean wallturn = true;
		if (Snake.size()>2){
			if (Snake.get(Snake.size()-1).x - Snake.get(Snake.size()-2).x == lines-1){
				wallturn= false;
			}
		}
		
		if (Snake.get(Snake.size()-1).x - Snake.get(Snake.size()-2).x != -1){
		  if (wallturn){	
			if (Snake.getLast().x != lines-1){
	                     Snake.add(new Point(Snake.getLast().x+1,Snake.getLast().y));
			}
			else{
	                     Snake.add(new Point(0,Snake.getLast().y));
			}
			if (!goBig){
			     Snake.removeFirst();
			}
			goBig = false;
    		checkImpact();
                repaint();
    		EatTarget();
    		direction = "down";
    		score = score - minus;
    		
            // Create Dataset
			if (createSet){
			addTrainingSet(3);
			}
		  }
		}
	}
	
	// --------- Set up Training Set for Neural Networks ------------
	public void addTrainingSet(int i){
		double input[] = new double[lines*lines];
		for (Point p : Snake){
			input[(p.x)*lines + p.y] = 1; // Snake's position
		}
		input[Snake.getLast().x*lines + Snake.getLast().y]= 2; // Head of the snake
		input[(target.x)*lines + target.y]= 5; // Target's position 
		double output[] = new double[4];
		output[i] = 1;
		NNset.addRow(input,output);
	}
	
	
	// ----------- Check Impact ----------------
	public void checkImpact(){
		for (int i=0;i<Snake.size()- 2;i++){
			if (Snake.get(i).x == Snake.getLast().x && Snake.get(i).y == Snake.getLast().y){
				if (onoffsound){ playSound(impactSound); };
				timer.cancel();
				canDraw = false;
				if (saveDataSet){
                SaveDataSet();
				}
			}
		}
	}
	
	// ----------- Eat Target -----------------
	public void EatTarget(){

		if (Snake.getLast().x == target.x && Snake.getLast().y == target.y){
			
			if (onoffsound){playSound(eatSound);};
			drawTarget();
			goBig = true;
			if (score >0){
			    totalscore = totalscore + score + 100;
			}
			else{
			    totalscore = totalscore + 100;
			}
			lbscore.setText("Score : " + Integer.toString(totalscore));
			score = 100;
			eatColor = eatColor +5;
		}
	}

	// -------- Draw Snake -----------
	public void drawSnake(Graphics g){
		int c1=0;
		try{
		for (Point snake : Snake){
			if (c1==Snake.size()-1){
				g.setColor(Color.RED);
			}
			else{
				if (eatColor<100){
				    g.setColor(new Color(eatColor,eatColor,255));
				}
				else if(eatColor<255 && eatColor >= 100){
					g.setColor(new Color(eatColor,100,255));
				}
				else{
					g.setColor(new Color(255,100,255));
				}
			}
			g.fillRect(cellX[snake.x][snake.y],cellY[snake.x][snake.y], cube.width, cube.height);
			c1++;
		}
		} catch (ConcurrentModificationException e){
			System.out.println("ConcurrentModificationException");
		}
		g.setColor(Color.black);
		 
	}
	
	// -------- Draw Target -------------
	public void drawTarget(Graphics g){
		Random r = new Random();
		g.setColor(new Color(r.nextInt(130),200,r.nextInt(130)));
        int size = r.nextInt(30)-1 + 10;
		g.fillRoundRect(cellX[target.x][target.y], cellY[target.x][target.y], cube.width, cube.height, size, size);
		g.setColor(Color.black);
	}
	
	
	
	// ----- Set Grid -------
	public void PaintGrid(){
		if (paintGrid){
		paintGrid = false;
		}
		else{
			paintGrid = true;
		}
	}
	
	// ------------ Draw Grid ---------------
	public void drawGrid(Graphics g){
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, 1, 800, 1);
		
		g.setColor(new Color(30,150,150));
		Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
		g.drawLine(xmargin, ymargin, width+xmargin, ymargin);
		g.drawLine(xmargin, ymargin, xmargin, height+ymargin);
		g.drawLine(xmargin, height+ymargin, width+xmargin, height+ymargin);
		g.drawLine(width+xmargin, ymargin, width+xmargin, height+ymargin);

		if (paintGrid){
		int c0 = width/lines;
		for (int j=0; j<2; j++){
			int c1 = width/lines + ymargin;
			for (int i=0; i<lines; i++){
				
				if (j==0){
					p1 = new Point(c1,xmargin);
					p2 = new Point(c1,height+ymargin);
				}
				
				if (j==1){
					p1 = new Point(xmargin,c1);
					p2 = new Point(width+xmargin,c1);
				}

				c1 = c1 + c0;
 
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			 

			}
		}
		}
		
		g.setColor(Color.black);
	}
	
	
	// ------------- Initialize Snake ----------------
	public void initSnake(){
		Random ran = new Random();
		int r1 = ran.nextInt(lines);
		int r2 = ran.nextInt(lines);
		Snake.add(new Point(r1,r2));
			if (r2>0){
				Snake.add(new Point(r1,r2-1));
			}
			else{
				Snake.add(new Point(r1,r2+1));
			}
		
	}
	
	// ------------ Initialize Target -----------------
	public void drawTarget(){
		Random r = new Random();
		int ran1 = r.nextInt(lines);
		int ran2 = r.nextInt(lines);

		while(Snake.contains(new Point(ran1,ran2))){
			ran1 = r.nextInt(lines);
			ran2 = r.nextInt(lines);
		}

		target = new Point(ran1,ran2);

	}
	
	// ------------- Initialize Coordinates ---------------
	public void initCoor(){
		
	    cellX = new int[lines][lines];
	    cellY = new int[lines][lines];
	    cube = new Dimension(width/lines,height/lines);

	    int ycoor=0;
	    for (int i=0;i<lines;i++){
	    	int xcoor=0;
	    	for (int j=0; j<lines; j++){
	    		cellX[i][j] = xcoor + xmargin;
	    		cellY[i][j] = ycoor + ymargin;
	    		xcoor = xcoor + cube.width;
	    	}
	    	ycoor = ycoor + cube.height;
	    }
	}
	
	// -------------- Load Data Set ---------------------
	public void loadDataSet(){
		File datafile = new File("dataset");
		if (datafile.exists()){
			DataSet tempSet = new DataSet(lines*lines,4);
			NNset = tempSet.load("dataset");
		}
	}
 
	// ------------- Save Neural Network DataSet ---------------
	public void SaveDataSet(){
		timer.cancel();
		File datafile = new File("dataset");
		if (datafile.exists()){
			DataSet saved = new DataSet(NNset.getInputSize(),NNset.getOutputSize());
			saved = NNset;
			saved.save("dataset");
		}
		else{
			try {
				datafile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DataSet saved = new DataSet(NNset.getInputSize(),NNset.getOutputSize());
			saved = NNset;
			NNset.save("dataset");
		}
	}
	
	// -------------- On/Off Sound --------------
	public void OnOffSound(){
		if (onoffsound){
			onoffsound = false;
		} else{
			onoffsound = true;
		}
	}
	
	// -------------- Pause Game ----------------------
	public void pauseGame(){
		if (pause){
			timer.cancel();
		    pause = false;
		}
		else{
			StartGame();
			pause = true;
		}
	}
	
	// ----------- Create DataSet ------------------
	public void createDataSet(){
		if (!createSet){
			saveDataSet = true;
		    createSet = true;
		    System.out.println("Creating dataset...");
		} else{
			createSet = false;
			saveDataSet = false;
			System.out.println("Dataset created");
		}
	}
 
	public void playSound(Clip clip){
		
		if (clip != null){
		clip.stop();
		clip.setMicrosecondPosition(0);
		clip.start();
		}
		
	}
	
	// ---------------- Load Sounds --------------------
	public Clip loadSound(String sound){
 
		 
	    Clip clip = null;
		File file = new File(sound+".wav");
		if (file.exists()){
			try {

			AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;
		    stream = AudioSystem.getAudioInputStream(file);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
			
			}
			catch (Exception e) {
	
			}
		}
		
		return clip;
	 
	}
	
 

	// ----------------- PAINT -----------------------------
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
  
        if (newgame){
		drawGrid(g);
		drawTarget(g);
		drawSnake(g);

		if (!canDraw){
			for (Point snake : Snake){
			   g.setColor(Color.MAGENTA);
			   g.fillRect(cellX[snake.x][snake.y],cellY[snake.x][snake.y], cube.width, cube.height);
			}
			g.setColor(Color.white);
			g.fillRect(cellX[Snake.getLast().x][Snake.getLast().y],cellY[Snake.getLast().x][Snake.getLast().y], cube.width, cube.height);
			startgame = false;
		}
        }
 
	}

	
	
	// ----- Press Button -----------
	@Override
	public void keyPressed(KeyEvent e) {

		 int keyCode = e.getKeyCode();
		 if (startgame){
		   if (pushbtn){
		    switch( keyCode ) { 
		    

		        case KeyEvent.VK_UP:
		        	if (!direction.equals("down")){
		            direction = "up";
		        	}
		            break;
		        case KeyEvent.VK_DOWN:
		        	if (!direction.equals("up")){
		        	direction = "down";
		        	}
		            break;
		        case KeyEvent.VK_LEFT:
		        	if (!direction.equals("right")){
		        	direction = "left";
		        	}
		            break;
		        case KeyEvent.VK_RIGHT :
		        	if (!direction.equals("left")){
		        	direction = "right";
		        	}
		            break;
		    }
		   pushbtn = false;
		   }
		 }
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	


}
