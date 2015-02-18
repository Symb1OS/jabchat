package ru.jabchat.client;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import ru.jabchat.server.model.UserModel;

public class TableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	public List<UserModel> data;
	
	public TableModel(List<UserModel> data){
		super();
		this.data = data;
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
    public String getColumnName(int c) {
		String result = "";
		switch (c) {
		    case 0:
		        result = "Пользователи";
		        break;
		    case 1:
		        result = "ip";
		        break;
		    case 2:
		        result = "Статус";
		        break;
		}
		return result;
    }
	

	
	@Override
	public Object getValueAt(int row, int cell) {
		switch (cell){
		case 0 : 
			return data.get(row).getUserName();
		case 1 :
			return data.get(row).getIp();
		case 2 :
			return data.get(row).getStatus();
		default : 
			return "";
	}
	}

}
