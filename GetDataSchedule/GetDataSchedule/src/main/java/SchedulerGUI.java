import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SchedulerGUI {

	public JFrame frame;
	private JTextField urlTextField;
	private JTextField timeTextField;
	private MainProgram mc;
	private ScheduledExecutorService scheduledThreadPool;
	private WorkerThread worker;

	
	/**
	 * Create the application.
	 */
	public SchedulerGUI(MainProgram mc) {
		this.mc = mc;
		initialize();
		this.initData();
	}
	
	public void initData(){
		this.urlTextField.setText(this.mc.getConfig().url);
		this.timeTextField.setText(this.mc.getConfig().runTimeHour + " : " + this.mc.getConfig().runTimeMin);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 358, 171);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("URL");
		lblNewLabel.setBounds(37, 25, 61, 16);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Run Time");
		lblNewLabel_1.setBounds(37, 67, 61, 16);
		frame.getContentPane().add(lblNewLabel_1);

		urlTextField = new JTextField();
		urlTextField.setEditable(false);
		urlTextField.setBounds(110, 19, 217, 28);
		frame.getContentPane().add(urlTextField);
		urlTextField.setColumns(10);

		timeTextField = new JTextField();
		timeTextField.setEditable(false);
		timeTextField.setBounds(110, 61, 128, 28);
		frame.getContentPane().add(timeTextField);
		timeTextField.setColumns(10);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});
		okButton.setBounds(61, 105, 70, 29);
		frame.getContentPane().add(okButton);

		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onExit();
			}
		});
		exitButton.setBounds(256, 105, 80, 29);
		frame.getContentPane().add(exitButton);

		JLabel lbleveryDay = new JLabel("(Every day)");
		lbleveryDay.setBounds(246, 67, 106, 16);
		frame.getContentPane().add(lbleveryDay);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onUpdate();
			}
		});
		updateButton.setBounds(158, 105, 80, 29);
		frame.getContentPane().add(updateButton);

	}
	
	public void onUpdate(){
		this.mc.updateEvent();
	}
	public void onExit(){
		JOptionPane.showMessageDialog(null, "Are you sure? Program will exit and your data is not update such as scheduler!", "Confirm", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
	public void onOK(){
		frame.setState(Frame.ICONIFIED);
	}
	
	public void startJob() throws InterruptedException {
		scheduledThreadPool = Executors.newScheduledThreadPool(5);
		worker = new WorkerThread("Scheduler get data then add to MongoDB");
		scheduledThreadPool.scheduleAtFixedRate(worker, 0, 5, TimeUnit.SECONDS);		
	}

	public void endJob() {
		scheduledThreadPool.shutdown();
		while (!scheduledThreadPool.isTerminated()) {
			// wait for all tasks to finish
		}
		System.out.println("Finished all threads");

	}
	
}
