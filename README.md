# FIRST Power-Up Simulator
This program simulates simple scenarios for FIRST Power-Up games in an attempt to formulate the best strategies to employ during competition.  
Created by Oliver Byl of [Team 4152](https://hoyarobotics.wixsite.com/team4152-hoya).

## How to Download
An executable jar has been included in the "build" folder (PowerUpSim.jar).  
Additionally, you can download the source code and compile it yourself.

## How to Use
Configure the tasks each robot will perform and then hit the simulate button to get a breakdown of the match.  
You can also configure when to use different power ups. It's recommended that you first run a version of your game without powerups, then add them in according to the seconds when you'll need them. This is because of the way power ups are implemented in this simulator.

## Limitations / Notes
* When moving robots, the simulation does not account for collision with other robots.
* The simulation does not actively try to account for human error.
* The simulation does not account for the ability of humans to return cubes through the exchange.
* The simulation assumes success of each task on the first attempt.
* In the game, the starting positions of the scale and switches are randomized. The simulation does not account for this when calculating times of cube delivery.
* In the game, each portal has 7 cubes, but cubes are subtracted for preloading robots. The simulation assumes that each robot is pre-loaded with 1 cube, but does not account for that in the portal cubes. The result is that the simulation has 3 more cubes in circulation than the actual game.
* The simulation assumes that every cube delivered through the exchange is placed directly into the vault.
* The simulations randomly (within certain parameters) generated the performance of the opponent alliance when considering cubes placed on switches and the scale.
  * EXTRA NOTE: these randomly generated opponents were created before any competitions took place, and are therefore just my best guess at possible performance.
  
Times supplied should be considered an underestimation for safety.

## Credit
Design and programming by Oliver Byl.  
Analysis techniques based on those used [Team 254](https://www.team254.com/) for FIRST Steamworks.  
A* search algorithm Java implementation by [Yan Chernikov](https://github.com/TheCherno).