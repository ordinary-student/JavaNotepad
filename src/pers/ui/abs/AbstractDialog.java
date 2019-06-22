package pers.ui.abs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 抽象Dialog类
 * 
 * @author ordinary-student
 *
 */
public abstract class AbstractDialog extends JDialog implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = -7284288302048136457L;

	@Override
	public void actionPerformed(ActionEvent e)
	{
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
	}

}
