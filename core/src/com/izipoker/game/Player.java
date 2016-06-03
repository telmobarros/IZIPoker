package com.izipoker.game;

import java.io.Serializable;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.izipoker.graphics.TexturesLoad;
import com.izipoker.interfaces.ClientCallbackInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.Image;

public abstract class Player extends Actor{
    private int id;
    private String name;
    private int money;
    private int avatarID;
    private boolean active;
    private boolean playing;

    private Hand hand;
    private PokerAction lastAction;

    private boolean acted = false;

    Player(int id, String name, int money) {
        this.id = id;
        this.name = name;
        this.money = money;
        hand = null;
        active = true;
        playing = false;
        this.avatarID = 0;
    }

    Player(int id, String name, int money, int avatarID) {
        this.id = id;
        this.name = name;
        this.money = money;
        hand = null;
        active = true;
        playing = false;
        this.avatarID = avatarID;
    }

    void check(Round r){
        
    }

    void call(Round r){

    }

    void bet(int n, Round r){
        //add to
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand){this.hand = hand;}

    @Override
    public void draw(Batch batch, float parentAlpha) {
        BitmapFont font = new BitmapFont();
        batch.draw(TexturesLoad.avatarTex[0][avatarID], super.getX(), super.getY(), super.getWidth(), super.getHeight());
        font.draw(batch, name, 0,0);

        //font.draw(batch, name, super.getX()-avatar.getWidth(), super.getY()-avatar.getHeight());
    }


    public boolean hasActed() {
        return acted;
    }

}
