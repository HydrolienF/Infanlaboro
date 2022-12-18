package fr.formiko.infanlaboro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Game extends ApplicationAdapter {
	static OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;

	private Santa santa;
	private Elf player;
	private List<Elf> prisoners;
	private static Random random = MathUtils.random;
	public static float w;
	public static float h;
	public static float racio;
	private Music music;
	private Music musicDialog;
	private Sound runAwaySound;
	private boolean playingMusic;
	public static float hearRadius;
	public boolean gameOver;
	private long lastTimeSeePlayer;
	private boolean victory;
	private Stage stageEndGame;
	private int idDialog;
	private Label.LabelStyle style;
	private Label label;
	public String language = "";
	public boolean drawStageEndGame;
	private Music musicEndGame;
	private Music musicVictory;
	private long over1Time;
	private long over2Time;
	private boolean over1;
	private boolean over2;
	private boolean haveWinGame;
	private int savedPrisoner;
	private Actor endGameActor1;
	private Actor endGameActor2;

	@Override
	public void create() {
		// full screen
		try {
			Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
			Gdx.graphics.setFullscreenMode(currentMode);
		} catch (Exception e) {
			Gdx.app.log("Init", "Fail to set full screen");
		}
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		racio = w / 1920f;
		hearRadius = 500 * racio;
		camera = new OrthographicCamera(30, 30 * (h / w));
		camera.position.set(w * 0.5f, h * 0.5f, 0);
		viewport = new ScreenViewport(camera);

		startDialog(0);

		InputProcessor inputProcessor = (InputProcessor) new InputCore(this);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(inputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	public void newGame() {
		stage = new Stage(viewport);
		santa = new Santa();
		santa.setCenterX(w / 2);
		santa.setCenterY(h / 4);
		santa.setSpeed(2f);
		santa.setRotation(200);
		player = new Elf(true);
		player.setSize(w * 2, w * 2);
		player.setCenterX(w / 2);
		player.setCenterY(h * 3 / 4);
		player.setSpeed(1f);
		player.setDarkArea(true);
		prisoners = new ArrayList<Elf>();
		for (int i = 0; i < 10; i++) {
			prisoners.add(new Elf());
		}

		stage = new Stage();
		for (Elf elf : prisoners) {
			elf.setCenterX(random.nextFloat() * w);
			elf.setCenterY(random.nextFloat() * h);
			stage.addActor(elf);
		}
		stage.addActor(santa);
		stage.addActor(player);

		if (style == null) {
			BitmapFont bmf = new BitmapFont(Gdx.files.internal("fonts/fontFull.fnt"));
			style = new Label.LabelStyle(bmf, Color.WHITE);
		}
		label = new Label("0/10", style);

		// define a table used to organize our hud's labels
		Table table = new Table();
		table.add(label);
		table.setSize(100, 100);
		table.setX(w - 100);
		table.setY(h - 100);
		stage.addActor(table);

		music = Gdx.audio.newMusic(Gdx.files.internal("tension.mp3"));
		runAwaySound = Gdx.audio.newSound(Gdx.files.internal("runAway.mp3"));
	}

	@Override
	public void render() {
		catchInput();

		if (gameOver) {
			ScreenUtils.clear(0f, 0f, 0f, 1);
			if (over1Time < System.currentTimeMillis() && over1 == false) {
				stageEndGame = new Stage();
				stageEndGame.addActor(endGameActor2);
				musicVictory.play();
				over1 = true;
			}
			if (over2Time < System.currentTimeMillis() && over2 == false) {
				stageEndGame = null;
				idDialog = 0;
				if (!haveWinGame) {
					startDialog(idDialog);
				}
			}

			if (stageEndGame != null && drawStageEndGame) {
				stageEndGame.draw();
			}
			return;
		}
		if (idDialog < 3) {
			ScreenUtils.clear(0f, 0f, 0f, 1);
			stage.act();
			stage.draw();
			// System.out.println("draw " + stage.getActors().get(0));
			return;
		}
		float santaSpeed = 0.8f;
		Vector2 v2 = getVectorStageCoordinates(Gdx.input.getX(), Gdx.input.getY());
		player.goTo(v2);
		boolean haveMove = false;
		if (!player.isNextTo(v2)) {
			player.moveFront();
			player.moveIn(w, h);
			haveMove = true;
			if (santa.see(player)) {
				santa.goTo(new Vector2(player.getCenterX(), player.getCenterY()));
				santaSpeed = 1f;
			}
		}
		if (!santa.see(player)) {
			if (random.nextFloat() > 0.95) {
				santa.setRotation(santa.getRotation() + (random.nextFloat() * 40) - 20);
			} else if (random.nextFloat() > 0.99) {
				santa.goTo(new Vector2(player.getCenterX(), player.getCenterY()));
			}
		}
		if (santa.see(player) && haveMove) {
			lastTimeSeePlayer = System.currentTimeMillis();
		}
		long durationSinceLastSeePlayer = System.currentTimeMillis() - lastTimeSeePlayer;
		if (player.see(santa) && !haveMove && durationSinceLastSeePlayer > 4000) {
			lastTimeSeePlayer = System.currentTimeMillis() - 1000;
			durationSinceLastSeePlayer = System.currentTimeMillis() - lastTimeSeePlayer;
		}
		// System.out.println(durationSinceLastSeePlayer);
		if (durationSinceLastSeePlayer > 300 && durationSinceLastSeePlayer < 2000) {
			santaSpeed = 0f;
		} else if (durationSinceLastSeePlayer > 2000 && durationSinceLastSeePlayer < 4000) {
			santa.runAwayFrom(new Vector2(player.getCenterX(), player.getCenterY()));
		}

		santa.moveFront(santaSpeed);
		// if have been move to avoid wall
		if (santa.moveIn(w, h)) {
			santa.setRotation(santa.getRotation() + (160f + (random.nextFloat() * 40)) % 360f);
			// santa.goTo(new Vector2(player.getCenterX(), player.getCenterY()));
		}


		changeMusicVolume();

		// TODO update actor visibility based on distance.

		float red = 0.04f;
		ScreenUtils.clear(0.9f, 0.9f - red, 0.9f - red, 1);
		stage.act();
		stage.draw();
		boolean haveWin = true;
		for (Elf elf : prisoners) {
			if (elf.hitBoxConnected(player)) {
				if (elf.isVisible()) {
					elf.setVisible(false);
					runAwaySound.play();
					savedPrisoner++;
					label.setText(savedPrisoner + "/10");
				}
			}
			if (elf.isVisible()) {
				haveWin = false;
			}
		}
		boolean haveLost = false;
		if (player.hitBoxConnected(santa)) {
			haveLost = true;
		}
		if (haveLost) {
			System.out.println("Game over!");
			victory = false;
		}
		if (haveWin) {
			System.out.println("Game win!");
			victory = true;
		}
		if (haveWin || haveLost) {
			music.stop();
			String fileName = "";
			String imageFileName = "";
			if (victory) {
				fileName = "win";
				imageFileName = "images/winGame.png";
			} else {
				fileName = "lost";
				imageFileName = "images/Mad santa.png";
			}
			musicEndGame = Gdx.audio.newMusic(Gdx.files.internal("gameOver.mp3"));
			musicVictory = Gdx.audio.newMusic(Gdx.files.internal(fileName + ".mp3"));
			final Texture endGameFrame = new Texture(imageFileName);
			musicEndGame.play();

			drawStageEndGame = false;
			stageEndGame = new Stage();
			endGameActor1 = new Actor() {
				@Override
				public void draw(Batch batch, float parentAlpha) {
					batch.draw(new Texture("images/running away.png"), 0, 0, getWidth(), getHeight());
				}
			};
			endGameActor1.setSize(w, h);
			stageEndGame.addActor(endGameActor1);
			endGameActor2 = new Actor() {
				@Override
				public void draw(Batch batch, float parentAlpha) { batch.draw(endGameFrame, 0, 0, getWidth(), getHeight()); }
			};
			endGameActor2.setSize(w, h);
			over1Time = System.currentTimeMillis() + 5000;
			if (haveWin) {
				over2Time = System.currentTimeMillis() + 18000;
				haveWinGame = true;
			} else {
				over2Time = System.currentTimeMillis() + 8000;
			}
			haveWin = false;
			haveLost = false;
			gameOver = true;
			over1 = false;
			over2 = false;
			drawStageEndGame = true;
		}
	}

	@Override
	public void dispose() {
		// batch.dispose();
		Gdx.app.exit();
	}

	private void catchInput() {
		// float moveX = 0;
		// float moveY = 0;
		// float speed = player.getSpeed() * racio;
		// if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
		// moveY += speed;
		// }
		// if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
		// moveY -= speed;
		// }
		// if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
		// moveX -= speed;
		// }
		// if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
		// moveX += speed;
		// }
		// if (Gdx.input.isKeyPressed(Input.Keys.F11)) {
		// Boolean fullScreen = Gdx.graphics.isFullscreen();
		// Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
		// if (fullScreen == true)
		// Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
		// else
		// Gdx.graphics.setFullscreenMode(currentMode);
		// }
		// player.move(moveX, moveY);
		// return moveX != 0 || moveY != 0;
	}

	private void changeMusicVolume() {
		float vol = (hearRadius - player.distanceTo(santa)) / hearRadius;
		if (vol <= 0) {
			vol = 0;
			playingMusic = false;
			music.stop();
		} else if (!playingMusic) {
			vol = (float) java.lang.Math.pow(vol, 2);
			music.play();
			music.setLooping(true);
		}
		music.setVolume(vol);
		// System.out.println("vol=" + vol);
	}

	private Vector2 getVectorStageCoordinates(float x, float y) { return stage.screenToStageCoordinates(new Vector2(x, y)); }

	private void startDialog(int id) {
		switch (id) {
		case 0:
			gameOver = false;
			setCurentDialog(getText(id), "Blue hat gnome");
			musicDialog = Gdx.audio.newMusic(Gdx.files.internal("gameOver.mp3"));
			musicDialog.setLooping(true);
			musicDialog.play();
			break;
		case 1:
			setCurentDialog(getText(id), "misterious santa");
			break;
		case 2:
			setCurentDialog(getText(id), "Blue hat gnome");
			break;
		case 3:
			savedPrisoner = 0;
			setCurentDialog(null, null);
			musicDialog.stop();
			Gdx.input.setCursorPosition((int) player.getCenterX(), (int) (h - player.getCenterY()));
			break;

		default:
			break;
		}
	}
	public void nextDialog() {
		idDialog++;
		startDialog(idDialog);
	}

	private void setCurentDialog(String text, final String speaker) {
		if (text == null) {
			newGame();
			return;
		}
		stage = new Stage(viewport);
		if (style == null) {
			BitmapFont bmf = new BitmapFont(Gdx.files.internal("fonts/fontFull.fnt"));
			style = new Label.LabelStyle(bmf, Color.WHITE);
		}
		label = new Label(text, style);

		// define a table used to organize our hud's labels
		Table table = new Table();
		table.top();
		table.setSize(w, h / 3);
		// table.setFillParent(true);
		Actor speakerActor = new Actor() {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.draw(new Texture("images/" + speaker + ".png"), 0, 0, getWidth(), getHeight());
			}
		};
		speakerActor.setSize(w / 4, w / 4);
		table.add(speakerActor).expandX();
		table.add(label).expandX();
		stage.addActor(table);
	}

	private String getText(int id) {
		switch (id) {
		case 0:
			if (language.equals("fr")) {
				return "Père Noël ! Fini l'esclavage !\nVoici nos revendications, nous voulons 1 jour de congé par mois, être payé ...";
			} else {
				return "Santa ! No more slavery !\nHere is our demands, we want 1 day off per months, be paid ...";
			}
		case 1:
			if (language.equals("fr")) {
				return "Rhoohoho ! Je vais écraser ce petit lutin insolent.";
			} else {
				return "Rhohoho ! I will crush this little insolent elf.";
			}
		case 2:
			if (language.equals("fr")) {
				return "Père Noël a perdu la tête et a attaché tous les autres elfes.\nAidez-les à s'échapper. Ne vous faites pas voir,\nou si vous êtes vu, arrêtez de courir, il ne voit que les choses en mouvement.";
			} else {
				return "Santa have become crazy & have tie up all other elves. \nHelp them to escape. Don't be seen,\nor if you do stop running he only see moving things.";
			}
		default:
			return null;
		}
	}
}
