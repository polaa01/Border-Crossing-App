import java.util.*;
import java.awt.Color;
import java.io.*;

public class Truck extends Vehicle implements IHasCargo {
	private Random rand = new Random();
	public static final int MIN_NUM_OF_PASSENGERS = 1;
	public static final int MAX_NUM_OF_PASSENGERS = 3;
	private boolean hasCargo = true;
	private boolean shouldGenerateCustomsDocumentation;
	private int actualCargoMass;
	private int declaredCargoMass; 
	public static Object policeTerminalForTruckLock = new Object();
	public static Object customsTerminalForTruckLock = new Object();
	public static final String FILENAME = "Specijalni";
	public static ArrayList<Truck> trucksForFile = new ArrayList<Truck>();
	public static String file = String.format(FILENAME + "%d.txt", System.currentTimeMillis());
	private int oldPos;

	public Truck() {
		super();
		this.capacity = MAX_NUM_OF_PASSENGERS;
		this.numOfPassengers = rand.nextInt(MAX_NUM_OF_PASSENGERS - MIN_NUM_OF_PASSENGERS + 1) + MIN_NUM_OF_PASSENGERS;
		if (this.numOfPassengers == MIN_NUM_OF_PASSENGERS) {
			this.hasOtherPassengers = false;
		}
		generatePassengers();
		this.shouldGenerateCustomsDocumentation = generateCustomsDocumentation();

	}

	@Override
	public String getType() {
		return "Truck";
	}

	public String toString() {
		return this.getType() + " " + this.getId();
	}

	public boolean generateCustomsDocumentation() {
		return rand.nextInt(100) < 50;
	}

	public boolean getCargo() {
		return this.hasCargo;
	}

	public void setCargo(boolean hasCargo) {
		this.hasCargo = hasCargo;
	}

	public boolean getShouldGenerateCustomsDocumentation() {
		return this.shouldGenerateCustomsDocumentation;
	}

	public int getActualCargoMass() {
		return this.actualCargoMass;
	}

	public void setActualCargoMass(int actualCargoMass) {
		this.actualCargoMass = actualCargoMass;
	}

	public int getDeclaredCargoMass() {
		return this.declaredCargoMass;
	}

	public void setDeclaredCargoMass(int declaredCargoMass) {
		this.declaredCargoMass = declaredCargoMass;
	}

	public void setDeclaredCargoMassRandomly() {
		int min = 100;
		int max = 1500;
		int mass = rand.nextInt(max - min + 1) + min;
		setDeclaredCargoMass(mass);
		setActualCargoMass(mass);
	}

	public static void generateSpecialTrucks() {
		int percentage = 20;
		int numOfTrucks = Main.NUM_OF_TRUCKS;
		int numOfSpecialTrucks = (int) (numOfTrucks * percentage / 100);
		Collections.shuffle(Main.trucks);
		for (int i = 0; i < numOfSpecialTrucks; i++) {
			Main.specialTrucks.add(Main.trucks.get(i));
		}

		for (Truck t1 : Main.specialTrucks) {
			t1.setActualCargoMassSpecial();
		}

	}

	public void setActualCargoMassSpecial()// specijalni kamioni
	{
		int minPercent = 1;
		int maxPercent = 30;
		int x = rand.nextInt(maxPercent - minPercent + 1) + minPercent;
		int value = (int) (getDeclaredCargoMass() * x / 100);
		int mass = getDeclaredCargoMass() + value;
		setActualCargoMass(mass);
	}

	public void processAtPolice() throws InterruptedException {
		synchronized(Vehicle.pauseLock) {
			if(Main.isPaused)
				try {
					Vehicle.pauseLock.wait();
				} catch (InterruptedException e) {
					//e.printStackTrace();
					Main.myLogger.logger.warning(e.getMessage());
				}
		}

		synchronized (Vehicle.pk) {

			while (Vehicle.pk.isReserved() || Vehicle.pk.isBlocked()) {
				System.out
						.println("Cekam: rezervisan - " + Vehicle.pk.isReserved() + "blokiran: " + Vehicle.pk.isBlocked());
				Vehicle.pk.wait();
			}
			Vehicle.pk.setReserved(true);
			BorderFrame.pk.setBackground(Color.BLUE);
			BorderFrame.pk.setText("T");
		}

		synchronized (Vehicle.lock) {
			oldPos=Main.vehiclesQueue.size();
			Main.vehiclesQueue.poll();
			Vehicle.lock.notifyAll();
		}
		// BorderFrame.pk.setBackground(Color.BLUE);
		System.out.println(this + " se procesira na policijskom terminalu...");
		sleep(this.numOfPassengers * 500);

		boolean cont = true;

		for (Person p : passengers) {
			if (p.isIllegalDocument()) {
				if ("Driver".equals(p.getType())) {
					System.out.println("Kaznjen je vozac." + this + " se izbacuje iz reda...");
					cont = false;
				} else if ("Passenger".equals(p.getType())) {
					System.out.println("Kaznjen je putnik " + p);
				}
				Vehicle.personToSerialize.add(p);
				Vehicle.serializePunished();
			}
		}

	
		
		if (cont)
			this.processAtCustoms();
		else
		{
			synchronized (Vehicle.pk) {
				Vehicle.pk.setReserved(false);
				BorderFrame.pk.setBackground(Color.white);
				BorderFrame.pk.setText("");
				Vehicle.pk.notifyAll();
				
			}
		}
		

	}

	public void processAtCustoms() throws InterruptedException {
		synchronized(Vehicle.pauseLock) {
			if(Main.isPaused)
				try {
					Vehicle.pauseLock.wait();
				} catch (InterruptedException e) {
					//e.printStackTrace();
					Main.myLogger.logger.warning(e.getMessage());
				}
		}
		
		
		
		synchronized (Truck.customsTerminalForTruckLock) {
			while (Vehicle.ck.isReserved() || Vehicle.ck.isBlocked()) {
				System.out.println("Terminal ck je zauzet");
				Truck.customsTerminalForTruckLock.wait();
			}

			Vehicle.ck.setReserved(true);
			BorderFrame.ck.setBackground(Color.BLUE);
			BorderFrame.ck.setText("T");
		}
		synchronized (Vehicle.pk) {
			Vehicle.pk.setReserved(false);
			BorderFrame.pk.setBackground(Color.white);
			BorderFrame.pk.setText("");
			Vehicle.pk.notifyAll();
			
		}
		System.out.println(this + " se procesira na carinskom terminalu...");
		

		sleep(500);

		if (Main.specialTrucks.contains(this)) {
			System.out.println(this + " ne moze da predje granicu specijalni");
			Main.trucks.remove(this);
			//System.out.println("txt");
			Truck.writeTruckInFile(this);
		}

		synchronized (Truck.customsTerminalForTruckLock) {
			Vehicle.ck.setReserved(false);
			BorderFrame.ck.setText("");
			BorderFrame.ck.setBackground(Color.white);
			Vehicle.numOfEnded++;
			BorderFrame.removeColourFromLabel(this, BorderFrame.vehicleLabels.size()-numOfEnded);
			Truck.customsTerminalForTruckLock.notifyAll();
		}
		

	}

	public static void writeTruckInFile(Truck t) {
		try {

			PrintWriter writer = new PrintWriter(new FileWriter(Truck.file, true));
			writer.println(
					t + " " + "Deklarisana: " + t.getDeclaredCargoMass() + " " + "Stvarna: " + t.getActualCargoMass());
			writer.close();
		} catch (IOException ex) {
			//ex.printStackTrace();
			Main.myLogger.logger.warning(ex.getMessage());
		}
	}

}
