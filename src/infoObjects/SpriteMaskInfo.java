package infoObjects;

public class SpriteMaskInfo extends MaskInfo {
	int x;
	int y;
	int z;
	
	public static final int MINHEIGHT = 0;
	public static final int MAXHEIGHT = 1024;

	int offsetsX[] = {8, 8, 16, 16, 8, 8, 16, 16};
	int offsetsY[] = {8, 16, 8, 16, 8, 16, 8, 16};
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
	public SpriteMaskBoundsInfo getBounds() {
		return new SpriteMaskBoundsInfo(this);
	}
	
	public void setPositionFromUpperLeftCorner(int xLeft, int yUpper) {
		setX(xLeft + offsetsX[getType().ordinal()]);
		setY(yUpper + offsetsY[getType().ordinal()]);
	}
}
