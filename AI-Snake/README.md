<b>AI Snake</b> is a game with a snake chasing a target. When the snake eat the target it grows in size. 

-------------------------------------------------------------------------------------

<img src="https://40.media.tumblr.com/368b9958332c878409d211c844a641c2/tumblr_o2lnwdEKk91s1v7hso1_1280.png" width="600" height="600"></img>

----------------------------------------------------------------------------------------

It also contains an artificial neural network (NN) which learn while you play. The NN has 900 inputs that
indicates the location of the snake and the target, 1 hidden layer with 50 neurons and 4 output neurons.

To train the NN I used the backpropagation algorithm with the <a href="http://neuroph.sourceforge.net/">Neural Network Framework Neuroph</a>.

If you want to train the NN you must first create a training set.  Each time you play a new game, you have to enable it from 
Neural Network/Create Training Set. To train the NN go to Neural Network/Train Neural Network. The training process is time consuming and depends 
on the amount of the training set. Finally to test the NN go to Neural Network/AI Player.

If you want to run the java programm in your IDE, you will need to import <a href="https://sourceforge.net/projects/neuroph/files/neuroph-2.9/neuroph-2.9.zip/download">neuroph-core-2.9.jar</a>,
 <a href="http://www.java2s.com/Code/Jar/s/Downloadslf4jsimple161jar.htm">slf4j-simple-1.6.1.jar</a>
, <a href="http://www.java2s.com/Code/Jar/s/Downloadslf4japi160jar.htm">slf4j-api-1.6.0.jar</a>
, <a href="http://www.jtattoo.net/downloads/JTattoo-1.6.11.jar">JTattoo-1.6.11.jar</a>
