package dpham.boardstrike.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dpham.boardstrike.piece.PieceType;
import dpham.boardstrike.strategy.Strategy;

public class Assets {

	private static final AssetManager manager = new AssetManager();
	private static final LinkedHashMap<String, TextureRegion> textures = new LinkedHashMap<String, TextureRegion>();
	private static final LinkedHashMap<String, AssetDescriptor<Texture>> assetDescs = new LinkedHashMap<String, AssetDescriptor<Texture>>();
	private static final AssetDescriptor<BitmapFont> font = new AssetDescriptor<BitmapFont>("font/silkscreen.fnt", BitmapFont.class);
	private static boolean loadedFont;
	
	public static BitmapFont getFont() {
		if (!loadedFont) {
			manager.load(font);
			manager.finishLoading();
			loadedFont = true;
		}
		return manager.get(font);
	}
	
	public static TextureRegion getPieceTexture(PieceType type, int team) {
		String typeName = type.toString();
		String textureName = typeName + ((team == 0) ? "_WHITE" : "_BLACK");
		
		if (textures.get(textureName) == null) {
			addPieceTexture(type);
		}
		
		return textures.get(textureName);
	}
	
	public static TextureRegion getTileTexture() {
		if (textures.get("TILE") == null) {
			Pixmap tilePix = new Pixmap(24, 24, Format.RGBA8888 );
			tilePix.setColor(0.8f, 0.8f, 0.8f , 1);
			tilePix.fill();
			tilePix.setColor(0.2f, 0.2f, 0.2f, 1);
			tilePix.drawRectangle(0, 0, 24, 24);
			textures.put("TILE", new TextureRegion(new Texture(tilePix)));
			tilePix.dispose();
		}
		return textures.get("TILE");
	}
	
	public static TextureRegion getRedTileTransparency() {
		if (textures.get("RED_TILE") == null) {
			Pixmap tilePix = new Pixmap(22, 22, Format.RGBA8888 );
			tilePix.setColor(0.8f, 0.0f, 0.0f , 0.25f);
			tilePix.fill();
			textures.put("RED_TILE", new TextureRegion(new Texture(tilePix)));
			tilePix.dispose();
		}
		return textures.get("RED_TILE");
	}
	
	public static TextureRegion getGreenTileTransparency() {
		if (textures.get("GREEN_TILE") == null) {
			Pixmap tilePix = new Pixmap(22, 22, Format.RGBA8888 );
			tilePix.setColor(0.0f, 0.8f, 0.0f , 0.25f);
			tilePix.fill();
			textures.put("GREEN_TILE", new TextureRegion(new Texture(tilePix)));
			tilePix.dispose();
		}
		return textures.get("GREEN_TILE");
	}
	
	public static TextureRegion getYellowTileTransparency() {
		if (textures.get("YELLOW_TILE") == null) {
			Pixmap tilePix = new Pixmap(22, 22, Format.RGBA8888 );
			tilePix.setColor(0.8f, 0.8f, 0.0f , 0.25f);
			tilePix.fill();
			textures.put("YELLOW_TILE", new TextureRegion(new Texture(tilePix)));
			tilePix.dispose();
		}
		return textures.get("YELLOW_TILE");
	}
	
	private static void addPieceTexture(PieceType type) {
		String typeName = type.toString();
		AssetDescriptor<Texture> assetDesc = new AssetDescriptor<Texture>("pieces/" + typeName.toLowerCase() + ".png", Texture.class);
		manager.load(assetDesc);
		manager.finishLoading();
		Texture image = manager.get(assetDesc);
		TextureRegion[] pieceTextures = TextureRegion.split(image, 16, 16)[0];
		textures.put(typeName + "_WHITE", pieceTextures[0]);
		textures.put(typeName + "_BLACK", pieceTextures[1]);
		assetDescs.put(typeName, assetDesc);
	}
	
	public static ArrayList<Class<?>> getExternalStrategyClasses() {
		ArrayList<Class<?>> strategyClasses = new ArrayList<Class<?>>();
		File externalFolder = new File("strategies/src/");
		File[] files = externalFolder.listFiles();
		for (File file : files) {
			String extension = getExtension(file);
			if (extension != null && extension.equals(".java")) {
				int lastDot = file.getName().lastIndexOf('.');
				String name = file.getName().substring(0, lastDot);
				strategyClasses.add(compileAndGetStrategyClass(name));
			}
		}
		return strategyClasses;
	}
	
	private static String getExtension(File file) {
		if (file.isFile()) {
			int lastDot = file.getName().lastIndexOf('.');
			if (lastDot != -1) {
				return file.getName().substring(lastDot);
			}
			return "";
		}
		return null;
	}
	
	private static Strategy compileAndCreateExternalStrategy(String strategyName) {
		try {
			Class<?> strategyClass = compileAndGetStrategyClass(strategyName);
			 
			if (strategyClass != null) {
				//Creating strategy object
				Strategy strategy = (Strategy) strategyClass.newInstance();
				return strategy;
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Class<?> compileAndGetStrategyClass(String strategyName) {
		try {
			//Check for package of class file
			File javaFile = new File("strategies/src/" + strategyName + ".java");
			String packageName = searchForPackage(javaFile);
			if (packageName == null) {
				addPackageLine(javaFile);
				packageName = "external.strategy";
			}
			//Compile file
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			compiler.run(null, null, null, javaFile.getPath());
			
			//Move class file to other files
			File classFile = new File("strategies/src/" + strategyName + ".class");
			String filePath = packageName.substring(0).replaceAll("[.]", "/");
			File newClassFile = new File("strategies/classFiles/" + filePath + "/" + strategyName + ".class");
			if (newClassFile.exists()) {
				newClassFile.delete();
			}
			newClassFile.getParentFile().mkdirs();
			classFile.renameTo(newClassFile);
			
			//Loading in class
			URL classURL = (new File("strategies/classFiles/")).toURI().toURL();
			URLClassLoader classLoader = new URLClassLoader(new URL[]{classURL});
			Class<?> strategyClass = classLoader.loadClass(packageName + "." + strategyName);
			
			return strategyClass;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String searchForPackage(File javaFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(javaFile));
		String packageName = "";
		String line;
		
		while ((line = reader.readLine()) != null) {
			if (line.trim().indexOf("package ") == 0) {
				packageName = line.trim().substring("package ".length(), line.trim().indexOf(';')).trim();
				reader.close();
				return packageName;
			}
			if (line.trim().indexOf("public class ") == 0) {
				reader.close();
				return null;
			}
		}
		reader.close();
		return null;
	}
	
	private static void addPackageLine(File javaFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(javaFile));
		String line;
		String text = "package external.strategy;\n";
		
		while ((line = reader.readLine()) != null) {
			text = text + line + "\n";
		}
		reader.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile));
		writer.write(text);
		writer.close();
	}

	public static void dipose() {
		manager.dispose();
	}
}
