package fr.formiko.infanlaboro;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].replaceAll("-", "").equalsIgnoreCase("version")) {
			try {
				InputStream is = new DesktopLauncher().getClass().getClassLoader().getResourceAsStream("version.md");
				String version = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
						.collect(Collectors.joining("\n"));
				System.out.println(version);
				System.exit(0);
			} catch (Exception e) {
				System.out.println("Fail to get version in DesktopLauncher.");
			}
		}
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
