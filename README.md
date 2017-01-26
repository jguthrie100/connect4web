# connect4web

Guide:

Start server with:
  mvn clean compile exec:java

To start a new game go to:
 http://localhost:8080/game/new/<player1name>/<player2name>/  (put in any string values you want to use as identifiers for player 1 and 2)
 
You'll be redirected to the game page where you can start making moves.
There is no user verification, so just putting the name of the player you want to be in the textbox will let you make moves for that player.

To go back to an old game, just navigate to:
http://localhost:8080/game/<game id>

TODO:

Improve Server Sent Events - is there a way to call sse.sendMessage() when a player makes a move for example, rather than endlessly looping and polling the state of the current Game to see if a move has been made.

Update the page using Ajax rather than by refreshing it.

Create player login profiles.

Error pages and handling non existent ids etc.

Decide whether to save data after every move or whether to just save all the moves and save them in bulk every 5 minutes or at the end of a game for example.

REQUIREMENTS:

Sql database - Details in DBAccessor.java
