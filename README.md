# Hearts

My first Java project was [hearts-java](https://github.com/kr-matthews/hearts-java), implementing the card game [Hearts](https://en.wikipedia.org/wiki/Black_Lady).
After making many mistakes and learning a lot, I redid that project here.

Currently it can only be played on a single screen (so effectively only one human player per game), but it was designed to easily print to different print-streams to allow for multiple interfaces/screens to be used.

### Play

To play, clone this repository to your computer (or download the zip file from GitHub and extract that). Also install a Java IDE, such as [Eclipse](https://www.eclipse.org/downloads/) or [IntelliJ](https://www.jetbrains.com/idea/download/). In the IDE, import the project folder as an existing project, and run the project. It will automatically start a game in the (text-based) console, with you against 3 computer players. To change the amount of players, and the player names, edit the lists in the PlayHearts.java file.

### Features

You can play (with a text-based interface) a game with 1-52 players, with your opponents being the computer player I programmed, which plays a simple but reasonable strategy. Gameplay includes all standard rules (breaking hearts, shooting the moon, not losing by shooting the moon, etc.).
