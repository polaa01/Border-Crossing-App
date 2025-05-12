import java.awt.Color;
import java.util.*;

public class Bus extends Vehicle implements IHasCargoSpace {
	private Random rand = new Random();
	public static final int MIN_NUM_OF_PASSENGERS = 1;
	public static final int MAX_NUM_OF_PASSENGERS = 52;
	public static ArrayList<Person> personsWithLuggage = new ArrayList<>();
	public static ArrayList<Person> illegalLuggages = new ArrayList<>();
	private int numOfLuggages = 0;
	private boolean hasCargoSpace;
	public static Object policeTerminalForBusLock = new Object();
	public static Object customsTermianlForBusLock = new Object();
	private PoliceTerminal policeTmp = null;
	private int oldPos;

	public Bus() {
		super();
		this.capacity = MAX_NUM_OF_PASSENGERS;
		this.numOfPassengers = rand.nextInt(MAX_NUM_OF_PASSENGERS - MIN_NUM_OF_PASSENGERS + 1) + MIN_NUM_OF_PASSENGERS;
		if (this.numOfPassengers == MIN_NUM_OF_PASSENGERS) {
			this.hasOtherPassengers = false;
		}
		generatePassengers();
		for (Person p : this.passengers) {
			if (p.hasPassengerLuggage()) {
				this.numOfLuggages++;
				personsWithLuggage.add(p);
			}
		}
		this.countIllegalLuggages();

	}

	public String toString() {
		return this.getType() + " " + this.getId();
	}

	public void countIllegalLuggages() 
	{
		int percentage = 10;
		int numOfIllegal = (this.numOfLuggages * percentage / 100);
		Collections.shuffle(personsWithLuggage);
		for (int i = 0; i < numOfIllegal; i++) {
			Bus.illegalLuggages.add(Bus.personsWithLuggage.get(i));
		}

	}

	@Override
	public String getType() {
		return "Bus";
	}

	public int getNumOfLuggages() {
		return this.numOfLuggages;
	}

	public void processAtPolice() throws InterruptedException {
		synchronized (Vehicle.pauseLock) {
			if (Main.isPaused)
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
					BorderFrame.p1.setBackground(Color.RED);
					BorderFrame.p1.setText("B");
				}
				else if (!Vehicle.p2.isReserved()&& !Vehicle.p2.isBlocked())
				{
					policeTmp = Vehicle.p2;
					BorderFrame.p2.setText("B");
					BorderFrame.p2.setBackground(Color.RED);
					
				}
				else {
					
					Vehicle.policeTerminalLock.wait();
				}
			}
			
			policeTmp.setReserved(true);
			
		}

		synchronized (Vehicle.lock) {
			oldPos = Main.vehiclesQueue.size();
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
		else {
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
		synchronized (Vehicle.pauseLock) {
			if (Main.isPaused)
				try {
					Vehicle.pauseLock.wait();
				} catch (InterruptedException e) {
					//e.printStackTrace();
					Main.myLogger.logger.warning(e.getMessage());
				}
		}
		synchronized (Bus.customsTermianlForBusLock ) {
			while (Vehicle.c1.isReserved() || Vehicle.c1.isBlocked()) {
				Bus.customsTermianlForBusLock.wait();
				System.out.println("Terminal c1 je zauzet");
			}
			Vehicle.c1.setReserved(true);
			BorderFrame.c1.setText("B");
			BorderFrame.c1.setBackground(Color.RED);
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

		System.out.println(this + " se procesira na carinskom terminalu...");
		sleep(100 * this.numOfPassengers);

		Iterator<Person> itr = passengers.iterator();
		while (itr.hasNext()) {
			Person p = itr.next();
			if (illegalLuggages.contains(p)) {
				System.out.println("Putnik " + p + " se izbacuje iz autobusa, " + this
						+ ", jer mu kofer sadrzi nedozvoljene stvari...");
				itr.remove();
			}
		}

		synchronized (Bus.customsTermianlForBusLock) {
			Vehicle.c1.setReserved(false);
			Vehicle.numOfEnded++;
			BorderFrame.removeColourFromLabel(this, BorderFrame.vehicleLabels.size()-numOfEnded);
			BorderFrame.c1.setBackground(Color.white);
			BorderFrame.c1.setText("");
			Bus.customsTermianlForBusLock.notifyAll();
			
		}
	

	}

}
