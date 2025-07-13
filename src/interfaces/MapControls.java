package interfaces;

public interface MapControls {
	public void reloadMap();
	void shiftLeft();
	void shiftRight();
	void shiftUp();
	void shiftDown();
	void resize(int newWidth, int newHeight);
}
