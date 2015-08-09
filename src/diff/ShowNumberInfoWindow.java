package diff;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class ShowNumberInfoWindow{
	private static final long serialVersionUID = 1L;
	private static final String ONE = "one";
	private static final String TWO = "two";
	String close = "/img/notification-close.gif";
	JTable table;
	String actionCommand = TWO;
	List<DiffInfo> list = new ArrayList<DiffInfo> ();
	
	public ShowNumberInfoWindow(List<DiffInfo> list){
		this.list = list;
	}
	
	public void showInfo(){
		launchFrame();
	}
	private void launchFrame(){
		JPanel jp = new JPanel();
		// diff style
		JToolBar toolBar = new JToolBar();
        JLabel jlStyle = new JLabel("diff style:",JLabel.TRAILING);
        JRadioButton jrb1 = new JRadioButton("one window",false);
        JRadioButton jrb2 = new JRadioButton("two window",true);
        ButtonGroup  bg = new ButtonGroup();
        bg.add(jrb1);
        bg.add(jrb2);
        toolBar.add(jlStyle);
        toolBar.add(jrb1);
        toolBar.add(jrb2);
        jrb1.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		actionCommand = ONE;
        	  }
        });
        jrb2.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		actionCommand = TWO;
        	}
        });
        jp.add(toolBar,BorderLayout.NORTH);

		// number info
		DiffTableModel dtm = new DiffTableModel(list);
		table = new JTable(dtm);
		int rowHeight = 20;
		table.setRowHeight(rowHeight);
		int tableWidth = MCA.width - 70;
		int tableHeight = (MCA.height - 150)/20 * 20;
		TableColumnModel tableColumnModel = table.getColumnModel();
		for (int k = 0; k < tableColumnModel.getColumnCount(); k++) {
            TableColumn col = tableColumnModel.getColumn(k);
            int cw = tableWidth*dtm.getColumnWidthPer(k)/2950;
            col.setPreferredWidth(cw);
            col.setCellRenderer(dtm);
        }
		table.setPreferredScrollableViewportSize(new Dimension(tableWidth, tableHeight));
		TableRowSorter<DiffTableModel> trs = new TableRowSorter<DiffTableModel>(dtm);
		table.setRowSorter(trs);
        table.setUpdateSelectionOnSort(true);
        //table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                try {
					tableMousePressed(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        });
        JScrollPane scrollpane = new JScrollPane(table);
		jp.add(scrollpane,BorderLayout.CENTER);
		setTab(jp);
		//set focus
		MainWindow.rjtp.setSelectedComponent(jp);
	}

	public void setTab(final JPanel jp) {
		MainWindow.rjtp.addTab(null, jp);
		JLabel tabLabel = new JLabel("general");
		tabLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int closeTabNumber = MainWindow.rjtp.indexOfComponent(jp);
					MainWindow.rjtp.removeTabAt(closeTabNumber);
				}else if(e.getClickCount() == 1){
					MainWindow.rjtp.setSelectedComponent(jp);
				}
			}
		});
		MainWindow.rjtp.setTabComponentAt(MainWindow.rjtp.getTabCount() - 1,
				tabLabel);
	}

	private void tableMousePressed(MouseEvent e) throws IOException {
        int selectedRow = table.getSelectedRow();
        // when the first time get in the window ,this situation(selectedRow == -1) could happen
        if(selectedRow == -1){
        	return;
        }
        selectedRow = (Integer)table.getValueAt(selectedRow, 0) - 1;
        if ((selectedRow != list.size() - 1 && selectedRow != -1) && e.getButton() == MouseEvent.BUTTON1) {	//BUTTON1:left BUTTON2:middle BUTTON3:right
            if(e.getClickCount() == 2) {
            	DiffInfo di = list.get(selectedRow);
            	String oldPath = di.getOldPath();
            	String newPath = di.getNewPath();
            	if(!(oldPath == null || oldPath.equals("") || newPath == null || newPath.equals(""))){
            		Diff diff = new Diff(oldPath,newPath);
                	diff.showDiff = true;
                	String content = null;
                	content = (String)diff.executeDiff();
    				String[] cnts = content.split("\n");
    				if(actionCommand.equals(TWO)){
    					showDiffInfo2(di.getNewSource(),di.enable,cnts);
    				}else{
    					showDiffInfo1(di.getNewSource(),di.enable,cnts);
    				}
            	}
            }
        } 
    }
	private void showDiffInfo1(String filename,boolean enable,String[] cnts){
		ShowSourceInfoWindow1 ssiw1 = new ShowSourceInfoWindow1(filename,enable);
		int len = cnts.length;
		for(int i = 0;i < len;i ++){
			if(cnts[i].matches("^(" + Diff.BEGIN2 + ").*")){
				ssiw1.showDiffInfo(cnts[i],2);
			}else if(cnts[i].matches("^(" + Diff.BEGIN3 + ").*")){
				ssiw1.showDiffInfo(cnts[i],3);
			}else if(cnts[i].matches("^(" + Diff.BEGIN4 + ").*")){
				ssiw1.showDiffInfo(cnts[i],4);
			}else if(cnts[i].matches("^(" + Diff.BEGIN5 + ").*")){
				ssiw1.showDiffInfo(cnts[i],5);
			}else{
				ssiw1.showDiffInfo(cnts[i],1);
			}
		}
		ssiw1.jtp.setCaretPosition(0);
		ssiw1.jtp.getCaret().setVisible(true);
	}
	private void showDiffInfo2(String filename,boolean enable,String[] cnts){
		ShowSourceInfoWindow2 ssiw2 = new ShowSourceInfoWindow2(filename,enable);
		int beginLen = Diff.BEGIN2.length();
		int len = cnts.length;
		
		for(int i = 0;i < len;i ++){
			if(cnts[i].matches("^(" + Diff.BEGIN2 + ").*")){
				String oldLine = "";
				String newLine = cnts[i].substring(beginLen);;
				ssiw2.showDiffInfo(oldLine,newLine,2);
			}else if(cnts[i].matches("^(" + Diff.BEGIN3 + ").*")){
				String oldLine = cnts[i].substring(beginLen);
				String newLine = "";
				ssiw2.showDiffInfo(oldLine,newLine,3);
			}else if(cnts[i].matches("^(" + Diff.BEGIN4 + ").*")){
				String oldLine = cnts[i].substring(beginLen);
				i ++;
				String newLine = cnts[i].substring(beginLen);
				ssiw2.showDiffInfo(oldLine,newLine,4);
			}else{
				if(i == 0){
					String oldLine = cnts[i];
					i ++;	
					String newLine = cnts[i];
					ssiw2.showDiffInfo(oldLine,newLine,1);
				}else{
					String oldLine = cnts[i].substring(beginLen);
					String newLine = cnts[i].substring(beginLen);	
					ssiw2.showDiffInfo(oldLine,newLine,1);
				}
			}
		}
		ssiw2.jtp2.setCaretPosition(0);
		ssiw2.jtp1.setCaretPosition(0);
		ssiw2.jtp1.getCaret().setVisible(true);
		ssiw2.jtp2.getCaret().setVisible(false);
	}
}


