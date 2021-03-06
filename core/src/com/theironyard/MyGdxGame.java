package com.theironyard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegion stand, down, up, right, left, zombieStand;

    float x, y, xv, yv;

    Animation walk;

    float time;

    boolean faceRight, faceLeft, faceUp, faceDown = true;

    static final int WIDTH = 16;
    static final int HEIGHT = 16;

    static final int DRAW_WIDTH = WIDTH * 3;
    static final int DRAW_HEIGHT = HEIGHT * 3;

    static final float MAX_VELOCITY = 300;
    static final float REAL_MAX_VELOCITY = 1000;


    @Override
    public void create() {
        batch = new SpriteBatch();
        Texture sheet = new Texture("tiles.png");
        TextureRegion[][] tiles = TextureRegion.split(sheet, WIDTH, HEIGHT);
        //TextureRegion[][] tilesBackground = TextureRegion.split(sheet, 30 , 30);
        down = tiles[6][0];
        up = tiles[6][1];
        right = tiles[6][3];
        left = new TextureRegion(right);
        left.flip(true, false);
        zombieStand = tiles[6][6];
        //tree = tilesBackground[10][0];

        walk = new Animation(0.2f, tiles[6][2], tiles[6][3]);

    }

    @Override
    public void render() {
        time += Gdx.graphics.getDeltaTime();

        move();

        Gdx.gl.glClearColor(0, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(zombieStand, 50, 50, DRAW_WIDTH, DRAW_HEIGHT);
        if (faceRight) {
            batch.draw(right, x, y, DRAW_WIDTH, DRAW_HEIGHT);
            if (faceRight && xv != 0) {
                batch.draw(walk.getKeyFrame(time, true), x, y, DRAW_WIDTH, DRAW_HEIGHT);
            }
        } else if (faceDown) {
            batch.draw(down, x, y, DRAW_WIDTH, DRAW_HEIGHT);
            if (faceDown && yv != 0) {
                batch.draw(down, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
            }
        } else if (faceLeft) {
            batch.draw(left, x, y, DRAW_WIDTH, DRAW_HEIGHT);
            if (faceLeft && xv != 0) {
                batch.draw(walk.getKeyFrame(time, true), x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
            }
        } else if (faceUp) {
            batch.draw(up, x, y, DRAW_WIDTH, DRAW_HEIGHT);
            if (faceUp && yv != 0) {
                batch.draw(up, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
            }
        } else {
            batch.draw(stand, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        }
        if (y > Gdx.graphics.getHeight()) {
            y = 0;
        }
        if (y < 0) {
            y = Gdx.graphics.getHeight();
        }
        if (x > Gdx.graphics.getWidth()) {
            x = 0;
        }
        if (x < 0) {
            x = Gdx.graphics.getWidth();
        }
        batch.end();
    }

    float decelerate(float velocity) {
        float deceleration = .25f;
        velocity *= deceleration;
        if (Math.abs(velocity) < 1) {
            velocity = 0;
        }
        return velocity;
    }

    void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yv = MAX_VELOCITY;
            faceRight = false;
            faceLeft = false;
            faceDown = false;
            faceUp = true;
            if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                yv = REAL_MAX_VELOCITY;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yv = MAX_VELOCITY * -1;
            faceRight = false;
            faceLeft = false;
            faceUp = false;
            faceDown = true;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                yv = REAL_MAX_VELOCITY * -1;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xv = MAX_VELOCITY;
            faceRight = true;
            faceLeft = false;
            faceDown = false;
            faceUp = false;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                xv = REAL_MAX_VELOCITY;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xv = MAX_VELOCITY * -1;
            faceRight = false;
            faceLeft = true;
            faceDown = false;
            faceUp = false;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                xv = REAL_MAX_VELOCITY * -1;
            }
        }

        y += yv * Gdx.graphics.getDeltaTime();
        x += xv * Gdx.graphics.getDeltaTime();

        yv = decelerate(yv);
        xv = decelerate(xv);
    }



    @Override
    public void dispose() {
        batch.dispose();
    }
}
