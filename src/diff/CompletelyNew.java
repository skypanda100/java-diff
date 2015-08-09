package diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CompletelyNew {
	String path;
	List<Comment> comments;
	public CompletelyNew(String path){
		this.path = path;
	}
	public int countLine(){
		Map<String,List<Comment>> langs = LoadProperties.langs;
		File file = new File(path);
		String fname = file.getName();
		String suffix = fname.substring(fname.lastIndexOf(".") + 1);
		BufferedReader br = null;
		String s = null;
		int line = 0;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		comments = langs.get(suffix);
		try {
			while((s = br.readLine()) != null){
				if(s.matches("[\t ]*")){
					continue;
				}
				boolean isComment = false;
				Iterator<Comment> iterator = comments.iterator();
				while(iterator.hasNext()){
					Comment cmt = iterator.next();
					String startCmt = cmt.getStartCmt();
					String endCmt = cmt.getEndCmt();
					if(endCmt == null){
						if(s.matches("^([\t ]*" + startCmt + ").*")){
							isComment = true;
							break;
						}	 
					}else{
						if(s.matches("^([\t ]*" + startCmt + ").*(" + endCmt + "[\t ]*)$")){
							isComment = true;
							break;
						}else if(s.matches("^([\t ]*" + startCmt + ").*")){
							while((s = br.readLine()) != null){
								if(s.matches(".*(" + endCmt + "[\t ]*)$")){
									isComment = true;
									break;
								}
							}
						}
					}
				}
				if(!isComment){
					line++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line;
	}
}
