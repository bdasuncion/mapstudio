package infoObjects;

import java.awt.image.BufferedImage;
import java.util.Vector;

import tools.ImageTools;

public class MaskInfo {
	int id;
	MaskType type;
	String name;
	BufferedImage mask;
	
	public enum MaskType{
		MT16x16Flat,
		MT16x32Flat,
		MT32x16Flat,
		MT32x32Flat,
		MT16x16Tall,
		MT16x32Tall,
		MT32x16Tall,
		MT32x32Tall,
		MaskTypeCount
	}
	
	public static final String[] types = {
		"16x16Flat","16x32Flat", "32x16Flat", "32x32Flat",
		"16x16Tall","16x32Tall", "32x16Tall", "32x32Tall"
	};
	
	public static final String[] typesExport = { "EMask16x16", "EMask16x32", "EMask32x16",
		"EMask32x32", "EMask16x16Tall", "EMask32x16Tall", "EMask16x32Tall",
	};

	public static final String[] genericMasks = {"generic16x16", "generic16x32", "generic32x16", "generic32x32"};
	private static final int[] genericWidth = {16, 16, 32, 32};
	private static final int[] genericHeight = {16, 32, 16, 32};
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MaskType getType() {
		return type;
	}
	
	public String getTypeToString() {
		return typesExport[type.ordinal()];
	}

	public void setType(MaskType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public BufferedImage getMask() {
		return mask;
	}

	public void setMask(BufferedImage mask) {
		this.mask = mask;
	}

	public String toString() {
		return name;
	}
	
	public static Vector<MaskInfo> generateStandardMask() {
		Vector<MaskInfo> masks = new Vector<MaskInfo>();
		
		int i = 0;
    	for (MaskInfo.MaskType maskVal : MaskInfo.MaskType.values()) {
    		if (i > MaskInfo.MaskType.MT32x32Flat.ordinal()) {
    			break;
    		}
    		
    		MaskInfo mask = new MaskInfo();
    		mask.setId(0);
    		mask.setName(genericMasks[i]);
    		mask.setType(maskVal);
    		BufferedImage image = ImageTools.createEmptyImage(genericWidth[i], genericHeight[i], ImageTools.createColorModel());
    		image = ImageTools.fill(image);
    		mask.setMask(image);
    		masks.add(mask);
    		++i;
    	}
    	
		return masks;
	}
	
}
