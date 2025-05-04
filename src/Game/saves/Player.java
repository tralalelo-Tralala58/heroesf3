package Game.saves;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Player implements Serializable {
    public String name;
    public int score;

    @JsonProperty("score")
    public int getScore() {
        return score;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("score")
    public void setScore(int score) {
        this.score = score;
    }

    @JsonCreator
    public Player(@JsonProperty("name") String name){
        this.name = name;
        this.score = 0;
    }
}
