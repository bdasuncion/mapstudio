package fileRW;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import infoObjects.ActorInfo;
import infoObjects.CollisionInfo;
import infoObjects.EventInfo;
import infoObjects.EventTransferMapInfo;
import infoObjects.MapInfo;
import infoObjects.MapLayerInfo;
import infoObjects.MaskInfo;
import infoObjects.PalletteInfo;
import infoObjects.SpriteMaskInfo;
import infoObjects.TileInfo;
import infoObjects.TileSetInfo;
import mapBlock.Map32x32Tiles;
import mapBlock.MapWxH;

public class ExportToC {
	private static final int WORDS_PER_TILE = 8;
	private static String convert[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E",
			"F" };

	public static void exportToCGBA(File f, MapInfo mapInfo) {

		writeTileSets(f, mapInfo.getPallete(), mapInfo.getTileSets());
		writeMap(f, mapInfo);
	}

	public static void writeTileSets(File f, Vector<PalletteInfo> palletes, Vector<TileSetInfo> tileSets) {
		File outFile = new File(f.getParent() + "\\" + "tile_" + appendExtensionC(f.getName().toLowerCase()));
		FileWriter tilePalletteFile = null;

		try {
			tilePalletteFile = new FileWriter(outFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (PalletteInfo pallette : palletes) {
			createPalette16BitGBA(outFile, tilePalletteFile, pallette);
		}

		for (int i = 1; i < tileSets.size(); ++i) {
			TileSetInfo tileSetInfo = tileSets.get(i);
			createTileSetGBA(outFile, tilePalletteFile, tileSetInfo);
		}

		try {
			tilePalletteFile.flush();
			tilePalletteFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createPalette16BitGBA(File f, FileWriter cFile, PalletteInfo pallette) {

		IndexColorModel cm = pallette.getPallette();
		try {
			cFile.write("const unsigned short " + "pallette_" + pallette.getName() + "[] = {");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < cm.getMapSize(); i++) {
			int RVal16 = (cm.getRed(i) / 8);
			int GVal16 = (cm.getGreen(i) / 8) << 5;
			int BVal16 = (cm.getBlue(i) / 8) << 10;
			int RGBVal16 = RVal16 | GVal16 | BVal16;

			try {
				cFile.write("0x");
				cFile.write(convertByteToHexa2((RGBVal16 >> 8) & 0xff));
				cFile.write(convertByteToHexa2(RGBVal16 & 0xff));
				cFile.write(",");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			cFile.write("};\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private static void createTileSetGBA(File f, FileWriter cFile, TileSetInfo tileSetInfo) {

		int countTiles = 0;
		for (TileInfo tileInfo : tileSetInfo.getTileSet()) {
			if (!tileInfo.isEmptyImage()) {
				++countTiles;
			}
		}
		
		try {
			cFile.write("const unsigned int " + "tile_" + tileSetInfo.getFileName() + "["
					+ countTiles * WORDS_PER_TILE + "] = \n{\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (TileInfo tileInfo : tileSetInfo.getTileSet()) {
			if (!tileInfo.isEmptyImage()) {
			    U324BPP1DGBA(tileInfo.getTileImage().getData().getDataBuffer(), cFile, 8, 8);
			}
		}
		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeMap(File f, MapInfo mapInfo) {
		File outFile = new File(f.getParent() + "\\" + appendExtensionC(f.getName()));
		FileWriter mapFile = null;

		try {
			mapFile = new FileWriter(outFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		writeExternConst(f, mapFile, mapInfo);

		try {
			mapFile.flush();
			mapFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeExternConst(File f, FileWriter cFile, MapInfo mapInfo) {
		for (PalletteInfo palletteInfo : mapInfo.getPallete()) {
			try {
				cFile.write("extern const unsigned short pallette_" + palletteInfo.getName() + "[16];\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 1; i < mapInfo.getTileSets().size(); ++i) {
			TileSetInfo tileSetInfo = mapInfo.getTileSets().get(i);
			int count = 0;
			for (TileInfo tile : tileSetInfo.getTileSet()) {
				if (!tile.isEmptyImage()) {
					++count;
				}
			}
			try {
				cFile.write("extern const unsigned int tile_" + tileSetInfo.getFileName() + "[" + 
			(count*WORDS_PER_TILE) + "];\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		writeMapEntry(f, cFile, mapInfo.getMapLayers());
		
		//writeCollision(f, cFile, mapInfo.getCollisionTiles());
		writeHeightMap(f, cFile, mapInfo.getCollisionTiles());
		
		writeTileSet(f, cFile, mapInfo.getTileSets());
		
		writePallette(f, cFile, mapInfo.getPallete());
		
		writeMapEntrySet(f, cFile, mapInfo.getMapLayers());
		
		writeCharacterInit(f, cFile, mapInfo.getActors());
		
		writeEvents(f, cFile, mapInfo.getEvents());
		
		writeMasks(f, cFile, mapInfo.getMasks());
		
		writeSpriteMasks(f, cFile, mapInfo.getSpriteMasks());
		
		writeMapInfo(f, cFile, mapInfo);
	}

	private static void writeSpriteMasks(File f, FileWriter cFile, Vector<SpriteMaskInfo> spriteMasks) {
		try {
			cFile.write("const SpriteMaskInit spritemask_" + getFileName(f).toLowerCase() + "[] = {\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (SpriteMaskInfo spriteMaskInfo : spriteMasks) {
			try {
				cFile.write("\t{ ");
				cFile.write(spriteMaskInfo.getX() + ", " + spriteMaskInfo.getY() + ", " + spriteMaskInfo.getZ() + ", " + 
				"" + spriteMaskInfo.getId() + "," + spriteMaskInfo.getTypeToString());
				cFile.write(" }, ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			cFile.write("\n};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeMasks(File f, FileWriter cFile, Vector<MaskInfo> masks) {
		try {
			cFile.write("const SpriteMaskImage spritemaskimage_" + getFileName(f).toLowerCase() + "[] = {\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (MaskInfo maskInfo : masks) {
			try {
				cFile.write("\t{ ");
				cFile.write(maskInfo.getTypeToString() + ", " + "imageName");
				cFile.write(" }, ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			cFile.write("\n};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeMapEntry(File f, FileWriter cFile, Vector<MapLayerInfo> mapLayers) {
		try {
			cFile.write("const unsigned short mapentry_" + getFileName(f).toLowerCase() + "[2]["
					+ (mapLayers.get(0).getHeightInTiles() * mapLayers.get(0).getWidthInTiles()) + "] = {\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 2; i < mapLayers.size(); ++i) {
			try {
				cFile.write("\t{\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			writeMapLayer(cFile, mapLayers.get(i));
			try {
				cFile.write("\t},\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeMapLayer(FileWriter cFile, MapLayerInfo mapLayerInfo) {
		for (int i = 0; i < mapLayerInfo.getTiles().size(); ++i) {
			TileInfo tileInfo = mapLayerInfo.getTiles().get(i);

			int tileIdx = tileInfo.getIndex();
			int hflip = (tileInfo.isHflip() ? 1 << 10 : 0);
			int vflip = (tileInfo.isVflip() ? 1 << 11 : 0);
			int palIndex = (tileInfo.getPalletteIndex()) << 12;
			
			int mapEntry = tileIdx | hflip | vflip | palIndex;

			if ((i) % mapLayerInfo.getWidthInTiles() == 0) {
				try {
					// cFile.write("\t\t\t");
					cFile.write("\t\t");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				cFile.write("0x");
				cFile.write(convertByteToHexa2((mapEntry >> 8) & 0xff));
				cFile.write(convertByteToHexa2(mapEntry & 0xff));
				cFile.write(",");
			} catch (IOException e) {
				e.printStackTrace();
			}

			if ((i + 1) % mapLayerInfo.getWidthInTiles() == 0) {
				try {
					cFile.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void writeTileSet(File f, FileWriter cFile, Vector<TileSetInfo> tileSets) {
		Vector<String> tileSetNames = new Vector<String>();
		for (int i = 1; i < tileSets.size(); ++i) {
			TileSetInfo tileSetInfo = tileSets.get(i);
			String tileSetName = "tileset_" + tileSetInfo.getFileName();
			int count = 0;
			for (TileInfo tile: tileSetInfo.getTileSet()) {
				if (!tile.isEmptyImage()) {
					++count;
				}
			}
			try {
				cFile.write("const TileSet " + tileSetName + " = { ");
				cFile.write(count + ", NO_COMPRESSION, ");
				cFile.write("tile_" + tileSetInfo.getFileName() + "};\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			tileSetNames.add(tileSetName);
		}

		try {
			cFile.write("const TileSet *tileset_" + getFileName(f).toLowerCase() + "[] = { ");
			for (String tileSetName : tileSetNames) {
				cFile.write("&" + tileSetName + ", ");
			}
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writePallette(File f, FileWriter cFile, Vector<PalletteInfo> palletteInfos) {
		try {
			cFile.write("const u16 *pallette_" + getFileName(f).toLowerCase() + "[] = { ");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (PalletteInfo palletteInfo : palletteInfos) {
			try {
				cFile.write(" pallette_" + palletteInfo.getName() + ",");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			cFile.write(" };\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeMapEntrySet(File f, FileWriter cFile, Vector<MapLayerInfo> mapLayerInfos) {
		try {
			cFile.write("const u16 *mapentryset_" + getFileName(f).toLowerCase() + "[] = { ");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 2; i < mapLayerInfos.size(); ++i) {
			try {
				cFile.write("mapentry_" + getFileName(f).toLowerCase() + "[" + (i - 2) + "], ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeCharacterInit(File f, FileWriter cFile, Vector<ActorInfo> actors) {
		try {
			cFile.write("const CharacterInit actors_" + getFileName(f).toLowerCase() + "[] = {\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (ActorInfo actorInfo : actors) {
			try {
				cFile.write("\t{ ");
				cFile.write(actorInfo.getX() + ", " + actorInfo.getY() + ", " + actorInfo.getZ() + ", " + actorInfo.getType());
				cFile.write(" }, ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			cFile.write("\n};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeEvents(File f, FileWriter cFile, Vector<EventInfo> events) {
		Vector<EventTransferMapInfo> transferEvents = new Vector<EventTransferMapInfo>();

		for (EventInfo eventInfo : events) {
			if (eventInfo.getType().contentEquals(EventInfo.TYPE_TRANSFER)) {
				transferEvents.add((EventTransferMapInfo) eventInfo);
			}
		}

		writeTransferEvents(f, cFile, transferEvents);
	}

	private static void writeTransferEvents(File f, FileWriter cFile, Vector<EventTransferMapInfo> events) {
		try {
			cFile.write("const EventTransfer transfer_" + getFileName(f).toLowerCase() + "[] = {\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (EventTransferMapInfo eventInfo : events) {
			try {
				cFile.write("\t{ " + eventInfo.getX() + ", " + eventInfo.getY() + ", " + eventInfo.getTransferToX() + ", "
						+ eventInfo.getTransferToY() + ", " + "&" + eventInfo.getTransferToMap() + ", "
						+ eventInfo.getWidth() + ", " + eventInfo.getLength() + ", " + eventInfo.getzOffset() + ", "
						+ eventInfo.getFaceDirectionOnTransfer() + "},\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeHeightMap(File f, FileWriter cFile, Vector<CollisionInfo> collisionSet) {
		try {
			cFile.write("const u8 heightMap_" + getFileName(f).toLowerCase() + "[] = {\n\t");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int conversionFactor = 8;
		
		int idx16 = 0;
		for (CollisionInfo collisionInfo : collisionSet) {
			try {
				cFile.write((collisionInfo.getHeight()/conversionFactor) + ",");
				++idx16;
				if (idx16 >= 16) {
					idx16 = 0;
					cFile.write("\n\t");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeCollision(File f, FileWriter cFile, Vector<CollisionInfo> collisionSet) {
		try {
			cFile.write("const MapCollision collision_" + getFileName(f).toLowerCase() + "[] = {\n\t");
		} catch (IOException e) {
			e.printStackTrace();
		}

		int idx16 = 0;
		for (CollisionInfo collisionInfo : collisionSet) {
			try {
				//System.out.println(collisionInfo.getX() + " " +  collisionInfo.getY());
				if (collisionInfo.getWidth() != 0 && collisionInfo.getLength() != 0) {
				    cFile.write("{ " + collisionInfo.getX() + ", " + collisionInfo.getY() + ", " + 
			        collisionInfo.getCollisionTypeToString() + ", " + collisionInfo.getWidth() + ", " +
			        collisionInfo.getLength() + "}, ");
				} else {
					cFile.write("{0, 0, 0, 0, 0 }, ");
				}
				
				++idx16;
				if (idx16 >= 16) {
					idx16 = 0;
					cFile.write("\n\t");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		try {
			cFile.write("};\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeMapInfo(File f, FileWriter cFile, MapInfo mapInfo) {
		try {
			String name = getFileName(f).toLowerCase();
			cFile.write("const MapInfo " + getFileName(f).toLowerCase() + " = { "
					+ (mapInfo.getWidthInTiles()*8) + ", " + (mapInfo.getHeightInTiles()*8) + ", "
					+ "2, " + (mapInfo.getTileSets().size() - 1) + ", " + mapInfo.getPallete().size()
					+ ", " + mapInfo.getEvents().size() + ", " + mapInfo.getActors().size() + ", "
					+ mapInfo.getSpriteMasks().size() + ", " + (mapInfo.getMasks().size() - MaskInfo.genericMasks.length) + ", "
					+ "NULL " + ", mapentryset_" + name + ", tileset_" + name
					+ ", pallette_" + name + ",\ntransfer_" + name + ", heightMap_" + name + ", actors_" + name
					+ ", spritemask_" + name + ", spritemaskimage_" + name
					+ ", NULL, NULL, NULL, NULL, {0,0,0,0,0} };\n" );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void U324BPP1DGBA(DataBuffer imageData, FileWriter cFile, int width, int height) {
		int offset = 0;
		int i = 0, x = 0;

		for (int k = 0; k < height / 8; k += 1) {
			offset = (width * 4) * k;
			for (int j = 0; j < width / 8; j++) {
				try {
					cFile.write("\t");
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (i = offset, x = 0; x < 8; i += width / 2, x++) {
					String hexa = convertByteToHexa((imageData.getElem(i + 3) & 0xff));
					hexa += convertByteToHexa((imageData.getElem(i + 2) & 0xff));
					hexa += convertByteToHexa((imageData.getElem(i + 1) & 0xff));
					hexa += convertByteToHexa((imageData.getElem(i) & 0xff));
					try {
						cFile.write("0x");
						cFile.write(hexa);
						if (i < (width * height / 2) - 4)
							cFile.write(",");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					cFile.write(",\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				offset += 4;
			}
		}
	}

	private static void createMapCollisionBlock(Map32x32Tiles mapBlock, FileWriter cFile) {
		for (int i = 0; i < mapBlock.getWidthOfMap() * mapBlock.getHeightOfMap(); i += 8) {
			int collison1 = mapBlock.getIndexOfTileAt(i);
			int collison2 = mapBlock.getIndexOfTileAt(i + 1) << 4;
			int collison3 = mapBlock.getIndexOfTileAt(i + 2) << 8;
			int collison4 = mapBlock.getIndexOfTileAt(i + 3) << 12;
			int collison5 = mapBlock.getIndexOfTileAt(i + 4) << 16;
			int collison6 = mapBlock.getIndexOfTileAt(i + 5) << 20;
			int collison7 = mapBlock.getIndexOfTileAt(i + 6) << 24;
			int collison8 = mapBlock.getIndexOfTileAt(i + 7) << 28;

			int collisionEntry = collison1 | collison2 | collison3 | collison4 | collison5 | collison6 | collison7
					| collison8;

			if ((i) % 32 == 0)
				try {
					cFile.write("\n\t");
				} catch (IOException e) {
					e.printStackTrace();
				}

			try {
				cFile.write("0x");
				cFile.write(convertByteToHexa(collisionEntry & 0xff));
				cFile.write(convertByteToHexa((collisionEntry >> 8) & 0xff));
				cFile.write(convertByteToHexa((collisionEntry >> 16) & 0xff));
				cFile.write(convertByteToHexa((collisionEntry >> 24) & 0xff));

				cFile.write(",");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void createMapCollisionBlock(MapWxH mapBlock, FileWriter cFile) {
		int nInt = (mapBlock.getWidthInTiles() / 8) - 1;
		int nCollision = 8;
		if (mapBlock.getWidthInTiles() % 8 > 0) {
			nInt += 1;
			nCollision = mapBlock.getWidthInTiles() % 8;
		}

		int collison[] = new int[8];
		for (int i = 0, checkLast = 0; i < mapBlock.getWidthInTiles() * mapBlock.getHeightInTiles(); checkLast++) {
			int addIdx = 0;

			if (checkLast < nInt) {
				int j = 0;
				for (j = 0; j < 8; j++) {
					collison[j] = mapBlock.getIndexFromTiles().get(i + j) << (j * 4);
				}
				addIdx = 8;
			} else {
				int j = 0;
				for (j = 0; j < nCollision; j++) {
					collison[j] = mapBlock.getIndexFromTiles().get(i + j) << (j * 4);
				}

				// padding
				for (; j < 8; j++) {
					collison[j] = 1 << (j * 4);
				}
				addIdx = nCollision;
				checkLast = -1;
			}

			int collisionEntry = collison[0] | collison[1] | collison[2] | collison[3] | collison[4] | collison[5]
					| collison[6] | collison[7];

			if (checkLast == 0)
				try {
					cFile.write("\n\t");
				} catch (IOException e) {
					e.printStackTrace();
				}

			try {
				cFile.write("0x");
				cFile.write(convertByteToHexa(collisionEntry & 0xff));
				cFile.write(convertByteToHexa((collisionEntry >> 8) & 0xff));
				cFile.write(convertByteToHexa((collisionEntry >> 16) & 0xff));
				cFile.write(convertByteToHexa((collisionEntry >> 24) & 0xff));

				cFile.write(",");
			} catch (IOException e) {
				e.printStackTrace();
			}
			i += addIdx;
		}
	}

	private static void createMapTileBlock(Map32x32Tiles mapBlock, FileWriter cFile) {
		for (int i = 0; i < mapBlock.getWidthOfMap() * mapBlock.getHeightOfMap(); i++) {
			int tileIdx = mapBlock.getIndexOfTileAt(i);
			int hflip = mapBlock.getHorizontalFlipAt(i) << 10;
			int vflip = mapBlock.getVerticalFlipAt(i) << 11;
			int palIndex = mapBlock.getPaletteBankAt(i) << 12;
			int mapEntry = tileIdx | hflip | vflip | palIndex;

			if ((i) % 16 == 0)
				try {
					cFile.write("\t\t\t");
				} catch (IOException e) {
					e.printStackTrace();
				}

			try {
				cFile.write("0x");
				cFile.write(convertByteToHexa2((mapEntry >> 8) & 0xff));
				cFile.write(convertByteToHexa2(mapEntry & 0xff));
				cFile.write(",");
			} catch (IOException e) {
				e.printStackTrace();
			}

			if ((i + 1) % 16 == 0)
				try {
					cFile.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private static void createMapTileBlock(MapWxH mapBlock, FileWriter cFile) {
		for (int i = 0; i < mapBlock.getWidthInTiles() * mapBlock.getHeightInTiles(); i++) {
			int tileIdx = mapBlock.getIndexFromTiles().get(i);
			int hflip = mapBlock.getFlipHorizontal().get(i) << 10;
			int vflip = mapBlock.getFlipVertical().get(i) << 11;
			int palIndex = mapBlock.getPaletteBank().get(i) << 12;
			int mapEntry = tileIdx | hflip | vflip | palIndex;

			if ((i) % mapBlock.getWidthInTiles() == 0) {
				try {
					// cFile.write("\t\t\t");
					cFile.write("\t\t");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				cFile.write("0x");
				cFile.write(convertByteToHexa2((mapEntry >> 8) & 0xff));
				cFile.write(convertByteToHexa2(mapEntry & 0xff));
				cFile.write(",");
			} catch (IOException e) {
				e.printStackTrace();
			}

			if ((i + 1) % mapBlock.getWidthInTiles() == 0) {
				try {
					cFile.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String getFileName(File f) {
		String fileName = f.getName();
		if (fileName.contains(".c")) {
			return fileName.substring(0, fileName.length() - 2);
		}
		return fileName;
	}

	private static String appendExtensionC(String fileName) {
		if (fileName.length() <= 2)
			return fileName + ".c";
		String ext = fileName.substring(fileName.length() - 2).toLowerCase();

		if (ext.matches(".c") == true)
			return fileName;

		return fileName + ".c";
	}

	private static String convertByteToHexa(int byteVal) {
		String hexaVal = convert[byteVal & 0xf];
		hexaVal += convert[(byteVal >> 4) & 0xf];
		return hexaVal;
	}

	private static String convertByteToHexa2(int byteVal) {
		String hexaVal = convert[(byteVal >> 4) & 0xf];
		hexaVal += convert[byteVal & 0xf];
		return hexaVal;
	}
}