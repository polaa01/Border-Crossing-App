import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.TextArea;

import javax.swing.JTextArea;

public class VehicleInfo extends JFrame {

	JPanel contentPane;
	private Vehicle vehicle;
	private BorderFrame borderFrame;
	//public TextArea ta;
	protected Object ta;

	public VehicleInfo(Vehicle v) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Vehicle Information");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		this.setSize(new Dimension(350, 350));
		this.setResizable(false);
		setContentPane(contentPane);
		getContentPane().setBackground(new Color(230, 230, 250));
		
		Border border = BorderFactory.createLineBorder(Color.black, 3);
		
		JLabel lblNewLabel = new JLabel("Informacije o vozilu: ");
		lblNewLabel.setForeground(SystemColor.inactiveCaptionText);
		lblNewLabel.setBackground(new Color(224, 255, 255));
		lblNewLabel.setFont(new Font("Courier", Font.BOLD, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(20, 10, 207, 34);
		lblNewLabel.setBorder(border);
		contentPane.add(lblNewLabel);
		
		JTextArea ta = new JTextArea();
		//ta.setEditable(false);
		//ta.setText("Id: " + v.getId()+ "\n" + v.getType() + "\n" + "Broj putnika: " + v.getNumOfPassengers());
		ta.setText(VehicleInfo.vehicleInformation(v));
		ta.setFont(new Font("Courier", Font.BOLD, 12));
		ta.setLineWrap(true);
		ta.setBounds(20, 71, 251, 103);
		contentPane.add(ta);	
		
	}
	
	public static String vehicleInformation(Vehicle v)
	{
		 
		StringBuilder sb = new StringBuilder();
		String res;
		sb.append("Id: " + v.getId() + "\nType: " + v.getType() + "\nBroj putnika: " + v.getNumOfPassengers() + "\n");
		for(Person p: v.passengers)
		{
			sb.append(p + " ");
		}
		res = sb.toString();
		return res;
	}
	
	public static Vehicle getVehicle(int index)
	{
		Vehicle v = Main.vehiclesQueue.get(index);
		return v;
	}
	
	
	

}
