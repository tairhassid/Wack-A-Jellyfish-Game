package whack.bl;

import android.content.Context;

import whack.data.DatabaseHelper;

public class GameManager {

    private static GameManager gameManager;
    private Player currentPlayer;

    private GameManager() {
    }

    public static GameManager getInstance() {
        if(gameManager == null)
            gameManager = new GameManager();
        return gameManager;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

}
