# My first game

I built this game in Spring 2019 Semester at UC Berkeley for the class CS 61B on data structures. This is a 2D game where the player controls his player to find locked doors to escape.  

### Setup
Use IntelliJ or another IDE to import the included libraries needed to run this game.

### How to play
Press 'n' to start a new game. You will be prompted to enter a seed, so enter any random number and press 's' to save. This will automatically start the game.  
Use the WASD keys to move around until you find a yellow tile. Move onto the tile to win. Type ':q' to quit and automatically save your progress. When you start up the game again, press 'l' to load your previous save.

### How I made it
I used a grid to draw tiles to form rooms and halls. The map is randomly generated using a binary space partitioning technique. The grid is divided up into sections recursively until a certain size. Then the rooms are generated and halls connect across the sections.  

### Bugs I encountered
There were some issues I encountered along the way. The first one was being able to generate well-shaped rooms. The first time I tried, the rooms were oddly shaped and not rectangular. I solved this by recognizing that the issue was that the rooms were generating too close to each other, creating weird shapes. Another issue I had was generating halls correctly. Since the tiling system is clunky to work with, there were a lot of edge cases to deal with. Sometimes the halls were connecting to the sides of the rooms. I solved this by limiting the amount of areas the halls could connect to. After I resolved map generation issues, I encountered problems with tracking keyboard inputs and states. I ultimately solved this by tweaking how I implemented keyboard inputs.
