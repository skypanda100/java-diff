package diff;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExeOneFolder implements FilenameFilter {

	Map<String,List<String>> urls = new HashMap<String,List<String>>();
	List<String> al;
	List<DiffInfo> list = new ArrayList<DiffInfo>();
	List<String> oldList = new ArrayList<String>();
	List<String> newList = new ArrayList<String>();
	String oldParentPath;
	String newParentPath;
	int sumOld = 0;
	int sumEdit = 0;
	int sumAdd = 0;
	int sumDelete = 0;
	int sumNew = 0;
	public void count(String path){
		LoadProperties.load();

		File file = new File(path);
		getDirectory(file);
		Collection<List<String>> lists = urls.values();
		Iterator<List<String>> iterator = lists.iterator();

		while(iterator.hasNext()){
			List<String> lst = (ArrayList<String>)iterator.next();
			setOldOrNew(lst);
			new Difference(oldList,newList,this,oldParentPath,newParentPath);

			oldList.clear();
			newList.clear();
			oldParentPath = null;
			newParentPath = null;
		}

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
	
	public void setOldOrNew(List<String> lst){
		Iterator<?> iterator = lst.iterator();
		while(iterator.hasNext()){
			String path = (String)iterator.next();
			File file = new File(path);
			if(oldParentPath == null){
				oldParentPath = file.getParent();
				newParentPath = file.getParent();
			}
			String fname = file.getName();
			String name  = fname.substring(0,fname.lastIndexOf("."));
			if(isOld(name)){
				oldList.add(fname);
			}else{
				newList.add(fname);
			}
		}
	}
	
	public boolean isOld(String name){
		if(name.matches(".*_[oO][lL][dD]")){
			return true;
		}
		return false;
	}
	
	public void getDirectory(File file) {
		if(file.isDirectory()){
			File[] subdirs = file.listFiles(new ExeOneFolder());
			int subDirSize = subdirs.length;
			for (int i = 0; i < subDirSize; i++) {
				File f = subdirs[i];
				if (f.isDirectory()) {
					getDirectory(f);
				} else if (f.isFile()) {
					String parentPath = f.getParent();
					String filePath = f.getAbsolutePath();
					if(!urls.containsKey(parentPath)){
						al = new ArrayList<String>();
						al.add(filePath);
						urls.put(parentPath, al);
					}else{
						al = (ArrayList<String>)urls.get(parentPath);
						al.add(filePath);
					}
				}
			}	
		}else if(file.isFile()){
			String parentPath = file.getParent();
			String filePath = file.getAbsolutePath();
			if(!urls.containsKey(parentPath)){
				al = new ArrayList<String>();
				al.add(filePath);
				urls.put(parentPath, al);
			}else{
				al = (ArrayList<String>)urls.get(parentPath);
				al.add(filePath);
			}
		}
	}

	public boolean accept(File dir, String name) {
		String path = dir.getAbsolutePath()
				+ System.getProperty("file.separator") + name;
		File file = new File(path);
		if (file.isDirectory()) {
			return true;
		} else if (file.isFile()) {
			String fname = file.getName();
			String suffix = fname.substring(fname.lastIndexOf(".") + 1);
			Set<String> set = LoadProperties.langs.keySet();
			if(set.contains(suffix)){
				return true;
			}
			return false;
		}
		return false;
	}
}
