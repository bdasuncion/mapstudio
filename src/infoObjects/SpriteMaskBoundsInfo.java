package infoObjects;

public class SpriteMaskBoundsInfo {
	int startX, startY, endX, endY;
	

	int offsetsX[] = {8, 8, 16, 16, 8, 8, 16, 16};
	int offsetsY[] = {8, 16, 8, 16, 8, 16, 8, 16,};
	public SpriteMaskBoundsInfo(SpriteMaskInfo spriteMask) {
		int x, y;
		x = spriteMask.getX();
		y = spriteMask.getY();
		MaskInfo.MaskType type = spriteMask.getType();
		
		startX = x - offsetsX[type.ordinal()];
		endX = x + offsetsX[type.ordinal()];
		startY = y - offsetsY[type.ordinal()];
		endY = y + offsetsY[type.ordinal()];
	}
	
	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}

	
}
