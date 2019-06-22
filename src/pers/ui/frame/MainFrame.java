package pers.ui.frame;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import pers.ui.abs.AbstractFrame;
import pers.ui.dialog.FindDialog;
import pers.ui.dialog.FontDialog;
import pers.ui.dialog.ReplaceDialog;
import pers.util.FileUtil;

/**
 * 主窗体类
 * 
 * @author ordinary-student
 *
 */
public class MainFrame extends AbstractFrame
{
	private static final long serialVersionUID = 7019955668365884828L;
	// 菜单
	private JMenu fileMenu, editMenu, formatMenu, viewMenu, helpMenu;
	// "文件"的菜单项
	private JMenuItem fileMenu_New, fileMenu_Open, fileMenu_Save, fileMenu_SaveAs, fileMenu_PageSetUp, fileMenu_Print,
			fileMenu_Exit;
	// "编辑"的菜单项
	private JMenuItem editMenu_Undo, editMenu_Cut, editMenu_Copy, editMenu_Paste, editMenu_Delete, editMenu_Find,
			editMenu_FindNext, editMenu_Replace, editMenu_GoTo, editMenu_SelectAll, editMenu_TimeDate;
	// "格式"的菜单项
	private JCheckBoxMenuItem formatMenu_LineWrap;
	private JMenuItem formatMenu_Font;
	// "查看"的菜单项
	private JCheckBoxMenuItem viewMenu_Status;
	// "帮助"的菜单项
	private JMenuItem helpMenu_HelpTopics, helpMenu_AboutNotepad;
	// 右键弹出菜单
	private JPopupMenu popupMenu;
	// 右键弹出菜单项
	private JMenuItem popupMenu_Undo, popupMenu_Cut, popupMenu_Copy, popupMenu_Paste, popupMenu_Delete,popupMenu_Find,
			popupMenu_SelectAll;
	// "文本"编辑区域
	private JTextArea editArea;
	// 状态栏标签
	private JLabel statusLabel;

	// 系统剪贴板
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Clipboard clipBoard = toolkit.getSystemClipboard();
	// 创建撤销操作管理器
	private UndoManager undoManager = new UndoManager();
	
	// 存放编辑区原来的内容，用于比较文本是否有改动
	private String oldValue;
	// 是否新文件(未保存过的)
	private boolean isNewFile = true;
	// 当前文件名
	private File currentFile;
	//是否已经保存
	private boolean isSave;
	

	/*
	 * 构造方法
	 */
	public MainFrame()
	{
		// 初始化界面
		initUI();
		// 检查菜单项的可用性
		checkMenuItemEnabled();
	}

