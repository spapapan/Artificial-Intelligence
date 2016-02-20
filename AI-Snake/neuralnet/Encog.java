package neuralnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

public class Encog {

	
	public Encog(){
		
	}
	
	public int getNNResults(BasicNetwork network, double[] inputs){
		
		MLData data = new BasicMLData(inputs);
		MLDataPair ml = new BasicMLDataPair(data);
		MLData output = network.compute(ml.getInput());
		double out[] = output.getData();
		int index=0;
		double max = out[0];
		
        	for (int i=0; i< 3; i++){
        		if (max < out[i+1]){
        			max = out[i+1];
        			index = i+1;
        		} 
        	}
        return index;
		
	}
	

	
	public void TrainNeuralNetwork(){
		

		MLDataSet trainingSet = getDataSet();

		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println(timeStamp + " : DataSet entries: " + trainingSet.size());
		    
		BasicNetwork network =(BasicNetwork)EncogDirectoryPersistence.loadObject(new File("NeuralNetwork"));
		
		final Train train = new ResilientPropagation(network, trainingSet);

		double minimumError = 0.05;
		int maxit=0;
		System.out.println("Minimum acceptable Error : " + minimumError);
		System.out.println("---- Start Training -----");
		do {
		  train.iteration();
		  System.out.println("Iteration : " + maxit + " Error: " + train.getError());
		  maxit++;
		} while(train.getError() > minimumError && maxit < 100);
		
		System.out.println("Total error: " + train.getError());
		
		EncogDirectoryPersistence.saveObject(new File("NeuralNetwork"), network);
		timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        	System.out.println(timeStamp + " : Neural Network trained!");
        
		
	}
	
	
	// -------------- Create Empty Neural Network ------------------
	public void CreateNeuralNetwork(int inputs, int hidden, int outputs){

	        BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true,inputs));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true,hidden));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true,outputs));
		network.getStructure().finalizeStructure();
		network.reset();
		
		EncogDirectoryPersistence.saveObject(new File("NeuralNetwork"), network);
		
		System.out.println("NeuralNetwork created with " + inputs + " inputs, " + hidden + " hidden neurons and 4 outputs");
	}
	
	
	
	// ------------- Get Data Set from File ---------------------------
	private MLDataSet getDataSet(){
		
	    MLDataSet trainingSet = null;
		
	    int totallines=0;
	    try (BufferedReader br1 = new BufferedReader(new FileReader("dataset.txt"))) {
	    	String line1;
	        while ((line1 = br1.readLine()) != null) {
	    	    if (!line1.equals("")){
	    		    totallines++;
 
	    	    }
	        }
		} catch (IOException e) {
			e.printStackTrace();
	        }
		
		try (BufferedReader br = new BufferedReader(new FileReader("dataset.txt"))) {
 
		    String line;
		    String head;
		    String snake;
		    String target;
		    String decision;
		    
		    double input[][] = new double[totallines][900];
		    double output[][] = new double[totallines][4];
		    
		    int i=0;
		    while ((line = br.readLine()) != null) {
 
		    	if (!line.equals("")){
		    		
		            head = line.substring(line.indexOf("<h>") +3, line.indexOf("</h>"));
		            snake = line.substring(line.indexOf("<snake>") + 7, line.indexOf("</snake>"));
		            target = line.substring(line.indexOf("<t>") + 3, line.indexOf("</t>"));
		            decision = line.substring(line.indexOf("<d>") + 3, line.indexOf("</d>"));
		            
		            // ---------------- Input ------------------
		            input[i][Integer.parseInt(target)] = 0.5;
		            while (!snake.equals("")){
		            	input[i][Integer.parseInt(snake.substring(0,snake.indexOf(",")))] = 0.9;
		            	snake = snake.replaceFirst(snake.substring(0,snake.indexOf(",")+1), "");
		            }
		            input[i][Integer.parseInt(head)] = 1;
		            
		            // -------------- Output ---------------------
		            output[i][Integer.parseInt(decision)] = 1;
 
		            i++;
		    	}
		    }
		    
		    trainingSet = new BasicMLDataSet(input,output);
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DeleteDataSet();
		return trainingSet;
		
	}
	
	
	// --------------- Get Loaded Neural Network ------------------------
	public BasicNetwork LoadNeuralNetwork(){
		return (BasicNetwork)EncogDirectoryPersistence.loadObject(new File("NeuralNetwork"));
	}
	
	// ------------ Delete Neural Network DataSet -----------
	public void DeleteDataSet(){
		File datafile = new File("dataset.txt");
		if (datafile.exists()){
		     datafile.delete();
		}
	}
	
	
	
	
	
}
