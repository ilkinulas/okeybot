#Okey Bot
## AI implementation of popular Turkish game in Java, Okey.

This is an open source Java implementation of Okey Game. Okey is a popular Turkish game which is played with four players. <a href="http://www.pagat.com/rummy/okey.html">Here</a> is a detailed description and rules of the game. 

Popular android game [Okey Mini](https://play.google.com/store/apps/details?id=com.appsonfire.okey) uses this library.

com.appsonfire.okey.bot.simulation package contains classes that simulates a game between for (smart) okey bots (players).


You can implement your own version of SmartOkeyBotInterface:
```java
public interface OkeyBotInterface {
	public Hand play(Tile joker, List<Tile> tiles);	
}
```