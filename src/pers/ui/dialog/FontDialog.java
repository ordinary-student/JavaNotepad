package pers.ui.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;

import pers.ui.abs.AbstractDialog;

/**
 * 字体设置窗口类
 * 
 * @author ordinary-student
 *
 */
public class FontDialog extends AbstractDialog
{
	private static final long serialVersionUID = -8029834279745628826L;
	// 实例
	private static FontDialog INSTANCE;
	// 获取系统支持的字体
	private static String[] FONT_NAME;
	private static final int[] STYLE = { Font.PLAIN, Font.BOLD, Font.ITALIC, (Font.BOLD + Font.ITALIC) };
	private static final String[] FONT_STYLE = { "常规", "粗体", "斜体", "粗斜体" };
	private static final String[] FONT_SIZE = { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26",
			"28", "36", "48", "72" };
	private JTextArea editArea;
	private JTextField fontTextField;
	private JTextField styleTextField;
	private JTextField sizeTextField;

	private JList<String> fontList;
	private JList<String> styleList;
	private JList<String> sizeList;

	private JLabel sampleLabel;

	private JButton okButton;
	private JButton cancelButton;

	static
	{
		// 图形环境
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		// 获取系统支持的字体
		FONT_NAME = ge.getAvailableFontFamilyNames();
	}

	/*
	 * 构造方法
	 */
	private FontDialog(JTextArea editArea)
	{
		this.editArea = editArea;
		// 初始化界面
		initUI();
	}

	/*
	 * 获取实例对象
	 */
	public static FontDialog getInstance(JTextArea editArea)
	{
		// 为空
		if (INSTANCE == null)
		{
			INSTANCE = new FontDialog(editArea);
		}

		// 返回实例对象
		return INSTANCE;
	}

	/*
	 * 初始化界面
	 */
	private void initUI()
	{
		// 设置标题
		setTitle("字体设置");
		// 设置位置
		setLocationRelativeTo(editArea);
		// 设置大小
		setSize(360, 380);
		// 阻塞父窗体
		setModal(true);
		// 设置不可调整大小
		setResizable(false);
		// 设置左对齐的流式布局
		setLayout(new FlowLayout(FlowLayout.CENTER));
		// 设置关闭方式
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// 创建面板1
		JPanel firstPanel = new JPanel();

		// 标签
		JLabel fontLabel = new JLabel("字体(F)：");
		fontLabel.setPreferredSize(new Dimension(100, 20));
		firstPanel.add(fontLabel);

		JLabel styleLabel = new JLabel("字形(Y)：");
		styleLabel.setPreferredSize(new Dimension(100, 20));
		firstPanel.add(styleLabel);

		JLabel sizeLabel = new JLabel("大小(S)：");
		sizeLabel.setPreferredSize(new Dimension(100, 20));
		firstPanel.add(sizeLabel);

		// 添加面板1
		getContentPane().add(firstPanel);

		// 创建面板2
		JPanel secondPanel = new JPanel();

		// 输入框
		fontTextField = new JTextField();
		fontTextField.setPreferredSize(new Dimension(100, 20));
		fontTextField.setEditable(false);
		secondPanel.add(fontTextField);

		styleTextField = new JTextField();
		styleTextField.setPreferredSize(new Dimension(100, 20));
		styleTextField.setEditable(false);
		secondPanel.add(styleTextField);

		sizeTextField = new JTextField();
		sizeTextField.setPreferredSize(new Dimension(100, 20));
		sizeTextField.setEditable(false);
		secondPanel.add(sizeTextField);

		// 添加面板2
		getContentPane().add(secondPanel);

		// 创建面板3
		JPanel thirdPanel = new JPanel();

		// 创建字体名称列表
		fontList = new JList<String>(FONT_NAME);
		// 设置宽高
		fontList.setFixedCellWidth(100);
		fontList.setFixedCellHeight(20);
		// 设置单选
		fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fontList.addListSelectionListener(this);

		// 滚动面板
		JScrollPane fontListScrollPane = new JScrollPane(fontList);
		fontListScrollPane.setPreferredSize(new Dimension(100, 150));
		thirdPanel.add(fontListScrollPane);

		// 字形列表
		styleList = new JList<String>(FONT_STYLE);
		// 设置宽高
		styleList.setFixedCellWidth(100);
		styleList.setFixedCellHeight(20);
		// 设置单选
		styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		styleList.addListSelectionListener(this);

		// 滚动面板
		JScrollPane styleListScrollPane = new JScrollPane(styleList);
		styleListScrollPane.setPreferredSize(new Dimension(100, 150));
		thirdPanel.add(styleListScrollPane);

		// 字体大小列表
		sizeList = new JList<String>(FONT_SIZE);
		// 设置宽高
		sizeList.setFixedCellWidth(50);
		sizeList.setFixedCellHeight(20);
		// 设置单选
		sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sizeList.addListSelectionListener(this);

		// 滚动面板
		JScrollPane sizeListScrollPane = new JScrollPane(sizeList);
		sizeListScrollPane.setPreferredSize(new Dimension(100, 150));
		thirdPanel.add(sizeListScrollPane);

		// 添加面板3
		getContentPane().add(thirdPanel);

		// 样例面板
		JPanel samplePanel = new JPanel();
		samplePanel.setPreferredSize(new Dimension(320, 65));
		samplePanel.setBorder(BorderFactory.createTitledBorder("示例"));
		samplePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		// 样例标签
		sampleLabel = new JLabel("这是一个样例！ JavaNotepad");
		sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		samplePanel.add(sampleLabel);

		// 添加样例面板
		getContentPane().add(samplePanel);

		// 按钮面板
		JPanel buttonPanel = new JPanel();

		// 按钮
		okButton = new JButton("确定");
		okButton.setPreferredSize(new Dimension(80, 30));
		okButton.setFocusPainted(false);
		okButton.addActionListener(this);
		buttonPanel.add(okButton);

		cancelButton = new JButton("取消");
		cancelButton.setPreferredSize(new Dimension(80, 30));
		cancelButton.setFocusPainted(false);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);

