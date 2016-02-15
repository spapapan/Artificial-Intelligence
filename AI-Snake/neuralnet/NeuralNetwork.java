package neuralnet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class NeuralNetwork implements LearningEventListener{

	public NeuralNetwork() {
		
	}
 
       public void TrainNeuralNetwork(DataSet s){
 
            if (s.size() != 0){
   		    String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
   		    System.out.println(timeStamp + " : DataSet entries: " + s.size());
    	   
	    	@SuppressWarnings("deprecation")
			MultiLayerPerceptron loadSnakeNN = (MultiLayerPerceptron) MultiLayerPerceptron.load("SnakeNN.nnet");
        	if( loadSnakeNN.getLearningRule() instanceof MomentumBackpropagation )
        		((MomentumBackpropagation)loadSnakeNN.getLearningRule()).setBatchMode(true);

        	MomentumBackpropagation learningRule = (MomentumBackpropagation)loadSnakeNN.getLearningRule();
        	learningRule.setMaxError(0.1);
        
        	loadSnakeNN.learn(s);
        	timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
            System.out.println(timeStamp + " : Neural Network trained!");
        	loadSnakeNN.save("SnakeNN.nnet");
 
   		    }
 
	    }
	    

	    // --------------- Return Results of the Neural Network --------------------
        public int AIResults(double[] inputs){
 
        	MultiLayerPerceptron nn = (MultiLayerPerceptron) MultiLayerPerceptron.load("SnakeNN.nnet");
 
            nn.setInput(inputs);
            nn.calculate();
            double[] out = nn.getOutput();
            
            int index=0;
            for (int i=0; i< 3; i++){
            	if (out[i] > out[i+1]){
            		index = i;
            	} else{
            		index = i+1;
            	}
            	
            }
            return index;
        }

 

	@Override
	public void handleLearningEvent(LearningEvent arg0) {
		 BackPropagation bp = (BackPropagation)arg0.getSource();
	}
	
	
	
	// ---------------- Create Neural Network if it doesn't exist ---------
	public void CreateNN(int lines, int hiddenNeurons){

		File datafile = new File("SnakeNN.nnet");
		if (!datafile.exists()){
			int inputs = lines*lines;
			DataSet set = new DataSet(inputs,4);
			double input[] = new double[inputs];
			double output[] = new double[4];
			set.addRow(input,output);
			
	    	MultiLayerPerceptron NN = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputs, hiddenNeurons, 4);
	    	NN.save("SnakeNN.nnet");
	    	
	    	System.out.println("Neural Network created with : " + inputs + ", : " + hiddenNeurons + " and : 4 outputs");
		}
		
	}

}
