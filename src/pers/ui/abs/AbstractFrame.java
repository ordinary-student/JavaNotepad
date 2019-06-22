package pers.ui.abs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/**
 * 抽象窗口类
 * 
 * @author ordinary-student
 *
 */
public abstract class AbstractFrame extends JFrame
		implements ActionListener, DocumentListener, MenuListener, MouseListener, UndoableEditListener, WindowListener
{
	private static final long serialVersionUID = 7552665022795133674L;

	@Override
	public void actionPerformed(ActionEvent e)
	{
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
	}

	@Override
	public void menuSelected(MenuEvent e)
	{
	}

	@Override
	public void menuDeselected(MenuEvent e)
	{
	}

	@Override
	public void menuCanceled(MenuEvent e)
	{
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e)
	{
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

}
