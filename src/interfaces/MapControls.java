package interfaces;

public interface MapControls {
	public void reloadMap();
	void shiftLeft();
	void shiftRight();
	void shiftUp();
	void shiftDown();
	void doResize(int newWidth, int newHeight);
}
