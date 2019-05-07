package com.samandarimohammad.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    //Sprite : sprite is essentially an image, charector, background and we create a sprite batch to manage those.
    SpriteBatch batch;
    //Texture is an image exactly.
    Texture background;
    //this is like batch an allow us to draw shapes.
    ShapeRenderer shapeRenderer;


    Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;

    int gameState = 0;
    float gravity = 2;
    float gap = 400;
    float maxTubeOffset;
    Random randomGenerator;
    float tubeVelocity = 4;
    int numberofTube = 4;
    float[] tubeX = new float[numberofTube];
    float[] tubeOffset = new float[numberofTube];
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;

    float distanceBetweenTubes;

    Texture bottomTube, topTube, gameover;

    Circle birdCircle;
    Rectangle[] topTubeRectangle, bottomTubeRectangle;

    @Override
    public void create() {
        //This method runs at startup.
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameover = new Texture("gameover.png");

        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        topTubeRectangle = new Rectangle[numberofTube];
        bottomTubeRectangle = new Rectangle[numberofTube];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        bottomTube = new Texture("bottomtube.png");
        topTube = new Texture("toptube.png");
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        startGame();
    }

    private void startGame() {
        birdY = (Gdx.graphics.getHeight() - birds[0].getHeight()) / 2;

        for (int i = 0; i < numberofTube; i++) {

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 500);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topTubeRectangle[i] = new Rectangle();
            bottomTubeRectangle[i] = new Rectangle();
        }
    }

    @Override
    public void render() {
        //This render method happens continuosly while tha app is running.

        batch.begin();  //This tells render method that we are going o start rendering the sprites.
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //Creating the background in is stretching it to be fullscreen.

        batch.draw(birds[flapState], (Gdx.graphics.getWidth() - birds[flapState].getWidth()) / 2, birdY);

        if (gameState == 1) {

            if (flapState == 0) {
                flapState = 1;
            } else {
                flapState = 0;
            }
            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                score++;
                Gdx.app.log("Lord-Score", String.valueOf(score));
                if (scoringTube < numberofTube - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }
            if (Gdx.input.justTouched()) {
                velocity = -30;
            }
            for (int i = 0; i < numberofTube; i++) {
                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numberofTube * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 500);
                } else {
                    tubeX[i] = tubeX[i] - tubeVelocity;

                }

                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());


            }
            if (birdY > 0 && birdY<Gdx.graphics.getHeight()) {
                velocity = velocity + gravity;
                birdY -= velocity;
            } else {
                gameState = 2;
            }

        } else if (gameState == 0) {

            if (Gdx.input.justTouched()) {
                gameState = 1;
            }

        } else if (gameState == 2) {
            batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
            if (Gdx.input.justTouched()) {
                gameState = 1;
                score=0;
                startGame();
                scoringTube=0;
                velocity=0;
            }
        }



        font.draw(batch, String.valueOf(score), 100, 200);

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int j = 0; j < numberofTube; j++) {
//            shapeRenderer.rect(tubeX[j],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[j],topTube.getWidth(),topTube.getHeight());
//            shapeRenderer.rect(tubeX[j],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[j],bottomTube.getWidth(),bottomTube.getHeight());

            if (Intersector.overlaps(birdCircle, topTubeRectangle[j]) || Intersector.overlaps(birdCircle, bottomTubeRectangle[j])) {
                gameState = 2;
            }
        }

//        shapeRenderer.end();

    }
}
