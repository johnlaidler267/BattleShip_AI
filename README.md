# Overview
This is a java console version of the popular boardgame 'Battleship,' where users can test their skills against a playable AI.

- The game consists of two modes, easy and hard.
- In the start of the game, users are able to place their ships wherever they wish on the 8x8 grid. The location and orientation (horizontal/vertical) of the ship placement is entirely up to the user.
As in a normal battleship game, the user and opponent (AI) will take turns guessing each other's ships until one of the fleets are completely destroyed and a player is declared a winner.

## AI
### Easy mode 
- The AI is not smart at all. This opponent will guess completely randomly even when it finds a ship.

### Hard mode
The AI is vastly upgraded and utilizes some key features/strategies
- It will guess according to the checkerboard strategy. This essentially means it will guess only every other location on the grid in order to cut the amount of guesses in half.
- Once the AI finds a ship, it will destroy the remainder of the ship before continuing on.
