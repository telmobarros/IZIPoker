package com.izipoker.network;

import com.izipoker.cardGame.Card;
import com.izipoker.game.Hand;

public interface ClientCallbackInterface  {
    public void notify(String message);
    public void receiveHand(Hand hand);
    public void receiveCard(Card card);
    public void receivePossibleActions(boolean possibleActions[]);
    public void receiveMoney(int money);
    public void receiveHighestBet(int highestbet);

}
