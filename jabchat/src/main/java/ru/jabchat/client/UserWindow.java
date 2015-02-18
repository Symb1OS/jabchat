package ru.jabchat.client;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ru.jabchat.server.model.UserModel;

public class UserWindow {

	private TableModel tModel;
	private JTable table;
	private JScrollPane pane;
	private ArrayList data;

	
	public UserWindow() {
		this.data = new ArrayList<UserModel>();
		this.tModel = new TableModel(data);
		this.table = new JTable(tModel);
		this.pane = new JScrollPane(table);
	}

	public JScrollPane getWindow() {
		return pane;
	}

	public void refreshTable(List dataIn) {
		this.data.clear();
		this.data.addAll(dataIn);
		tModel.fireTableDataChanged();
	}
}
