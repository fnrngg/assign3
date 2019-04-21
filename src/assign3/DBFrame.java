package assign3;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

public class DBFrame extends JFrame{
	private DatabaseTableModel model;
	private JComponent content;
	private JButton add;
	private JButton search;
	private JTextField metroField;
	private JTextField continentField;
	private JTextField populationField;
	private DBConnect database;
	private JComboBox<String> populationOptions;
	private JComboBox<String> matchTypes;
	private JTable table;
	private ArrayList<MetropolisInfo> metropolises;
	private JScrollPane scrol;
	
	public DBFrame() {
		content = (JComponent)getContentPane();
		content.setLayout(new BorderLayout(4, 4));
		content.setVisible(true);


		metropolises = new ArrayList<MetropolisInfo>();
		database = new DBConnect(metropolises);
		database.getFullData();
		createInputs();
		
		model = new DatabaseTableModel(metropolises);
		createTableSearchAndAdd();

		
		model.fireTableDataChanged();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void createInputs() {
		JPanel inputs = new JPanel();
		inputs.setVisible(true);
		JLabel metro = new JLabel("Metropolis:");
		metroField = new JTextField(15);
		continentField = new JTextField(15);
		JLabel continent = new JLabel("Continent:");
		populationField = new JTextField(15);
		JLabel population = new JLabel("Population:");
		inputs.add(metro);
		inputs.add(metroField);
		inputs.add(continent);
		inputs.add(continentField);
		inputs.add(population);
		inputs.add(populationField);
		content.add(BorderLayout.NORTH, inputs);
	}
	
	private void createTableSearchAndAdd() {
		table = new JTable(model);
		scrol = new JScrollPane(table);
		content.add(BorderLayout.CENTER, scrol);
		model.fireTableDataChanged();
		
		
		JPanel right = new JPanel();
		right.setLayout(new FlowLayout());
		
		add = new JButton("Add");
		right.add(add);
		
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String metropolis = metroField.getText();
				String continent = continentField.getText();
				String populationInStr = populationField.getText();
				int population;
				if(populationInStr.equals("")) {
					population = 0;
				} else {
					population = Integer.parseInt(populationInStr);
				}
				if(!(metropolis.equals("") && continent.equals("") && population == 0)) {
					MetropolisInfo newMetro = new MetropolisInfo(metropolis, continent, population);
					database.add(newMetro);
					System.out.println(metropolises.size());
					model.fireTableDataChanged();
				}
			}
		});
		search = new JButton("Search");
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String metropolis = metroField.getText();
				String continent = continentField.getText();
				String populationInStr = populationField.getText();
				int population;
				if(populationInStr.equals("")) {
					population = 0;
				} else {
					population = Integer.parseInt(populationInStr);
				}
				String match = (String) matchTypes.getSelectedItem();
				boolean isPartial = match.equals("Partial Match");
				String populationOption = (String) populationOptions.getSelectedItem();
				if(metropolis.equals("") && continent.equals("") && population == 0 && isPartial) {
					database.getFullData();
				} else {
					MetropolisInfo newMetro = new MetropolisInfo(metropolis, continent, population);
					database.search(newMetro, isPartial, populationOption);
					model.fireTableDataChanged();
				}
			}
		});
		right.add(search);
		
		JPanel options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		options.setBorder(new TitledBorder("Search Options"));
		populationOptions = new JComboBox<String>();
		populationOptions.addItem("Population Larger Than");
		populationOptions.addItem("Population Smaller Than");
		populationOptions.addItem("Population Equal To");
		options.add(populationOptions);
		
		matchTypes = new JComboBox<String>();
		matchTypes.addItem("Exact Match");
		matchTypes.addItem("Partial Match");
		options.add(matchTypes);
		right.add(options);
		
		content.add(BorderLayout.EAST, right);		
	}
	
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		DBFrame data = new DBFrame();
		data.setSize(600, 600);
		
	}
}
