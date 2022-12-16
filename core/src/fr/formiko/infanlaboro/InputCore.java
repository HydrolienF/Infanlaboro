package fr.formiko.infanlaboro;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * {@summary React to user inputs.}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class InputCore implements InputProcessor {
    private Game game;


    // CONSTRUCTORS --------------------------------------------------------------
    public InputCore(Game game) { this.game = game; }

    // FUNCTIONS -----------------------------------------------------------------
    @Override
    public boolean scrolled(float amountX, float amountY) { return false; }

    /**
     * {@summary React to key pressed once.}
     * 
     * @param keycode the key pressed
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
            game.nextDialog();
        }
        return true;
    }
    // Not usefull yet.
    @Override
    public boolean keyDown(int keycode) { return false; }
    @Override
    public boolean keyTyped(char character) { return false; }
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        game.nextDialog();
        return true;
    }
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) { return false; }
    @Override
    public boolean touchDragged(int x, int y, int pointer) { return false; }
    @Override
    public boolean mouseMoved(int x, int y) { return false; }
}
