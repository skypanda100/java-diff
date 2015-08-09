package diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class LangTableModel extends DefaultTableCellRenderer implements TableModel{

	private static final long serialVersionUID = 1L;
	private List<LangAndCmt> lcs;
	private List<String> heads;
	private List<Integer> widthPers;

	public LangTableModel(){
		initHeads();
		lcs = LoadProperties.lcs;
	}
	
	public void initHeads(){
		heads = new ArrayList<String>();
		heads.add("#");
		heads.add("suffix");
		heads.add("startComment");
		heads.add("endComment");
		widthPers = new ArrayList<Integer>();
		widthPers.add(new Integer(50));
		widthPers.add(new Integer(100));
		widthPers.add(new Integer(100));
		widthPers.add(new Integer(100));
	}

	 public int getColumnWidthPer(int columnIndex) {
        if(columnIndex < 0||columnIndex >= widthPers.size()) {
            return 0;
        }
        return widthPers.get(columnIndex).intValue();
	}

	@Override
	public int getRowCount() {
		int rowSize = 0;
		rowSize = lcs.size();
		return rowSize + 1;			// to add a line
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return heads.get(columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> cls = null;
		switch(columnIndex){
			case 1:
			case 2:
			case 3:
				cls = String.class;
				break;
			case 0:
				cls = Integer.class;
				break;
		}
		return cls;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex > 0){
			return true;	
		}
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj = null;
		String lang = null;
		String stCmt = null;
		String edCmt = null;
		if(rowIndex + 1 <= lcs.size()){
			lang = lcs.get(rowIndex).getLang();
			stCmt = lcs.get(rowIndex).getStCmt();
			edCmt = lcs.get(rowIndex).getEdCmt();
			if(stCmt != null){
				stCmt = stCmt.replace("\\*", "*");
			}
			if(edCmt != null){
				edCmt = edCmt.replace("\\*", "*");
			}
		}

		switch(columnIndex){
			case 0:
				obj = rowIndex + 1;
				break;
			case 1:
				obj = lang;
				break;
			case 2:
				obj = stCmt;
				break;
			case 3:	
				obj = edCmt;
				break;
		}
		return obj;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(rowIndex + 1 > lcs.size()){
			LangAndCmt lcLst = new LangAndCmt(null,null,null);
			switch (columnIndex){
				case 0:
					break;
				case 1:
					String lang = (String)aValue;
					if(!lang.equals("")){
						lcLst.setLang(lang);	
						lcs.add(lcLst);
						resetLang();
					}
					break;
				case 2:
					String stCmt = (String)aValue;
					if(!stCmt.equals("")){
						stCmt = stCmt.replace("*", "\\*");
						lcLst.setStCmt(stCmt);	
						lcs.add(lcLst);
						resetLang();
					}
					break;
				case 3:
					String edCmt = (String)aValue;
					if(!edCmt.equals("")){
						edCmt = edCmt.replace("*", "\\*");
						lcLst.setEdCmt(edCmt);	
						lcs.add(lcLst);
						resetLang();
					}
					break;
			}
		}else{
			LangAndCmt lc = lcs.get(rowIndex);
			switch (columnIndex){
				case 0:
					break;
				case 1:
					lc.setLang((String)aValue);
					resetLang();
					break;
				case 2:
					String stCmt = (String)aValue;
					stCmt = stCmt.replace("*", "\\*");
					lc.setStCmt(stCmt);
					resetLang();
					break;
				case 3:
					String edCmt = (String)aValue;
					edCmt = edCmt.replace("*", "\\*");
					lc.setEdCmt(edCmt);
					resetLang();
					break;
			}
		}
		
		LoadProperties.load();
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

	public void resetLang(){
		File file = new File("./language.txt");
		FileWriter fw = null;
		int lcSize = lcs.size();
		List<Integer> ls = new ArrayList<Integer>();
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0;i < lcSize;i ++){
			LangAndCmt lc = lcs.get(i);
			String lang = lc.getLang();
			String stCmt = lc.getStCmt();
			if(stCmt == null){
				stCmt = "";
			}else{
				stCmt = stCmt.replace("\\*", "*");
			}
			String edCmt = lc.getEdCmt();
			if(edCmt == null){
				edCmt = "";
			}else{
				edCmt = edCmt.replace("\\*", "*");
			}
			if(!(stCmt.equals("") && edCmt.equals("") && lang.equals(""))){
				try {
					fw.write("language="+lang+"\n");
					fw.write("startCmt="+stCmt+"\n");
					fw.write("endCmt="+edCmt+"\n");
					fw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}else{
				ls.add(i);	// the deleted element's index
			}
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int lsSize = ls.size();
		for(int i = 0;i < lsSize;i ++){
			int idx = ls.get(i);
			lcs.remove(idx);
		}
	}
}
