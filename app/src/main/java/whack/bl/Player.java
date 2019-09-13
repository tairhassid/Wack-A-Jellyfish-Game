package whack.bl;

public class Player {

    private String name;
    private int score;

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

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
