import java.awt.Color;
import java.util.*;

public class Car extends Vehicle {
	private Random rand = new Random();
	public static final int MIN_NUM_OF_PASSENGERS = 1;
	public static final int MAX_NUM_OF_PASSENGERS = 5;
	public static Object policeTerminalForCarLock = new Object();
	public static Object customsTermianlForCarLock = new Object();
	private PoliceTerminal policeTmp = null;
	private int oldPos;

	public Car() {
		super();
		this.capacity = MAX_NUM_OF_PASSENGERS; // kapacitet
		this.numOfPassengers = rand.nextInt(MAX_NUM_OF_PASSENGERS - MIN_NUM_OF_PASSENGERS + 1) + MIN_NUM_OF_PASSENGERS;
		if (this.numOfPassengers == MIN_NUM_OF_PASSENGERS) {
			this.hasOtherPassengers = false;
		}
		generatePassengers();
		

	}

	public String toString() {
		return this.getType() + " " + this.getId();
	}

	@Override
	public String getType() {
		return "Car";
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
		synchronized (Vehicle.policeTerminalLock) {
			while (policeTmp == null) {
				if (!Vehicle.p1.isReserved() && !Vehicle.p1.isBlocked())
				{
					policeTmp = Vehicle.p1;
					BorderFrame.p1.setBackground(Color.GREEN);
					BorderFrame.p1.setText("C");
				}
				else if (!Vehicle.p2.isReserved()&& !Vehicle.p2.isBlocked())
				{
					policeTmp = Vehicle.p2;
					BorderFrame.p2.setBackground(Color.GREEN);
					BorderFrame.p2.setText("C");
					//BorderFrame.p2.setText(");
					
				}
				else {
					
					Vehicle.policeTerminalLock.wait();
				}
			}
			
			policeTmp.setReserved(true);
			
		}

		synchronized (Vehicle.lock) {
			oldPos=Main.vehiclesQueue.size();
			Main.vehiclesQueue.poll();
			Vehicle.lock.notifyAll();
		}

		System.out.println(this + " se procesira na policijskom terminalu.");
		sleep(100 * this.numOfPassengers);

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
			synchronized (Vehicle.policeTerminalLock) {
				policeTmp.setReserved(false);
				if(policeTmp == Vehicle.p1)
				{
					BorderFrame.p1.setBackground(Color.WHITE);
					BorderFrame.p1.setText("");
				}
				else if(policeTmp == Vehicle.p2)
				{
					BorderFrame.p2.setBackground(Color.WHITE);
					BorderFrame.p2.setText("");
				}
				policeTmp = null;
				Vehicle.policeTerminalLock.notifyAll();
			}
		}

	}

	public void processAtCustoms() throws InterruptedException {
		synchronized(Vehicle.pauseLock) {
			if(Main.isPaused)
				try {
					Vehicle.pauseLock.wait();
				} catch (InterruptedException e) {
					//ke.printStackTrace();
					Main.myLogger.logger.warning(e.getMessage());
				}
		}
		
		
		synchronized (Bus.customsTermianlForBusLock) {
			while(Vehicle.c1.isBlocked()) {
				Bus.customsTermianlForBusLock.wait();
			}
			
			while (Vehicle.c1.isReserved()) {
				Bus.customsTermianlForBusLock.wait();
				
			}
			Vehicle.c1.setReserved(true);
			BorderFrame.c1.setBackground(Color.GREEN);
			BorderFrame.c1.setText("C");
		}

		synchronized (Vehicle.policeTerminalLock) {
			policeTmp.setReserved(false);
			if(policeTmp == Vehicle.p1)
			{
				BorderFrame.p1.setBackground(Color.WHITE);
				BorderFrame.p1.setText("");
			}
			else if(policeTmp == Vehicle.p2)
			{
				BorderFrame.p2.setBackground(Color.WHITE);
				BorderFrame.p2.setText("");
			}
			policeTmp = null;
			Vehicle.policeTerminalLock.notifyAll();
		}
		System.out.println(this + " se procesira na carinskom terminalu");
		sleep(2000);

		synchronized (Bus.customsTermianlForBusLock) {
			Vehicle.numOfEnded++;
			Vehicle.c1.setReserved(false);
			BorderFrame.c1.setBackground(Color.white);
			BorderFrame.c1.setText("");
			BorderFrame.removeColourFromLabel(this, BorderFrame.vehicleLabels.size()-numOfEnded);
			Bus.customsTermianlForBusLock.notifyAll();
		}
		

	}

}