	/**
	 * 初始化界面
	 */
	private void initUI()
	{
		// 设置标题
		setTitle("Java记事本");
		// 设置位置和大小
		setBounds(200, 100, 650, 550);
		// 添加窗口监听
		addWindowListener(this);

		// 创建菜单栏
		JMenuBar menuBar = new JMenuBar();

		// 创建文件菜单及菜单项并注册事件监听
		fileMenu = new JMenu("文件(F)");
		// 设置快捷键ALT+F
		fileMenu.setMnemonic('F');

		// 新建-菜单项
		fileMenu_New = new JMenuItem("新建(N)");
		fileMenu_New.setIcon(new ImageIcon(MainFrame.class.getResource("/res/new.gif")));
		fileMenu_New.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		fileMenu_New.addActionListener(this);
		fileMenu.add(fileMenu_New);

		// 打开-菜单项
		fileMenu_Open = new JMenuItem("打开(O)...");
		fileMenu_Open.setIcon(new ImageIcon(MainFrame.class.getResource("/res/open.gif")));
		fileMenu_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		fileMenu_Open.addActionListener(this);
		fileMenu.add(fileMenu_Open);

		// 保存-菜单项
		fileMenu_Save = new JMenuItem("保存(S)");
		fileMenu_Save.setIcon(new ImageIcon(MainFrame.class.getResource("/res/save.png")));
		fileMenu_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		fileMenu_Save.addActionListener(this);
		fileMenu.add(fileMenu_Save);

		// 另存为-菜单项
		fileMenu_SaveAs = new JMenuItem("另存为(A)...");
		fileMenu_SaveAs.setIcon(new ImageIcon(MainFrame.class.getResource("/res/saveas.png")));
		fileMenu_SaveAs.addActionListener(this);
		fileMenu.add(fileMenu_SaveAs);

		// 分隔线
		fileMenu.addSeparator();

		// 页面设置-菜单项
		fileMenu_PageSetUp = new JMenuItem("页面设置(U)...");
		fileMenu_PageSetUp.setIcon(new ImageIcon(MainFrame.class.getResource("/res/setting.png")));
		fileMenu_PageSetUp.addActionListener(this);
		fileMenu.add(fileMenu_PageSetUp);

		// 打印-菜单项
		fileMenu_Print = new JMenuItem("打印(P)...");
		fileMenu_Print.setIcon(new ImageIcon(MainFrame.class.getResource("/res/print.png")));
		fileMenu_Print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		fileMenu_Print.addActionListener(this);
		fileMenu.add(fileMenu_Print);

		// 分隔线
		fileMenu.addSeparator();

		// 退出-菜单项
		fileMenu_Exit = new JMenuItem("退出(X)");
		fileMenu_Exit.setIcon(new ImageIcon(MainFrame.class.getResource("/res/exit.png")));
		fileMenu_Exit.addActionListener(this);
		fileMenu.add(fileMenu_Exit);

		// 向菜单栏添加"文件"菜单
		menuBar.add(fileMenu);

		// 创建编辑菜单及菜单项并注册事件监听
		editMenu = new JMenu("编辑(E)");
		// 设置快捷键ALT+E
		editMenu.setMnemonic('E');
		// 添加菜单监听器
		editMenu.addMenuListener(this);

		// 撤销-菜单项
		editMenu_Undo = new JMenuItem("撤销(U)");
		editMenu_Undo.setIcon(new ImageIcon(MainFrame.class.getResource("/res/undo.png")));
		editMenu_Undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		editMenu_Undo.addActionListener(this);
		editMenu_Undo.setEnabled(false);
		editMenu.add(editMenu_Undo);

		// 分隔线
		editMenu.addSeparator();

		// 剪切-菜单项
		editMenu_Cut = new JMenuItem("剪切(T)");
		editMenu_Cut.setIcon(new ImageIcon(MainFrame.class.getResource("/res/cut.png")));
		editMenu_Cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		editMenu_Cut.addActionListener(this);
		editMenu.add(editMenu_Cut);

		// 复制-菜单项
		editMenu_Copy = new JMenuItem("复制(C)");
		editMenu_Copy.setIcon(new ImageIcon(MainFrame.class.getResource("/res/copy.gif")));
		editMenu_Copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		editMenu_Copy.addActionListener(this);
		editMenu.add(editMenu_Copy);

		// 粘贴-菜单项
		editMenu_Paste = new JMenuItem("粘贴(P)");
		editMenu_Paste.setIcon(new ImageIcon(MainFrame.class.getResource("/res/paste.png")));
		editMenu_Paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		editMenu_Paste.addActionListener(this);
		editMenu.add(editMenu_Paste);

		// 删除-菜单项
		editMenu_Delete = new JMenuItem("删除(D)");
		editMenu_Delete.setIcon(new ImageIcon(MainFrame.class.getResource("/res/delete.png")));
		editMenu_Delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		editMenu_Delete.addActionListener(this);
		editMenu.add(editMenu_Delete);

		// 分隔线
		editMenu.addSeparator();

		// 查找-菜单项
		editMenu_Find = new JMenuItem("查找(F)...");
		editMenu_Find.setIcon(new ImageIcon(MainFrame.class.getResource("/res/find.png")));
		editMenu_Find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		editMenu_Find.addActionListener(this);
		editMenu.add(editMenu_Find);

		// 查找下一个-菜单项
		editMenu_FindNext = new JMenuItem("查找下一个(N)");
		editMenu_FindNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		editMenu_FindNext.addActionListener(this);
		editMenu.add(editMenu_FindNext);

		// 替换-菜单项
		editMenu_Replace = new JMenuItem("替换(R)...", 'R');
		editMenu_Replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		editMenu_Replace.addActionListener(this);
		editMenu.add(editMenu_Replace);

		// 转到-菜单项
		editMenu_GoTo = new JMenuItem("转到(G)...", 'G');
		editMenu_GoTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		editMenu_GoTo.addActionListener(this);
		editMenu.add(editMenu_GoTo);

		// 分隔线
		editMenu.addSeparator();

		// 全选-菜单项
		editMenu_SelectAll = new JMenuItem("全选", 'A');
		editMenu_SelectAll.setIcon(new ImageIcon(MainFrame.class.getResource("/res/selectall.png")));
		editMenu_SelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		editMenu_SelectAll.addActionListener(this);
		editMenu.add(editMenu_SelectAll);

		// 时间/日期-菜单项
		editMenu_TimeDate = new JMenuItem("时间/日期(D)", 'D');
		editMenu_TimeDate.setIcon(new ImageIcon(MainFrame.class.getResource("/res/datetime.png")));
		editMenu_TimeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		editMenu_TimeDate.addActionListener(this);
		editMenu.add(editMenu_TimeDate);

		// 向菜单栏添加"编辑"菜单
		menuBar.add(editMenu);

		// 创建格式菜单及菜单项并注册事件监听
		formatMenu = new JMenu("格式(O)");
		// 设置快捷键ALT+O
		formatMenu.setMnemonic('O');

		// 自动换行-菜单项
		formatMenu_LineWrap = new JCheckBoxMenuItem("自动换行(W)");
		// 设置快捷键ALT+W
		formatMenu_LineWrap.setMnemonic('W');
		formatMenu_LineWrap.setState(true);
		formatMenu_LineWrap.addActionListener(this);
		formatMenu.add(formatMenu_LineWrap);

		// 字体-菜单项
		formatMenu_Font = new JMenuItem("字体(F)...");
		formatMenu_Font.setIcon(new ImageIcon(MainFrame.class.getResource("/res/font.png")));
		formatMenu_Font.addActionListener(this);
		formatMenu.add(formatMenu_Font);

		// 向菜单栏添加"格式"菜单
		menuBar.add(formatMenu);

		// 创建查看菜单及菜单项并注册事件监听
		viewMenu = new JMenu("查看(V)");
		// 设置快捷键ALT+V
		viewMenu.setMnemonic('V');

		// 状态栏-菜单项
		viewMenu_Status = new JCheckBoxMenuItem("状态栏(S)");
		viewMenu_Status.setMnemonic('S');// 设置快捷键ALT+S
		viewMenu_Status.setState(true);
		viewMenu_Status.addActionListener(this);
		viewMenu.add(viewMenu_Status);

		// 向菜单栏添加"查看"菜单
		menuBar.add(viewMenu);

		// 创建帮助菜单及菜单项并注册事件监听
		helpMenu = new JMenu("帮助(H)");
		// 设置快捷键ALT+H
		helpMenu.setMnemonic('H');

		// 帮助主题-菜单项
		helpMenu_HelpTopics = new JMenuItem("帮助主题(H)");
		helpMenu_HelpTopics.setIcon(new ImageIcon(MainFrame.class.getResource("/res/help.png")));
		helpMenu_HelpTopics.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMenu_HelpTopics.addActionListener(this);
		helpMenu.add(helpMenu_HelpTopics);

		// 分隔线
		helpMenu.addSeparator();

		// 关于记事本-菜单项
		helpMenu_AboutNotepad = new JMenuItem("关于记事本(A)");
		helpMenu_AboutNotepad.addActionListener(this);
		helpMenu.add(helpMenu_AboutNotepad);

		// 向菜单栏添加"帮助"菜单
		menuBar.add(helpMenu);

		// 向窗体添加菜单栏
		this.setJMenuBar(menuBar);

		// 创建文本编辑区并添加滚动条
		editArea = new JTextArea(20, 50);
		// 设置字体
		editArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		// 设置换行
		editArea.setWrapStyleWord(true);
		// 设置文本编辑区自动换行
		editArea.setLineWrap(true);
		// 添加鼠标监听
		editArea.addMouseListener(this);
		// 编辑区注册事件监听(与撤销操作有关)
		editArea.getDocument().addUndoableEditListener(this);
		editArea.getDocument().addDocumentListener(this);
		// 获取原文本编辑区的内容
		oldValue = editArea.getText();

		// 创建带滚动条的面板
		JScrollPane scroller = new JScrollPane(editArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// 向窗口添加文本编辑区
		this.add(scroller, BorderLayout.CENTER);

		// 创建右键弹出菜单
		popupMenu = new JPopupMenu();

		// 撤销-右键菜单项
		popupMenu_Undo = new JMenuItem("撤销(U)");
		popupMenu_Undo.setIcon(new ImageIcon(MainFrame.class.getResource("/res/undo.png")));
		popupMenu_Undo.setEnabled(false);
		popupMenu_Undo.addActionListener(this);
		popupMenu.add(popupMenu_Undo);

		// 分隔线
		popupMenu.addSeparator();

		// 剪切-右键菜单项
		popupMenu_Cut = new JMenuItem("剪切(T)");
		popupMenu_Cut.setIcon(new ImageIcon(MainFrame.class.getResource("/res/cut.png")));
		popupMenu_Cut.addActionListener(this);
		popupMenu.add(popupMenu_Cut);

		// 复制-右键菜单项
		popupMenu_Copy = new JMenuItem("复制(C)");
		popupMenu_Copy.setIcon(new ImageIcon(MainFrame.class.getResource("/res/copy.gif")));
		popupMenu_Copy.addActionListener(this);
		popupMenu.add(popupMenu_Copy);

		// 粘帖-右键菜单项
		popupMenu_Paste = new JMenuItem("粘帖(P)");
		popupMenu_Paste.setIcon(new ImageIcon(MainFrame.class.getResource("/res/paste.png")));
		popupMenu_Paste.addActionListener(this);
		popupMenu.add(popupMenu_Paste);

		// 删除-右键菜单项
		popupMenu_Delete = new JMenuItem("删除(D)");
		popupMenu_Delete.setIcon(new ImageIcon(MainFrame.class.getResource("/res/delete.png")));
		popupMenu_Delete.addActionListener(this);
		popupMenu.add(popupMenu_Delete);

		// 分隔线
		popupMenu.addSeparator();

		// 查找-右键菜单项
		popupMenu_Find = new JMenuItem("查找(F)...");
		popupMenu_Find.setIcon(new ImageIcon(MainFrame.class.getResource("/res/find.png")));
		popupMenu_Find.addActionListener(this);
		popupMenu.add(popupMenu_Find);

		// 分隔线
		popupMenu.addSeparator();

		// 全选-右键菜单项
		popupMenu_SelectAll = new JMenuItem("全选(A)");
		popupMenu_SelectAll.setIcon(new ImageIcon(MainFrame.class.getResource("/res/selectall.png")));
		popupMenu_SelectAll.addActionListener(this);
		popupMenu.add(popupMenu_SelectAll);

		// 创建状态栏
		statusLabel = new JLabel("按F1获取帮助");
		statusLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/res/help.png")));
		// 向窗口添加状态栏标签
		this.add(statusLabel, BorderLayout.SOUTH);

		validate();
		// 设置可见
		setVisible(true);
		// 文本编辑区获取光标
		editArea.requestFocus();
	}

	/**
	 * 检查菜单项的可用性：剪切，复制，粘帖，删除功能
	 */
	private void checkMenuItemEnabled()
	{
		// 获取选择的文本
		String selectText = editArea.getSelectedText();
		// 判断
		if (selectText == null)
		{
			// 为空则不可用
			editMenu_Cut.setEnabled(false);
			popupMenu_Cut.setEnabled(false);
			editMenu_Copy.setEnabled(false);
			popupMenu_Copy.setEnabled(false);
			editMenu_Delete.setEnabled(false);
			popupMenu_Delete.setEnabled(false);
		} else
		{
			// 不为空则可用
			editMenu_Cut.setEnabled(true);
			popupMenu_Cut.setEnabled(true);
			editMenu_Copy.setEnabled(true);
			popupMenu_Copy.setEnabled(true);
			editMenu_Delete.setEnabled(true);
			popupMenu_Delete.setEnabled(true);
		}

		// 粘帖功能可用性判断
		// 获取剪贴板内容
		Transferable contents = clipBoard.getContents(this);
		// 判断
		if (contents == null)
		{
			// 为空则不可用
			editMenu_Paste.setEnabled(false);
			popupMenu_Paste.setEnabled(false);
		} else
		{
			// 不为空则可用
			editMenu_Paste.setEnabled(true);
			popupMenu_Paste.setEnabled(true);
		}
	}

    //点击事件
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// 判断来源
		if (e.getSource() == fileMenu_New)
		{
			// 创建新文件
			createNewFile();
		} else if (e.getSource() == fileMenu_Open)
		{
			// 打开文件
			open();
		} else if (e.getSource() == fileMenu_Save)
		{
			// 如果是新文件
			if (isNewFile)
			{
				// 保存文件
				save();
			} else
			{
				// 否则直接写文件
				FileUtil.writeFile(currentFile, editArea.getText());
			}

		} else if (e.getSource() == fileMenu_SaveAs)
		{
			// 另存为
			saveAs();
		} else if (e.getSource() == fileMenu_PageSetUp)
		{
			// 页面设置
			// TODO...
			JOptionPane.showMessageDialog(this, "对不起，此功能尚未实现！", "提示", JOptionPane.WARNING_MESSAGE);
		} else if (e.getSource() == fileMenu_Print)
		{
			// 打印
			// TODO...
			JOptionPane.showMessageDialog(this, "对不起，此功能尚未实现！", "提示", JOptionPane.WARNING_MESSAGE);
		} else if (e.getSource() == fileMenu_Exit)
		{
			// 退出
			exit();
		} else if ((e.getSource() == editMenu_Undo) || (e.getSource() == popupMenu_Undo))
		{
			// 撤销操作
			undo();
		} else if ((e.getSource() == editMenu_Cut) || (e.getSource() == popupMenu_Cut))
		{
			// 剪切
			cut();
		} else if ((e.getSource() == editMenu_Copy) || (e.getSource() == popupMenu_Copy))
		{
			// 复制
			copy();
		} else if ((e.getSource() == editMenu_Paste) || (e.getSource() == popupMenu_Paste))
		{
			// 粘帖
			paste();
		} else if (e.getSource() == editMenu_Delete || e.getSource() == popupMenu_Delete)
		{
			// 删除
			delete();
		} else if ((e.getSource() == editMenu_Find) || (e.getSource() == editMenu_FindNext)
				|| (e.getSource() == popupMenu_Find))
		{
			// 查找
			find();
		} else if (e.getSource() == editMenu_Replace)
		{
			// 替换
			replace();
		} else if (e.getSource() == editMenu_GoTo)
		{
			// 转到
			JOptionPane.showMessageDialog(this, "对不起，此功能尚未实现！", "提示", JOptionPane.WARNING_MESSAGE);
		} else if (e.getSource() == editMenu_TimeDate)
		{
			// 时间日期
			getCurrentTime();
		} else if ((e.getSource() == editMenu_SelectAll) || (e.getSource() == popupMenu_SelectAll))
		{
			// 全选
			editArea.selectAll();
		} else if (e.getSource() == formatMenu_LineWrap)
		{
			// 自动换行
			setLineWrap();

		} else if (e.getSource() == formatMenu_Font)
		{
			// 文本编辑区获取光标
			editArea.requestFocus();
			// 字体设置
			setFont();
		} else if (e.getSource() == viewMenu_Status)
		{
			// 设置状态栏可见性
			showStatusLabel();

		} else if (e.getSource() == helpMenu_HelpTopics)
		{
			// 帮助主题
			showHelpDialog();
		}

		else if (e.getSource() == helpMenu_AboutNotepad)
		{
			// 关于
			showAboutDialog();
		}
	}

