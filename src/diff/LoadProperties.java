package diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadProperties {
	static Map<String,List<Comment>> langs = new HashMap<String,List<Comment>>();
	static List<LangAndCmt> lcs = new ArrayList<LangAndCmt>();
	static List<Comment> list = null;
	static String lang = null;
	static String startCmt = null;
	static String endCmt = null;
	static File file = null;
	static BufferedReader br = null;
	static FileReader fr = null;

	public static void load(){
		langs.clear();
		lcs.clear();
		file = new File("./language.txt");
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br =  new BufferedReader(fr);
		String s = null;
		try {
			while((s = br.readLine()) != null){
				loadSuffix(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
	}
	
	public static void loadSuffix(String line){
		if(line.matches("language=.*")){
			lang = line.substring(line.indexOf("=") + 1);
			List<Comment> lst = langs.get(lang);
			if(lst != null && lst.size() != 0){
				list = lst;
			}else{
				list = new ArrayList<Comment>();
			}
		}else if(line.matches("startCmt=.*")){
			startCmt = line.substring(line.indexOf("=") + 1);
			if(startCmt != null){
				startCmt = startCmt.replace("*", "\\*");				
			}
		}else if(line.matches("endCmt=.*")){
			endCmt = line.substring(line.indexOf("=") + 1);
			if(endCmt == null || endCmt.equals("")){
				if(startCmt != null && !startCmt.equals("")){
					list.add(new Comment(startCmt));					
				}
			}else{
				endCmt = endCmt.replace("*", "\\*");
				if(startCmt != null && !startCmt.equals("")){
					list.add(new Comment(startCmt,endCmt));
				}
			}
			langs.put(lang, list);
			lcs.add(new LangAndCmt(lang,startCmt,endCmt));
		}
	}
	
	public static void close() {
		try{
			if(fr != null){
				fr.close();
			}
			if(br != null){
				br.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
