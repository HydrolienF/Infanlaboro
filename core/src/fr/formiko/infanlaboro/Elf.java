package fr.formiko.infanlaboro;

import com.badlogic.gdx.graphics.Color;

public class Elf extends HatActor {
    public Elf(boolean isPlayer) { super((int) (20 * Game.racio), (int) (200 * Game.racio), new Color(0, 0, 1, 1)); }
    public Elf() { super((int) (20 * Game.racio), 0, new Color(0, 1, 0, 1)); }

}
