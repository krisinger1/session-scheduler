package gui;

public class Utils {

	public static String getFileExtension(String name){
		int pointIndex = name.lastIndexOf(".");
		if (pointIndex==-1)	return null;
		else if (pointIndex==name.length()-1) return null;
		else return name.substring(pointIndex+1,name.length());
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
