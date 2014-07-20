import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class GenConfigGUI {

	public JFrame frame;
	private JTextField urlTextField;
	private MainProgram mc;
	private JComboBox hourCombo;
	private JComboBox minCombo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			GenConfigGUI window = new GenConfigGUI(null, false);
			window.frame.setVisible(true);
			//window.frame.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 */
	public GenConfigGUI(MainProgram mc, boolean isInitData) {
		this.mc = mc;
		initialize();
		if(isInitData){
			initData();
		}
	}
	
	public void initData(){
		this.urlTextField.setText(this.mc.getConfig().url);
		this.hourCombo.setSelectedIndex(this.mc.getConfig().runTimeHour);
		
		this.minCombo.setSelectedIndex(this.mc.getConfig().runTimeMin/5);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 358, 204);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("URL");
		lblNewLabel.setBounds(37, 25, 61, 16);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Run Time");
		lblNewLabel_1.setBounds(37, 67, 61, 16);
		frame.getContentPane().add(lblNewLabel_1);

		urlTextField = new JTextField();
		urlTextField.setBounds(110, 19, 217, 28);
		frame.getContentPane().add(urlTextField);
		urlTextField.setColumns(10);

		JButton btnNewButton = new JButton("OK");
		btnNewButton.setBounds(74, 134, 70, 29);
		frame.getContentPane().add(btnNewButton);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});
		btnCancel.setBounds(224, 134, 80, 29);
		frame.getContentPane().add(btnCancel);
		
		hourCombo = new JComboBox();
		hourCombo.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		hourCombo.setBounds(184, 63, 80, 27);
		frame.getContentPane().add(hourCombo);
		
		minCombo = new JComboBox();
		minCombo.setModel(new DefaultComboBoxModel(new String[] {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"}));
		minCombo.setBounds(184, 98, 80, 27);
		frame.getContentPane().add(minCombo);
		
		JLabel lblHour = new JLabel("Hour:");
		lblHour.setBounds(110, 67, 49, 16);
		frame.getContentPane().add(lblHour);
		
		JLabel lblMinute = new JLabel("Minute:");
		lblMinute.setBounds(110, 102, 61, 16);
		frame.getContentPane().add(lblMinute);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});


	}
	public void onCancel(){
		mc.cancelUpdateConfig();
	}
	
	public void onOK(){
		int hour = Integer.parseInt(hourCombo.getSelectedItem().toString());
		int minute = Integer.parseInt(minCombo.getSelectedItem().toString());		
		this.mc.updateConfig(hour, minute, urlTextField.getText());
		this.frame.dispose();
	}
}
