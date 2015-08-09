package diff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class ShowSourceInfoWindow1 implements ActionListener {
	private static final long serialVersionUID = 1L;
	String back = "/img/backward.gif";
	String backPress = "/img/backwardPressed.jpg";
	String backDisable = "/img/backwardDisabled.gif";
	String forward = "/img/forward.gif";
	String forwardPress = "/img/forwardPressed.jpg";
	String forwardDisable = "/img/forwardDisabled.gif";
	String close = "/img/notification-close.gif";
	Container container;
	JScrollPane jsp;
	JTextPane jtp;
	StyledDocument doc;
	JToolBar toolBar = new JToolBar();
	boolean enable;
	int startOffset;
	List<Integer> offsets = new ArrayList<Integer>();
	int lineNum = 0;
	String separator = System.getProperty("line.separator");
	JTextPane lines = new JTextPane(){
		private static final long serialVersionUID = 1L;

		public boolean getScrollableTracksViewportWidth(){
			return false;
		}
	};
	StyledDocument docLn;
	JTextPane paths = new JTextPane(){
		private static final long serialVersionUID = 1L;

		public boolean getScrollableTracksViewportWidth(){
			return false;
		}
	};
	StyledDocument docPh;
	String path = "";
	Font font = new Font("Courier New",0,11);
	String filename;
	ShowSourceInfoWindow1(String filename,boolean enable) {
		this.filename = filename;
		this.enable = enable;
		Container container = new JRootPane();
		container.setLayout(new BorderLayout());
		this.jtp = new JTextPane(){
			private static final long serialVersionUID = 1L;

			public boolean getScrollableTracksViewportWidth(){
				return false;
			}
			public void setSize(Dimension d){
				if(d.width < getParent().getSize().width){
					d.width = getParent().getSize().width;
				}
				//d.width += 100;
				super.setSize(d);
			}
		};		// cut off the auto wrap and set the JTextpane's viewWidth

		jtp.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
            	int currentLine = getLineFromOffset(jtp,e);
            	startOffset = getLineStartOffsetForLine(jtp, currentLine);
            	jtp.getCaret().setVisible(true);
            }
        });
		jtp.setEditable(false);
		jtp.setFont(font);
		this.doc = jtp.getStyledDocument();
		setTabs(jtp,doc,4);		// tab stop value is 4,the same as UE
		
		this.jsp = new JScrollPane(jtp,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		
		docLn = lines.getStyledDocument();
		setTabs(lines,docLn,4);
		lines.setFont(font);
		lines.setBackground(new Color(238,236,225));
		lines.setEditable(false);
		jsp.setRowHeaderView(lines);
		docPh = paths.getStyledDocument();
		paths.setFont(font);
		paths.setBackground(new Color(238,236,225));
		paths.setEditable(false);
		jsp.setColumnHeaderView(paths);
		
		SimpleAttributeSet setLn = new SimpleAttributeSet();
		StyleConstants.setAlignment(setLn, StyleConstants.ALIGN_RIGHT);
		docLn.setParagraphAttributes(0, docLn.getLength(), setLn, true);
		
		JButton jb1 = null;
		if(enable){
			jb1 = new JButton(new ImageIcon(this.getClass().getResource(back)));
			jb1.setPressedIcon(new ImageIcon(this.getClass().getResource(backPress)));
			jb1.setActionCommand("back");
			jb1.addActionListener(this);
		}else{
			jb1 = new JButton(new ImageIcon(this.getClass().getResource(backDisable)));
		}
		
		jb1.setBorder(null);
		jb1.setContentAreaFilled(false);

		JButton jb2 = null;
		if(enable){
			jb2 = new JButton(new ImageIcon(this.getClass().getResource(forward)));
			jb2.setPressedIcon(new ImageIcon(this.getClass().getResource(forwardPress)));
			jb2.setActionCommand("forward");
			jb2.addActionListener(this);
		}else{
			jb2 = new JButton(new ImageIcon(this.getClass().getResource(forwardDisable)));
		}
		jb2.setBorder(null);
		jb2.setContentAreaFilled(false);
		toolBar.add(jb1);
		toolBar.add(jb2);
		container.add(toolBar,BorderLayout.NORTH);
		container.add(jsp,BorderLayout.CENTER);
		setTab(container);
		//set focus
		MainWindow.rjtp.setSelectedComponent(container);
	}

	public void setTab(final Container container) {
		MainWindow.rjtp.addTab(null, container);

		JLabel tabLabel = new JLabel(filename);

		tabLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int closeTabNumber = MainWindow.rjtp
							.indexOfComponent(container);
					MainWindow.rjtp.removeTabAt(closeTabNumber);
				} else if (e.getClickCount() == 1) {
					MainWindow.rjtp.setSelectedComponent(container);
				}
			}
		});

		MainWindow.rjtp.setTabComponentAt(MainWindow.rjtp.getTabCount() - 1,
				tabLabel);
	}
	public int getLineFromOffset(JTextComponent component, CaretEvent e) {
        return component.getDocument().getDefaultRootElement().getElementIndex(e.getDot());
    }

    public int getLineStartOffsetForLine(JTextComponent component, int line) {
        return component.getDocument().getDefaultRootElement().getElement(line).getStartOffset();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		int idx = offsets.indexOf(startOffset);
		int size = offsets.size();
		if(command.equals("back")){
			if(idx != -1){
				if(idx == 0){
					idx = size;
				}
				jtp.setCaretPosition(offsets.get(idx - 1));
			}else{
				for(int i = 0;i < size;i ++){
					if(i == size - 1){
						jtp.setCaretPosition(offsets.get(i));	
						break;
					}
					if(offsets.get(i)<startOffset && startOffset<offsets.get(i + 1)){							
						jtp.setCaretPosition(offsets.get(i));
						break;
					}
				}
			}
		}else{
			if(idx != -1){
				if(idx == size - 1){
					idx = -1;
				}
				jtp.setCaretPosition(offsets.get(idx + 1));
			}else{
				for(int i = 0;i < size;i ++){
					if(i == size - 1){
						jtp.setCaretPosition(offsets.get(0));	
						break;
					}
					if(offsets.get(i)<startOffset && startOffset<offsets.get(i + 1)){
						jtp.setCaretPosition(offsets.get(i + 1));
						break;
					}
				}
			}
		}
	}
	public void setTabs(JTextPane jtp,StyledDocument doc,int charactersPerTab){
		FontMetrics fm = jtp.getFontMetrics(jtp.getFont());
		int charWidth = fm.charWidth('w');
		int tabWidth = charWidth*charactersPerTab;
		TabStop[] tabs = new TabStop[10];
		for(int i = 1;i <= tabs.length;i ++){
			tabs[i - 1] = new TabStop(i*tabWidth);
		}
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setTabSet(set, tabSet);
		doc.setParagraphAttributes(0, doc.getLength(), set, true);
	}

	public void showDiffInfo(String line,int type) {
		if(line.matches("^(oldPath:).*")||line.matches("^(newPath:).*")){
			try {
				docPh.insertString(docPh.getLength(),line + (line.matches("^(oldPath:).*")?separator:""), null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			return;
		}
		Color foreColor = null;
		Color backColor = null;
		SimpleAttributeSet set = new SimpleAttributeSet();
		switch (type) {
			case 1:
				foreColor = Color.BLACK;
				backColor = Color.WHITE;
				try {
					docLn.insertString(docLn.getLength(), " " + ++lineNum + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;
			case 2:
				foreColor = new Color(0,176,80) ;		// green
				backColor = new Color(216,216,216);		// light gray
				try {
					docLn.insertString(docLn.getLength(), " " + "" + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;
			case 3:
				foreColor = Color.RED;
				backColor = new Color(216,216,216);		// light gray
				StyleConstants.setStrikeThrough(set, true);		// when this line has been deleted,this line add strikethrough
				try {
					docLn.insertString(docLn.getLength(), " " + ++lineNum + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;				
			case 4:
				foreColor = Color.BLUE;
				backColor = new Color(216,216,216);		// light gray
				try {
					docLn.insertString(docLn.getLength(), " " + ++lineNum + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;
			case 5:
				foreColor = new Color(228,109,10);
				backColor = new Color(216,216,216);		// light gray
				try {
					docLn.insertString(docLn.getLength(), " " + "" + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;
		}
		StyleConstants.setForeground(set, foreColor);
		StyleConstants.setBackground(set, backColor);

		try {
			if(type != 1){
				offsets.add(doc.getLength());
			}
			doc.insertString(doc.getLength(), line + separator, set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
