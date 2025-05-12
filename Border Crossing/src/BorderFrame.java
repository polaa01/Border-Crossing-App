import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;

import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.Window;

import javax.swing.JLabel;

//public static JPanel contentPane = new JPanel();

public class BorderFrame extends JFrame {

	private JPanel contentPane;
	public static ArrayList<JLabel> vehicleLabels = new ArrayList<>();
	public static JLabel p1 = new JLabel("");
	public static JLabel p2 = new JLabel("");
	public static JLabel pk = new JLabel("");
	public static JLabel c1 = new JLabel("");
	public static JLabel ck = new JLabel("");
	public static Timer timer;
	public static TimerTask task;
	private long elapsedTime = 0;
	

	public static JLabel time = new JLabel();
	private final JButton btnOpis = new JButton("Opis");

	public BorderFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		setBounds(100, 100, 450, 300);
		this.setSize(new Dimension(600, 600));
		this.setTitle("Border Crossing");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		getContentPane().setBackground(Color.cyan);
		
		timer = new Timer();
		 task = new TimerTask() {
		     
		    
		    @Override
		    public void run() {
		        if (!Main.isPaused) {
		            if (Main.isStarted) {
		                elapsedTime = (System.currentTimeMillis() - Vehicle.startTime) / 1000;
		            }
		            long hours = elapsedTime / 3600;
		            long minutes = (elapsedTime % 3600) / 60;
		            long seconds = elapsedTime % 60;
		            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		            BorderFrame.time.setText(timeString);
		        }
		    }
		};

