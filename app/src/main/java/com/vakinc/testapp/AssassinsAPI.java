package com.vakinc.testapp;

public class AssassinsAPI {

    //TODO: Configure root_url
    private static final String ROOT_URL = "http://YOUR IP HERE/assassins/v1.0/API.php?apicall=";

    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;

    public static final String URL_ADD_PLAYER = ROOT_URL + "addplayer";
    public static final String URL_GET_PLAYERS = ROOT_URL + "getplayers";
    public static final String URL_GET_PLAYERS_IN_GAME = ROOT_URL + "getplayersingame";
    public static final String URL_GET_GAME_INFO = ROOT_URL + "getgameinfo";
    public static final String URL_GET_ALL_GAMES = ROOT_URL + "getallgames";
    public static final String URL_UPDATE_PLAYER = ROOT_URL + "updateplayer";
    public static final String URL_DELETE_PLAYER = ROOT_URL + "deleteplayer";
    public static final String URL_GET_TARGET_FOR = ROOT_URL + "gettargetfor";
    public static final String URL_START_GAME = ROOT_URL + "startgame";
    public static final String URL_CHECK_GAME_ACTIVE = ROOT_URL + "checkgameactive";
    public static final String URL_ASSASSINATE_TARGET = ROOT_URL + "assassinatetarget";
}
