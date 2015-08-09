package diff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
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
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class ShowSourceInfoWindow2 implements ActionListener {
	private static final long serialVersionUID = 1L;
	String back = "/img/backward.gif";
	String backPress = "/img/backwardPressed.jpg";
	String backDisable = "/img/backwardDisabled.gif";
	String forward = "/img/forward.gif";
	String forwardPress = "/img/forwardPressed.jpg";
	String forwardDisable = "/img/forwardDisabled.gif";
	String close = "/img/notification-close.gif";
	int i = 1;
	int first = 1;
	Container container;
	JToolBar toolBar = new JToolBar();
	JSplitPane splitPane = new JSplitPane ();
	JScrollPane jsp1;
	JTextPane jtp1;
	StyledDocument doc1;
	JScrollPane jsp2;
	JTextPane jtp2;
	StyledDocument doc2;
	DefaultHighlighter.DefaultHighlightPainter grayPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(216,216,216));
	DefaultHighlighter.DefaultHighlightPainter whitePainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,255,255));
	DefaultHighlighter.DefaultHighlightPainter greenPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(0,255,255));
	Highlighter hilite1;
	Highlighter hilite2;
	Object lstTag1;
	int lstStart1;
	int lstEnd1;
	Object lstTag2;
	int lstStart2;
	int lstEnd2;
	int startOffset;
	List<Integer> leftOffsets = new ArrayList<Integer>();
	List<Integer> rightOffsets = new ArrayList<Integer>();
	int flg = 0;
	boolean enable;
	
	int lineNum1 = 0;
	int lineNum2 = 0;
	String separator = System.getProperty("line.separator");
	JTextPane lines1 = new JTextPane(){
		private static final long serialVersionUID = 1L;

		public boolean getScrollableTracksViewportWidth(){
			return false;
		}
	};
	JTextPane lines2 = new JTextPane(){
		private static final long serialVersionUID = 1L;

		public boolean getScrollableTracksViewportWidth(){
			return false;
		}
	};
	StyledDocument docLn1;
	StyledDocument docLn2;
	JTextPane paths1 = new JTextPane(){
		private static final long serialVersionUID = 1L;

		public boolean getScrollableTracksViewportWidth(){
			return false;
		}
	};
	JTextPane paths2 = new JTextPane(){
		private static final long serialVersionUID = 1L;

		public boolean getScrollableTracksViewportWidth(){
			return false;
		}
	};
	StyledDocument docPh1;
	StyledDocument docPh2;
	String path1 = "";
	String path2 = "";
	Font font = new Font("Courier New",0,11);
	String filename;
	ShowSourceInfoWindow2(String filename,boolean enable) {
		this.filename = filename;
		this.enable = enable;
		Container container = new JRootPane();
		container.setLayout(new BorderLayout());
		hilite1 = new MyHighlighter();
		hilite2 = new MyHighlighter();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.jtp1 = new JTextPane(){
			private static final long serialVersionUID = 1L;

			public boolean getScrollableTracksViewportWidth(){
				return false;
			}
			public void setSize(Dimension d){
				if(d.width < getParent().getSize().width){
					d.width = getParent().getSize().width;
				}
				d.width += 100;
				super.setSize(d);
			}
		};		// cut off the auto wrap and set the JTextpane's viewWidth
		//jtp1.setSelectionColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		jtp1.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
            	int currentLine = getLineFromOffset(jtp1,e);
            	startOffset = getLineStartOffsetForLine(jtp1, currentLine);
            	flg = 0;
            	jtp1.getCaret().setVisible(true);
            	jtp2.getCaret().setVisible(false);
            }
        });
		jtp1.setEditable(false);
	    jtp1.setHighlighter(hilite1);
		jtp1.setFont(font);
		this.doc1 = jtp1.getStyledDocument();
		this.setTabs(jtp1,doc1,4);		// tab stop value is 4,the same as UE
		this.jsp1 = new JScrollPane(jtp1,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		//lines1.setAlignmentX(RIGHT_ALIGNMENT);
		docLn1 = lines1.getStyledDocument();
		setTabs(lines1,docLn1,4);
		lines1.setFont(font);
		lines1.setBackground(new Color(238,236,225));
		lines1.setEditable(false);
		jsp1.setRowHeaderView(lines1);
		docPh1 = paths1.getStyledDocument();
		paths1.setFont(font);
		paths1.setBackground(new Color(238,236,225));
		paths1.setEditable(false);
		jsp1.setColumnHeaderView(paths1);

		this.jtp2 = new JTextPane(){
			private static final long serialVersionUID = 1L;

			public boolean getScrollableTracksViewportWidth(){
				return false;
			}
			public void setSize(Dimension d){
				if(d.width < getParent().getSize().width){
					d.width = getParent().getSize().width;
				}
				d.width += 100;
				super.setSize(d);
			}
		};		// cut off the auto wrap and set the JTextpane's viewWidth
		jtp2.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
            	int currentLine = getLineFromOffset(jtp2,e);
            	startOffset = getLineStartOffsetForLine(jtp2, currentLine);
            	flg = 1;
            	jtp1.getCaret().setVisible(false);
            	jtp2.getCaret().setVisible(true);
            }
        });
		jtp2.setEditable(false);
		jtp2.setHighlighter(hilite2);
		jtp2.setFont(font);
		this.doc2 = jtp2.getStyledDocument();
		this.setTabs(jtp2,doc2,4);		// tab stop value is 4,the same as UE
		this.jsp2 = new JScrollPane(jtp2,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		docLn2 = lines2.getStyledDocument();
		setTabs(lines2,docLn2,4);
		lines2.setFont(font);
		lines2.setBackground(new Color(238,236,225));
		lines2.setEditable(false);
		jsp2.setRowHeaderView(lines2);
		docPh2 = paths2.getStyledDocument();
		paths2.setFont(font);
		paths2.setBackground(new Color(238,236,225));
		paths2.setEditable(false);
		jsp2.setColumnHeaderView(paths2);
		
		SimpleAttributeSet setLn = new SimpleAttributeSet();
		StyleConstants.setAlignment(setLn, StyleConstants.ALIGN_RIGHT);
		docLn1.setParagraphAttributes(0, docLn1.getLength(), setLn, true);
		docLn2.setParagraphAttributes(0, docLn2.getLength(), setLn, true);
		
		ChangeListener cl = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JViewport src = null;
				JViewport tgt = null;
				if (e.getSource() == jsp1.getViewport()) {
					src = jsp1.getViewport();
					tgt = jsp2.getViewport();
				} else if (e.getSource() == jsp2.getViewport()) {
					src = jsp2.getViewport();
					tgt = jsp1.getViewport();
				}
				Point pnt1 = src.getViewPosition();
				tgt.setViewPosition(pnt1);
			}
		};
		jsp1.getViewport().addChangeListener(cl);
		jsp2.getViewport().addChangeListener(cl);
		
		this.splitPane.setOneTouchExpandable (true);
		this.splitPane.setContinuousLayout (true);
		this.splitPane.setPreferredSize (d);  
		this.splitPane.setOrientation (JSplitPane.HORIZONTAL_SPLIT);
		this.splitPane.setLeftComponent (jsp1);
		this.splitPane.setRightComponent (jsp2);  
		this.splitPane.setDividerSize (5);
		this.splitPane.setDividerLocation(0.5056);
		this.splitPane.setResizeWeight(0.5056);
		
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
		container.add(splitPane,BorderLayout.CENTER);
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
	public void setTabs(JTextPane jtp,StyledDocument doc,int charactersPerTab){
		FontMetrics fm = jtp.getFontMetrics(jtp.getFont());
		int charWidth = fm.charWidth('W');
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
	public void showDiffInfo(String oldLine,String newLine,int type) {
		if(first == 1 && oldLine.matches("^(oldPath:).*") && newLine.matches("^(newPath:).*")){
			try {
				docPh1.insertString(docPh1.getLength(),oldLine, null);
				docPh2.insertString(docPh2.getLength(),newLine, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			first ++;
			return;
		}
		Color foreColor = null;
		SimpleAttributeSet set1 = new SimpleAttributeSet();
		SimpleAttributeSet set2 = new SimpleAttributeSet();

		switch (type) {
			case 1:
				foreColor = Color.BLACK;
				StyleConstants.setForeground(set1, foreColor);
				StyleConstants.setForeground(set2, foreColor);
				try {
					docLn1.insertString(docLn1.getLength()," " + ++lineNum1 + separator, null);
					docLn2.insertString(docLn2.getLength()," " + ++lineNum2 + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;
			case 2:
				foreColor = new Color(0,176,80) ;		// green
				StyleConstants.setForeground(set1, foreColor);
				StyleConstants.setForeground(set2, foreColor);
				try {
					docLn1.insertString(docLn1.getLength()," " + "" + separator, null);
					docLn2.insertString(docLn2.getLength()," " + ++lineNum2 + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;
			case 3:
				foreColor = Color.RED;
				StyleConstants.setForeground(set1, foreColor);
				StyleConstants.setForeground(set2, foreColor);
				try {
					docLn1.insertString(docLn1.getLength()," " + ++lineNum1 + separator, null);
					docLn2.insertString(docLn2.getLength()," " + "" + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;				
			case 4:
				foreColor = Color.BLUE;
				StyleConstants.setForeground(set1, foreColor);
				foreColor = new Color(228,109,10);
				StyleConstants.setForeground(set2, foreColor);
				try {
					docLn1.insertString(docLn1.getLength(), " " + ++lineNum1 + separator, null);
					docLn2.insertString(docLn2.getLength(), " " + ++lineNum2 + separator, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				break;
		}
		
		try {
			int lastEndOffset1 = doc1.getLength();
			int lastEndOffset2 = doc2.getLength();
			doc1.insertString(doc1.getLength(), oldLine + separator, set1);
			doc2.insertString(doc2.getLength(), newLine + separator, set2);
			int currentEndOffset1 = doc1.getLength();
			int currentEndOffset2 = doc2.getLength();
			Object currentTag1 = null;
			Object currentTag2 = null;
			if(type != 1){
				currentTag1 = hilite1.addHighlight(lastEndOffset1, currentEndOffset1, grayPainter);
				currentTag2 = hilite2.addHighlight(lastEndOffset2, currentEndOffset2, grayPainter);
				leftOffsets.add(lastEndOffset1);
				rightOffsets.add(lastEndOffset2);
			}else{
				currentTag1 = hilite1.addHighlight(lastEndOffset1, currentEndOffset1, whitePainter);
				currentTag2 = hilite2.addHighlight(lastEndOffset2, currentEndOffset2, whitePainter);
			}
			if(lstTag1 != null){
				hilite1.changeHighlight(lstTag1, lstStart1, lstEnd1);
			}
			if(lstTag2 != null){
				hilite2.changeHighlight(lstTag2, lstStart2, lstEnd2);
			}
			lstTag1 = currentTag1;
			lstStart1 = lastEndOffset1;
			lstEnd1 = currentEndOffset1;
			lstTag2 = currentTag2;
			lstStart2 = lastEndOffset2;
			lstEnd2 = currentEndOffset2;

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("back")){
			if(flg == 0){
				int idx = leftOffsets.indexOf(startOffset);
				int size = leftOffsets.size();
				if(idx != -1){
					if(idx == 0){
						idx = size;
					}
					jtp2.setCaretPosition(rightOffsets.get(idx - 1));
					jtp1.setCaretPosition(leftOffsets.get(idx - 1));
				}else{
					for(int i = 0;i < size;i ++){
						if(i == size - 1){
							jtp2.setCaretPosition(rightOffsets.get(i));	
							jtp1.setCaretPosition(leftOffsets.get(i));
							break;
						}
						if(leftOffsets.get(i)<startOffset && startOffset<leftOffsets.get(i + 1)){							
							jtp2.setCaretPosition(rightOffsets.get(i));
							jtp1.setCaretPosition(leftOffsets.get(i));
							break;
						}
					}
				}

			}else{
				int idx = rightOffsets.indexOf(startOffset);
				int size = rightOffsets.size();
				if(idx != -1){
					if(idx == 0){
						idx = size;
					}
					jtp1.setCaretPosition(leftOffsets.get(idx - 1));
					jtp2.setCaretPosition(rightOffsets.get(idx - 1));
				}else{
					for(int i = 0;i < size;i ++){
						if(i == size - 1){
							jtp1.setCaretPosition(leftOffsets.get(i));
							jtp2.setCaretPosition(rightOffsets.get(i));	
							break;
						}
						if(rightOffsets.get(i)<startOffset && startOffset<rightOffsets.get(i + 1)){
							jtp1.setCaretPosition(leftOffsets.get(i));
							jtp2.setCaretPosition(rightOffsets.get(i));
							break;
						}
					}
				}

			}
		}else{
			if(flg == 0){
				int idx = leftOffsets.indexOf(startOffset);
				int size = leftOffsets.size();
				if(idx != -1){
					if(idx == size - 1){
						idx = -1;
					}
					jtp2.setCaretPosition(rightOffsets.get(idx + 1));
					jtp1.setCaretPosition(leftOffsets.get(idx + 1));
				}else{
					for(int i = 0;i < size;i ++){
						if(i == size - 1){
							jtp2.setCaretPosition(rightOffsets.get(0));	
							jtp1.setCaretPosition(leftOffsets.get(0));
							break;
						}
						if(leftOffsets.get(i)<startOffset && startOffset<leftOffsets.get(i + 1)){
							jtp2.setCaretPosition(rightOffsets.get(i + 1));
							jtp1.setCaretPosition(leftOffsets.get(i + 1));
							break;
						}
					}
				}

			}else{
				int idx = rightOffsets.indexOf(startOffset);
				int size = rightOffsets.size();
				if(idx != -1){
					if(idx == size - 1){
						idx = -1;
					}
					jtp1.setCaretPosition(leftOffsets.get(idx + 1));
					jtp2.setCaretPosition(rightOffsets.get(idx + 1));
				}else{
					for(int i = 0;i < size;i ++){
						if(i == size - 1){
							jtp1.setCaretPosition(leftOffsets.get(0));
							jtp2.setCaretPosition(rightOffsets.get(0));	
							break;
						}
						if(rightOffsets.get(i)<startOffset && startOffset<rightOffsets.get(i + 1)){
							jtp1.setCaretPosition(leftOffsets.get(i + 1));
							jtp2.setCaretPosition(rightOffsets.get(i + 1));
							break;
						}
					}
				}

			}
		}
	}
}
class MyHighlighter extends DefaultHighlighter{

    private JTextComponent component;
    /**
     * @see javax.swing.text.DefaultHighlighter#install(javax.swing.text.JTextComponent)
     */
    @Override
    public final void install(final JTextComponent c)
    {
        super.install(c);
        this.component = c;
    }

    /**
     * @see javax.swing.text.DefaultHighlighter#deinstall(javax.swing.text.JTextComponent)
     */
    @Override
    public final void deinstall(final JTextComponent c)
    {
        super.deinstall(c);
        this.component = null;
    }

    /**
     * Same algo, except width is not modified with the insets.
     * 
     * @see javax.swing.text.DefaultHighlighter#paint(java.awt.Graphics)
     */
    @Override
    public final void paint(final Graphics g){
        final Highlighter.Highlight[] highlights = getHighlights();
        final int len = highlights.length;
        for (int i = 0; i < len; i++){
            Highlighter.Highlight info = highlights[i];
            if (info.getClass().getName().indexOf("LayeredHighlightInfo") > -1){
                // Avoid allocing unless we need it.
                final Rectangle a = this.component.getBounds();
                final Insets insets = this.component.getInsets();
                a.x = insets.left;
                a.y = insets.top;
                //a.width -= insets.left + insets.right + 100;
                a.height -= insets.top + insets.bottom;
                for (; i < len; i++){
                    info = highlights[i];
                    if (info.getClass().getName().indexOf("LayeredHighlightInfo") > -1){
                        final Highlighter.HighlightPainter p = info.getPainter();
                        p.paint(g, info.getStartOffset(), info.getEndOffset(), a, this.component);
                    }
                }
            }
        }
    }
}