package XXLChess.Interfaces;

import java.util.*;

public interface Jump {

    /**
     * A list of camel's moving directions
     * @return a list of camel's moving directions
     */
    public static ArrayList<int[]> camelDirections() {
        return new ArrayList<int[]>() {
            {
                add(new int[] {-1, -3});
                add(new int[] {1, -3});
                add(new int[] {-1, 3});
                add(new int[] {1, 3});
                add(new int[] {-3, -1});
                add(new int[] {-3, 1});
                add(new int[] {3, -1});
                add(new int[] {3, 1});
            }
        };
    }

    /**
     * A list of knight's moving directions
     * @return a list of knight's moving directions
     */
    public static ArrayList<int[]> knightDirections() {
        return new ArrayList<int[]>() {
            {
                add(new int[] {-1, -2});
                add(new int[] {1, -2});
                add(new int[] {-1, 2});
                add(new int[] {1, 2});
                add(new int[] {-2, -1});
                add(new int[] {-2, 1});
                add(new int[] {2, -1});
                add(new int[] {2, 1});
            }
        };
    }

    /**
     * Returns a list of jump directions, based on the piece that calls the method
     * @return a list of jump directions
     */
    public ArrayList<int[]> jumpDirections();
}
