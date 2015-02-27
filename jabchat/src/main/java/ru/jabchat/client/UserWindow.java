package ru.jabchat.client;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ru.jabchat.server.model.UserModel;

public class UserWindow {

	private TableModel tModel;
	private Table table;
	private JScrollPane pane;
	private ArrayList data;

	
	public UserWindow() {
		this.data = new ArrayList<UserModel>();
		this.tModel = new TableModel(data);
		this.table = new Table(tModel);
		this.pane = new JScrollPane(table);
		setColumnSize();
	}

	public JScrollPane getWindow() {
		return pane;
	}

	public void refreshTable(List dataIn) {
		this.data.clear();
		this.data.addAll(dataIn);
		tModel.fireTableDataChanged();
	}
	
	private void setColumnSize(){
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.table.setPreferredScrollableViewportSize(new Dimension(360, 0));
		this.table.setFillsViewportHeight(true);
		
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(JLabel.RIGHT);

		this.table.getColumn("Цвет").setMinWidth(40);
		this.table.getColumn("Цвет").setMaxWidth(40);
		
		this.table.getColumn("Пользователи").setMinWidth(180);
		this.table.getColumn("Пользователи").setMaxWidth(180);
		
		this.table.getColumn("ip").setMinWidth(90);
		this.table.getColumn("ip").setMaxWidth(90);
		this.table.getColumn("ip").setCellRenderer(center);
		
		this.table.getColumn("Статус").setMaxWidth(50);
		this.table.getColumn("Статус").setMaxWidth(50);
		this.table.getColumn("Статус").setCellRenderer(center);
		
	}
	
}