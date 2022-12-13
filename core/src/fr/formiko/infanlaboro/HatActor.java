package fr.formiko.infanlaboro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HatActor extends Actor {
    private static ShapeRenderer shapeRenderer;
    private int hatRadius;
    private Color color;
    private float speed;
    protected float visionRadius;
    protected float wantedRotation;
    private Texture textureWithDark;
    private boolean darkArea;


    public HatActor(int hatRadius, int visionRadius, Color color) {
        this.hatRadius = hatRadius;
        this.visionRadius = visionRadius;
        this.color = color;
        speed = 1;
    }


    public float getCenterX() { return getX() + getWidth() / 2; }
    public float getCenterY() { return getY() + getHeight() / 2; }
    public void setCenterX(float x) { setX(x - getWidth() / 2); }
    public void setCenterY(float y) { setY(y - getHeight() / 2); }
    public void translate(float x, float y) {
        setX(getX() + x);
        setY(getY() + y);
    }
    public int getHatRadius() { return hatRadius; }
    public void setHatRadius(int hatRadius) { this.hatRadius = hatRadius; }
    public int getHitRadius() { return getHatRadius(); }
    public float getVisionRadius() { return visionRadius; }
    public void setVisionRadius(float visionRadius) { this.visionRadius = visionRadius; }
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public boolean see(HatActor mi) { return isInRadius(mi, visionRadius); }
    public float getWantedRotation() { return wantedRotation; }
    public float getMaxRotationPerSecond() { return 900f; }
    public void setWantedRotation(float wantedRotation) { this.wantedRotation = wantedRotation; }
    public boolean isDarkArea() { return darkArea; }
    public void setDarkArea(boolean darkArea) { this.darkArea = darkArea; }
    /**
     * {@summary Move in the facing direction at speed percentOfSpeed.}
     * If a wantedRotation have been set, go for it.
     * 
     * @param percentOfSpeed percent of max speed.
     */
    public void moveFront(float percentOfSpeed) {
        float distance = getSpeed() * percentOfSpeed;
        float facingAngle = getRotation() + 90;
        translate((float) (distance * java.lang.Math.cos(java.lang.Math.toRadians(facingAngle))),
                (float) (distance * java.lang.Math.sin(java.lang.Math.toRadians(facingAngle))));
    }
    /***
     * {@summary Move in the facing direction at max speed.}
     */
    public void moveFront() { moveFront(1f); }
    /**
     * {@summary Draw this actor.}
     * 
     * @param batch       batch were to draw
     * @param parentAlpha alpha of the parent to draw at same alpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO do a "bonnet" draw.

        batch.end();
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
        // shapeRenderer.setProjectionMatrix(GameScreen.getCamera().combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(getColor());
        shapeRenderer.circle(getCenterX(), getCenterY(), (float) getHatRadius());
        float ponponSize = getHatRadius() / 4f;
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        // shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.line(new Vector3(getCenterX() + getHatRadius(), getCenterY(), 0),
                new Vector3(getCenterX() + ponponSize / 2, getCenterY() + getHatRadius() * 1.5f, 0));
        shapeRenderer.line(new Vector3(getCenterX() - getHatRadius(), getCenterY(), 0),
                new Vector3(getCenterX() - ponponSize / 2, getCenterY() + getHatRadius() * 1.5f, 0));
        // shapeRenderer.line(new Vector3(getCenterX() + 2 * ponponSize, getCenterY() - ponponSize, 0),
        // new Vector3(getCenterX() + ponponSize / 2, getCenterY() + getHatRadius() / 2, 0));
        // shapeRenderer.line(new Vector3(getCenterX() - 2 * ponponSize, getCenterY() - ponponSize, 0),
        // new Vector3(getCenterX() - ponponSize / 2, getCenterY() + getHatRadius() / 2, 0));
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(getCenterX(), getCenterY() + getHatRadius() * 1.5f, ponponSize);
        shapeRenderer.end();


        // debug
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
        shapeRenderer.setProjectionMatrix(Game.camera.combined);
        shapeRenderer.begin(ShapeType.Line);
        // shapeRenderer.setColor(new Color(0f, 0f, 1f, parentAlpha * 1f));
        // shapeRenderer.circle(getCenterX(), getCenterY(), (float) getVisionRadius());
        // shapeRenderer.setColor(new Color(1f, 0f, 0f, parentAlpha * 1f));
        // shapeRenderer.circle(getCenterX(), getCenterY(), (float) getHitRadius());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);


        batch.begin();
        if (isDarkArea()) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            // Gdx.gl.glColorMask(true, true, true, true);
            // Gdx.gl.glDepthFunc(GL30.GL_LESS);
            batch.draw(getMaskedTexture(), getX(), getY(), getWidth(), getHeight());
        }
    }

    public void move(float x, float y) {
        x = getCenterX() + x;
        y = getCenterY() + y;
        if (x < 0) {
            x = 0;
        } else if (x > Game.w) {
            x = Game.w;
        }
        if (y < 0) {
            y = 0;
        } else if (y > Game.h) {
            y = Game.h;
        }
        setCenterX(x);
        setCenterY(y);
    }

    /***
     * Return true if hit box of the 2 MapItems are connected.
     */
    public boolean hitBoxConnected(HatActor it) { return isInRadius(it, hatRadius + it.hatRadius); }

    /***
     * Return true if other MapItem is in radius.
     */
    public boolean isInRadius(HatActor mi2, double radius) { return distanceTo(mi2) < radius; }

    /**
     * {@summary Return the distance between center point of this &#38; center point
     * of an other MapItem.}
     */
    public float distanceTo(HatActor mi2) {
        return (float) Math.getDistanceBetweenPoints(getCenterX(), getCenterY(), mi2.getCenterX(), mi2.getCenterY());
    }

    /**
     * {@summary move Creature location between the rectangle 0,0,maxX,maxY if needed.}
     * 
     * @param maxX the max x for the creature
     * @param maxY the max y for the creature
     * @return true if Creature have been move
     */
    public boolean moveIn(float maxX, float maxY) {
        boolean haveMove = false;
        if (getCenterX() > maxX) {
            setCenterX(maxX);
            haveMove = true;
        } else if (getCenterX() < 0) {
            setCenterX(0f);
            haveMove = true;
        }
        if (getCenterY() > maxY) {
            setCenterY(maxY);
            haveMove = true;
        } else if (getCenterY() < 0) {
            setCenterY(0f);
            haveMove = true;
        }
        return haveMove;
    }

    /**
     * {@summary Set wanted rotation to go to v.}
     * 
     * @param v      contains coordinate of Point to got to
     * @param degdif Degre difference to go to
     */
    public void goTo(Vector2 v, float degdif) {
        // Update wantedRotation
        Vector2 v2 = new Vector2(v.x - getCenterX(), v.y - getCenterY());
        // float previousRotation = getRotation() % 360;
        float newRotation = v2.angleDeg() - 90;
        // float wantedRotation = (previousRotation - newRotation + 360 + degdif) % 360;
        setRotation(newRotation - degdif);
    }
    public void goTo(Vector2 v) { goTo(v, 0f); }

    public boolean isNextTo(Vector2 v) { return Math.getDistanceBetweenPoints(getCenterX(), getCenterY(), v.x, v.y) < getHatRadius(); }

    /***
     * {@summary Set wanted rotation to run away from v.}
     * To run away from we calculate angle to go to, then add 180 degre to go to the oposite direction.
     * 
     * @param v contains coordinate of Point to run away from
     */
    public void runAwayFrom(Vector2 v) { goTo(v, 180f); }

    /**
     * {@summary Return maskedTexture.}
     * Default texture is full of color.
     * It have a circle area to exclude.
     * 
     * @return texture without first circle area to exclude
     */
    private Texture getMaskedTexture() {
        if (textureWithDark == null) {
            // textureWithDark = new Texture(getMaskPixmap((int) visionRadius));
            textureWithDark = new Texture(Gdx.files.internal("images/darkedArea.png"));
        }
        return textureWithDark;
    }

    /**
     * {@summary Return pixmap without a centered circle of pixel.}
     * 
     * @return pixmap without a centered circle of pixel
     */
    private Pixmap getMaskPixmap(int radius) {
        final int blackLevel = 255; // [0; 255]
        final float egdeSize = 0.2f;

        Pixmap darkedArea = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        int xCenter = (int) (darkedArea.getWidth() / 2);
        int yCenter = (int) (darkedArea.getHeight() / 2);
        float edgeLength = radius * egdeSize;

        for (int x = 0; x < darkedArea.getWidth(); x++) {
            for (int y = 0; y < darkedArea.getHeight(); y++) {
                int distToCenter = (int) Math.getDistanceBetweenPoints(x, y, xCenter, yCenter);
                if (distToCenter > radius) {
                    darkedArea.drawPixel(x, y, blackLevel);
                } else if (distToCenter > radius - edgeLength) {
                    float nextToTheEdgess = 1f - (radius - distToCenter) / edgeLength;
                    darkedArea.drawPixel(x, y, (int) (blackLevel * nextToTheEdgess));
                }
            }
        }
        // save pixmap
        // FileHandle fh;
        // int counter = 0;
        // do {
        // fh = new FileHandle("screenshot" + counter++ + ".png");
        // } while (fh.exists());
        // PixmapIO.writePNG(fh, darkedArea);
        return darkedArea;
    }
}
