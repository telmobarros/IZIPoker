package com.izipoker.game.desktop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.izipoker.game.Dealer;
import com.izipoker.game.Table;

/**
 * Created by Telmo on 03/05/2016.
 */
public class GameDesktop implements Screen{
    //GUI Variables
    private Stage stage;
    private Skin skin;
    private TextArea chat;
    private ScrollPane chatSP;
    private int lastChatSize = 0;

    //Game variables
    private Table table;
    private Dealer dealer;


    public GameDesktop(Table table) {
        //Game variables initialization
        this.table = table;
        this.dealer = table.getDealer();
        Thread game = new Thread(dealer);
        //super( new StretchViewport(320.0f, 240.0f, new OrthographicCamera()) );
        create();
        skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas("uiskin.atlas"));


        buildStage();

        game.start();

        /*Deck d = new Deck();
        System.out.println(d);
        d.shuffle(3);
        System.out.println(d);*/
    }

    public void buildStage() {
        //Actors
        table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        stage.addActor(table);

        chat = new TextArea("CHAT\n", skin);
        chatSP = new ScrollPane(chat);
        chat.setPrefRows(chat.getLines());
        chatSP.setSize(stage.getWidth() / 4, stage.getHeight() / 3);
        chatSP.setPosition(0, 0, Align.bottomLeft);
        chatSP.layout();
        stage.addActor(chatSP);



        //Listeners

    }

    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show(){
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(lastChatSize != table.getChatHistory().size()){
            for(int i = lastChatSize; i < table.getChatHistory().size(); i++){
                chat.appendText(table.getChatHistory().get(i) + "\n");
            }
            lastChatSize = table.getChatHistory().size();
            chat.setPrefRows(chat.getLines());
            chatSP.layout();
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}