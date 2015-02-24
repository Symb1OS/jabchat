package ru.jabchat.client;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
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
		return 4;
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
	    	    result = "Цвет";
		        break;
		    case 1:
		        result = "Пользователи";
		        break;
		    case 2:
		        result = "Статус";
		        break;
		    case 3:
		        result = "ip";
		        break;
		}
		return result;
    }
	
	@Override
	public Object getValueAt(int row, int cell) {
		
		switch (cell){
		case 0 : 
			JLabel color = new JLabel();
			color.setOpaque(true);
			color.setBackground( new Color( data.get(row).getColor() ) );
			return color;
		case 1 :
			return new JLabel( "  "+data.get(row).getUserName());
		case 2 :
			JLabel status = new JLabel( data.get(row).getStatus() );
			status.setBackground( Color.WHITE);
			status.setOpaque(true);
			if (data.get(row).getStatus().equals("on"))
				status.setForeground( new Color( -16751104 ) );
			else
				status.setForeground( new Color( -6750157 ));
			return status;
			
			//return new JLabel( data.get(row).getStatus() );
		case 3 :
			return new JLabel( data.get(row).getIp() );
		default : 
			return new JLabel();
		}
	}
	
//	@Override
//	public Object getValueAt(int row, int cell) {
//		switch (cell){
//		case 0 : 
//			return data.get(row).getUserName();
//		case 1 :
//			return data.get(row).getIp();
//		case 2 :
//			return data.get(row).getStatus();
//		default : 
//			return "";
//	}

}
