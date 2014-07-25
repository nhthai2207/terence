import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.SwingConstants;

public class SchedulerGUI {

	public JFrame frame;
	private JTextField urlTextField;
	private JTextField timeTextField;
	private MainProgram mc;

	/**
	 * Create the application.
	 */
	public SchedulerGUI(MainProgram mc) {
		this.mc = mc;
		initialize();
		this.initData();

	}

	public void initData() {
		this.urlTextField.setText(this.mc.getConfig().url);
		this.timeTextField.setText(this.mc.getConfig().runTimeHour + " : " + this.mc.getConfig().runTimeMin);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(95, 158, 160));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 358, 171);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("URL");
		lblNewLabel.setBounds(37, 25, 61, 16);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Run Time");
		lblNewLabel_1.setBounds(37, 67, 61, 16);
		frame.getContentPane().add(lblNewLabel_1);

		urlTextField = new JTextField();
		lblNewLabel.setLabelFor(urlTextField);
		urlTextField.setEditable(false);
		urlTextField.setBounds(110, 19, 217, 28);
		frame.getContentPane().add(urlTextField);
		urlTextField.setColumns(10);

		timeTextField = new JTextField();
		lblNewLabel_1.setLabelFor(timeTextField);
		timeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		timeTextField.setEditable(false);
		timeTextField.setBounds(110, 61, 70, 28);
		frame.getContentPane().add(timeTextField);
		timeTextField.setColumns(10);

		JButton okButton = new JButton("OK");		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});
		okButton.setBounds(60, 105, 70, 29);
		frame.getContentPane().add(okButton);

		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onExit();
			}
		});
		exitButton.setBounds(242, 105, 85, 29);
		frame.getContentPane().add(exitButton);

		JLabel lbleveryDay = new JLabel("(Every day)");
		lbleveryDay.setBounds(180, 67, 70, 16);
		frame.getContentPane().add(lbleveryDay);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onUpdate();
			}
		});
		updateButton.setBounds(143, 105, 80, 29);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				onExit();
			}
		});

		frame.getContentPane().add(updateButton);

		JButton btnRunNow = new JButton("Run Now");
		btnRunNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onRunNow();
			}
		});		
		btnRunNow.setBounds(242, 62, 85, 29);
		frame.getContentPane().add(btnRunNow);

	}

	public void onRunNow() {
		boolean tmp = this.mc.runNowJob();
		String msg = "Run done. Go to database for checking result";
		if(!tmp){
			msg = "It's problem with run. Run again to contact administrator";
		}
		
		JOptionPane.showMessageDialog(null, msg);

	}

	public void onUpdate() {
		this.mc.updateEvent();
	}

	public void onExit() {
		int result = JOptionPane.showConfirmDialog(null, "Are you sure? Program will exit and your data is not update such as scheduler!", "Confirm",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == 0) {
			System.exit(0);
		}
	}

	public void onOK() {
		frame.setState(Frame.ICONIFIED);
	}
}
