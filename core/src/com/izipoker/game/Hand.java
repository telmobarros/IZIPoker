package com.izipoker.game;

import com.izipoker.cardGame.Card;

import javafx.util.Pair;

/**
 * Created by Telmo on 26/04/2016.
 */
public class Hand{

public enum handRank{
    ROYAL_FLUSH,
    STRAIGHT_FLUSH,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    FLUSH,
    STRAIGHT,
    THREE_OF_A_KIND,
    TWO_PAIR,
    PAIR,
    HIGH_CARD
}

    public final static int HAND_SIZE = 2;
    private Card cards[];

    /**
     * Hand constructor, creates new Hand given two cards
     * @param c1    First card of hand
     * @param c2    Second card of hand
     */
    Hand(Card c1, Card c2){
        cards = new Card[2];
        cards[0] = c1;
        cards[1] = c2;
    }

    /**
     * Hand constructor, creates new Hand given an array of size 2
     * @param cards Array of size 2 containing the two cards of hand
     */
    Hand(Card cards[]){
        if (cards.length != HAND_SIZE) {
            this.cards = cards;
        } else {
            throw new IllegalArgumentException("Array passed by argument must be of size 2");
        }
    }

    public Pair<handRank, Card.rankType> checkHandRank(Card[] cardsOnTable){
        //TO FINISH
        return new Pair(handRank.PAIR, Card.rankType.ACE);
    }
}
