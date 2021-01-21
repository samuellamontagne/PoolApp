package company;

import java.util.ArrayList;

public class Players {

    public String fullName;
    public int ID;
    public String position;
    public int teamID;
    public int points;
    public int assists;
    public int goals;
    public int pointsHot;
    public int assistsHot;
    public int goalsHot;
    public ArrayList<Games> lastSixGamesPlayed = new ArrayList<Games>();
    public String belongTo;
    public boolean isHot = false;
    public boolean isTrending = false;
    public String teamName;

    public void setIsHot(boolean hot) {
        isHot = hot;
    }

    public boolean getIsHot() {
        return isHot;
    }

    public boolean isTrending() {
        return isTrending;
    }

    public void setTrending(boolean trending) {
        isTrending = trending;
    }

    public void setLastSixGamesPlayed(Games lastGamePlayed) {
        this.lastSixGamesPlayed.add(lastGamePlayed);
    }

    public ArrayList<Games> getLastSixGamesPlayed() {
        return lastSixGamesPlayed;
    }

    public void setFullName(String newfullName) {
        this.fullName = newfullName;
    }

    public void setID(int newID){
        this.ID = newID;
    }

    public void setPosition(String newPosition){
        this.position = newPosition;
    }

    public void setTeamID(int newTeamID){
        this.teamID = newTeamID;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setBelongTo(String playerBelongTo) { this.belongTo = playerBelongTo; }

    public int getAssists() {
        return assists;
    }

    public int getGoals() {
        return goals;
    }

    public int getPoints() {
        return points;
    }

    public void setAssistsHot(int assists) {
        this.assistsHot = assists;
    }

    public void setGoalsHot(int goals) {
        this.goalsHot = goals;
    }

    public void setPointsHot(int points) {
        this.pointsHot = points;
    }

    public int getAssistsHot() {
        return assistsHot;
    }

    public int getGoalsHot() {
        return goalsHot;
    }

    public int getPointsHot() {
        return pointsHot;
    }

    public int getID() {
        return ID;
    }

    public int getTeamID() {
        return teamID;
    }

    public String getBelongTo() { return belongTo; }

    public String getFullName() {
        return fullName;
    }

    public String getPosition() {
        return position;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return("\n\nFull Name :" + this.fullName + "\nBelongs to : " + this.belongTo + "\nPostion :" + this.position + "\nStats for 6 games : \nPoints  : " + this.points + "\nGoals   : " + this.goals + "\nAssists : " + this.assists + "\nStats for 3 games : \nPoints  : " + this.pointsHot + "\nGoals   : " + this.goalsHot + "\nAssists : " + this.assistsHot);
    }

}
