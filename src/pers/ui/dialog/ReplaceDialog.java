package pers.ui.dialog;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pers.ui.abs.AbstractDialog;

/**
 * 替换窗口类
 * 
 * @author ordinary-student
 *
 */
public class ReplaceDialog extends AbstractDialog
{
	private static final long serialVersionUID = -30138942255188631L;
	private static ReplaceDialog INSTANCE;
	private JTextField findTextField;
	private JButton findNextButton;
	private JButton cancelButton;
	private JCheckBox matchCheckBox;
	private JRadioButton upButton;
	private JRadioButton downButton;
	private JButton replaceButton;
	private JButton replaceAllButton;
	private JTextField replaceTextField;
	private JTextArea editArea;

	/*
	 * 构造方法
	 */
	private ReplaceDialog(JTextArea editArea)
	{
		this.editArea = editArea;
		// 初始化界面
		initUI();
	}

	/*
	 * 获取实例对象
	 */
	public static ReplaceDialog getInstance(JTextArea editArea)
	{
		// 为空
		if (INSTANCE == null)
		{
			INSTANCE = new ReplaceDialog(editArea);
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
		setTitle("替换");
		// 设置位置
		setLocationRelativeTo(editArea);
		// 设置大小
		setSize(450, 220);
		// 设置不可调整大小
		setResizable(false);
		// 设置左对齐的流式布局
		setLayout(new FlowLayout(FlowLayout.CENTER));
		// 设置关闭方式
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// 创建面板1
		JPanel firstPanel = new JPanel();

		// 标签
		JLabel findContentLabel = new JLabel("查找内容(N)：");
		firstPanel.add(findContentLabel);

		// 输入框
		findTextField = new JTextField(20);
		findTextField.setText(editArea.getSelectedText());
		firstPanel.add(findTextField);

		// 查找下一个-按钮
		findNextButton = new JButton("查找下一个(F)");
		findNextButton.setFocusPainted(false);
		findNextButton.addActionListener(this);
		firstPanel.add(findNextButton);

		// 添加面板1
		getContentPane().add(firstPanel);

		// 创建面板2
		JPanel secondPanel = new JPanel();

		// 标签
		JLabel replaceContentLabel = new JLabel("替换为(P)：");
		secondPanel.add(replaceContentLabel);

		// 输入框
		replaceTextField = new JTextField(20);
		secondPanel.add(replaceTextField);

		// 按钮面板
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));

		// 按钮
		replaceButton = new JButton("替换(R)");
		replaceButton.setFocusPainted(false);
		replaceButton.addActionListener(this);
		buttonPanel.add(replaceButton);

		replaceAllButton = new JButton("全部替换(A)");
		replaceAllButton.setFocusPainted(false);
		replaceAllButton.addActionListener(this);
		buttonPanel.add(replaceAllButton);

		// 添加按钮面板
		secondPanel.add(buttonPanel);

		// 添加面板2
		getContentPane().add(secondPanel);

		// 创建面板3
		JPanel thirdPanel = new JPanel();

		// 勾选框
		matchCheckBox = new JCheckBox("区分大小写(C)");
		matchCheckBox.setFocusPainted(false);
		thirdPanel.add(matchCheckBox);

		// 方向面板
		JPanel directionPanel = new JPanel();
		directionPanel.setBorder(BorderFactory.createTitledBorder("方向"));

		// 按钮组
		ButtonGroup buttonGroup = new ButtonGroup();
		// 单选按钮
		upButton = new JRadioButton("向上(U)");
		upButton.setFocusPainted(false);
		buttonGroup.add(upButton);
		directionPanel.add(upButton);

		downButton = new JRadioButton("向下(U)", true);
		downButton.setFocusPainted(false);
		buttonGroup.add(downButton);
		directionPanel.add(downButton);

		// 添加方向面板
		thirdPanel.add(directionPanel);

		// 取消-按钮
		cancelButton = new JButton("取消");
		cancelButton.setFocusPainted(false);
		cancelButton.addActionListener(this);
		thirdPanel.add(cancelButton);