		JButton btnNewButton = new JButton("START/PAUSE");
		btnNewButton.addActionListener((action) -> {
		    if (!Main.isStarted) {
		       /*Main.isStarted = true;
		        Vehicle.startTime = System.currentTimeMillis();
		        startThreads();
		        timer.schedule(task, 0, 1000);*/
		    	new GameThread().start();
		    } else {
		        synchronized (Vehicle.pauseLock) {
		            if (Main.isPaused) {
		                Main.isPaused = false;
		                Vehicle.pauseLock.notifyAll();
		                Vehicle.startTime = System.currentTimeMillis() - (elapsedTime * 1000); 
		            } else {
		                Main.isPaused = true;
		            }
		        }
		    }
		});

		
		btnNewButton.setFont(new Font("Courier", Font.BOLD | Font.ITALIC, 14));
		btnNewButton.setForeground(new Color(0, 250, 154));
		btnNewButton.setBackground(SystemColor.activeCaption);
		btnNewButton.setBounds(418, 10, 126, 40);
		btnNewButton.setBorder(new LineBorder(Color.black, 2));
		btnNewButton.setFocusPainted(false);
		Cursor cursor = btnNewButton.getCursor();
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnNewButton);

		Border blackline = BorderFactory.createLineBorder(Color.black, 2);

		
		c1.setHorizontalAlignment(SwingConstants.CENTER);
		c1.setFont(new Font("Courier", Font.BOLD, 14));
		c1.setBounds(161, 149, 45, 31);
		c1.setBorder(blackline);
		c1.setOpaque(true);
		contentPane.add(c1);

	
		p1.setFont(new Font("Courier", Font.BOLD, 14));
		p1.setHorizontalAlignment(SwingConstants.CENTER);
		p1.setBounds(161, 190, 45, 31);
		p1.setBorder(blackline);
		p1.setOpaque(true);
		contentPane.add(p1);

		
		p2.setHorizontalAlignment(SwingConstants.CENTER);
		p2.setFont(new Font("Courier", Font.BOLD, 14));
		p2.setBounds(251, 190, 45, 31);
		p2.setBorder(blackline);
		p2.setOpaque(true);
		contentPane.add(p2);

		
		pk.setFont(new Font("Courier", Font.BOLD, 14));
		pk.setHorizontalAlignment(SwingConstants.CENTER);
		pk.setBounds(345, 190, 45, 31);
		pk.setBorder(blackline);
		pk.setOpaque(true);
		contentPane.add(pk);

	
		ck.setFont(new Font("Courier", Font.BOLD, 14));
		ck.setHorizontalAlignment(SwingConstants.CENTER);
		ck.setBounds(345, 149, 45, 31);
		ck.setBorder(blackline);
		ck.setOpaque(true);
		contentPane.add(ck);

		JLabel label = null;
		
		for (int i = 0; i < 5; i++) {
			label = new JLabel();
			label.setOpaque(true);
			label.setFont(new Font("Courier", Font.BOLD, 14));
			label.setBorder(blackline);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			BorderFrame.vehicleLabels.add(label);
			BorderFrame.paintLabels(Main.vehiclesQueue.get(i), i);
			contentPane.add(label);
			int index=i;
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Vehicle v=null;
					synchronized(Vehicle.lock) {
						v=Main.vehiclesQueue.get(index);
					}
					VehicleInfo vi = new VehicleInfo(v);
					vi.setVisible(true);
					
				}

			});
		}
		BorderFrame.vehicleLabels.get(0).setBounds(251, 243, 45, 31);
		BorderFrame.vehicleLabels.get(1).setBounds(251, 272, 45, 31);
		BorderFrame.vehicleLabels.get(2).setBounds(251, 301, 45, 31);
		BorderFrame.vehicleLabels.get(3).setBounds(251, 330, 45, 31);
		BorderFrame.vehicleLabels.get(4).setBounds(251, 359, 45, 31);


		BorderFrame.time.setFont(new Font("Courier", Font.BOLD, 14));
		BorderFrame.time.setHorizontalAlignment(SwingConstants.CENTER);
		BorderFrame.time.setBounds(29, 10, 126, 40);
		BorderFrame.time.setBorder(blackline);
		contentPane.add(BorderFrame.time);

		
	
		

		JButton btnNewButton_1 = new JButton("45");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.f2.setVisible(true);

			}
		});

		btnNewButton_1.setForeground(Color.GREEN);
		btnNewButton_1.setFont(new Font("Courier", Font.BOLD | Font.ITALIC, 12));
		btnNewButton_1.setBounds(418, 349, 126, 40);
		btnNewButton_1.setBorder(blackline);
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnNewButton_1);

		JLabel lblNewLabel_6 = new JLabel("Border Crossing");
		lblNewLabel_6.setForeground(new Color(255, 0, 0));
		lblNewLabel_6.setFont(new Font("Courier", Font.BOLD | Font.ITALIC, 12));
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setBounds(220, 10, 126, 40);
		contentPane.add(lblNewLabel_6);
		
		btnOpis.setForeground(Color.GREEN);
		btnOpis.setFont(new java.awt.Font("Courier", java.awt.Font.BOLD | java.awt.Font.ITALIC, 12));
		btnOpis.setBounds(22, 349, 98, 40);
		btnOpis.setBorder(blackline);
		btnOpis.setFocusPainted(false);
		btnOpis.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnOpis);
		
		btnOpis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DescriptionFrame().setVisible(true);

			}
		});

	

	}

	public void startThreads() {
		for (Vehicle v : Main.vehicles) {
			v.start();
		}
	}


	public static void paintLabels(Vehicle v, int index) {

		if (index >= 0 && index < BorderFrame.vehicleLabels.size()) {
			JLabel label = BorderFrame.vehicleLabels.get(index);
			label.setOpaque(true);
			String type = v.getType();
			if ("Truck".equals(type)) {
				label.setText("T");
				label.setBackground(Color.BLUE);
			}

			else if ("Bus".equals(type)) {
				label.setText("B");
				label.setBackground(Color.RED);
			}

			else {
				label.setText("C");
				label.setBackground(Color.GREEN);
			}
		}
		

	}

	public static void removeColourFromLabel(Vehicle v, int index) {

		JLabel label = BorderFrame.vehicleLabels.get(index);
		if (index >= 5 && index < BorderFrame.vehicleLabels.size()) {
			label.setVisible(false);
		}
		else if(index>=0 && index<5) {
			label.setBackground(Color.WHITE);
			label.setText("");
		}
	}

	public static void printVehicleInfo() {
	
		JLabel label = null;
		for (int i=0; i<BorderFrame.vehicleLabels.size(); i++) {
			 final Vehicle v = Main.vehiclesQueue.get(i);
			 label = BorderFrame.vehicleLabels.get(i);
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					VehicleInfo vi = new VehicleInfo(v);
					vi.setVisible(true);
					
				}

			});
		}
	}
  }
    

