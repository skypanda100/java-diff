package diff;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class DiffTableModel extends DefaultTableCellRenderer implements TableModel{
	private static final long serialVersionUID = 1L;
	private List<DiffInfo> list;
	private List<String> heads;
	private List<Integer> widthPers;
	private Color initColor;
	public DiffTableModel(List<DiffInfo> list){
		this.initColor = this.getBackground();
		this.list = list;
		initHeads();
	}
	
	public void initHeads(){
		heads = new ArrayList<String>();
		heads.add("#");
		heads.add("参照ソースパース");
		heads.add("参照ソース");
		heads.add("新規ソースパース");
		heads.add("新規ソース");
		heads.add("修正前STEP数");
		heads.add("編集STEP数");
		heads.add("増加STEP数");
		heads.add("削除STEP数");
		heads.add("新規・修正STEP数");
		widthPers = new ArrayList<Integer>();
		widthPers.add(new Integer(100));
		widthPers.add(new Integer(450));
		widthPers.add(new Integer(400));
		widthPers.add(new Integer(450));
		widthPers.add(new Integer(400));
		widthPers.add(new Integer(250));
		widthPers.add(new Integer(200));
		widthPers.add(new Integer(200));
		widthPers.add(new Integer(200));
		widthPers.add(new Integer(300));
	}

	 public int getColumnWidthPer(int columnIndex) {
        if(columnIndex < 0||columnIndex >= widthPers.size()) {
            return 0;
        }
        return widthPers.get(columnIndex).intValue();
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return 10;
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
			case 4:
				cls = String.class;
				break;
			case 0:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				cls = Integer.class;
				break;
		}
		
		return cls;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj = null;
		if(columnIndex == 0){
			if(rowIndex+1 < list.size() && list.get(rowIndex).getOldSteps() > 0 && list.get(rowIndex).getNewSteps() > 0){
				this.setBackground(new Color(52,216,142));
			}else{
				this.setBackground(initColor);
			}
		}

		switch(columnIndex){
			case 0:
				obj = rowIndex + 1;
				break;
			case 1:
				obj = list.get(rowIndex).getOldPath();
				break;
			case 2:
				obj = list.get(rowIndex).getOldSource();
				break;
			case 3:
				obj = list.get(rowIndex).getNewPath();
				break;
			case 4:
				obj = list.get(rowIndex).getNewSource();
				break;
			case 5:
				obj = list.get(rowIndex).getOldSteps();
				break;
			case 6:
				obj = list.get(rowIndex).getEditSteps();
				break;
			case 7:
				obj = list.get(rowIndex).getAddSteps();
				break;
			case 8:
				obj = list.get(rowIndex).getDeleteSteps();
				break;
			case 9:
				obj = list.get(rowIndex).getNewSteps();
				break;
		}
		
		return obj;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

}
