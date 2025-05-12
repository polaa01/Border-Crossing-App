import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextArea;

public class DescriptionFrame extends JFrame {
	public static ArrayList<Person> personList = new ArrayList<>();
	private JPanel contentPane;


	
	public DescriptionFrame() {
		//setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Vozila sa incidentima");
		//this.setResizable(false);
		//this.setSize(new Dimension(450, 346));
		setBounds(100, 100, 775, 432);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		getContentPane().setBackground(Color.cyan);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.CYAN);
		panel.setBounds(0, 0, 355, 377);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(10, 5, 1, 1));
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(365, 10, 386, 367);
		contentPane.add(textArea);
		deserialize();
		
		Map<String, List<Person>> temp=personList.stream().collect(Collectors.groupingBy(Person::getVehicleName));
		for(var el:temp.entrySet()) {
			JLabel label = new JLabel();
			label.setForeground(Color.BLACK);
			label.setBounds(100, 100, 250, 200);
			label.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			panel.add(label);
			label.setText(el.getKey());
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					StringBuilder builder=new StringBuilder();
					builder.append("Informacije o incidentu u vozilu: " + el.getKey()+"\n");
					for(var p:el.getValue())
					{
						if("Driver".equals(p.getType()))
							builder.append("Nije presao granicu, jer je kaznjen vozac " + p+"\n");
						else
							builder.append("Imao problem sa putnikom: " + p+"\n");
					}
					textArea.setText(builder.toString());
				
					
				}

			});
		}
	
		
	}
	
	
	public static void deserialize()
	{
		try
		{
		String text = "";
		
			FileInputStream fis = new FileInputStream(Vehicle.fajl);
			ObjectInputStream in = new ObjectInputStream(fis);
			DescriptionFrame.personList=(ArrayList<Person>)in.readObject();
			in.close();
			fis.close();
		}catch(IOException | ClassNotFoundException e) {
			//e.printStackTrace();
		//	System.out.println(e.getMessage());
		}
			
	}
	}