	/*
	 * 创建新文件
	 */
	private void createNewFile()
	{
		// 获取当前编辑区文字
		String currentValue = editArea.getText();
		// 如果文字没有改变过
		if (currentValue.equals(oldValue))
		{
			// 直接创建新文件
			newFile();
		} else
		{
			// 提示
			int saveChoose = JOptionPane.showConfirmDialog(this, "您的文件尚未保存，是否保存？", "提示",
					JOptionPane.YES_NO_CANCEL_OPTION);

			// 选择保存
			if (saveChoose == JOptionPane.YES_OPTION)
			{
				// 保存文件
				save();
			} else if (saveChoose == JOptionPane.NO_OPTION)
			{
				// 选择不保存则直接创建新文件
				newFile();
			} else
			{
				// 选择其它则退出方法
				return;
			}
		}
	}

	/*
	 * 保存
	 */
	private void save()
	{
		saveFile("保存");
	}

	/*
	 * 另存为
	 */
	private void saveAs()
	{
		saveFile("另存为");
	}

	/**
	 * 保存文件
	 */
	private void saveFile(String dialogTitle)
	{
		// 创建文件选择器
		JFileChooser fileChooser = new JFileChooser();
		// 设置文件选择模式
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// 设置标题
		fileChooser.setDialogTitle(dialogTitle);
		// 显示弹窗
		int result = fileChooser.showSaveDialog(this);
		// 选择取消
		if (result == JFileChooser.CANCEL_OPTION)
		{
			// 状态栏提示
			statusLabel.setText("您没有选择任何文件");
			isSave = false;
			// 退出方法
			return;
		}

		// 获取选择的文件
		File selectedFile = fileChooser.getSelectedFile();
		// 获取文件名
		String fileName = fileChooser.getName(selectedFile);

		// 判断
		if ((selectedFile == null) || (fileName.equals("")))
		{
			JOptionPane.showMessageDialog(this, "不合法的文件名", "不合法的文件名", JOptionPane.ERROR_MESSAGE);
			isSave = false;
			// 退出方法
			return;
		}

		// 若文件名缺少后缀名，默认TXT后缀
		if (fileName.indexOf(".") == -1)
		{
			selectedFile = new File(fileChooser.getCurrentDirectory(), fileName + ".txt");
		}

		// 写文件
		FileUtil.writeFile(selectedFile, editArea.getText());
		// 更改状态
		isNewFile = false;
		isSave = true;
		// 设置为当前文件
		currentFile = selectedFile;
		// 保存当前文字值
		oldValue = editArea.getText();
		// 更改标题
		this.setTitle(selectedFile.getName() + " - Java记事本");
		// 状态栏提示
		statusLabel.setText("当前打开文件：" + selectedFile.getAbsoluteFile());
		// 文本编辑区获取光标
		editArea.requestFocus();
	}