		// 添加面板3
		getContentPane().add(thirdPanel);

		validate();
		// 设置可视
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// 判断来源
		if (e.getSource() == findNextButton)
		{
			// 查找下一个
			findNext();
		} else if (e.getSource() == replaceButton)
		{
			// 替换
			replace();
		} else if (e.getSource() == replaceAllButton)
		{
			// 替换所有
			replaceAll();
		} else if (e.getSource() == cancelButton)
		{
			// 关闭
			this.dispose();
			INSTANCE = null;
		}

	}

	/*
	 * 查找下一个
	 */
	private void findNext()
	{
		// 索引
		int index = 0;

		// 文本编辑区的内容
		String editAreaContent = editArea.getText();
		// 待查找的字符串
		String stringOfSearched = findTextField.getText();

		// 区分大小写
		if (matchCheckBox.isSelected())
		{

		} else// 不区分大小写,此时把所选内容全部化成大写，以便于查找
		{
			editAreaContent = editAreaContent.toUpperCase();
			stringOfSearched = stringOfSearched.toUpperCase();
		}

		// 向上查找
		if (upButton.isSelected())
		{
			// 判断索引
			if (editArea.getSelectedText() == null)
			{
				index = editAreaContent.lastIndexOf(stringOfSearched, editArea.getCaretPosition() - 1);
			} else
			{
				index = editAreaContent.lastIndexOf(stringOfSearched,
						editArea.getCaretPosition() - findTextField.getText().length() - 1);
			}

			// 有结果
			if (index > -1)
			{
				// 跳到结果处
				editArea.setCaretPosition(index);
				// 选择结果
				editArea.select(index, index + stringOfSearched.length());
			} else
			{
				// 没结果
				JOptionPane.showMessageDialog(this, "找不到您要查找的内容！", "查找", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		// 向下查找
		else if (downButton.isSelected())
		{
			// 判断索引
			if (editArea.getSelectedText() == null)
			{
				index = editAreaContent.indexOf(stringOfSearched, editArea.getCaretPosition() + 1);
			} else
			{
				index = editAreaContent.indexOf(stringOfSearched,
						editArea.getCaretPosition() - findTextField.getText().length() + 1);
			}

			// 有结果
			if (index > -1)
			{
				// 跳到结果处
				editArea.setCaretPosition(index);
				// 选择结果
				editArea.select(index, index + stringOfSearched.length());
			} else
			{
				// 没结果
				JOptionPane.showMessageDialog(this, "找不到您查找的内容！", "查找", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	/*
	 * 替换
	 */
	private void replace()
	{
		// 替换前的文本
		String selectedText = editArea.getSelectedText();
		// 替换后的文本
		String replaceText = replaceTextField.getText();
		// 长度
		int length = replaceText.length();
		// 判断
		if (selectedText != null)
		{
			// 替换前的文本不为空
			// 替换后的文本为空
			if (length == 0)
			{
				// 替换为空
				editArea.replaceSelection("");
			} else if (length > 0)
			{
				// 替换后的文本不为空则替换
				editArea.replaceSelection(replaceTextField.getText());
			}
		} else
		{
			return;
		}

	}

	/*
	 * 替换所有
	 */
	private void replaceAll()
	{
		// 替换前的文本
		String selectedText = editArea.getSelectedText();
		// 全部文本
		String allText = editArea.getText();
		// 替换后的文本
		String replaceText = replaceTextField.getText();
		// 长度
		int length = replaceText.length();
		// 判断
		if (selectedText != null)
		{
			// 替换前的文本不为空
			// 替换后的文本为空
			if (length == 0)
			{
				// 替换为空
				allText.replaceAll(selectedText, "");
			} else if (length > 0)
			{
				// 替换后的文本不为空则替换
				allText.replaceAll(selectedText, replaceText);
			}

			// 将内容写回编辑区
			editArea.setText(allText);
		} else
		{
			return;
		}

	}

}
