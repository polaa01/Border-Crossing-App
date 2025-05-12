import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class OtherVehiclesFrame extends JFrame {
	private JPanel contentPane;
	public OtherVehiclesFrame() {
		
		this.setResizable(false);
		setBounds(100, 100, 450, 300);
		this.setTitle("Ostala Vozila");
		this.setSize(new Dimension(500, 500));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getContentPane().setBackground(Color.cyan);
		this.setLayout(new GridLayout(0, 5));
		
		 
	        for (int i = 5; i < 50; i++) {
	            JLabel label = new JLabel();
	            label.setOpaque(true);
	            //label.setBackground(Color.WHITE);
	            label.setHorizontalAlignment(JLabel.CENTER); 
	            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
	            BorderFrame.vehicleLabels.add(label);
	            contentPane.add(label);
	        }
	        //BorderFrame.printVehicleInfo();
	       
	}
	


}
