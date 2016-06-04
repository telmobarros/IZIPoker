package com.izipoker.network;

public interface ServerInterface {
    public boolean join(String name, int avatarID, ClientCallbackInterface client);
    public void tell(String name, String message);
    public void tellAll(ClientCallbackInterface client, String message);
    public void leave(ClientCallbackInterface c);
    public String getName();
    public void sendHand(String name);
    public void sendCard(String name);
    public boolean isLobbyState();
    public void sendPossibleActions(String name, boolean possibleActions[]);
    public void sendMoney(String name);
}