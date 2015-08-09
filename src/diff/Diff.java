package diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Diff {
	public static final String BEGIN1 = "        ";
	public static final String BEGIN2 = "add     ";
	public static final String BEGIN3 = "del     ";
	public static final String BEGIN4 = "edtBf   ";
	public static final String BEGIN5 = "edtAf   ";
	private int editLines = 0;
	private int addLines = 0;
	private int deleteLines = 0;
	ArrayList<String> newLines = new ArrayList<String>();
	ArrayList<String> oldLines = new ArrayList<String>();
	ArrayList<ArrayList<Integer>> rectangle = new ArrayList<ArrayList<Integer>>();
	ArrayList<String> chgOldLines = new ArrayList<String>();
	ArrayList<String> chgNewLines = new ArrayList<String>();
	String newPath = null;
	String oldPath = null;
	boolean showDiff = false;
	public Diff(String oldPath,String newPath){
		this.oldPath = oldPath;
		this.newPath = newPath;
	}
	
	public Object executeDiff(){
		newLines = readFile(newPath);
		oldLines = readFile(oldPath);
		initRectangle();
		setRectangle();
		analyseRectangle();
		if(showDiff){
			StringBuffer sb = new StringBuffer();
			sb.append("oldPath:" + oldPath + "\n");
			sb.append("newPath:" + newPath + "\n");
			
			for(int i = chgOldLines.size() - 1;i >= 0;i --){
				String oldLine = chgOldLines.get(i);
				String newLine = chgNewLines.get(i);
				if(oldLine.equals(newLine)){
					sb.append(BEGIN1 + oldLine + "\n");
				}else if(oldLine.equals("_")){
					sb.append(BEGIN2 + newLine + "\n");
				}else if(newLine.equals("_")){
					sb.append(BEGIN3 + oldLine + "\n");
				}else{
					sb.append(BEGIN4 + oldLine + "\n");
					sb.append(BEGIN5 + newLine + "\n");
				}
			}
			Object obj = sb.toString();
			return obj;
		}else{
			for(int i = chgOldLines.size() - 1;i >= 0;i --){
				String oldLine = chgOldLines.get(i);
				String newLine = chgNewLines.get(i);
				if(oldLine.equals(newLine)){
					
				}else if(oldLine.equals("_")){
					addLines ++;
				}else if(newLine.equals("_")){
					deleteLines ++;
				}else{
					editLines ++;
				}
			}
			int[] obj = {editLines,addLines,deleteLines,editLines + addLines + deleteLines};
			return obj;
		}
	}
	
	public ArrayList<String> readFile(String path){
		ArrayList<String> al = new ArrayList<String>();
		File file = new File(path);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		try {
			while((line = br.readLine()) != null){
				al.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(fr != null){
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(br != null){
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return al;
	}
	
	public void initRectangle(){
		int oldSize = oldLines.size() + 1;
		int newSize = newLines.size() + 1;
		for(int i = 0;i < oldSize;i ++){
			ArrayList<Integer> horizontal = new ArrayList<Integer>();
			if(i == 0){
				for(int j = 0;j < newSize;j ++){
					horizontal.add(0);
				}
			}else{
				horizontal.add(0);
			}
			rectangle.add(horizontal);
		}
	}
	
	public void setRectangle(){
		int oldSize = oldLines.size() + 1;
		int newSize = newLines.size() + 1;
		for(int i = 1;i < oldSize;i ++){
			String oldLine = oldLines.get(i - 1);
			for(int j = 1;j < newSize;j ++){
				String newLine = newLines.get(j - 1);
				if(oldLine.equals(newLine)){
					int e = rectangle.get(i - 1).get(j - 1) + 1;
					rectangle.get(i).add(e);
				}else{
					int e = Math.max(Math.max(rectangle.get(i - 1).get(j - 1),
							rectangle.get(i - 1).get(j)),
							rectangle.get(i).get(j - 1));
					rectangle.get(i).add(e);
				}
			}
		}
	}
	
	public void analyseRectangle(){
		int oldSize = oldLines.size() + 1;
		int newSize = newLines.size() + 1;
		int i = oldSize - 1;
		int j = newSize - 1;
		if(i == 0 && j != 0){
			while(true){
				String oldLine = null;
				String newLine = null;
				oldLine = "_";
				newLine = newLines.get(j - 1);
				chgOldLines.add(oldLine);
				chgNewLines.add(newLine);
				j --;
				if(j == 0){
					break;
				}
			}
		}else if(i != 0 && j == 0){
			while(true){
				String oldLine = null;
				String newLine = null;
				oldLine = oldLines.get(i - 1);
				newLine = "_";
				chgOldLines.add(oldLine);
				chgNewLines.add(newLine);
				i --;
				if(i == 0){
					break;
				}
			}
		}else if(i == 0 && j == 0){
			String oldLine = null;
			String newLine = null;
			oldLine = "";
			newLine = "";
			chgOldLines.add(oldLine);
			chgNewLines.add(newLine);
		}else{
			while(true){
				String oldLine = null;
				String newLine = null;
				newLine = newLines.get(j - 1);	
				oldLine = oldLines.get(i - 1);

				if(oldLine.equals(newLine)){
					if(i == 1 && j != 1){
						j -= 1;
						chgOldLines.add("_");
						chgNewLines.add(newLine);
					}else if(i != 1 && j == 1){
						i -= 1;
						chgOldLines.add(oldLine);
						chgNewLines.add("_");
					}else{
						i -= 1;	
						j -= 1;
						chgOldLines.add(oldLine);
						chgNewLines.add(newLine);
					}
					
				}else{
					if(i == 1 && j != 1){
						j -= 1;
						chgOldLines.add("_");
						chgNewLines.add(newLine);
					}else if(i != 1 && j == 1){
						i -= 1;
						chgOldLines.add(oldLine);
						chgNewLines.add("_");
					}else{
						int x1 = i - 1;
						int y1 = j - 1;
						int e1 = rectangle.get(x1).get(y1);
						int x2 = i - 1;
						int y2 = j;
						int e2 = rectangle.get(x2).get(y2);
						int x3 = i;
						int y3 = j - 1;
						int e3 = rectangle.get(x3).get(y3);
						int e4 = Math.max(Math.max(e1, e2),e3);
						if(e4 == e1){
							i = x1;
							j = y1;
							chgOldLines.add(oldLine);
							chgNewLines.add(newLine);
						}else if(e4 == e2){
							i = x2;
							j = y2;
							chgOldLines.add(oldLine);
							chgNewLines.add("_");
						}else if(e4 == e3){
							i = x3;
							j = y3;
							chgOldLines.add("_");
							chgNewLines.add(newLine);
						}
					}
				}
				
				if(i == 0 && j == 0){
					break;
				}
			}	
		}
	}
	public void clear(){		
		newLines.clear();
		oldLines.clear();
		rectangle.clear();
		chgOldLines.clear();
		chgNewLines.clear();
	}
}











