package infoObjects;

import java.util.HashMap;

public class VramInfo {
	private HashMap<String, Integer> vramMap;
	private int currentIndex;
	public VramInfo() {
		vramMap = new HashMap<String, Integer>();
		currentIndex = 0;
	}
	
	public int setIndex(String tileName) {
		if (!vramMap.containsKey(tileName)) {
			++currentIndex;
			vramMap.put(tileName, currentIndex);
			return currentIndex;
		}
		return vramMap.get(tileName);
	}
	
	public int setIndex(String tileName, int index) {
		vramMap.put(tileName, index);
		if (index > currentIndex) {
			currentIndex = index;
		}
		return currentIndex;
	}
	
	public String[] getIndexedTiles() {
		
		String tileList[] = new String[currentIndex];
		for (String tileName:vramMap.keySet()) {
			tileList[vramMap.get(tileName) - 1] = tileName;
		}
		return tileList;
	}
	
}
