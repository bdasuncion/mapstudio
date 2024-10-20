package fileRW;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import com.sun.org.apache.xpath.internal.operations.Bool;

import infoObjects.ActorInfo;
import infoObjects.CollisionInfo;
import infoObjects.EventInfo;
import infoObjects.EventTransferMapInfo;
import infoObjects.MapInfo;
import infoObjects.MapLayerInfo;
import infoObjects.MaskInfo;
import infoObjects.SpriteMaskInfo;
import infoObjects.TileInfo;
import infoObjects.TileSetInfo;
import mapBlock.Map32x32Tiles;
import mapBlock.MapWxH;
import tileSetSelection.TileSetButtonDisplay;

public class MapFileReaderWriter {
	DocumentBuilderFactory factory;
	DocumentBuilder builder;

	private static final String ROOT = "map";
	private static final String TILESETS = "tilesets";
	private static final String TILESET = "tileset";
	private static final String TILESETCOLLISION = "tilesetcollision";
	private static final String FILE = "file";

	private static final String MAPWIDTH = "mapWidth";
	private static final String MAPHEIGHT = "mapHeight";

	private static final String COLLISIONWIDTH = "width";
	private static final String COLLISIONLENGTH = "length";
	private static final String COLLISIONHEIGHT = "height";

	private static final String MAPENTRY = "mapEntry";
	private static final String TILE = "tile";
	private static final String TILES = "tiles";

	private static final String TILENAME = "tileName";
	private static final String ISVFLIP = "vflip";
	private static final String ISHFLIP = "hflip";
	private static final String LAYER = "layer";
	private static final String INDEX = "index";

	private static final String COLLISIONMAP = "collisionMap";
	private static final String COLLISION = "collision";
	private static final String STARTX = "startX";
	private static final String STARTY = "startY";
	
	private static final String ACTORS = "actors";
	private static final String ACTOR = "actor";
	private static final String ACTOR_XPOS = "actor_xpos";
	private static final String ACTOR_YPOS = "actor_ypos";
	private static final String ACTOR_ZPOS = "actor_zpos";
	private static final String ACTOR_TYPE = "actor_type";
	
	private static final String EVENTS = "events";
	private static final String EVENT = "event";
	private static final String EVENT_XPOS = "event_xpos";
	private static final String EVENT_YPOS = "event_ypos";
	private static final String EVENT_TYPE = "event_type";
	private static final String EVENT_WIDTH = "event_width";
	private static final String EVENT_LENGTH = "event_length";
	private static final String EVENT_ZOFFSET = "event_zoffset";
	private static final String EVENTTRANSFER_MAP = "eventtransfer_map";
	private static final String EVENTTRANSFER_X = "eventtransfer_x";
	private static final String EVENTTRANSFER_Y = "eventtransfer_y";
	private static final String EVENTTRANSFER_Z = "eventtransfer_Z";
	private static final String EVENTTRANSFER_DIRECTION = "eventtransfer_direction";
	
	private static final String SPRITEMASKS = "spritemasks";
	private static final String SPRITEMASK = "spritemask";
	private static final String SPRITEMASK_ID = "spritemask_id";
	private static final String SPRITEMASK_TYPE = "spritemask_type";
	private static final String SPRITEMASK_X = "spritemask_x";
	private static final String SPRITEMASK_Y = "spritemask_y";
	private static final String SPRITEMASK_Z = "spritemask_z";
	private static final String SPRITEMASK_NAME = "spritemask_name";
	
	private static final String VERSION = "version";
	private static final String CURRENTVERSION = "2";
	

	public MapFileReaderWriter() {

	}