	/*
	 * 新文件
	 */
	private void newFile()
	{
		// 清空内容
		editArea.setText("");
		// 状态栏提示
		statusLabel.setText(" 新建文件");
		// 设置标题
		this.setTitle("无标题 - 记事本");
		// 更改状态
		isNewFile = true;
		// 撤消所有的"撤消"操作
		undoManager.discardAllEdits();
		// 撤消菜单项不可用
		editMenu_Undo.setEnabled(false);
		popupMenu_Undo.setEnabled(false);
		// 保存文字值
		oldValue = editArea.getText();
		// 文本编辑区获取光标
		editArea.requestFocus();
	}

	/*
	 * 打开文件
	 */
	private void open()
	{
		// 获取当前编辑区文字
		String currentValue = editArea.getText();
		// 如果文字没有改变过
		if (currentValue.equals(oldValue))
		{
			// 直接打开文件
			openFile();
		} else
		{
			// 提示
			int saveChoose = JOptionPane.showConfirmDialog(this, "您的文件尚未保存，是否保存？", "提示",
					JOptionPane.YES_NO_CANCEL_OPTION);

			// 选择保存
			if (saveChoose == JOptionPane.YES_OPTION)
			{
				// 保存文件
				save();
			} else if (saveChoose == JOptionPane.NO_OPTION)
			{
				// 选择不保存则直接打开文件
				openFile();
			} else
			{
				// 选择其它则退出方法
				return;
			}
		}
	}

