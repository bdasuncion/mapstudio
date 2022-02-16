package infoObjects;

public class EventInfo {
    String type;
    int x;
    int y;
    int width;
    int height;
    
    public static final int MIN_VALUE = 16;
    public static final int WIDTHHEIGHT_MAX = 64;
	
    static public final String TYPE_NONE = "";
    static public final String TYPE_TRANSFER = "TRANSFER";
    
    protected EventInfo(String type) {
    	this.type = type;
    }
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
    
    
}
