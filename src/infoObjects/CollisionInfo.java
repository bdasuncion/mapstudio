package infoObjects;

public class CollisionInfo {
	public static int TYPE_SOLID = 0;
	public static int TYPE_CHANGE_LAYER = 1;
	
	private static final String collisionString[] = {
		"TYPE_SOLID", 
		"TYPE_CHANGE_LAYER"
	};
	
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int length = 0;
    private int height = 0;
    private boolean hasCollision = false;
    private int collisionType = TYPE_SOLID;
    private boolean hflip = false;
    private boolean vflip = false;
    
    public static int WIDTHLENGTH_MAX_VALUE = 15;
    public static int HEIGHT_MAX_VALUE = 4080;
    public static int XY_MAX_VALUE = 7;
	public static int MIN_VALUE = 0;
		
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
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int height) {
		this.length = height;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isHasCollision() {
		return hasCollision;
	}
	public void setHasCollision(boolean hasCollision) {
		this.hasCollision = hasCollision;
	}
	public int getCollisionType() {
		return collisionType;
	}
	public void setCollisionType(int collisionType) {
		this.collisionType = collisionType;
	}
    
    public void set(CollisionInfo ci) {
    	setX(ci.getX());
    	setY(ci.getY());
    	setWidth(ci.getWidth());
    	setLength(ci.getLength());
    	setHeight(ci.getHeight());
    	setCollisionType(ci.getCollisionType());
    	setHasCollision(ci.isHasCollision());
    }
    
	public boolean isHflip() {
		return hflip;
	}
	
	public void setHflip(boolean hflip) {
		if (this.hflip != hflip && width > 0) {
			x = WIDTHLENGTH_MAX_VALUE - (x + width);
		}
		this.hflip = hflip;
	}
	
	public boolean isVflip() {
		return vflip;
	}
	
	public void setVflip(boolean vflip) {
		if (this.vflip != vflip && length > 0) {
			y = WIDTHLENGTH_MAX_VALUE - (y + length);
		}
		this.vflip = vflip;
	}
	
	public String getCollisionTypeToString() {
		return collisionString[collisionType];
	}
}
