package com.theironyard;

/**
 * Created by ericweidman on 2/25/16.
 */
public class Stat {

    int id;
    String userCharacter;
    String opponentCharacter;
    String winLoss;

    public Stat(int id, String userCharacter, String opponentCharacter, String winLoss) {
        this.userCharacter = userCharacter;
        this.opponentCharacter = opponentCharacter;
        this.winLoss = winLoss;
        this.id = id;
    }

    public Stat(String userCharacter, String opponentCharacter, String winLoss) {
        this.userCharacter = userCharacter;
        this.opponentCharacter = opponentCharacter;
        this.winLoss = winLoss;
    }
}
