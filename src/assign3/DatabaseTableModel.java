package assign3;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class DatabaseTableModel extends AbstractTableModel {

	private ArrayList<MetropolisInfo> metropolises;
	
	public DatabaseTableModel(ArrayList<MetropolisInfo> metropolies) {
		this.metropolises = metropolies;
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return metropolises.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		if(arg1 == 0) {
			return metropolises.get(arg0).getMetropolis();
		} else if(arg1 == 1) {
			return metropolises.get(arg0).getContinent();
		} else if(arg1 == 2) {
			return metropolises.get(arg0).getPopulation();
		}
		return null;
	}

}
