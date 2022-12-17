package fr.formiko.infanlaboro;

import java.awt.Toolkit;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Infanlaboro");
		// config.setFullscreenMode(Gdx.graphics.getDisplayMode());
		// Gdx.graphics.setDisplayMode(1280, 720, true);
		config.setWindowedMode((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		config.useVsync(true);
		config.setTitle("Elf-Slavery");
		config.setWindowIcon("images/Blue hat gnome.png");
		Game game = new Game();
		game.language = System.getProperty("user.language");
		new Lwjgl3Application(game, config);
	}
}
