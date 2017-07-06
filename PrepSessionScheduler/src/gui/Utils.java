package gui;

import java.io.File;

public class Utils {

	public static String getFileExtension(String name){
		int pointIndex = name.lastIndexOf(".");
		if (pointIndex==-1)	return null;
		else if (pointIndex==name.length()-1) return null;
		else return name.substring(pointIndex+1,name.length());
	}

	public static String removeExtension(String name){
		int pointIndex = name.lastIndexOf(".");
		if (pointIndex==-1) return name;
		return name.substring(0, pointIndex);
	}

	public static File changeExtension(File file, String ext){
		//if (!ext.equals(getFileExtension(file.getName()))){
			return new File(removeExtension(file.getAbsolutePath())+"."+ext);
		//}
		//return file;
	}

	public static boolean extensionOK(File file,String ext){
		return (ext.equals(getFileExtension(file.getAbsolutePath())));
	}

	public static int[][] copyOf(int[][] array){
		int r=array.length;
		int c=array[0].length;
		int[][] copy = new int[r][c];
		for (int i=0;i<r;i++){
			for (int j=0;j<c;j++){
				copy[i][j]=array[i][j];
			}
		}
		System.out.println(copy+" "+array);
		return copy;
	}
}
