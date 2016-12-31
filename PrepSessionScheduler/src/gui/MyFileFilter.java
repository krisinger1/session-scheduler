package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter{

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) return true; //allow directories to be seen in fileChooser
		String name = file.getName();
		String extension = Utils.getFileExtension(name);
		if (extension == null) return false;
		else if (extension.equals("stu")) return true;
		else return false;
	}

	@Override
	public String getDescription() {
		return "Comma separated values file (*.stu)";
	}

}