	public void write(MapInfo mapInfo, File f) {
		File fileOut = new File(f.getParent() + "\\" + this.appendExtension(f.getName()));

		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Document doc = builder.newDocument();
		
		Element rootElement = doc.createElement(ROOT);
		doc.appendChild(rootElement);

		Element version = doc.createElement(VERSION);
		version.appendChild(doc.createTextNode(CURRENTVERSION));
		rootElement.appendChild(version);
		
		rootElement.appendChild(createTileSets(doc, mapInfo));
		Element width = doc.createElement(MAPWIDTH);
		width.appendChild(doc.createTextNode(mapInfo.getWidthInTiles() + ""));
		rootElement.appendChild(width);
		Element height = doc.createElement(MAPHEIGHT);
		height.appendChild(doc.createTextNode(mapInfo.getHeightInTiles() + ""));
		rootElement.appendChild(height);

		rootElement.appendChild(createCollisionMap(doc, mapInfo.getCollisionTiles()));

		rootElement.appendChild(createActors(doc, mapInfo.getActors()));
		
		rootElement.appendChild(createEvents(doc, mapInfo.getEvents()));
		
		rootElement.appendChild(createMasks(doc, mapInfo.getSpriteMasks()));
		
		for (MapLayerInfo mapLayer : mapInfo.getMapLayers()) {
			rootElement.appendChild(createMapEntry(doc, mapLayer));
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fileOut);
			transformer.transform(source, result);

			// Output to console for testing
			// StreamResult consoleResult = new StreamResult(System.out);
			// transformer.transform(source, consoleResult);

		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Node createMasks(Document doc, Vector<SpriteMaskInfo> spriteMasks) {
		Element eventsElement = doc.createElement(SPRITEMASKS);
		for (SpriteMaskInfo spriteMask: spriteMasks) {
			eventsElement.appendChild(createMask(doc, spriteMask));
		}
		
		return eventsElement;
	}
	
	private Element createMask(Document doc, SpriteMaskInfo spriteMask) {
		Element event = doc.createElement(SPRITEMASK);
		Element type = doc.createElement(SPRITEMASK_TYPE);
		type.appendChild(doc.createTextNode(Integer.toString(spriteMask.getType().ordinal())));
		Element x = doc.createElement(SPRITEMASK_X);
		x.appendChild(doc.createTextNode(Integer.toString(spriteMask.getX())));
		Element y = doc.createElement(SPRITEMASK_Y);
		y.appendChild(doc.createTextNode(Integer.toString(spriteMask.getY())));
		Element z = doc.createElement(SPRITEMASK_Z);
		z.appendChild(doc.createTextNode(Integer.toString(spriteMask.getZ())));
		Element id  = doc.createElement(SPRITEMASK_ID);
		id.appendChild(doc.createTextNode(Integer.toString(spriteMask.getId())));
		Element name  = doc.createElement(SPRITEMASK_NAME);
		name.appendChild(doc.createTextNode(spriteMask.getName()));
		
		event.appendChild(type);
		event.appendChild(x);
		event.appendChild(y);
		event.appendChild(z);
		event.appendChild(id);
		event.appendChild(name);
		
		return event;
	}

	private Element createEvents(Document doc, Vector<EventInfo> events) {
		Element eventsElement = doc.createElement(EVENTS);
		for (EventInfo eventInfo: events) {
			eventsElement.appendChild(createEvent(doc, eventInfo));
		}
		
		return eventsElement;
	}
	
	private Element createEvent(Document doc, EventInfo eventInfo) {
		Element event = doc.createElement(EVENT);
		Element type = doc.createElement(EVENT_TYPE);
		type.appendChild(doc.createTextNode(eventInfo.getType()));
		Element x = doc.createElement(EVENT_XPOS);
		x.appendChild(doc.createTextNode(Integer.toString(eventInfo.getX())));
		Element y = doc.createElement(EVENT_YPOS);
		y.appendChild(doc.createTextNode(Integer.toString(eventInfo.getY())));
		Element width = doc.createElement(EVENT_WIDTH);
		width.appendChild(doc.createTextNode(Integer.toString(eventInfo.getWidth())));
		Element length  = doc.createElement(EVENT_LENGTH);
		length.appendChild(doc.createTextNode(Integer.toString(eventInfo.getLength())));
		Element zOffset  = doc.createElement(EVENT_ZOFFSET);
		zOffset.appendChild(doc.createTextNode(Integer.toString(eventInfo.getzOffset())));
		
		event.appendChild(type);
		event.appendChild(x);
		event.appendChild(y);
		event.appendChild(width);
		event.appendChild(length);
		event.appendChild(zOffset);
		
		if (eventInfo.getType().contentEquals(EventInfo.TYPE_TRANSFER)) {
			createEventTransfer(event, doc, (EventTransferMapInfo) eventInfo);
		}
		
		return event;
	}
	
	private void createEventTransfer(Element event, Document doc, EventTransferMapInfo eventInfo) {
		Element transferToMap = doc.createElement(EVENTTRANSFER_MAP);
		transferToMap.appendChild(doc.createTextNode(eventInfo.getTransferToMap()));
		Element x = doc.createElement(EVENTTRANSFER_X);
		x.appendChild(doc.createTextNode("" + eventInfo.getTransferToX()));
		Element y = doc.createElement(EVENTTRANSFER_Y);
		y.appendChild(doc.createTextNode("" + eventInfo.getTransferToY()));
		Element z = doc.createElement(EVENTTRANSFER_Z);
		z.appendChild(doc.createTextNode("" + eventInfo.getTransferToZ()));
		Element direction = doc.createElement(EVENTTRANSFER_DIRECTION);
		direction.appendChild(doc.createTextNode(eventInfo.getFaceDirectionOnTransfer()));
		
		event.appendChild(transferToMap);
		event.appendChild(x);
		event.appendChild(y);
		event.appendChild(z);
		event.appendChild(direction);
	}
	
	private Element createActors(Document doc, Vector<ActorInfo> actors) {
		Element actorsElement = doc.createElement(ACTORS);
		for (ActorInfo actorInfo : actors) {
			actorsElement.appendChild(createActor(doc, actorInfo));
		}
		return actorsElement;
	}
	
	private Element createActor(Document doc, ActorInfo actorInfo) {
		Element actor = doc.createElement(ACTOR);
		
		Element type = doc.createElement(ACTOR_TYPE);
		type.appendChild(doc.createTextNode(actorInfo.getType()));
		Element x = doc.createElement(ACTOR_XPOS);
		x.appendChild(doc.createTextNode(Integer.toString(actorInfo.getX())));
		Element y = doc.createElement(ACTOR_YPOS);
		y.appendChild(doc.createTextNode(Integer.toString(actorInfo.getY())));
		Element z = doc.createElement(ACTOR_ZPOS);
		z.appendChild(doc.createTextNode(Integer.toString(actorInfo.getZ())));
		
		actor.appendChild(type);
		actor.appendChild(x);
		actor.appendChild(y);
		actor.appendChild(z);
		
		return actor;
	}

	private Element createTileSets(Document doc, MapInfo mapInfo) {
		Element tileSets = doc.createElement(TILESETS);

		Vector<TileSetInfo> tileSetsInfo = mapInfo.getTileSets();
		for (TileSetInfo tileSet : tileSetsInfo) {
			tileSets.appendChild(createTileSet(doc, tileSet));
		}
		return tileSets;
	}

	private Element createTileSet(Document doc, TileSetInfo tileSetInfo) {

		Element tileSet = doc.createElement(TILESET);

		Element file = doc.createElement(FILE);
		file.appendChild(doc.createTextNode(tileSetInfo.getFileName()));

		Element tiles = doc.createElement(TILES);
		for (TileInfo tileInfo : tileSetInfo.getTileSet()) {
			tiles.appendChild(createTile(doc, tileInfo));
		}

		Element collision = doc.createElement(TILESETCOLLISION);
		for (CollisionInfo collisionInfo : tileSetInfo.getCollision()) {
			collision.appendChild(createCollision(doc, collisionInfo));
		}

		tileSet.appendChild(file);
		tileSet.appendChild(tiles);
		tileSet.appendChild(collision);
		return tileSet;
	}

	private Element createMapEntry(Document doc, MapLayerInfo mapLayerInfo) {
		Element mapEntry = doc.createElement(MAPENTRY);
		for (TileInfo tile : mapLayerInfo.getTiles()) {
			mapEntry.appendChild(createTile(doc, tile));
		}
		return mapEntry;
	}

	private Element createTile(Document doc, TileInfo tileInfo) {
		Element tile = doc.createElement(TILE);

		Element tileName = doc.createElement(TILENAME);
		tileName.appendChild(doc.createTextNode(tileInfo.getName()));
		Element isVflip = doc.createElement(ISVFLIP);
		isVflip.appendChild(doc.createTextNode(tileInfo.isVflip() + ""));
		Element isHflip = doc.createElement(ISHFLIP);
		isHflip.appendChild(doc.createTextNode(tileInfo.isHflip() + ""));
		Element setLayer = doc.createElement(LAYER);
		setLayer.appendChild(doc.createTextNode(tileInfo.getSetToLayer() + ""));
		Element setIndex = doc.createElement(INDEX);
		setIndex.appendChild(doc.createTextNode(tileInfo.getIndex() + ""));

		tile.appendChild(tileName);
		tile.appendChild(isVflip);
		tile.appendChild(isHflip);
		tile.appendChild(setLayer);
		tile.appendChild(setIndex);

		return tile;
	}

	private Element createCollisionMap(Document doc, Vector<CollisionInfo> collisionMap) {
		Element collisions = doc.createElement(COLLISIONMAP);
		for (CollisionInfo collision : collisionMap) {
			collisions.appendChild(createCollision(doc, collision));
		}

		return collisions;
	}

	private Element createCollision(Document doc, CollisionInfo collisionInfo) {
		Element collision = doc.createElement(COLLISION);

		Element startX = doc.createElement(STARTX);
		startX.appendChild(doc.createTextNode(collisionInfo.getX() + ""));
		Element startY = doc.createElement(STARTY);
		startY.appendChild(doc.createTextNode(collisionInfo.getY() + ""));
		Element width = doc.createElement(COLLISIONWIDTH);
		width.appendChild(doc.createTextNode(collisionInfo.getWidth() + ""));
		Element length = doc.createElement(COLLISIONLENGTH);
		length.appendChild(doc.createTextNode(collisionInfo.getLength() + ""));
		Element height = doc.createElement(COLLISIONHEIGHT);
		height.appendChild(doc.createTextNode(collisionInfo.getHeight() + ""));
		Element hflip = doc.createElement(ISHFLIP);
		hflip.appendChild(doc.createTextNode(Boolean.toString(collisionInfo.isHflip())));
		Element vflip = doc.createElement(ISVFLIP);
		vflip.appendChild(doc.createTextNode(Boolean.toString(collisionInfo.isVflip())));
		
		collision.appendChild(startX);
		collision.appendChild(startY);
		collision.appendChild(width);
		collision.appendChild(length);
		collision.appendChild(height);
		collision.appendChild(hflip);
		collision.appendChild(vflip);
		return collision;
	}

	private String appendExtension(String fileName) {
		if (fileName.length() <= 4)
			return fileName + ".map";
		String ext = fileName.substring(fileName.length() - 4).toLowerCase();

		if (ext.matches(".map") == true)
			return fileName;

		return fileName + ".map";
	}

	public void read(File f, Dimension dimension, Vector<TileSetInfo> tileSetInfo, Vector<CollisionInfo> collisionInfos, 
			Vector<Vector<TileInfo>> layers, Vector<ActorInfo> actors, Vector<EventInfo> events, 
			Vector<SpriteMaskInfo> spriteMasks, Vector<MaskInfo> masks) {
		// File fileOut = new File(f.getParent() + "\\" +
		// this.appendExtension(f.getName()));
		String fileDirectory = f.getParent();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		int mapWidth = 0, mapHeight = 0;
		DocumentBuilder dBuilder;
		Document doc;
		int version = 1;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(f);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		NodeList saveVersion = doc.getElementsByTagName(VERSION);
		for (int idx = 0; idx < saveVersion.getLength(); idx++) {
			Node versionNode = saveVersion.item(idx);
			if (versionNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) versionNode;
				if (eElement.getTagName().matches(VERSION)) {
					version = Integer.parseInt(eElement.getTextContent());
				}
			}
		}
		System.out.println("VERSION: " + version);

		NodeList width = doc.getElementsByTagName(MAPWIDTH);
		for (int idx = 0; idx < width.getLength(); idx++) {
			Node tileSetListNode = width.item(idx);
			if (tileSetListNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetListNode;
				if (eElement.getTagName().matches(MAPWIDTH)) {
					mapWidth = Integer.parseInt(eElement.getTextContent());
				}
			}
		}

		NodeList height = doc.getElementsByTagName(MAPHEIGHT);
		for (int idx = 0; idx < height.getLength(); idx++) {
			Node tileSetListNode = height.item(idx);
			if (tileSetListNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetListNode;
				if (eElement.getTagName().matches(MAPHEIGHT)) {
					mapHeight = Integer.parseInt(eElement.getTextContent());
				}
			}
		}
		
		dimension.setSize(mapWidth, mapHeight);

		NodeList tileSetList = doc.getElementsByTagName(TILESETS);
		for (int idx = 0; idx < tileSetList.getLength(); idx++) {
			Node tileSetListNode = tileSetList.item(idx);
			if (tileSetListNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetListNode;
				if (eElement.getTagName().matches(TILESETS)) {
					readTileSets(eElement.getChildNodes(), fileDirectory, tileSetInfo);
				}
			}
		}

		NodeList collisionMap = doc.getElementsByTagName(COLLISIONMAP);
		for (int idx = 0; idx < collisionMap.getLength(); idx++) {
			Node collisionListNode = collisionMap.item(idx);
			if (collisionListNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) collisionListNode;
				if (eElement.getTagName().matches(COLLISIONMAP)) {
					readCollisionMap(eElement.getChildNodes(), collisionInfos);
				}
			}
		}
		
