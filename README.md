# A-Implementation
Implements the A* search algorithm 
The robot is allowed to ping anywhere on the map and will return whether or not it is a valid spot to move. 
My implementation uses the A Star algorithm by finding the path first and moving after. 

The program takes in the first argument as the path for the world file. 
An example would be java myRobot.java myInputFile4.txt
The O in the inputfile represents a open space while X represents blocked spaces. The robot starts on S and its destination is X. 

The program prints out stats at the end showing how many pings and moves are made. 

To run, it is important to put the HW2.jar as a dependency. 

myUncertainRobot.java has uncertainty in the validity of pings when the robot pings further from its location, adding further challenge to the problem. 
