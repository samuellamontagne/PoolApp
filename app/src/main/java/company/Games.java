package company;

import java.util.ArrayList;

public class Games {
    public int points;
    public int goals;
    public int assists;

    public void setPoints(int pointsDone){
        this.points = pointsDone;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public int getPoints() {
        return points;
    }

    public int getGoals() {
        return goals;
    }
}
