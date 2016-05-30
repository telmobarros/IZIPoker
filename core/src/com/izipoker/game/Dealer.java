package com.izipoker.game;

import com.izipoker.cardGame.Card;
import com.izipoker.cardGame.Deck;

import java.util.ArrayList;

/**
 * Created by Telmo on 26/04/2016.
 */
public class Dealer {
    private Table table;
    private Deck deck;

    /**
     * Creates a poker dealer
     * @param table Table where dealer is dealing
     */
    public Dealer(Table table){

        this.table = table;
        this.deck = new Deck();
    }

    public boolean createRound(){
        if(table.getActivePlayers().length >= 2) {
            //Round round = new Round(table.getActivePlayers(), table.getJoker());
            Round round = new Round(table.getActivePlayers(), table.getActivePlayers()[0]);
            table.addRounds(round);
            //round.getFirstPlayer().bet(table.getSmallBlind(), round);
            //round.getFirstPlayer().bet(table.getBigBlind(), round);
            return true;
        }
        else return false;
    }
    public void setNewJoker(){
        table.nextJoker();
    }

    public void giveHands(){
        Player[] players_in_round =
                table.getTopRound().getCurrentPlayers().toArray(new Player[table.getTopRound().getCurrentPlayers().size()]);
        for(int i = 0; i < players_in_round.length; i++){
            players_in_round[i].setHand(new Hand(deck.getTopCard(), deck.getTopCard()));
        }
    }
    public void showFlop(){
        deck.getTopCard();
        ArrayList<Card> tempFlop = new ArrayList<Card>();
        Card temp =  deck.getTopCard();
        temp.flip();
        tempFlop.add(temp);
        temp =  deck.getTopCard();
        temp.flip();
        tempFlop.add(temp);
        temp =  deck.getTopCard();
        temp.flip();
        tempFlop.add(temp);
        table.getTopRound().setFlop(tempFlop.toArray(new Card[tempFlop.size()]));
    }
    public void showTurn(){
        deck.getTopCard();
        deck.getTopCard().flip();
        table.getTopRound().setTurn(deck.getTopCard());
    }
    public void showRiver(){
        deck.getTopCard();
        deck.getTopCard().flip();
        table.getTopRound().setRiver(deck.getTopCard());
    }
}