	/*
	 * 打开文件
	 */
	private void openFile()
	{
		// 创建文件选择器
		JFileChooser fileChooser = new JFileChooser();
		// 设置文件选择模式
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// 设置标题
		fileChooser.setDialogTitle("打开文件");
		// 显示弹窗
		int result = fileChooser.showOpenDialog(this);
		// 选择取消
		if (result == JFileChooser.CANCEL_OPTION)
		{
			// 状态栏提示
			statusLabel.setText(" 您没有选择任何文件 ");
			// 退出方法
			return;
		}

		// 获取选择的文件
		File selectedFile = fileChooser.getSelectedFile();
		// 获取文件名
		String fileName = fileChooser.getName(selectedFile);

		// 判断
		if ((selectedFile == null) || (fileName.equals("")))
		{
			JOptionPane.showMessageDialog(this, "不合法的文件名", "不合法的文件名", JOptionPane.ERROR_MESSAGE);
			// 退出方法
			return;
		}

		// 读取文件
		editArea.setText(FileUtil.readFile(selectedFile));

		// 更改状态
		isNewFile = false;
		// 设置为当前文件
		currentFile = selectedFile;
		// 保存当前文字值
		oldValue = editArea.getText();
		// 更改标题
		this.setTitle(fileName + " - 记事本");
		// 状态栏提示
		statusLabel.setText(" 当前打开文件：" + selectedFile.getAbsoluteFile());
		// 文本编辑区获取光标
		editArea.requestFocus();
	}

