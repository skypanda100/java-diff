package diff;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
//import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class MainWindow extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private static final String BROWSE_1_1 = "browse_1_1";
	private static final String BROWSE_2_1 = "browse_2_1";
	private static final String BROWSE_2_2 = "browse_2_2";
	private static final String COUNT_ONE = "count_1";
	private static final String COUNT_TWO = "count_2";
	private static final String CLEAR_ONE = "clear_1";
	private static final String CLEAR_TWO = "clear_2";
	private static final String LANG = "language";
	private static final String PROJ = "project";
	
	public static JTabbedPane rjtp = new JTabbedPane(JTabbedPane.TOP);
	JTextField jtf1;
	JTextField jtf2;
	JTextField jtf3;
	JTextField jtf4;
	JTextField jtfPath;
	JTextField jtfOldPath;
	JTextField jtfNewPath;
	JFileChooser fileChooser = null;
	String browse = "/img/search.gif";
	boolean flg = false;
	JDialog langJd;
	JDialog projJd;
	public static void main(String[] args) {
		LoadProperties.load();
		MainWindow tw = new MainWindow();
		tw.launchMainFrame();
	}

	public void launchMainFrame() {
		/*try {
			if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
				UIManager.put("JFileChooser", this);
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}*/
		setIconImage(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/img/logo.GIF")));
		//Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		//setSize(d.width,d.height - 100);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocation(0, 0);

		initMenu();
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		//launchLangFrame();
		//launchProjFrame();
		rjtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		container.add(rjtp,BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setVisible(true);
		addComponentListener(new MCA());
		//setResizable(false);
		//pack();
	
	}
	public void initMenu(){
		JMenuBar menuBar = new JMenuBar();
		
		JMenu langMenu = new JMenu("Language");
		JMenuItem lang = new JMenuItem("language");
		lang.setActionCommand(LANG);
		lang.addActionListener(this);
		langMenu.add(lang);
		menuBar.add(langMenu);
		
		JMenu projMenu = new JMenu("Project");
		JMenuItem proj = new JMenuItem("project");
		proj.setActionCommand(PROJ);
		proj.addActionListener(this);
		projMenu.add(proj);
		menuBar.add(projMenu);
		
		setJMenuBar(menuBar);
	}
	public void launchLangFrame(Container container){
		JPanel jpLang = new JPanel();
		LangTableModel ltm = new LangTableModel();
		JTable table = new JTable(ltm);
		int rowHeight = 20;
		table.setRowHeight(rowHeight);
		TableColumnModel tableColumnModel = table.getColumnModel();
		for (int k = 0; k < tableColumnModel.getColumnCount(); k++) {
            TableColumn col = tableColumnModel.getColumn(k);
            int cw = 800*ltm.getColumnWidthPer(k)/350;
            col.setPreferredWidth(cw);
            col.setCellRenderer(ltm);
        }
		table.setPreferredScrollableViewportSize(new Dimension(400, 420));
		TableRowSorter<LangTableModel> trs = new TableRowSorter<LangTableModel>(ltm);
		table.setRowSorter(trs);
        table.setUpdateSelectionOnSort(true);
        //table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        JScrollPane scrollpane = new JScrollPane(table);
        jpLang.add(scrollpane);
        container.add(jpLang);
	}

	@SuppressWarnings("static-access")
	public void launchProjFrame(Container container){
		JTabbedPane pjtp = new JTabbedPane(JTabbedPane.TOP);
		//one
		JPanel jpOne = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		Dimension d = new Dimension(200,24);
		jpOne.setLayout(gridbag);
		JLabel jlPath = new JLabel("Path:",JLabel.TRAILING);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.2;
		constraints.weighty = 0.05;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jlPath, constraints);
		jpOne.add(jlPath);
		jtfPath = new JTextField("",30);
		jtfPath.setMaximumSize(d);
		jtfPath.setMaximumSize(d);
		jtfPath.setPreferredSize(d);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.7;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jtfPath, constraints);
		jpOne.add(jtfPath);
		
		JButton jbBrowse_1_1 = new JButton(new ImageIcon(this.getClass().getResource(browse)));
		//jbBrowse_1_1.setBorder(null);
		//jbBrowse_1_1.setContentAreaFilled(false);
		jbBrowse_1_1.setActionCommand(BROWSE_1_1);
		jbBrowse_1_1.addActionListener(this);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.1;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jbBrowse_1_1, constraints);
		jpOne.add(jbBrowse_1_1);
		
		JPanel subJpOne = new JPanel();
		constraints.fill = constraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 0.05;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		gridbag.setConstraints(subJpOne, constraints);
		jpOne.add(subJpOne);
		JButton jbPathOne = new JButton("count");	
		jbPathOne.setActionCommand(COUNT_ONE);
		jbPathOne.addActionListener(this);
		JButton jbClearOne = new JButton("clear");	
		jbClearOne.setActionCommand(CLEAR_ONE);
		jbClearOne.addActionListener(this);
		
		subJpOne.add(jbPathOne);
		subJpOne.add(jbClearOne);
		
		JPanel subBlankJpOne = new JPanel();
		constraints.fill = constraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 0.9;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		gridbag.setConstraints(subBlankJpOne, constraints);
		jpOne.add(subBlankJpOne);
		
		pjtp.add("one folder",jpOne);
		
		//two
		JPanel jpTwo = new JPanel();
		jpTwo.setLayout(gridbag);
		
		JLabel jlOldPath = new JLabel("OldPath:",JLabel.TRAILING);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.2;
		constraints.weighty = 0.05;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jlOldPath, constraints);
		jpTwo.add(jlOldPath);
		jtfOldPath = new JTextField("F:\\new_laxweb\\workspace\\CountLine_jar_withOutVSS_01",30);
		jtfOldPath.setMaximumSize(d);
		jtfOldPath.setMaximumSize(d);
		jtfOldPath.setPreferredSize(d);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.7;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jtfOldPath, constraints);
		jpTwo.add(jtfOldPath);
		
		JButton jbBrowse_2_1 = new JButton(new ImageIcon(this.getClass().getResource(browse)));
		//jbBrowse_1_1.setBorder(null);
		//jbBrowse_1_1.setContentAreaFilled(false);
		jbBrowse_2_1.setActionCommand(BROWSE_2_1);
		jbBrowse_2_1.addActionListener(this);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.1;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jbBrowse_2_1, constraints);
		jpTwo.add(jbBrowse_2_1);
		
		JLabel jlNewPath = new JLabel("NewPath:",JLabel.TRAILING);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.2;
		constraints.weighty = 0.05;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jlNewPath, constraints);
		jpTwo.add(jlNewPath);
		jtfNewPath = new JTextField("F:\\new_laxweb\\workspace\\CountLine_jar_withOutVSS_04",30);
		jtfNewPath.setMaximumSize(d);
		jtfNewPath.setMaximumSize(d);
		jtfNewPath.setPreferredSize(d);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.7;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jtfNewPath, constraints);
		jpTwo.add(jtfNewPath);
		
		JButton jbBrowse_2_2 = new JButton(new ImageIcon(this.getClass().getResource(browse)));
		//jbBrowse_1_1.setBorder(null);
		//jbBrowse_1_1.setContentAreaFilled(false);
		jbBrowse_2_2.setActionCommand(BROWSE_2_2);
		jbBrowse_2_2.addActionListener(this);
		constraints.fill = constraints.NONE;
		constraints.weightx = 0.1;
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		gridbag.setConstraints(jbBrowse_2_2, constraints);
		jpTwo.add(jbBrowse_2_2);
		
		JPanel subJpTwo = new JPanel();
		constraints.fill = constraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 0.05;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		gridbag.setConstraints(subJpTwo, constraints);
		jpTwo.add(subJpTwo);
		JButton jbPathTwo = new JButton("count");	
		jbPathTwo.setActionCommand(COUNT_TWO);
		jbPathTwo.addActionListener(this);
		JButton jbClearTwo = new JButton("clear");	
		jbClearTwo.setActionCommand(CLEAR_TWO);
		jbClearTwo.addActionListener(this);
		
		subJpTwo.add(jbPathTwo);
		subJpTwo.add(jbClearTwo);
		
		JPanel subBlankJpTwo = new JPanel();
		constraints.fill = constraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 0.85;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		gridbag.setConstraints(subBlankJpTwo, constraints);
		jpTwo.add(subBlankJpTwo);

		pjtp.add("two folder",jpTwo);		
		container.add(pjtp);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if(command.equals(COUNT_ONE)){
			String path = jtfPath.getText();
			new ExeOneFolder().count(path);
			if(projJd != null){
				projJd.dispose();	
				projJd = null;
			}
		}else if(command.equals(CLEAR_ONE)){
			jtfPath.setText("");
		}else if(command.equals(COUNT_TWO)){
			String oldPath = jtfOldPath.getText();
			String newPath = jtfNewPath.getText();
			new ExeTwoFolder().count(oldPath,newPath);
			if(projJd != null){
				projJd.dispose();
				projJd = null;
			}
		}else if(command.equals(CLEAR_TWO)){
			jtfOldPath.setText("");
			jtfNewPath.setText("");
		}else if(command.equals(BROWSE_1_1)){
			fileChooser = new JFileChooser("C:\\winnt");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				jtfPath.setText(file.getAbsolutePath());
			} else if (result == fileChooser.CANCEL_OPTION) {
				// nothing to do
			}
		}else if(command.equals(BROWSE_2_1)){
			fileChooser = new JFileChooser("C:\\winnt");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				jtfOldPath.setText(file.getAbsolutePath());
			} else if (result == fileChooser.CANCEL_OPTION) {
				// nothing to do
			}
		}else if(command.equals(BROWSE_2_2)){
			fileChooser = new JFileChooser("C:\\winnt");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				jtfNewPath.setText(file.getAbsolutePath());
			} else if (result == fileChooser.CANCEL_OPTION) {
				// nothing to do
			}
		}else if(command.equals(LANG)){
			if(langJd == null){
				langJd = new JDialog(this,"LANGUAGE",true);
			}
			launchLangFrame(langJd.getContentPane());
			langJd.setSize(470,490);
			langJd.pack();
			langJd.setVisible(true);
			langJd = null;
		}else if(command.equals(PROJ)){
			if(projJd == null){
				projJd = new JDialog(this,"PROJECT",true);
			}
			launchProjFrame(projJd.getContentPane());
			projJd.setSize(500,170);
			projJd.pack();
			projJd.setVisible(true);
			projJd = null;
		}
	}
}
class MCA extends ComponentAdapter {
	public static int width = 0;
	public static int height = 0;

    public void componentResized(ComponentEvent e) {
    	width = e.getComponent().getWidth();
    	height = e.getComponent().getHeight();
   }
}
