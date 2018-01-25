# Whitechapel
Whitechapel is a console based application which simulates the Board Game "Letters From Whitechapel". The project uses JgraphT library to build the board and provides the engine for moving detectives. User can play as Jack the Ripper against the computer.

![whitechapel box](https://cf.geekdo-images.com/images/pic971115_lg.jpg)
The board is represented by unweighted graphs that allows the user to move Jack and Detectives. The move of Jack is maintained using a MoveTree which is a directed graph.

## Installation
Install `Maven` which is a software project management and comprehension tool. `Maven` helps us install any library which is needed for this project. Follow this link below to install Maven:
[Maven – Installing Apache Maven](https://maven.apache.org/install.html)

## How this works
For full understanding of the game logic, please read the rules in the following link: [White Chapel Rules]( https://images-cdn.fantasyflightgames.com/filer_public/55/ff/55ff98ec-c39b-4607-9055-fadb150605dd/lfh_rules_letter_en_low_res.pdf)  
Notation in the graph:
* C27: Circle 27
* SC101S1: Square near Circle 101 located 1 move to the South
The logic and the rules of the game are strictly implemented to ensure future improvement of the project.

User can play as Jack and let the program handle detective by changing the `this.detectiveController = new DetectiveConsoleController(gb, detectives);` to `DetectivesEngineController`. 



## Built with
`JgraphT`- library that provides graph-theory objects and algorithms.

## Author
An Q Tran      
Anwar Reddick 

