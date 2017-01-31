# connect4web

If you're lucky, there'll be a server instance up and running at: [http://ec2-35-156-8-164.eu-central-1.compute.amazonaws.com:8080/](http://ec2-35-156-8-164.eu-central-1.compute.amazonaws.com:8080/)

#Guide:

Start server with:
  mvn clean compile exec:java

To start a new game go to:
 [http://localhost:8080/](http://localhost:8080/) and click on "Start new game"
 
You'll be redirected to the game page where you can start making moves.
There is no user verification at the moment, so just select the player you wish to play as.
(Change the names by starting a new game manually and changing the player name values in the URL)

To go back to an old game(i.e. game 1), just navigate to:
[http://localhost:8080/game/1](http://localhost:8080/game/1)

#TODO:

Improve Server Sent Events - is there a way to call sse.sendMessage() when a player makes a move for example, rather than endlessly looping and polling the state of the current Game to see if a move has been made.

Update the page using Ajax rather than by refreshing it.

Create player login profiles.

Error pages and handling non existent ids etc.

#REQUIREMENTS:

Sql database - Details in DBAccessor.java
