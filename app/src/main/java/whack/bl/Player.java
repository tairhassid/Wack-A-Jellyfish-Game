package whack.bl;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Player {

    private String name;
    private int score;
    private LatLng gameLocation;

    public Player(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public LatLng getGameLocation() {
        return gameLocation;
    }

    public void setGameLocation(LatLng gameLocation) {
        this.gameLocation = gameLocation;
        Log.d("Player", "setGameLocation: " + gameLocation.latitude + " " + gameLocation.longitude);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
