package infoObjects;

import java.util.Vector;

public class CollisionLayerInfo {
	Vector<CollisionInfo> collisionTiles;
	int width;
	int height;
	
	public CollisionLayerInfo(int widthInTiles, int heightInTiles) {
		
		this.width = widthInTiles/2;
		this.height = heightInTiles/2;
		
    	collisionTiles = new Vector<CollisionInfo>();
    	//System.out.println("WIDTH IN TILES: " + this.width + " HEIGHT IN TILES: " + this.height);
    	//for (int i = 0; i < (widthInTiles*heightInTiles)/2; ++i ) {
    	for (int i = 0; i < width*height; ++i ) {
    		collisionTiles.add(new CollisionInfo());
    	}
	}
	
	public Vector<CollisionInfo> getCollisionTiles() {
		return collisionTiles;
	}

	public void setCollisionTiles(Vector<CollisionInfo> collisionTiles) {
		this.collisionTiles = collisionTiles;
	}
	
	 public void setCollisionAt(int x, int y, CollisionInfo ci) {
    	if (x >= this.width || y >= this.height) {
    		return;
    	}
    	
    	collisionTiles.get(x + y*this.width).set(ci);
	 }
	 
	 private Vector<CollisionInfo> copy() {
		 Vector<CollisionInfo> collisionCopy = new Vector<CollisionInfo>();
		 
		 //System.out.println("WIDTH IN TILES: " + this.width + " HEIGHT IN TILES: " + this.height);
		 for (int i = 0; i < (this.width*this.height); ++i ) {
			 CollisionInfo copyInfo = new CollisionInfo();
			 copyInfo.set(this.collisionTiles.get(i));
			 collisionCopy.add(copyInfo);
		 }
		 
		 return collisionCopy;
	 }
	 
	 public void shiftUp() {
		 Vector<CollisionInfo> collisionCopy = copy();
		 for (int i = 0; i < height; ++i) {
	        	int copyFromRow = (i + 1)%height;
	        	int copyToRow = i%height;
	        	for (int j = 0; j < width; ++j) {
	        		setCollisionAt(j, copyToRow, collisionCopy.get(copyFromRow*width + j));
	        	}
	        	System.out.println("I: " + i);
	      }
	 }
	 
	 public void shiftDown() {
		 Vector<CollisionInfo> collisionCopy = copy();
		 for (int i = 0; i < height; ++i) {
	        	int copyFromRow = i%height;
	        	int copyToRow = (i + 1)%height;
	        	for (int j = 0; j < width; ++j) {
	        		setCollisionAt(j, copyToRow, collisionCopy.get(copyFromRow*width + j));
	        	}
	        }
		 
		 
	 }
	 
	 public void shiftLeft() {
		 Vector<CollisionInfo> collisionCopy = copy();
		 for (int i = 0; i < height; ++i) {
	    	for (int j = 0; j < width; ++j) {
	    		int copyFromColumn = (j + 1)%width;
	    		setCollisionAt(j, i, collisionCopy.get(i*width + copyFromColumn));
	    	}
	     }
	 }
	 
	 public void shiftRight() {
		 Vector<CollisionInfo> collisionCopy = copy();
		 for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				int copyToColumn = (j + 1)%width;
				int copyFromColumn = j%width;
				setCollisionAt(copyToColumn, i, collisionCopy.get(i*width + copyFromColumn));
			}
		  }
	 }
	 
	 public void resize(int newWidth, int newHeight) {
		Vector<CollisionInfo> collisionCopy = copy();
	    
        int currentWidth = this.width;
        int currentHeight = this.height;
        int copyWidth = Math.min(currentWidth, newWidth);
        int copyHeight = Math.min(currentHeight, newHeight);
        
        this.width = newWidth;
        this.height = newHeight;
        
        collisionTiles = new Vector<CollisionInfo>();
    	for (int i = 0; i < this.width*this.height; ++i ) {
    		collisionTiles.add(new CollisionInfo());
    	}
    	
    	for (int i = 0; i < copyHeight; ++i) {
        	for (int j = 0; j < copyWidth; ++j) {
        		//this.collisionTiles.set(width*i + j, collisionCopy.get(i*width + j));
        		setCollisionAt(j, i, collisionCopy.get((i*currentWidth) + j));
        	}
        }

	}
}
