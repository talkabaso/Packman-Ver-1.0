# **EX3-Project**

## *packman and fruit game*

Our project is a pack-man game that include fruits and pack-mans each of them has a unique data that influence the moving of the game.

The goal of the game is to calculate the time taken for all the Pack-mans together to eat all the fruits, in addition each Pacman collects points depending on the weight of the fruits he ate.


<img src="https://imagizer.imageshack.com/img924/3935/ahoICm.png" width="750" height="450" >


**There are two possibilities for creating game:**

1.open csv file

2.create game by yourself: **Create fruit by short press, create pack-man long press.**

*Here is some description of the project's design:*

The packages are:

- **Geom:**
Point3D- represent a geometric element in a Point3D object. represents an object that stores 3 values usually it will be latitude longitude and altitude.in addition this object stores timestamp it will be the time related to the start time of the game. 

- **Coords:**
Including some functions on three-dimensional space points as distance, angle calculation
We will use this class mainly when we want to calculate a distance between two three-dimensional points

- **fileFormat:** 

###### *read csv-*
Helps us to read csv files using the information written there to create a game that contains the pack-mans and fruits that appear in the file.

<img src="https://imagizer.imageshack.com/img924/7779/Ao93Ml.png" width="500" height="200" >

###### *write csv-*
helps us to save the game information in a csv file by reading the game information and creating an appropriate string to be saved as a csv.
###### *Path2KML-*
One more option is to save the information of the game as kml file This file will store information including the location of fruits and pack-mans on the map, the paths of the them on the way to eating the fruit, and the time at which the fruits were eaten.
This class allows the game to be viewed as a kml file with the possibility to see the game's course including the motion of the pack-mans and the eating depending on time.

<img src="https://imagizer.imageshack.com/img923/2107/L961qs.png" width="700" height="400" >

- **GIS:**
###### *Fruit/packman-*
Including the gis Elements (pack-man,fruit) each of them stores a unique data and position.
###### *Game-*
collection of the gis Elements ,in this function we have the possibility to create a game from specific csv file.in addition this class has the aggregate score of all the pack-mans and the start time of the game.
From this class we can call to function that shows us the game(location of the elements) in gui window.
###### *Solution–*
this class stores all the paths of the pack-mans and allows as to get this information in order to Show the course of the game.

- **Gui**
This class allows us to view the game on a screen. Under the file menu, you can select one of the following functions:
###### *Open-*
open csv file from directory on your computer.
###### *Save-*
save game as csv file, you allow to save game that you created by placing the fruit and pack-mans on the screen or game that you edit.
###### *Run-*
this button start the game by running our algorithm "calcAll", in that moment we can see the moving of all pack-mans on the screen to the fruits according to calculations in the algorithm.
###### *Clear-*
this button remove all the previous data that stores in the game and shows as empty gui screen.
###### *Save as kml-*
this button allow as to save all the data that stores in the game as kml file. This kml file has the possibility to show the game's course including the motion of the pack-mans depending on time.

- **Map**
This class holding the information of our fame map, the coordinates were taken from the specific location that the picture depicts and therefore each calculation of distance in the game is actually a distance calculation that relates to this place.
###### *Converts-*
including all the converts function from pixels to coordinates and vice versa. We can converts sole element or even collection of elements from both types.in addition this class helps as to calculate the distance and angle between two pixels.
###### *Path-*
this class stores collection of point3D elements each of them describe the location of the pack-man in his way to the fruits and the updated time of each point according to the time related to the start time and the progress of the pack-man.
###### *Pix-*
this class contains two elements x and y that represents pixels in image.

- **Algo**
This is the department of calculations related to the performance of the game in this department is carried out a major algorithm to calculate the preferred way for each Pacman so that all the fruits will be eaten as fast as possible considering the time it takes each Pacman to reach the fruit.
the main function is: calcAll (Repeat as long as there are fruits)
###### *calcAll :*
pass all the packmans for each of them find the index of fruit he should eat(the fast time to eat some fruit) calculate the time it takes and check if this time is the minimum(check for each packman with all the fruits). After this loop we found the fast packman to eat some fruit .
###### *Use packEatFruit function:*
add this fruit to the eatenFruit array of the packman. Add the weight of the fruit to his score and the time it takes to his total time.
###### *Use proccedd to fruit function:*
(Progress to the fruit each second by move in radius, set the packman position according to his current position after each step and add this position to his path).after that the fruit will remove from the collection of the fruits.
Update the game total score which will be the previous one plus the current weight of this fruit.
