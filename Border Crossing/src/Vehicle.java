import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.time.*;

public abstract class Vehicle extends Thread implements Serializable {
	private static int globalId = 1;
	private Random rand = new Random();
	protected int capacity;
	protected int numOfPassengers;
	protected boolean hasDriver = true;
	protected boolean hasOtherPassengers = true;
	private String type;
	public static Object lock = new Object();
	public static Object runLock = new Object();
	public static Object blockTerminalLock = new Object();
	private int id;
	protected ArrayList<Person> passengers = new ArrayList<>();
	public static ArrayList<Person> allPassengers = new ArrayList<>();
	public static ArrayList<Person> prohibitedPassengers = new ArrayList<>();
	public static PoliceTerminal p1 = new PoliceTerminal();
	public static PoliceTerminal p2 = new PoliceTerminal();
	public static PoliceTerminal pk = new PoliceTerminal();
	public static CustomsTerminal c1 = new CustomsTerminal();
	public static CustomsTerminal ck = new CustomsTerminal();
	public static Object policeTerminalLock = new Object();
	public static String SER = "kaznjeni";
	BorderFrame frame;
	public static String durationText;
	public static boolean pause = true;
	public static Object pauseLock = new Object();
	public static String fajl = String.format(SER + "%d.ser", System.currentTimeMillis());
	public static long startTime;
	public static long endTime;
	public static int  numOfEnded;
	public static ArrayList<Person> personToSerialize=new ArrayList<>();

	public Vehicle() {
		super();
		this.id = globalId++;
	}

	public abstract String getType();

	public long getId() {
		return this.id;
	}

	public abstract void processAtPolice() throws InterruptedException;

	public abstract void processAtCustoms() throws InterruptedException;

	public void generatePassengers() {
		Person p = new Driver();
		p.setVehicleName(this.toString());
		passengers.add(p);
		Vehicle.allPassengers.add(p);
		for (int i = 0; i < numOfPassengers - 1; i++) {
			p = new Passenger();
			p.setVehicleName(this.toString());
			passengers.add(p);
			Vehicle.allPassengers.add(p);
		}
	}

	public void printPassengers() {
		for (Person p : passengers) {
			System.out.print(p + " ");
		}
	}

	public int getNumOfPassengers() {
		return this.numOfPassengers;
	}

	public static void generateIllegalDocuments()
	{
		int numOfDocuments = sumAllDocuments();
		int percentageOfIllegalDocuments = 3;
		int numOfPassengersToSelect = (numOfDocuments * percentageOfIllegalDocuments / 100);
		Collections.shuffle(Vehicle.allPassengers);
		for (int i = 0; i < numOfPassengersToSelect; i++) {
			Vehicle.allPassengers.get(i).setIllegalDocument(true);
		}
	}

	public static int sumAllDocuments() {
		int num = 0;
		for (var v : Main.vehicles) {
			num += v.getNumOfPassengers();
		}
		return num;
	}

	public static void printProhibitedPassengers() {
		for (Person p : prohibitedPassengers) {
			System.out.print(p + " ");
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		return id == other.id;
	}

	@Override
	public void run() {

		while (true) {
			boolean tmp = false;
			synchronized(Vehicle.pauseLock) {
				if(Main.isPaused)
					try {
						Vehicle.pauseLock.wait();
					} catch (InterruptedException e) {
						
						//e.printStackTrace();
						Main.myLogger.logger.warning(e.getMessage());
					}
			}
			synchronized (Vehicle.lock) {
				if ((this).equals(Main.vehiclesQueue.peek())) {// dosao sam do kraja puta tj. do prvog terminala
					tmp = true;

				} else {
					synchronized (Vehicle.lock) {

						BorderFrame.paintLabels(this, Main.vehiclesQueue.indexOf(this));
					}
					try {
						Vehicle.lock.wait();
					} catch (InterruptedException e) {
						//e.printStackTrace();
						Main.myLogger.logger.warning(e.getMessage());
					}
					

				}
			}
			if (tmp) {

				if (this instanceof IHasCargo) // kamion
				{
					synchronized (Vehicle.lock) {

						BorderFrame.paintLabels(this, Main.vehiclesQueue.indexOf(this));
					}
					try {
						
						this.processAtPolice();
						
					} catch (InterruptedException ex) {
						//ex.printStackTrace();
						Main.myLogger.logger.warning(ex.getMessage());
					}

				}

				else if (this instanceof IHasCargoSpace) // bus
				{
					synchronized (Vehicle.lock) {

						BorderFrame.paintLabels(this, Main.vehiclesQueue.indexOf(this));
					}
					
					try {
						this.processAtPolice();
					} catch (InterruptedException e) {
						//e.printStackTrace();
						Main.myLogger.logger.warning(e.getMessage());
					}
				}

				else {
					synchronized (Vehicle.lock) {

						BorderFrame.paintLabels(this, Main.vehiclesQueue.indexOf(this));
					}
					try {
						this.processAtPolice();

					} catch (InterruptedException e) {
						//e.printStackTrace();
						Main.myLogger.logger.warning(e.getMessage());
					}
				}
		
				break;
			}

		}
		
	}

	public static void serializePunished()
	{
		try {
			FileOutputStream fos = new FileOutputStream(Vehicle.fajl);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(personToSerialize);
			out.close();
			fos.close();
		} catch (IOException ex) {
			//ex.printStackTrace();
			Main.myLogger.logger.warning(ex.getMessage());
		}
	}

	

}
