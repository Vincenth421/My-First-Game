package byow.Core;

public class Point {

    protected int x;
    protected int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Equals using x and y
     * @param obj Object to compare to
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        return this.x == ((Point) obj).x && this.y == ((Point) obj).y;
    }

    /**
     * Hashcode override
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return x * 2 + y;
    }
}
