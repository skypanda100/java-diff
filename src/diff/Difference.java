package diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Difference {
	List<String> oldList = new ArrayList<String>();
	List<String> newList = new ArrayList<String>();
	String oldParentPath;
	String newParentPath;
	ExeOneFolder tof;
	ExeTwoFolder ttf;
	boolean isOne;
	static int sumOld = 0;
	static int sumEdit = 0;
	static int sumAdd = 0;
	static int sumDelete = 0;
	static int sumNew = 0;
	
	public Difference(List<String> oldList,List<String> newList,ExeOneFolder tof,String oldParentPath,String newParentPath){
		this.oldList = oldList;
		this.newList = newList;
		this.tof = tof;
		this.oldParentPath = oldParentPath;
		this.newParentPath = newParentPath;
		this.isOne = true;
		doDiff();
	}
	
	public Difference(List<String> oldList,List<String> newList,ExeTwoFolder ttf,String oldParentPath,String newParentPath){
		this.oldList = oldList;
		this.newList = newList;
		this.ttf = ttf;
		this.oldParentPath = oldParentPath;
		this.newParentPath = newParentPath;
		this.isOne = false;
		doDiff();
	}
	
	public void doDiff(){
		DiffInfo di = null;
		Iterator<?> iterator = oldList.iterator();
		while(iterator.hasNext()){
			String fname = (String)iterator.next();
			String name = null;
			int pos1 = fname.lastIndexOf("_");
			int pos2 = fname.lastIndexOf(".");
			int idx = -1;
			if(isOne){
				name = fname.substring(0,pos1) + fname.substring(pos2);
				idx = newList.indexOf(name);
			}else{
				if(ttf.singleFile){
					idx = 0;
					name = newList.get(0);
				}else{
					idx = newList.indexOf(fname);
					if(idx != -1){
						name = fname;
					}else{
						if(fname.substring(0,pos2).matches(".*_[oO][lL][dD]")){
							name = fname.substring(0,pos1) + fname.substring(pos2);
							idx = newList.indexOf(name);
						}
					}
				}
			}
			
			String oldPath = oldParentPath + System.getProperty("file.separator") + fname;
			String newPath = newParentPath + System.getProperty("file.separator") + name;
			if(idx == -1){		// this old file has no new file to match with!
				CompletelyNew cn = new CompletelyNew(oldPath);
				int oldSum = cn.countLine();
				di = new DiffInfo("","",oldPath,fname,0,0,0,0,oldSum);
				if(isOne){
					tof.list.add(di);
				}else{
					ttf.list.add(di);
				}
				sumNew = sumNew + oldSum;	// to count the new sources'lines
			}else{		// this old file has a new file to match with!
				Diff diff = new Diff(oldPath,newPath);
				int[] newSum = (int[])diff.executeDiff();
				diff.clear();
				CompletelyNew cn = new CompletelyNew(oldPath);
				int oldSum = cn.countLine(); 
				newList.remove(idx);
				di = new DiffInfo(oldPath,fname,newPath,name,oldSum,newSum[0],newSum[1],newSum[2],newSum[3]);
				if(isOne){
					tof.list.add(di);
				}else{
					ttf.list.add(di);
				}
				sumOld = sumOld + oldSum;
				sumEdit = sumEdit + newSum[0];
				sumAdd = sumAdd + newSum[1];
				sumDelete = sumDelete + newSum[2];
				sumNew = sumNew + newSum[3];
			}
		}
		
		// this new file has no old file to match with!
		for(int i = 0;i < newList.size();i++){
			String newPath = newParentPath + System.getProperty("file.separator") + newList.get(i);
			CompletelyNew cn = new CompletelyNew(newPath);
			int newSum = cn.countLine();
			di = new DiffInfo("","",newPath,newList.get(i),0,0,0,0,newSum);
			if(isOne){
				tof.list.add(di);
			}else{
				ttf.list.add(di);
			}
			sumNew = sumNew + newSum;	// to count the new sources'lines
		}
	}
}
