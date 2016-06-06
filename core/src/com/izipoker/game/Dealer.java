package com.izipoker.game;

import com.izipoker.cardGame.Card;
import com.izipoker.cardGame.Deck;

import java.util.ArrayList;

/**
 * Represents the dealer of a poker game, person who deal cards, add bets from the players to the pot and check winner
 */
public class Dealer implements Runnable {
    private Table table;
    private Deck deck;

    /**
     * Creates a poker dealer
     *
     * @param table Table where dealer is dealing
     */
    public Dealer(Table table) {

        this.table = table;
        this.deck = new Deck();
    }

    /**
     * Creates a new round and add it to the table he is dealing
     *
     * @return True if there are at least two players in the table, and false otherwise
     */
    public boolean createRound() {
        if (table.getActivePlayers().length >= 2) {
            Round round = new Round(table.getActivePlayers(), table.getJoker());
            //Round round = new Round(table.getActivePlayers(), table.getActivePlayers()[0]);
            table.addRounds(round);
            round.addBet(round.getFirstPlayer(), table.getSmallBlind());
            round.addBet(round.getFirstPlayer(), table.getBigBlind());
            return true;
        } else return false;
    }

    /**
     * Sets the new player to start playing in next round, finds next starting player
     */
    public void setNewJoker() {
        table.nextJoker();
    }

    /**
     * Give two cards to all the players in the table
     */
    public void giveHands() {
        Player[] players_in_round =
                table.getTopRound().getCurrentPlayers().toArray(new Player[table.getTopRound().getCurrentPlayers().size()]);
        for (int i = 0; i < players_in_round.length; i++) {
            players_in_round[i].setHand(new Hand(deck.getTopCard(), deck.getTopCard()));
        }
    }

    /**
     * Sets the flop (first three cards) in the current round
     */
    public void showFlop() {
        ArrayList<Card> tempFlop = new ArrayList<Card>();
        for (int i = 0; i < 3; i++) {
            Card temp = deck.getTopCard();
            temp.setFlipped(true);
            tempFlop.add(temp);
        }
        table.getTopRound().setFlop(tempFlop.toArray(new Card[tempFlop.size()]));
    }

    /**
     * Sets the turn (fourth card) in the current round
     */
    public void showTurn() {
        Card temp = deck.getTopCard();
        temp.setFlipped(true);
        table.getTopRound().setTurn(temp);
    }

    /**
     * Sets the river (fifth card) in the current round
     */
    public void showRiver() {
        Card temp = deck.getTopCard();
        temp.setFlipped(true);
        table.getTopRound().setRiver(temp);
    }

    /**
     * Plays the game while there are sufficient players(two)
     */
    @Override
    public void run() {
        table.setState(Table.tableState.PLAYING);
        Round r;

        while (table.getActivePlayers().length > 1) {
            createRound();
            r = table.getTopRound();

            deck.shuffle(1);
            giveHands();

            for (Player p : table.getActivePlayers()) {
                // System.out.println(p.getHand().getCards()[0]);
                // System.out.println(p.getHand().getCards()[1]);
                table.sendHand(p.getName());
                table.sendMoney(p.getName());
            }

            System.out.println("Joker: " + table.getJoker().getName());


            System.out.println("PRE-FLOP");
            handleTableActions();
            r.addToPot();
            System.out.println(r.getPot());
            System.out.println("SAIMOS PRE-FLOP");

            if (r.getCurrentPlayers().size() != 1) {
                System.out.println("FLOP");
                r.updateState();
                showFlop();
                handleTableActions();
                r.addToPot();
                System.out.println(r.getPot());
                System.out.println("SAIMOS FLOP");

                if (r.getCurrentPlayers().size() != 1) {
                    System.out.println("TURN");
                    r.updateState();
                    showTurn();
                    handleTableActions();
                    r.addToPot();
                    System.out.println(r.getPot());
                    System.out.println("SAIMOS TURN");

                    if (r.getCurrentPlayers().size() != 1) {
                        System.out.println("RIVER");
                        r.updateState();
                        showRiver();
                        handleTableActions();
                        r.addToPot();
                        System.out.println(r.getPot());
                        System.out.println("SAIMOS RIVER");

                        if (r.getCurrentPlayers().size() != 1) {
                            r.updateState();
                            handleTableActions();

                            //check hands!
                        }
                    }
                }
            }
            r.getCurrentPlayers().get(0).setMoney(r.getPot());
            setNewJoker();
        }
        table.setState(Table.tableState.CLOSED);
    }

    /**
     * Handle all actions from the table between table cards showing
     * Waits the time specified by table
     * If during this time player acts action is handled
     * If not the player is considered folded
     */
    private void handleTableActions() {
        Round r = table.getTopRound();
        Player p;
        boolean atLeastOnePlayed = false;
        while ((r.getCurrentPlayers().peek() != r.getHighestPlayer() || !atLeastOnePlayed) && r.getCurrentPlayers().size() > 1) {
            p = r.getCurrentPlayers().peek();
            System.out.println(p.getName() + " Turn");
            table.sendHighestBet(p.getName());
            table.sendPossibleActions(p.getName(), checkPossibleActions(p));
            Thread t = new Thread(new CheckPlayerAction(p));
            t.start();
            try {
                t.join(table.getPlayingTime() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (t.isAlive()) {
                table.sendPossibleActions(p.getName(), new boolean[]{false, false, false, false});
                t.stop();
            }
            handlePlayerAction(p);
            atLeastOnePlayed = true;
        }
    }

    /**
     * Handles player action
     *
     * @param p Player to act
     */
    private void handlePlayerAction(Player p) {
        Round r = table.getTopRound();
        if (!p.hasActed()) {
            r.foldPlayer(p);
            System.out.println(p.getName() + " Folded by timeout");
        } else {
            switch (p.getLastAction().getType()) {
                case FOLD:
                    r.foldPlayer(p);
                    System.out.println(p.getName() + " Folded");
                    break;
                case CHECK:
                    r.addBet(p, 0);
                    System.out.println(p.getName() + " Checked");
                    break;
                case CALL:
                    r.addCall(p);
                    table.sendMoney(p.getName());
                    System.out.println(p.getName() + " Called");
                    break;
                case RAISE:
                    r.addBet(p, p.getLastAction().getAmount());
                    table.sendMoney(p.getName());
                    System.out.println(p.getName() + " Raised");
                    break;
            }
            p.setActed(false);
        }
    }

    private boolean[] checkPossibleActions(Player p) {
        boolean[] possibleActions = new boolean[]{true, false, false, false};
        Round r = table.getTopRound();

        if (r.getHighestBet() == 0) {
            possibleActions[1] = true;
            possibleActions[3] = true;
        } else if (p.getMoney() > r.getHighestBet()) {
            possibleActions[2] = true;
            possibleActions[3] = true;
        } else if (p.getMoney() == r.getHighestBet()) {
            possibleActions[2] = true;
        }

        return possibleActions;
    }
}