		NodeList actorList = doc.getElementsByTagName(ACTORS);
		for (int idx = 0; idx < actorList.getLength(); idx++) {
			Node actorsListNode = actorList.item(idx);
			if (actorsListNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) actorsListNode;
				if (eElement.getTagName().matches(ACTORS)) {
					readActorsEntry(eElement.getChildNodes(), actors);
				}
			}
		}
		
		NodeList eventList = doc.getElementsByTagName(EVENTS);
		for (int idx = 0; idx < eventList.getLength(); idx++) {
			Node eventsListNode = eventList.item(idx);
			if (eventsListNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) eventsListNode;
				if (eElement.getTagName().matches(EVENTS)) {
					readEventsEntry(eElement.getChildNodes(), events);
				}
			}
		}
		
		NodeList spriteMaskList = doc.getElementsByTagName(SPRITEMASKS);
		for (int idx = 0; idx < spriteMaskList.getLength(); idx++) {
			Node eventsListNode = spriteMaskList.item(idx);
			if (eventsListNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) eventsListNode;
				if (eElement.getTagName().matches(SPRITEMASKS)) {
					readSpriteMaskEntry(eElement.getChildNodes(), spriteMasks, masks);
				}
			}
		}
		
		NodeList mapEntry = doc.getElementsByTagName(MAPENTRY);
		for (int idx = 0; idx < mapEntry.getLength(); idx++) {
			Node tileSetListNode = mapEntry.item(idx);
			if (tileSetListNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetListNode;
				if (eElement.getTagName().matches(MAPENTRY)) {
					layers.add(new Vector<TileInfo>());
					readMapEntry(eElement.getChildNodes(), layers.lastElement());
				}
			}
		}
		
		if (version == 1) {
			convertFromVersion1(tileSetInfo, layers);
		}

	}
	
	private void convertFromVersion1(Vector<TileSetInfo> tileSetInfo, Vector<Vector<TileInfo>> layers) {
		for (TileSetInfo tileSet: tileSetInfo) {
			int width = tileSet.getWidthInTiles(), height = tileSet.getHeightInTiles();
			for (Vector<TileInfo> layer: layers) {
				for (TileInfo tile: layer) {
					String tileName = tile.getName();
					if (tileName.contains(tileSet.getFileName())) {
						int index = tileName.lastIndexOf('_');
						String number = tileName.substring(index + 1);
						int tileCount = Integer.parseInt(number);
						int row = tileCount/width;
						int column = tileCount%width;
						tile.setName(tileSet.getFileName() + "_" + row + "_" + column);
					}
				}
			}
		}
	}

	public void readTileSets(NodeList tileSet, String parentDirectory, Vector<TileSetInfo> tileSetInfos) {
		int tileIdx = MapInfo.TILE_START_IDX;
		for (int idx = 0; idx < tileSet.getLength(); idx++) {
			Node tileSetAttributeNode = tileSet.item(idx);

			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;
				if (eElement.getTagName() == TILESET) {
					tileIdx = readTileSet(eElement.getChildNodes(), parentDirectory, tileIdx, tileSetInfos);
				}
			}
		}
	}

	public int readTileSet(NodeList tileSet, String parentDirectory, int tileIdx, Vector<TileSetInfo> tileSetInfos) {
		int tileIdxReturn = tileIdx;
		TileReader read = null;
		String name = "";
		Vector<TileInfo> tiles = new Vector<TileInfo>();
		HashMap<String, Integer> tilesIndex = new HashMap<String, Integer>();
		Vector<CollisionInfo> collisionMap = new Vector<CollisionInfo>();
		for (int idx = 0; idx < tileSet.getLength(); idx++) {
			Node tileSetAttributeNode = tileSet.item(idx);
			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;

				if (eElement.getTagName().matches(FILE)) {
					if (eElement.getTextContent().matches("ERASER")) {
						continue;
					}
					name = eElement.getTextContent();
					File tileFile = new File(parentDirectory + "\\" + eElement.getTextContent() + ".tile");
					read = new TileReader();
					read.read(tileFile);
				} else if (eElement.getTagName().matches(TILES)) {
					readTileList(eElement.getChildNodes(), tilesIndex);
				} else if (eElement.getTagName().matches(TILESETCOLLISION)) {
					readCollisionMap(eElement.getChildNodes(), collisionMap);
				}
			}
		}
		if (read != null) {
			int row = 0, column = 0;
			for (int i = 0; i < read.getTile().length; ++i) {
				if (tiles.size() >= i) {
				    tiles.add(new TileInfo(8, 8));
				}
				
				tiles.get(i).setTile(read.getTile()[i]);
				tiles.get(i).setName(TileSetInfo.formatName(name, row, column));
				//System.out.println("READ:" + TileSetInfo.formatName(name, row, column));
				++column;
				if (column >= read.getWidthInTiles()) {
					column = 0;
					++row;
				}
				//tiles.get(i).setIndex(tilesIndex.get(tiles.get(i).getName()));
				/*if (!tiles.get(i).isEmptyImage()) {
					//tiles.get(i).setIndex(tileIdxSet);
					//++tileIdxSet;
				} else {
					tiles.get(i).setIndex(0);
				}*/
			}
			TileSetInfo tileSetInfo = new TileSetInfo(read.getWidthInTiles(), 
					read.getHeightInTiles(), tiles, collisionMap);
			tileSetInfo.setFileName(name);
			tileSetInfos.add(tileSetInfo);
			tileIdxReturn = tileSetInfo.getTileIdxEnd() + 1;
		}
		return tileIdxReturn;
	}

	public void readTileList(NodeList tileSet, HashMap<String, Integer> tilesIndex) {
		for (int idx = 0; idx < tileSet.getLength(); idx++) {
			Node tileSetAttributeNode = tileSet.item(idx);
			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;
				if (eElement.getTagName().matches(TILE)) {
					TileInfo tileInfo = readTile(eElement.getChildNodes());
					tilesIndex.put(tileInfo.getName(), tileInfo.getIndex());
				}
			}
		}
	}

	public void readCollisionMap(NodeList collisions, Vector<CollisionInfo> collisionMap) {
		for (int idx = 0; idx < collisions.getLength(); idx++) {
			Node tileSetAttributeNode = collisions.item(idx);
			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;
				if (eElement.getTagName().matches(COLLISION)) {
					collisionMap.add(readCollision(eElement.getChildNodes()));
				}
			}
		}
	}

	public TileInfo readTile(NodeList tile) {
		TileInfo tileInfo = new TileInfo(8, 8);
		for (int idx = 0; idx < tile.getLength(); idx++) {
			Node tileSetAttributeNode = tile.item(idx);
			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;

				if (eElement.getTagName().matches(TILENAME)) {
					// System.out.println("TILENAME " + eElement.getTextContent());
					tileInfo.setName(eElement.getTextContent());
				} else if (eElement.getTagName().matches(ISVFLIP)) {
					// System.out.println("VFLIP " + eElement.getTextContent());
					tileInfo.setVflip(Boolean.parseBoolean(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(ISHFLIP)) {
					// System.out.println("HFLIP " + eElement.getTextContent());
					tileInfo.setHflip(Boolean.parseBoolean(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(LAYER)) {
					// System.out.println("LAYER " + eElement.getTagName());
					tileInfo.setSetToLayer(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(INDEX)) {
					// System.out.println("INDEX " + eElement.getTagName());
					tileInfo.setIndex(Integer.parseInt(eElement.getTextContent()));
				}
			}
		}
		return tileInfo;
	}

	public CollisionInfo readCollision(NodeList collision) {
		CollisionInfo collisionInfo = new CollisionInfo();
		for (int idx = 0; idx < collision.getLength(); idx++) {
			Node tileSetAttributeNode = collision.item(idx);
			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;

				if (eElement.getTagName().matches(STARTX)) {
					// System.out.println("TILENAME " + eElement.getTextContent());
					collisionInfo.setX(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(STARTY)) {
					// System.out.println("LAYER " + eElement.getTagName());
					collisionInfo.setY(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(ISHFLIP)) {
					// System.out.println("HFLIP " + eElement.getTextContent());
					collisionInfo.setHflip(Boolean.parseBoolean(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(ISVFLIP)) {
					// System.out.println("VFLIP " + eElement.getTextContent());
					collisionInfo.setVflip(Boolean.parseBoolean(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(COLLISIONWIDTH)) {
					// System.out.println("INDEX " + eElement.getTagName());
					collisionInfo.setWidth(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(COLLISIONLENGTH)) {
					collisionInfo.setLength(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(COLLISIONHEIGHT)) {
					collisionInfo.setHeight(Integer.parseInt(eElement.getTextContent()));
				}
			}
		}
		return collisionInfo;
	}

	public void readMapEntry(NodeList mapEntry, Vector<TileInfo> layer) {
		for (int idx = 0; idx < mapEntry.getLength(); idx++) {
			Node tilesCollection = mapEntry.item(idx);
			if (tilesCollection.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tilesCollection;
				if (eElement.getTagName().matches(TILE)) {
					layer.add(readTile(eElement.getChildNodes()));
				}
			}
		}
	}
	
	public void readActorsEntry(NodeList actorsEntry, Vector<ActorInfo> actors) {
		for (int idx = 0; idx < actorsEntry.getLength(); idx++) {
			Node actorsCollection = actorsEntry.item(idx);
			if (actorsCollection.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) actorsCollection;
				if (eElement.getTagName().matches(ACTOR)) {
					actors.add(readActor(eElement.getChildNodes()));
				}
			}
		}
	}
	
	public ActorInfo readActor(NodeList actor) {
		ActorInfo actorInfo = new ActorInfo();
		
		for (int idx = 0; idx < actor.getLength(); idx++) {
			Node tileSetAttributeNode = actor.item(idx);
			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;

				if (eElement.getTagName().matches(ACTOR_XPOS)) {
					actorInfo.setX(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(ACTOR_YPOS)) {
					actorInfo.setY(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(ACTOR_ZPOS)) {
					actorInfo.setZ(Integer.parseInt(eElement.getTextContent()));
				} else  if (eElement.getTagName().matches(ACTOR_TYPE)) {
					actorInfo.setType(eElement.getTextContent());
				}
			}
		}
		
		return actorInfo;
	}
	
	public void readEventsEntry(NodeList eventsEntry, Vector<EventInfo> events) {
		for (int idx = 0; idx < eventsEntry.getLength(); idx++) {
			Node eventsCollection = eventsEntry.item(idx);
			if (eventsCollection.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) eventsCollection;
				if (eElement.getTagName().matches(EVENT)) {
					events.add(readEvent(eElement.getChildNodes()));
				}
			}
		}
	}
	
	public EventInfo readEvent(NodeList event) {
		//Replace with factory
		EventInfo eventInfo = new EventTransferMapInfo();
		
		for (int idx = 0; idx < event.getLength(); idx++) {
			Node tileSetAttributeNode = event.item(idx);
			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;

				if (eElement.getTagName().matches(EVENT_XPOS)) {
					eventInfo.setX(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(EVENT_YPOS)) {
					eventInfo.setY(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(EVENT_WIDTH)) {
					eventInfo.setWidth(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(EVENT_LENGTH)) {
					eventInfo.setLength(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(EVENT_ZOFFSET)) {
					eventInfo.setzOffset(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(EVENT_TYPE)) {
					eventInfo.setType(eElement.getTextContent());
				} else if (eElement.getTagName().matches(EVENTTRANSFER_MAP)) {
					((EventTransferMapInfo)eventInfo).setTransferToMap(eElement.getTextContent());
				} else if (eElement.getTagName().matches(EVENTTRANSFER_X)) {
					((EventTransferMapInfo)eventInfo).setTransferToX(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(EVENTTRANSFER_Y)) {
					((EventTransferMapInfo)eventInfo).setTransferToY(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(EVENTTRANSFER_Z)) {
					((EventTransferMapInfo)eventInfo).setTransferToZ(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(EVENTTRANSFER_DIRECTION)) {
					((EventTransferMapInfo)eventInfo).setFaceDirectionOnTransfer(eElement.getTextContent());
				}
			}
		}
		
		return eventInfo;
	}
	
	public void readSpriteMaskEntry(NodeList spriteMasksEntry, Vector<SpriteMaskInfo> spriteMasks, Vector<MaskInfo> masks) {
		for (int idx = 0; idx < spriteMasksEntry.getLength(); idx++) {
			Node spriteMaskCollection = spriteMasksEntry.item(idx);
			if (spriteMaskCollection.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) spriteMaskCollection;
				if (eElement.getTagName().matches(SPRITEMASK)) {
					spriteMasks.add(readSpriteMask(eElement.getChildNodes(), masks));
				}
			}
		}
	}

	private SpriteMaskInfo readSpriteMask(NodeList spriteMask, Vector<MaskInfo> masks) {
		
		SpriteMaskInfo spriteMaskInfo = new SpriteMaskInfo();
		
		for (int idx = 0; idx < spriteMask.getLength(); idx++) {
			Node tileSetAttributeNode = spriteMask.item(idx);
			if (tileSetAttributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) tileSetAttributeNode;

				if (eElement.getTagName().matches(SPRITEMASK_X)) {
					spriteMaskInfo.setX(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(SPRITEMASK_Y)) {
					spriteMaskInfo.setY(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(SPRITEMASK_Z)) {
					spriteMaskInfo.setZ(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(SPRITEMASK_ID)) {
					spriteMaskInfo.setId(Integer.parseInt(eElement.getTextContent()));
				} else if (eElement.getTagName().matches(SPRITEMASK_NAME)) {
					spriteMaskInfo.setName(eElement.getTextContent());
					for (MaskInfo mask: masks) {
						if (spriteMaskInfo.getName().contentEquals(mask.getName())) {
							spriteMaskInfo.setMask(mask.getMask());
							spriteMaskInfo.setType(mask.getType());
						}
					}
				}
			}
		}
		
		return spriteMaskInfo;
	}
};
