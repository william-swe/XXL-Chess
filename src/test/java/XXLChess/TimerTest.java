package XXLChess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TimerTest {
    
    @Test
    public void constructor() {
        Timer timer = new Timer(60, 1, true);
        assertNotNull(timer);
    }

    @Test
    public void isHumanTimer() {
        Timer timer = new Timer(60, 1, true);
        assertTrue(timer.isHumanTimer());
    }

    @Test
    public void tick() {
        Timer timer1 = new Timer(60, 1, true);
        timer1.tick();
        Timer timer2 = new Timer(0, 1, true);
        timer2.tick();
        Timer timer3 = new Timer(60, 1, true);
        for (int i=1; i<=60; i++) {
            timer3.tick();
        }
        Timer timer4 = new Timer(8, 1, true);
        for (int i=1; i<=60; i++) {
            timer4.tick();
        }
    }

    @Test
    public void increment() {
        Timer timer1 = new Timer(10, 1, true);
        timer1.increment();
        assertEquals(timer1.getSeconds(), 11);
        assertEquals(timer1.getMinutes(), 0);
        Timer timer2 = new Timer(50, 10, true);
        timer2.increment();
        assertEquals(timer2.getSeconds(), 0);
        assertEquals(timer2.getMinutes(), 1);
        Timer timer3 = new Timer(50, 69, true);
        timer3.increment();
        assertEquals(timer3.getSeconds(), 59);
        assertEquals(timer3.getMinutes(), 1);
    }

    @Test
    public void reachZero() {
        Timer timer1 = new Timer(60, 1, true);
        assertFalse(timer1.reachZero());
        Timer timer2 = new Timer(0, 1, true);
        assertTrue(timer2.reachZero());
    }

    @Test
    public void convertTimeToString() {
        Timer timer1 = new Timer(625, 1, true);
        assertEquals(timer1.convertTimeToString(), "10:25");
        Timer timer2 = new Timer(85, 1, true);
        assertEquals(timer2.convertTimeToString(), "01:25");
        Timer timer3 = new Timer(65, 1, true);
        assertEquals(timer3.convertTimeToString(), "01:05");
    }

}
