package company;

import java.util.ArrayList;

public class Games {

    public int goals;
    public int assists;

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
        return goals + assists;
    }

    public int getGoals() {
        return goals;
    }
}
