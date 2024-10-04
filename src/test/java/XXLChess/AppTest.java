package XXLChess;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import XXLChess.Pieces.Knight;

import static org.junit.jupiter.api.Assertions.*;

import processing.data.JSONObject;
import processing.core.PImage;
import java.util.*;
import java.io.*;

public class AppTest {
    
    public AppTest() {
        String configPath = "config.json";
        JSONObject conf = PApplet.loadJSONObject(new File(configPath));
        App.PIECE_MOVEMENT_SPEED = (double) conf.get("piece_movement_speed");
        App.MAX_MOVEMENT_TIME = (double) conf.get("max_movement_time");
    }

    @Test
    public void constructor() {
        App app = new App();
        assertNotNull(app);
        assertEquals(App.SPRITESIZE, 480);
        assertEquals(App.CELLSIZE, 48);
        assertEquals(App.SIDEBAR, 120);
        assertEquals(App.BOARD_WIDTH, 14);
        assertEquals(App.FPS, 60);
        assertFalse(app.gameEnd);
        assertFalse(app.isPvP);
        assertFalse(app.isResign);
        Knight knight = new Knight
        ("n", 2, "white", 7, 0, new PImage(0, 0));
        assertNotNull(knight);
        knight.setSelected(true);
    }

    @Test
    public void loadLayoutName() {
        App app = new App();
        assertNotNull(app.loadLayoutName());
    }

    @Test
    public void loadPieceMovementTime() {
        App app = new App();
        app.loadPieceMovementTime();
    }

    @Test
    public void loadPlayerColour() {
        App app = new App();
        assertNotNull(app.loadPlayerColour());
    }

    @Test
    public void loadPlayersTime() {
        App app = new App();
        assertEquals(app.loadPlayersTime().length, 4);
    }

    @Test
    public void initializeTimers() {
        App app = new App();
        app.initializeTimers();
        assertEquals(app.timers.size(), 2);
    }

    @Test
    public void initializePlayers() {
        App app = new App();
        app.initializePlayers();
        assertEquals(app.players.size(), 2);
    }

    @Test
    public void loadLayout() {
        App app = new App();
        App.IMAGES = new HashMap<String, PImage>();
        app.resizeImages();

        // Load images during setup
        App.IMAGES = new HashMap<String, PImage>();
        App.IMAGES.put("P", new PImage(0, 0));
        App.IMAGES.put("p", new PImage(0, 0));
        App.IMAGES.put("R", new PImage(0, 0));
        App.IMAGES.put("r", new PImage(0, 0));
        App.IMAGES.put("N", new PImage(0, 0));
        App.IMAGES.put("n", new PImage(0, 0));
        App.IMAGES.put("B", new PImage(0, 0));
        App.IMAGES.put("b", new PImage(0, 0));
        App.IMAGES.put("H", new PImage(0, 0));
        App.IMAGES.put("h", new PImage(0, 0));
        App.IMAGES.put("C", new PImage(0, 0));
        App.IMAGES.put("c", new PImage(0, 0));
        App.IMAGES.put("G", new PImage(0, 0));
        App.IMAGES.put("g", new PImage(0, 0));
        App.IMAGES.put("A", new PImage(0, 0));
        App.IMAGES.put("a", new PImage(0, 0));
        App.IMAGES.put("K", new PImage(0, 0));
        App.IMAGES.put("k", new PImage(0, 0));
        App.IMAGES.put("E", new PImage(0, 0));
        App.IMAGES.put("e", new PImage(0, 0));
        App.IMAGES.put("Q", new PImage(0, 0));
        App.IMAGES.put("q", new PImage(0, 0));
        
        app.loadPieces();
    }

    @Test
    public void keyPressed() {
        App app = new App();

        // game end and not restart
        app.gameEnd = true;
        app.isResign = true;
        app.keyCode = 70;
        app.keyPressed();
        assertTrue(app.gameEnd);
        assertTrue(app.isResign);
        app.gameEnd = false;
        app.isResign = false;

        // can't resign with wrong keyCode
        app.keyCode = 70;
        app.keyPressed();
        assertFalse(app.gameEnd);
        assertFalse(app.isResign);

        // resign
        app.keyCode = 69;
        app.keyPressed();
        assertTrue(app.gameEnd);
        assertTrue(app.isResign);

        // key release
        app.keyReleased();

    }

    @Test
    public void main() {
        App.main(new String[] {});
    }

    // @Test
    // public void draw() {
    //     App app = new App();
    //     app.frameRate(App.FPS);
    //     app.setup();
    // }

    // @Test
    // public void mouseClicked() {
    //     App app = new App();

    //     MouseEvent me = new MouseEvent(MouseEvent.EVENT_MOUSE_CLICKED, 
    //     app, (long) 1, 1, 0, 0, MouseEvent.BUTTON_COUNT, MouseEvent.BUTTON1);
        
    // }

    // @Test
    // public void settings() {
    //     App app = new App();
    //     app.settings();
    // }

    // @Test
    // public void setup() {
    //     App app = new App();
    //     app.setup();
    // }

}
