package com.example.poolapp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.net.URL;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import company.ConnectAndGet;
import company.Games;
import company.Players;
import simple.JSONArray;
import simple.JSONObject;

import java.lang.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstFragment extends Fragment {

    public int nbTeams = 0;
    public int i = 0;
    public int nbPlayers = 0;
    public int players = 0;
    ConnectAndGet connectAndGet = new ConnectAndGet();

    /**
     * Add the numbers of points, goals and assists for multiple Games in one game
     *
     * @param gamesPlayer All the games played by a player
     * @param nbGamesToAdd The number of games to add together
     * @return The games with the stats added.
     * */
    public static Games total(ArrayList<Games> gamesPlayer, int nbGamesToAdd){
        Games finalGame = new Games();
        int tempPoints = 0;
        int tempAssists = 0;
        int tempGoals = 0;

        for(int i = 0; i < nbGamesToAdd; i++){
            Games curGame = gamesPlayer.get(i);

            tempAssists += curGame.getAssists();
            tempGoals += curGame.getGoals();
            tempPoints += curGame.getPoints();
        }

        finalGame.setPoints(tempPoints);
        finalGame.setAssists(tempAssists);
        finalGame.setGoals(tempGoals);

        return finalGame;
    }

    /**
     * Add the rosters from every boys together
     *
     * @param belongTo Who the player belong to
     * @param playerList The list of Players to add this roster to
     * @param teamRoster The roster that is to be added
     * @return Nothing, the list is passed by reference
     * */
    public static void getAllPoolPlayers(JSONArray teamRoster, ArrayList<Players> playerList, String belongTo){

        for(int j = 0; j < teamRoster.size(); j++){
            Players curPlayer = new Players();
            JSONObject playerNo = (JSONObject) teamRoster.get(j);
            JSONObject playerPoolEntry = (JSONObject) playerNo.get("playerPoolEntry");
            JSONObject playerInfos = (JSONObject) playerPoolEntry.get("player");
            String playerName = (String) playerInfos.get("fullName");
            curPlayer.setFullName(playerName);
            curPlayer.setBelongTo(belongTo);

            playerList.add(curPlayer);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    /**
     * Fetch the trending (7 pts in 6 games) and really trending (4 points in 3 games) players that are going to play today
     *
     * @param view The view of the application
     * @param handler The handler that allows to update the UI while fetching the players
     * @param trendingPlayerListTemp List of players that are trending
     * @param recentlyTrendingPlayersTemp List of players that are really trending
     * @return Nothing, the lists are passed by reference
     * */
    private void fetchDatas(View view, Handler handler, ArrayList<Players> trendingPlayerListTemp, ArrayList<Players> recentlyTrendingPlayersTemp){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL nhlSchedule = new URL("https://statsapi.web.nhl.com/api/v1/schedule"); // test with ?date=2020-08-26
            JSONObject schedule = connectAndGet.returnObj(nhlSchedule);
            Long nbTeamsLong = (2 * (Long) schedule.get("totalGames"));
            nbTeams = nbTeamsLong.intValue();
            int[] teamsId = new int[nbTeams];
            String[] teamsNames = new String[nbTeams];
            Long nbGamesLong = (Long) schedule.get("totalGames");
            int nbGames = nbGamesLong.intValue();

            int teams = 0;
            int playerId = 0;

            LocalDate rightNow = LocalDate.now();

            String inTotPlayers = showTotalPlayersTextView.getText().toString();
            String inCurPlayer = showCurPlayerTextView.getText().toString();
            String inTotTeams = showTotalTeamsTextView.getText().toString();
            String inCurTeam = showCurTeamTextView.getText().toString();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    showTotalTeamsTextView.setText(String.valueOf(nbTeams));
                    /*showCurTeamTextView.setText(String.valueOf(i));
                    showTotalPlayersTextView.setText(String.valueOf(nbPlayers));
                    showCurPlayerTextView.setText(String.valueOf(players)); // update UI*/
                }
            });

            JSONArray datesGames = (JSONArray) schedule.get("dates");
            JSONObject allDatesGames = (JSONObject) datesGames.get(0);
            JSONArray allGames = (JSONArray) allDatesGames.get("games");

            for(int games = 0; games < 2*nbGames; games += 2) {

                JSONObject teamsGame = (JSONObject) allGames.get(teams);
                JSONObject teamsFromGame = (JSONObject) teamsGame.get("teams");
                JSONObject homeTeam = (JSONObject) teamsFromGame.get("home");
                JSONObject homeTeamStats = (JSONObject) homeTeam.get("team");
                Long homeTeamIdLong = (Long) homeTeamStats.get("id");
                String homeTeamName = (String) homeTeamStats.get("name");
                int homeTeamId = homeTeamIdLong.intValue();
                JSONObject awayTeam = (JSONObject) teamsFromGame.get("away");
                JSONObject awayTeamStats = (JSONObject) awayTeam.get("team");
                Long awayTeamIdLong = (Long) awayTeamStats.get("id");
                String awayTeamName = (String) awayTeamStats.get("name");
                int awayTeamId = awayTeamIdLong.intValue();

                teamsId[games] = homeTeamId;
                teamsId[games + 1] = awayTeamId;
                teamsNames[games] = homeTeamName;
                teamsNames[games + 1] = awayTeamName;
                teams ++;
            }

            for(i = 0; i < nbTeams; i++) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showCurTeamTextView.setText(String.valueOf(i));
                    }
                });

                URL teamLink = new URL("https://statsapi.web.nhl.com/api/v1/teams/" + teamsId[i] + "/roster");
                JSONObject teamRoster = connectAndGet.returnObj(teamLink);

                JSONArray rosterSize = (JSONArray) teamRoster.get("roster");
                nbPlayers = rosterSize.size();
                String[] playerList = new String[nbPlayers];

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showTotalPlayersTextView.setText(String.valueOf(nbPlayers));
                    }
                });

                for(players = 0; players < nbPlayers; players ++){

                    int NB_GAMES_GOALIES = 3;
                    int NB_GAMES = 6;
                    int NB_POINTS_TRENDING = 6;
                    int NB_POINTS_REALLY_TRENDING = 3;
                    int NB_GAMES_REALLY_TRENDING = 3;
                    Players curPlayer = new Players();

                    JSONObject playerProfile = (JSONObject) rosterSize.get(players);
                    JSONObject playerInfo = (JSONObject) playerProfile.get("person");
                    JSONObject playerPos = (JSONObject) playerProfile.get("position");
                    String playerName = ((String) playerInfo.get("fullName"));
                    Long playerIdLong = (Long) playerInfo.get("id");
                    playerId = playerIdLong.intValue();

                    curPlayer.setBelongTo("No one");
                    curPlayer.setID(playerId);
                    curPlayer.setFullName(playerName);
                    curPlayer.setPosition(((String) playerPos.get("abbreviation")));
                    curPlayer.setTeamID(teamsId[i]);
                    curPlayer.setTeamName(teamsNames[i]);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showCurPlayerTextView.setText(String.valueOf(players)); // update UI
                            showPlayersTextView.setText("Currently analysing : \n\n" + curPlayer.getFullName() + "\n\n from \n\n" + curPlayer.getTeamName());
                        }
                    });


                    URL playerLink = new URL("https://statsapi.web.nhl.com/api/v1/people/" + playerId + "/stats?stats=gameLog"); //Add &season=20192020 when testing
                    JSONObject playerGame = connectAndGet.returnObj(playerLink);
                    JSONArray playerStats = (JSONArray) playerGame.get("stats");
                    JSONObject allStats = (JSONObject) playerStats.get(0);
                    JSONArray latestSeasonStats = (JSONArray) allStats.get("splits");

                    if(latestSeasonStats.size() == 0) {
                        //System.out.println("No stats for " + playerName + "\n");
                    } else{
                        JSONObject latestGameStats = (JSONObject) latestSeasonStats.get(0);
                        LocalDate dateLastGame = LocalDate.parse((String) latestGameStats.get("date"));
                        if(dateLastGame.until(rightNow, ChronoUnit.DAYS) < 20){ //change for 400 when testing

                            Games threeHotGamesStats = new Games();
                            Games sixTrendingGamesStats = new Games();

                            if(latestSeasonStats.size() < NB_GAMES){
                                NB_GAMES = latestSeasonStats.size();
                            }
                            if(latestSeasonStats.size() < NB_GAMES_REALLY_TRENDING){
                                NB_GAMES_REALLY_TRENDING = latestSeasonStats.size();
                            }
                            if(((String) playerPos.get("abbreviation")).equals("D")){
                                NB_POINTS_TRENDING = 3;
                                NB_POINTS_REALLY_TRENDING = 5;
                            }
                            if(!((String) playerPos.get("abbreviation")).equals("G")){

                                for(int statsPerGames = 0; statsPerGames < NB_GAMES; statsPerGames++){
                                    Games curGame = new Games();
                                    JSONObject statsGameNo = (JSONObject) latestSeasonStats.get(statsPerGames);
                                    JSONObject statsGame = (JSONObject) statsGameNo.get("stat");
                                    Long pointsGameLong = (Long) statsGame.get("points");
                                    Long goalsGameLong = (Long) statsGame.get("goals");
                                    Long assistsGameLong = (Long) statsGame.get("assists");


                                    curGame.setAssists(assistsGameLong.intValue());
                                    curGame.setGoals(goalsGameLong.intValue());
                                    curGame.setPoints(pointsGameLong.intValue());
                                    curPlayer.setLastSixGamesPlayed(curGame);
                                }

                                threeHotGamesStats = total(curPlayer.getLastSixGamesPlayed(), NB_GAMES_REALLY_TRENDING);
                                sixTrendingGamesStats = total(curPlayer.getLastSixGamesPlayed(), NB_GAMES);

                                curPlayer.setAssistsHot(threeHotGamesStats.getAssists());
                                curPlayer.setPointsHot(threeHotGamesStats.getPoints());
                                curPlayer.setGoalsHot(threeHotGamesStats.getGoals());
                                curPlayer.setAssists(sixTrendingGamesStats.getAssists());
                                curPlayer.setGoals(sixTrendingGamesStats.getGoals());
                                curPlayer.setPoints(sixTrendingGamesStats.getPoints());

                                if(sixTrendingGamesStats.getPoints() > NB_POINTS_TRENDING){
                                    //System.out.println("Add " + curPlayer.getFullName() + " to trending list\n");
                                    curPlayer.setTrending(true);
                                    trendingPlayerListTemp.add(curPlayer);
                                }
                                if(threeHotGamesStats.getPoints() > NB_POINTS_REALLY_TRENDING){
                                    //System.out.println("Add " + curPlayer.getFullName() + " to really trending list\n");
                                    curPlayer.setIsHot(true);
                                    recentlyTrendingPlayersTemp.add(curPlayer);
                                }
                            }
                        } else {
                            //System.out.println(curPlayer.getFullName() + " last game played too long ago " + dateLastGame.until(rightNow, ChronoUnit.DAYS) + " days\n");
                        }
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch every players in the boys roster and put them all in a list
     *
     * @param poolPlayersTemp List of all the players from everyone roster
     * @return Nothing, the list is passed by reference
     * */
    private void getPoolPlayers(ArrayList<Players> poolPlayersTemp){

        try {
            URL poolLink = new URL("https://fantasy.espn.com/apis/v3/games/fhl/seasons/2021/segments/0/leagues/56450384?view=mRoster");
            JSONObject poolAll = connectAndGet.returnObj(poolLink);
            // id 1 = BILL, id 2 = CHUCK, id 3 = gignac, id4 = sam, id 5 = jay, id 6 = dubuc
            JSONArray allTeams = (JSONArray) poolAll.get("teams");

            JSONObject billAll = (JSONObject) allTeams.get(0);
            JSONObject chuckAll = (JSONObject) allTeams.get(1);
            JSONObject gigiAll = (JSONObject) allTeams.get(2);
            JSONObject samAll = (JSONObject) allTeams.get(3);
            JSONObject jayAll = (JSONObject) allTeams.get(4);
            JSONObject dubucAll = (JSONObject) allTeams.get(5);

            JSONObject billRoster = (JSONObject) billAll.get("roster");
            JSONObject chuckRoster = (JSONObject) chuckAll.get("roster");
            JSONObject gigiRoster = (JSONObject) gigiAll.get("roster");
            JSONObject samRoster = (JSONObject) samAll.get("roster");
            JSONObject jayRoster = (JSONObject) jayAll.get("roster");
            JSONObject dubucRoster = (JSONObject) dubucAll.get("roster");

            JSONArray billPlayers = (JSONArray) billRoster.get("entries");
            JSONArray chuckPlayers = (JSONArray) chuckRoster.get("entries");
            JSONArray gigiPlayers = (JSONArray) gigiRoster.get("entries");
            JSONArray samPlayers = (JSONArray) samRoster.get("entries");
            JSONArray jayPlayers = (JSONArray) jayRoster.get("entries");
            JSONArray dubucPlayers = (JSONArray) dubucRoster.get("entries");

            getAllPoolPlayers(billPlayers, poolPlayersTemp, "Bill");
            getAllPoolPlayers(chuckPlayers, poolPlayersTemp, "Chuck");
            getAllPoolPlayers(gigiPlayers, poolPlayersTemp, "Gigi");
            getAllPoolPlayers(samPlayers, poolPlayersTemp, "Sam");
            getAllPoolPlayers(jayPlayers, poolPlayersTemp, "Jay");
            getAllPoolPlayers(dubucPlayers, poolPlayersTemp, "Dubuc");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if trending players are in someone roster's
     *
     * @param allPlayers Players that are trending or recently trending in the NHL
     * @param playerPool Players that are in someone's roster
     * @return Nothing, this functions only change the belongTo of a player
     * */
    private void crossReferences(ArrayList<Players> playerPool, ArrayList<Players> allPlayers) {

        for(Players everyPlayers : playerPool) {
            String nameCur = everyPlayers.getFullName().replaceFirst(" ", "");
            for (int j = 0; j < allPlayers.size(); j++) {
                Players curPlayer = allPlayers.get(j);
                if (nameCur.equals(curPlayer.getFullName())) {
                    //System.out.println("Removing " + curPlayer.getFullName() + " from trending list\n");
                    curPlayer.setBelongTo(everyPlayers.getBelongTo());
                    allPlayers.set(j, curPlayer);
                }
            }
        }
    }

    //TextViews for updating where we are in the program
    TextView showPlayersTextView;
    TextView showTotalPlayersTextView;
    TextView showCurPlayerTextView;
    TextView showTotalTeamsTextView;
    TextView showCurTeamTextView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);

        //Updates UI During execution
        showTotalPlayersTextView = fragmentFirstLayout.findViewById(R.id.totalPlayers);
        showCurPlayerTextView = fragmentFirstLayout.findViewById(R.id.curPlayer);
        showTotalTeamsTextView = fragmentFirstLayout.findViewById(R.id.totalTeams);
        showCurTeamTextView = fragmentFirstLayout.findViewById(R.id.curTeam);

        //Make the Players Scrollable
        showPlayersTextView = fragmentFirstLayout.findViewById(R.id.textview_first);
        showPlayersTextView.setMovementMethod(new ScrollingMovementMethod());


        return fragmentFirstLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(); // to update UI  from background //thread.
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<Players> trendingPlayerList = new ArrayList<Players>();
                            ArrayList<Players> recentlyTrendingPlayers = new ArrayList<Players>();
                            ArrayList<Players> poolPlayers = new ArrayList<Players>();
                            String trendingToDisplay;
                            String reallyTrendingToDisplay;

                            fetchDatas(view, handler, trendingPlayerList, recentlyTrendingPlayers); //background thread

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    showCurTeamTextView.setText(String.valueOf(i));
                                    showCurPlayerTextView.setText(String.valueOf(players)); // update UI
                                    showPlayersTextView.setText("Fetching Pool Players");
                                }
                            });

                            getPoolPlayers(poolPlayers);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    showPlayersTextView.setText("Cross Referencing with Pool");
                                }
                            });

                            crossReferences(poolPlayers, trendingPlayerList);
                            crossReferences(poolPlayers, recentlyTrendingPlayers);

                            trendingToDisplay = trendingPlayerList.toString();
                            reallyTrendingToDisplay = recentlyTrendingPlayers.toString();

                            showPlayersTextView.setText("Players that are trending : " + trendingToDisplay + "\n\nPlayers that are Really trending : " + reallyTrendingToDisplay);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    showTotalTeamsTextView.setText(String.valueOf(nbTeams));
                                    showCurTeamTextView.setText(String.valueOf(i));
                                    showTotalPlayersTextView.setText(String.valueOf(nbPlayers));
                                    showCurPlayerTextView.setText(String.valueOf(players)); // update UI
                                }
                            });
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });

        /*view.findViewById(R.id.Run_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }
}