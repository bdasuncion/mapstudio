package infoObjects;

public class EventTransferMapInfo extends EventInfo {
    private int transferToX;
    private int transferToY;
    private int transferToZ;
    private String faceDirectionOnTransfer;
    private String transferToMap;
    
	
	static public final String LEFT = "ELeft";
	static public final String RIGHT = "ERight";
	static public final String UP = "EUp";
	static public final String DOWN = "EDown";
	static public final String UPLEFT = "EUpLeft";
	static public final String UPRIGHT = "EUpRight";
	static public final String DOWNLEFT = "EDownLeft";
	static public final String DOWNRIGHT = "EDownRight";
	
	static public final int XYMIN_VALUE = 8;
	static public final int XYMAX_VALUE = 65535;

	public EventTransferMapInfo() {
		super(TYPE_TRANSFER);
	}
	
	public EventTransferMapInfo(EventInfo info) {
		super(TYPE_TRANSFER);
		setX(info.getX());
		setY(info.getY());
		setWidth(info.getWidth());
		setLength(info.getLength());
	}
	
	public int getTransferToX() {
		return transferToX;
	}
	public void setTransferToX(int transferToX) {
		this.transferToX = transferToX;
	}
	public int getTransferToY() {
		return transferToY;
	}
	public void setTransferToY(int transferToY) {
		this.transferToY = transferToY;
	}
	public int getTransferToZ() {
		return transferToZ;
	}

	public void setTransferToZ(int transferToZ) {
		this.transferToZ = transferToZ;
	}

	public String getFaceDirectionOnTransfer() {
		return faceDirectionOnTransfer;
	}
	public void setFaceDirectionOnTransfer(String direction) {
		this.faceDirectionOnTransfer = direction;
	}
	public String getTransferToMap() {
		return transferToMap;
	}
	public void setTransferToMap(String transferToMap) {
		this.transferToMap = transferToMap;
	}
    
    
}
