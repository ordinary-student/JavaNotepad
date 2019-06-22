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
 * 查找窗口类
 * 
 * @author ordinary-student
 *
 */
public class FindDialog extends AbstractDialog
{
	private static final long serialVersionUID = 2045961146194046134L;
	private static FindDialog INSTANCE;
	private JTextField findTextField;
	private JButton findNextButton;
	private JButton cancelButton;
	private JCheckBox matchCheckBox;
	private JRadioButton upButton;
	private JRadioButton downButton;
	private JTextArea editArea;

	/*
	 * 构造方法
	 */
	private FindDialog(JTextArea editArea)
	{
		this.editArea = editArea;
		// 初始化界面
		initUI();
	}

	/*
	 * 获取实例对象
	 */
	public static FindDialog getInstance(JTextArea editArea)
	{
		// 为空
		if (INSTANCE == null)
		{
			INSTANCE = new FindDialog(editArea);
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
		setTitle("查找");
		// 设置位置
		setLocationRelativeTo(editArea);
		// 设置大小
		setSize(450, 200);
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

		// 按钮面板
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));

		// 查找下一个-按钮
		findNextButton = new JButton("查找下一个(F)");
		findNextButton.setFocusPainted(false);
		findNextButton.addActionListener(this);
		buttonPanel.add(findNextButton);
		// 取消-按钮
		cancelButton = new JButton("取消");
		cancelButton.setFocusPainted(false);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);

		// 添加按钮面板
		firstPanel.add(buttonPanel);

		// 添加面板1
		getContentPane().add(firstPanel);

		// 创建面板2
		JPanel secondPanel = new JPanel();

		// 勾选框
		matchCheckBox = new JCheckBox("区分大小写(C)");
		matchCheckBox.setFocusPainted(false);
		secondPanel.add(matchCheckBox);

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
		secondPanel.add(directionPanel);

		// 添加面板2
		getContentPane().add(secondPanel);

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
}
