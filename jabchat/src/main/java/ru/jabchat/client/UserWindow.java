package ru.jabchat.client;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ru.jabchat.server.model.UserModel;

public class UserWindow {

	private TableModel  tModel;
	private JTable      table;
	private JScrollPane pane;
	
	public UserWindow(List<UserModel> data){
		
		this.tModel = new TableModel(data);
		this.table 	= new JTable(tModel);
        this.pane 	= new JScrollPane(table);
	}
	
	public JScrollPane getWindow(){
		return pane;
	}
	
	public void refreshTable(){
		tModel.fireTableDataChanged();
	}
}
