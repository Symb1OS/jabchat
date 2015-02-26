package ru.jabchat.client;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


public class Table  extends JTable implements TableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	public Table(TableModel tableModel) {
		setModel(tableModel);
	}

	public TableCellRenderer getCellRenderer(int row, int col) {
		return this;
	}

	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		return (JLabel) arg1;
	}

}
