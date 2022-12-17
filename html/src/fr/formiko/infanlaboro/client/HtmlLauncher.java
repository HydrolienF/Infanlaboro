package fr.formiko.infanlaboro.client;

import fr.formiko.infanlaboro.Game;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.GwtGraphics;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        // Resizable application, uses available space in browser
        GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(true);
        // Fixed size application:
        // return new GwtApplicationConfiguration(1600, 900);
        cfg.fullscreenOrientation = GwtGraphics.OrientationLockType.LANDSCAPE;
        cfg.padHorizontal = 0;
        cfg.padVertical = 0;
        return cfg;
    }
    // @Override
    // public Preloader.PreloaderCallback getPreloaderCallback() {
    // try {
    // return createPreloaderPanel(GWT.getHostPageBaseURL() + "images/Blue hat gnome.png");
    // } catch (Exception e) {
    // return super.getPreloaderCallback();
    // }
    // }

    @Override
    public ApplicationListener createApplicationListener() { return new Game(); }
}