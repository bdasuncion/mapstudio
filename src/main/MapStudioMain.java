package main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import debugTools.DebugDialog;

import mapMaker.MapDesktop;
import mapTiles.TileSizeConverter;
import fileRW.ExportToC;
import fileRW.MapFileReader;
import fileRW.MapFileReaderWriter;
import fileRW.TileReader;
import infoObjects.ActorInfo;
import infoObjects.CollisionInfo;
import infoObjects.EventInfo;
import infoObjects.MapInfo;
import infoObjects.TileInfo;
import infoObjects.TileSetInfo;


public class MapStudioMain
{
	JFrame MainFrame;
	
	private JTabbedPane FilePane; 
	private JMenuBar mainMenu;
	private JMenu file;
	private JMenuItem newMap;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem export;
	private JMenuItem importTile;
	private JMenuItem importTextGlyph;
	private CreateNewMap mapDialog;

	
	public MapStudioMain()
	{
		MainFrame = new JFrame("Brave Knight Map Studio");
		MainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		FilePane = new JTabbedPane();
		MainFrame.getContentPane().add(FilePane);
		
		mapDialog = new CreateNewMap(MainFrame);
		mapDialog.setVisible(false);
		
		this.createMenu();
		MainFrame.setVisible(true);
		MainFrame.setSize(800,800);
		MainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void createMenu()
	{
		mainMenu = new JMenuBar();
	
		file = new JMenu("File");

		newMap = new JMenuItem("New Map");
		newMap.addActionListener(new newFile());

		open = new JMenuItem("Open");
		open.addActionListener(new openFile());
		
		importTile = new JMenuItem("Import Tile");
		importTile.addActionListener(new importTile());
		importTile.setEnabled(false);
		
		save = new JMenuItem("Save File");
		save.addActionListener(new SaveFile());
		save.setEnabled(false);
		
		saveAs = new JMenuItem("Save As");
		saveAs.addActionListener(new SaveFile());
		saveAs.setEnabled(false);
		
		export = new JMenuItem("Export to GBA");
		export.addActionListener(new exportCFile());
		export.setEnabled(false);
		
		importTextGlyph = new JMenuItem("Import text glyph");
		importTextGlyph.addActionListener(new importTextGlyph());

		file.add(newMap);
		file.add(open);
		file.add(importTile);
		file.add(save);
		file.add(saveAs);
		file.add(export);
		file.add(importTextGlyph);
		
		mainMenu.add(file);
		
		MainFrame.setJMenuBar(mainMenu);
	}
	/*TODO
	 * Fix this part
	 * An error occurs when the tile set dimensions are off
	 */
	private class importTile implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			JFileChooser importTile = new JFileChooser();
			importTile.setAcceptAllFileFilterUsed(false);
			importTile.setFileFilter(new TileFileFilter());
			importTile.showDialog(MainFrame, "Import");
			
			if(importTile.getSelectedFile() == null)
				return;
			TileReader read = new TileReader();
			read.read(importTile.getSelectedFile());
			MapDesktop mapEditor = (MapDesktop)FilePane.getSelectedComponent();

			//The 8 is the standard tile width and height from Braveknight Sprite Studio
			TileSizeConverter convert =
				new TileSizeConverter(8, 8, mapEditor.getTileWidth(), 
						mapEditor.getTileHeight(),
						read.getWidthInTiles(), 
						read.getHeightInTiles(), 
						read.getTile());
			
			convert.convert();

			mapEditor.importTiles(convert.getnewTiles());
			mapEditor.importTiles(convert.getWidthInTiles(),convert.getHeightInTiles(), 
					 convert.getnewTiles(), getTileFileName(importTile.getSelectedFile()));	
		}
	}
	
	private String getTileFileName(File f)
	{
		String filename = f.getName();
		return filename.substring(0, filename.length() - 5);
	}
	
	private class newFile implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			mapDialog.setVisible(true);
			if(mapDialog.getOKStaus() == false)
				return;
			
			MapDesktop mapEditor = new MapDesktop(MainFrame, mapDialog.getMapWidth(), mapDialog.getMapHeight(),
					mapDialog.getTileWidth(), mapDialog.getTileHeight());
			FilePane.addTab("New", mapEditor);
			
			if(FilePane.getComponentCount()>0) {
				importTile.setEnabled(true);
				save.setEnabled(true);
				saveAs.setEnabled(true);
				export.setEnabled(true);
			}
		}
	}
	
	class SaveFile implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MapDesktop desktop = (MapDesktop)FilePane.getSelectedComponent();
			MapInfo mapInfo = desktop.getMapInfo();
			File saveFile = null;
			if (e.getSource() == save) {
				saveFile = mapInfo.getSaveFile();
			}
			
			if (saveFile == null) {
				JFileChooser saveFileDialog;
				saveFileDialog = new JFileChooser();
				saveFileDialog.setAcceptAllFileFilterUsed(false);
				saveFileDialog.addChoosableFileFilter(new MapStudioFileFilter());
				saveFileDialog.showSaveDialog(MainFrame);
				saveFile = saveFileDialog.getSelectedFile();
				if(saveFile == null) {
					return;
				}
			}
			
			mapInfo.setSaveFile(saveFile);
			FilePane.setTitleAt(FilePane.getSelectedIndex(), saveFile.getName());
			MapFileReaderWriter fileOut  = new MapFileReaderWriter();
			fileOut.write(desktop.getMapInfo(), saveFile);				
		}
	}
	
	class openFile implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			File openFile = null;
			JFileChooser openFileDialog;
			openFileDialog = new JFileChooser();
			openFileDialog.setAcceptAllFileFilterUsed(false);
			openFileDialog.addChoosableFileFilter(new MapStudioFileFilter());
			openFileDialog.showOpenDialog(MainFrame);
			
			openFile = openFileDialog.getSelectedFile();
			if(openFile == null) {
				return;
			}
			
			MapFileReaderWriter fileRead  = new MapFileReaderWriter();
			Vector<TileSetInfo> tileSetInfos = new Vector<TileSetInfo>();
			Vector<CollisionInfo> collisionMap = new Vector<CollisionInfo>();
			Vector<Vector<TileInfo>> layers = new Vector<Vector<TileInfo>>();
			Vector<ActorInfo> actors = new Vector<ActorInfo>();
			Vector<EventInfo> events = new Vector<EventInfo>();
			Dimension dimension = new Dimension();
			fileRead.read(openFile, dimension,
					tileSetInfos, collisionMap, layers, actors, events);
			
			int tileWidth = 8, tileHeight = 8;
			MapDesktop mapEditor = new MapDesktop(MainFrame, dimension.width*tileWidth, dimension.height*tileHeight,
					tileWidth, tileHeight);
			
			
			setMapEditorData(mapEditor, tileSetInfos, collisionMap, layers, actors, events, dimension);
			mapEditor.getMapInfo().setSaveFile(openFile);
			
			FilePane.addTab(openFile.getName(), mapEditor);
			
			if(FilePane.getComponentCount()>0) {
				importTile.setEnabled(true);
				save.setEnabled(true);
				saveAs.setEnabled(true);
				export.setEnabled(true);
			}
		}
	}
	
	private void setMapEditorData(MapDesktop mapEditor, Vector<TileSetInfo> tileSetInfos,
	Vector<CollisionInfo> collisionMap, Vector<Vector<TileInfo>> layers,
	Vector<ActorInfo> actors, Vector<EventInfo> events, Dimension dimension) {
		for (TileSetInfo tileSetInfo : tileSetInfos) {
			mapEditor.importTileSet(tileSetInfo);
		}
		
		mapEditor.importCollisionMap(collisionMap);
		
		for (Vector<TileInfo> layer: layers) {
			for (TileInfo tileInfoLayer : layer) {
				setTileFromTileSet(tileInfoLayer, tileSetInfos);
			}
		}
		
		for (Vector<TileInfo> layer: layers) {
			for (int i = 0; i < dimension.height; ++i) {
				for (int j = 0; j < dimension.width; ++j) {
					TileInfo setTile = layer.get(j + i*dimension.width);
					if (setTile.getSetToLayer() >= 0) {
						mapEditor.getMapInfo().getMapLayers().get(setTile.getSetToLayer()).setTileAt(j, i, setTile);
					}
				}
			}
		}
		
		mapEditor.getMapInfo().getActors().addAll(actors);
		mapEditor.getMapInfo().getEvents().addAll(events);
	}
	
	public void setTileFromTileSet(TileInfo tileInfo, Vector<TileSetInfo> tileSets) {
		for (TileSetInfo tileSetInfo : tileSets) {
			for (TileInfo tileFromTileSet : tileSetInfo.getTileSet()) {
				if (tileInfo.getName().matches(tileFromTileSet.getName())) {
					//System.out.println(tileInfoLayer.getName());
					tileInfo.setTile(tileFromTileSet.getTileImage());
					tileInfo.setIndex(tileFromTileSet.getIndex());
					tileInfo.setPalletteIndex(tileSetInfo.getPaletteIdx());
				}
			}
		}
	}
	
	class exportCFile implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			JFileChooser exporter = new JFileChooser();
			exporter.setAcceptAllFileFilterUsed(false);
			exporter.setFileFilter(new CFileFilter());
			exporter.showDialog(MainFrame, "Export");
			
			if(exporter.getSelectedFile() == null)
				return;
			
			//ExportToCDialog ExDialog = new  ExportToCDialog(MainFrame);
			
			MapDesktop desktop = (MapDesktop)FilePane.getSelectedComponent();
			ExportToC.exportToCGBA(exporter.getSelectedFile(), desktop.getMapInfo());
		}
	}
	
	private class importTextGlyph implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0)
		{
			JFileChooser importTile = new JFileChooser();
			importTile.setAcceptAllFileFilterUsed(false);
			importTile.setFileFilter(new TileFileFilter());
			importTile.setMultiSelectionEnabled(true);
			importTile.showDialog(MainFrame, "Import Text Glyph");
			
			if(importTile.getSelectedFiles().length == 0)
				return;
			File[] files = importTile.getSelectedFiles();
			for(int i = 0; i<importTile.getSelectedFiles().length; i++)
			{
			    TileReader read = new TileReader();
			    read.read(files[i]);
			    MapDesktop mapEditor = (MapDesktop)FilePane.getSelectedComponent();
			    mapEditor.importTextGlyph(read.getTile());
			}
			
		}
	}
	
	public static void showGUI()
	{
		MapStudioMain display = new MapStudioMain();	
	}
	
	public static void main(String[] Args)
	{
		javax.swing.SwingUtilities.invokeLater
		(
			new Runnable()
			{
				public void run()
				{
					showGUI();	
				}
			}
		);
	}
	
	private class TileFileFilter extends FileFilter
	{

		public boolean accept(File f) {
			if (f.isDirectory()) 
		     {
				return true;
		     }
		     
		     String fileName = f.getName();
		     int i = fileName.lastIndexOf('.');
		     
		    String ext = null; 
	        if (i > 0 &&  i < fileName.length() - 1)
	        {
	            ext = fileName.substring(i+1).toLowerCase();
	        }
	        else 
	        	return false;
	        if(ext.matches("tile") == true)
	        	return true;

	        return false;
		}
		
		public String getDescription() 
		{
			return "Tile(*.tile)";
		}
	}
	
	private class MapStudioFileFilter extends FileFilter
	{

		public boolean accept(File f) 
		{
			if (f.isDirectory()) 
		     {
				return true;
		     }
		     
		     String fileName = f.getName();
		     int i = fileName.lastIndexOf('.');
		     
		    String ext = null; 
	        if (i > 0 &&  i < fileName.length() - 1)
	        {
	            ext = fileName.substring(i+1).toLowerCase();
	        }
	        else 
	        	return false;
	        if(ext.matches("map") == true)
	        	return true;

	        return false;
		}
		
		public String getDescription() 
		{
			return "map(*.map)";
		}
	}
	
	private class CFileFilter extends FileFilter
	{

		public boolean accept(File f) {
			if (f.isDirectory()) 
		     {
				return true;
		     }
		     
		     String fileName = f.getName();
		     int i = fileName.lastIndexOf('.');
		     
		    String ext = null; 
	        if (i > 0 &&  i < fileName.length() - 1)
	        {
	            ext = fileName.substring(i+1).toLowerCase();
	        }
	        else 
	        	return false;
	        if(ext.matches("c") == true || ext.matches("h") == true)
	        	return true;

	        return false;
		}

		public String getDescription() 
		{
			return "C File(*.c,*.h)";
		}
	}
}