		// 添加按钮面板
		getContentPane().add(buttonPanel);

		// 获取当前字体
		getCurrentFont();
		validate();
		// 设置可视
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// 判断来源
		if (e.getSource() == okButton)
		{
			// 设置字体
			Font font = new Font(fontTextField.getText(), STYLE[styleList.getSelectedIndex()],
					Integer.parseInt(sizeTextField.getText()));
			editArea.setFont(font);
			// 关闭
			this.dispose();
			INSTANCE = null;

		} else if (e.getSource() == cancelButton)
		{
			// 关闭
			this.dispose();
			INSTANCE = null;
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent event)
	{
		// 判断来源
		if (event.getSource() == fontList)
		{
			// 改变字体
			changeFont();
		} else if (event.getSource() == styleList)
		{
			// 改变字形
			changeStyle();
		} else if (event.getSource() == sizeList)
		{
			// 改变字体大小
			changeSize();
		}
	}

	/*
	 * 改变字体
	 */
	private void changeFont()
	{
		// 设置字体
		fontTextField.setText(FONT_NAME[fontList.getSelectedIndex()]);
		// 改变样例
		Font sampleFont = new Font(fontTextField.getText(), STYLE[styleList.getSelectedIndex()],
				Integer.parseInt(sizeTextField.getText()));
		sampleLabel.setFont(sampleFont);
	}

	/*
	 * 改变字形
	 */
	private void changeStyle()
	{
		// 设置字形
		int style = STYLE[styleList.getSelectedIndex()];
		styleTextField.setText(FONT_STYLE[style]);

		// 改变样例
		Font sampleFont = new Font(fontTextField.getText(), STYLE[styleList.getSelectedIndex()],
				Integer.parseInt(sizeTextField.getText()));
		sampleLabel.setFont(sampleFont);
	}

	/*
	 * 改变字体大小
	 */
	private void changeSize()
	{
		// 设置字体大小
		sizeTextField.setText(FONT_SIZE[sizeList.getSelectedIndex()]);
		// 改变样例
		Font sampleFont = new Font(fontTextField.getText(), STYLE[styleList.getSelectedIndex()],
				Integer.parseInt(sizeTextField.getText()));
		sampleLabel.setFont(sampleFont);
	}

	/*
	 * 获取当前字体
	 */
	private void getCurrentFont()
	{
		// 获取当前字体
		Font currentFont = editArea.getFont();
		// 显示当前字体名字
		fontTextField.setText(currentFont.getFontName());

		// 获取字形
		int fontStyle = currentFont.getStyle();
		// 判断
		if (fontStyle == Font.PLAIN)
		{
			styleTextField.setText("常规");
			styleList.setSelectedIndex(0);
		} else if (fontStyle == Font.BOLD)
		{
			styleTextField.setText("粗体");
			styleList.setSelectedIndex(1);
		} else if (fontStyle == Font.ITALIC)
		{
			styleTextField.setText("斜体");
			styleList.setSelectedIndex(2);
		} else if (fontStyle == (Font.BOLD + Font.ITALIC))
		{
			styleTextField.setText("粗斜体");
			styleList.setSelectedIndex(3);
		}

		// 获取字体大小
		String fontSize = String.valueOf(currentFont.getSize());
		sizeTextField.setText(fontSize);
		// 获取数据模型
		ListModel<String> listModel = sizeList.getModel();
		// 单项的值
		String value = null;
		// 遍历
		for (int i = 0; i < listModel.getSize(); i++)
		{
			// 获取每一项的值
			value = listModel.getElementAt(i);
			// 比较
			if (fontSize.equals(value))
			{
				// 相同就选中它
				sizeList.setSelectedIndex(i);
				break;
			}
		}
	}
}
