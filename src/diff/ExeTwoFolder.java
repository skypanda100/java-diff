package diff;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExeTwoFolder implements FilenameFilter {
	List<String> oldList = new ArrayList<String>();
	List<String> newList = new ArrayList<String>();
	List<DiffInfo> list = new ArrayList<DiffInfo>();
	String oldParentPath;
	String newParentPath;
	boolean isDir;
	boolean singleFile;	//when the oldPath's fileName and the newPath's fileName arent (wca0001.pc,wca0001.pc) or (wca0001_old.pc,wca0001.pc),this flg will be useful 
	int sumOld = 0;
	int sumEdit = 0;
	int sumAdd = 0;
	int sumDelete = 0;
	int sumNew = 0;
	ExeTwoFolder() {

	}

	ExeTwoFolder(boolean isDir) {
		this.isDir = isDir;
	}

	public void count(String oldPath, String newPath) {
		LoadProperties.load();

		doSubFolder(oldPath, newPath);
		
		sumOld = Difference.sumOld;
		sumEdit = Difference.sumEdit;
		sumAdd = Difference.sumAdd;
		sumDelete = Difference.sumDelete;
		sumNew = Difference.sumNew;
		
		Difference.sumOld = 0;
		Difference.sumEdit = 0;
		Difference.sumAdd = 0;
		Difference.sumDelete = 0;
		Difference.sumNew = 0;

		list.add(new DiffInfo("çáåv","","","",sumOld,sumEdit,sumAdd,sumDelete,sumNew));
		new ShowNumberInfoWindow(list).showInfo();
	}

	public void doSubFolder(String oldPath, String newPath) {
		execute(oldPath,newPath);	// these are two folder or two file
		List<String> oldDirNameList = new ArrayList<String>();
		List<String> oldDirPathList = new ArrayList<String>();
		List<String> newDirNameList = new ArrayList<String>();
		List<String> newDirPathList = new ArrayList<String>();
		File oldFile = new File(oldPath);
		getDir(oldFile, oldDirNameList, oldDirPathList);
		File newFile = new File(newPath);
		getDir(newFile, newDirNameList, newDirPathList);
		
		int oldDirSize = oldDirNameList.size();
		for (int i = 0; i < oldDirSize; i++) {
			String name = oldDirNameList.get(i);
			int idx = newDirNameList.indexOf(name);
			if (idx != -1) {
				doSubFolder(oldDirPathList.get(i), newDirPathList.get(idx));
				newDirNameList.remove(idx);
				newDirPathList.remove(idx);
			} else {
				doSubFolder(oldDirPathList.get(i), "");
			}
		}
		int newDirSize = newDirNameList.size();
		for (int i = 0; i < newDirSize; i++) {
			doSubFolder("", newDirPathList.get(i));
		}
	}

	public void getDir(File file, List<String> dirNameList,
			List<String> dirPathList) {
		if (file.isDirectory()) {
			File[] subdirs = file.listFiles(new ExeTwoFolder(true));
			for (int i = 0; i < subdirs.length; i++) {
				File f = subdirs[i];
				String name = f.getName();
				String path = f.getAbsolutePath();
				if (dirNameList.indexOf(name) == -1) {
					dirNameList.add(name);
					dirPathList.add(path);
				}
			}
		}
	}

	public void execute(String oldPath, String newPath) {
		File oldFile = new File(oldPath);
		getFile(oldFile, true);
		File newFile = new File(newPath);
		getFile(newFile, false);
		new Difference(oldList, newList, this, oldParentPath, newParentPath);
		oldList.clear();
		newList.clear();
		oldParentPath = null;
		newParentPath = null;
	}

	public void getFile(File file, boolean isOld) {
		if (file.isDirectory()) {
			File[] subdirs = file.listFiles(new ExeTwoFolder(false));
			if (oldParentPath == null && isOld) {
				oldParentPath = file.getAbsolutePath();
			} else if (newParentPath == null && !isOld) {
				newParentPath = file.getAbsolutePath();
			}
			for (int i = 0; i < subdirs.length; i++) {
				File f = subdirs[i];
				String fname = f.getName();
				if (isOld) {
					if (oldList.indexOf(fname) == -1) {
						oldList.add(fname);
					}
				} else {
					if (newList.indexOf(fname) == -1) {
						newList.add(fname);
					}
				}
			}
		} else if (file.isFile()) {
			if (oldParentPath == null && isOld) {
				oldParentPath = file.getParent();
			} else if (newParentPath == null && !isOld) {
				newParentPath = file.getParent();
			}
			String fname = file.getName();
			if (isOld) {
				if (oldList.indexOf(fname) == -1) {
					oldList.add(fname);
				}
			} else {
				if (newList.indexOf(fname) == -1) {
					newList.add(fname);
				}
			}
			singleFile = true;
		}
	}

	public boolean accept(File dir, String name) {
		String path = dir.getAbsolutePath()
				+ System.getProperty("file.separator") + name;
		File file = new File(path);
		if (file.isDirectory()) {
			if (isDir) {
				return true;
			} else {
				return false;
			}
		} else if (file.isFile()) {
			if (isDir) {
				return false;
			} else {
				String fname = file.getName();
				String suffix = fname.substring(fname.lastIndexOf(".") + 1);
				Set<String> set = LoadProperties.langs.keySet();
				if (set.contains(suffix)) {
					return true;
				}
			}

			return false;
		}
		return false;
	}
}