	/*
	 * 撤销操作
	 */
	private void undo()
	{
		// 判断
		if (undoManager.canUndo())
		{
			// 如果可撤销
			try
			{
				undoManager.undo();
			} catch (CannotUndoException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "不可撤销！", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else
		{
			// 如果不可撤销
			editMenu_Undo.setEnabled(false);
		}

		// 检查
		checkMenuItemEnabled();
	}

	/*
	 * 剪切操作
	 */
	private void cut()
	{
		// 获取选择的文本
		String text = editArea.getSelectedText();
		// 创建能传输指定 String 的 Transferable
		StringSelection selection = new StringSelection(text);
		// 剪贴板添加文本
		clipBoard.setContents(selection, null);
		// 去掉剪切部分
		editArea.replaceRange("", editArea.getSelectionStart(), editArea.getSelectionEnd());
		// 检查剪切，复制，粘帖，删除功能的可用性
		checkMenuItemEnabled();
		// 文本编辑区获取光标
		editArea.requestFocus();
	}

	/*
	 * 复制操作
	 */
	private void copy()
	{
		// 获取选择的文本
		String text = editArea.getSelectedText();
		// 创建能传输指定 String 的 Transferable
		StringSelection selection = new StringSelection(text);
		// 剪贴板添加文本
		clipBoard.setContents(selection, null);
		// 检查剪切，复制，粘帖，删除功能的可用性
		checkMenuItemEnabled();
		// 文本编辑区获取光标
		editArea.requestFocus();
	}

	/*
	 * 粘贴操作
	 */
	private void paste()
	{
		// 文本编辑区获取光标
		editArea.requestFocus();
		// 获取剪贴板内容
		Transferable contents = clipBoard.getContents(this);
		// 判断
		if (contents == null)
		{
			// 若内容为空则退出方法
			return;
		}

		// 文字内容
		String text = "";
		try
		{
			// 获取剪贴板内容
			text = (String) contents.getTransferData(DataFlavor.stringFlavor);
			// 粘贴内容
			editArea.replaceRange(text, editArea.getSelectionStart(), editArea.getSelectionEnd());
			// 检查剪切，复制，粘帖，删除功能的可用性
			checkMenuItemEnabled();
		} catch (Exception e)
		{
			// 粘贴失败
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "粘贴失败！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/*
	 * 删除
	 */
	private void delete()
	{
		// 删除选择部分
		editArea.replaceRange("", editArea.getSelectionStart(), editArea.getSelectionEnd());
		// 检查剪切、复制、粘贴、删除等功能的可用性
		checkMenuItemEnabled();
		// 文本编辑区获取光标
		editArea.requestFocus();

	}

	/*
	 * 查找
	 */
	private void find()
	{
		FindDialog.getInstance(editArea);
	}

	/*
	 * 替换
	 */
	private void replace()
	{
		ReplaceDialog.getInstance(editArea);
	}

	/*
	 * 设置字体
	 */
	private void setFont()
	{
		FontDialog.getInstance(editArea);
		// 文本编辑区获取光标
		editArea.requestFocus();
	}

	/*
	 * 获取当前时间
	 */
	private void getCurrentTime()
	{
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		editArea.insert(date.toString(), editArea.getCaretPosition());
	}

	/*
	 * 设置换行
	 */
	private void setLineWrap()
	{
		if (formatMenu_LineWrap.getState())
		{
			editArea.setLineWrap(true);
		} else
		{
			editArea.setLineWrap(false);
		}
	}

	/*
	 * 显示状态栏
	 */
	private void showStatusLabel()
	{
		if (viewMenu_Status.getState())
		{
			statusLabel.setVisible(true);
		} else
		{
			statusLabel.setVisible(false);
		}
	}

	/*
	 * 显示帮助窗口
	 */
	private void showHelpDialog()
	{
		JOptionPane.showMessageDialog(this, "路漫漫其修远兮，吾将上下而求索。", "帮助主题", JOptionPane.INFORMATION_MESSAGE);
	}

	/*
	 * 显示关于窗口
	 */
	private void showAboutDialog()
	{
		String aboutMess = "JavaNotepad是一个用Java写的，仿Notepad的记事本软件。\r\n@author： ordinary-student\r\n@version： 1.0\r\n@update time： 2019-06-22";
		JOptionPane.showMessageDialog(this, aboutMess, "About-JavaNotepad", JOptionPane.INFORMATION_MESSAGE);
	}

	/*
	 * 窗口退出
	 */
	private void exit()
	{
		// 获取当前值
		String currentValue = editArea.getText();
		// 判断
		if (currentValue.equals(oldValue))
		{
			// 如果新值等于旧值，退出
			System.exit(0);
		} else
		{
			// 否则，尚未保存
			// 提示
			int saveChoose = JOptionPane.showConfirmDialog(this, "您的文件尚未保存，是否保存？", "提示",
					JOptionPane.YES_NO_CANCEL_OPTION);

			// 选择保存
			if (saveChoose == JOptionPane.YES_OPTION)
			{
				// 保存标志
				isSave = false;
				// 如果是新文件
				if (isNewFile)
				{
					// 保存文件
					save();

				} else
				{
					// 否则直接写文件
					FileUtil.writeFile(currentFile, editArea.getText());
					isSave = true;
				}

				// 成功保存了文件才能退出
				if (isSave)
				{
					System.exit(0);
				} else
				{
					return;
				}
			} else if (saveChoose == JOptionPane.NO_OPTION)
			{
				// 选择不保存则直接退出
				System.exit(0);
			} else
			{
				// 选择其它则退出方法，返回编辑窗口
				return;
			}
		}
	}

	@Override
	public void menuCanceled(MenuEvent e)
	{
		// 取消菜单时调用
		// 检查剪切、复制、粘贴、删除等功能的可用性
		checkMenuItemEnabled();
	}

	@Override
	public void menuDeselected(MenuEvent e)
	{
		// 取消选择某个菜单时调用
		// 检查剪切、复制、粘贴、删除等功能的可用性
		checkMenuItemEnabled();
	}

	@Override
	public void menuSelected(MenuEvent e)
	{
		// 选择某个菜单时调用
		// 检查剪切、复制、粘贴、删除等功能的可用性
		checkMenuItemEnabled();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// 判断此鼠标事件是否为该平台的弹出菜单触发事件
		if (e.isPopupTrigger())
		{
			// 在组件调用者的坐标空间中的位置 X、Y 显示弹出菜单
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}

		// 检查剪切，复制，粘帖，删除等功能的可用性
		checkMenuItemEnabled();
		// 编辑区获取焦点
		editArea.requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// 判断此鼠标事件是否为该平台的弹出菜单触发事件
		if (e.isPopupTrigger())
		{
			// 在组件调用者的坐标空间中的位置 X、Y 显示弹出菜单
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}

		// 检查剪切，复制，粘帖，删除等功能的可用性
		checkMenuItemEnabled();
		// 编辑区获取焦点
		editArea.requestFocus();
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		// 移除文字时
		editMenu_Undo.setEnabled(true);
		popupMenu_Undo.setEnabled(true);
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		// 插入文字时
		editMenu_Undo.setEnabled(true);
		popupMenu_Undo.setEnabled(true);
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		// 改变文字时
		editMenu_Undo.setEnabled(true);
		popupMenu_Undo.setEnabled(true);
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent uee)
	{
		// 撤销操作，恢复内容
		undoManager.addEdit(uee.getEdit());
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		// 窗口关闭时
		exit();
	}

}
