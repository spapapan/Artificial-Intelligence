<b>AI Snake</b> is a game with a snake chasing a target. When the snake eat the target it grows in size. 
It also contains an artificial neural network (ANN) which learn while you play. 

-------------------------------------------------------------------------------------

<img src="https://40.media.tumblr.com/368b9958332c878409d211c844a641c2/tumblr_o2lnwdEKk91s1v7hso1_1280.png" width="600" height="600"></img>

----------------------------------------------------------------------------------------

The ANN has 900 inputs that indicates the location of the snake and the target, 1 hidden layer with 450 neurons and 4 output neurons. Each output neuron indicates if the snake will move left,right,up or down.

To train the ANN I used the backpropagation algorithm with the <a href="http://www.heatonresearch.com/encog/">Neural Network Framework Encog</a>.

To train the ANN go to Neural Network/Train Neural Network. The training process is time consuming and depends 
on the amount of the training sets. To test the ANN go to Neural Network/AI Player. If you want to create a dataset
and train the network another time, don't forget to save the dataset by pressing S or from Neural Network/Save Training Set before you exit.

If you want to run the java programm in your IDE, you will need to import <a href="encog-core-3.3.0-release.zip">encog-core-3.3.0.jar</a>,
, <a href="http://www.jtattoo.net/downloads/JTattoo-1.6.11.jar">JTattoo-1.6.11.jar</a>
<br></br>
<br></br>

You can download the runnable jar file along with the sound files <a href="https://drive.google.com/file/d/0B46-skjIP2h-VVplMV94QUJLanM/view?usp=sharing">from here</a>. To run the program extract the zip file, open your terminal/cmd and type:

    cd "the location of the folder you extracted"
    java -Xss2m -jar AISnake.jar
    

*To pause the game press space bar.

Have fun !
