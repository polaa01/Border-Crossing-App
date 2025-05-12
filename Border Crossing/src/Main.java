import java.io.File;
import java.lang.System.Logger;
import java.util.*;
import java.util.logging.*;
import java.util.logging.FileHandler;

import javax.print.attribute.standard.MediaSize.Other;
public class Main {
	private static Random rand = new Random();
	public static final int NUM_OF_VEHICLES = 50;
	public static final int NUM_OF_CARS = 35;
	public static final int NUM_OF_BUSES = 5;
	public static final int NUM_OF_TRUCKS = 10;
	public static final int NUM_OF_POLICE_TERMINALS=3;
	public static final int NUM_OF_CUSTOMS_TERMINALS=2;
	public static ArrayList<Truck> trucks = new ArrayList<>();
	public static ArrayList<Bus> buses = new ArrayList<>();
	public static ArrayList<Car> cars = new ArrayList<>();
	public static ArrayList<Truck> specialTrucks = new ArrayList<>(); 
	public static final int ROW_LENGTH = 50;
	public static Object []row = new Object [ROW_LENGTH]; 
	public static ArrayList<Vehicle>vehicles = new ArrayList<>();
	public static LinkedList<Vehicle>vehiclesQueue = new LinkedList<>();
	public static final String CONFIG_DIRECTORY = "Config";
    public static final String FILE_NAME = "terminali.txt";
    public static final String FILE_PATH = CONFIG_DIRECTORY + File.separator + FILE_NAME;
    public static OtherVehiclesFrame f2 = null;
    public static boolean isPaused = false;
    public static boolean isStarted=false;
	private static final String LOG_FILE = "BorderCrossing.log";
    public static MyLogger myLogger = new MyLogger(LOG_FILE);

	
   public static void main(String []args)
   {
	  Thread watcher=new Thread(new FileWatcher()); watcher.setDaemon(true);
	   watcher.start(); 
	   setTrucks();
	   setCars();
	   setBuses();
	   Vehicle.generateIllegalDocuments();
	   for(Truck truck: trucks)
	   {
		   truck.setDeclaredCargoMassRandomly();
	   }
	   Truck.generateSpecialTrucks();
	   Collections.shuffle(vehicles);
	   for(Vehicle v: vehicles)
	   {
		   vehiclesQueue.add(v);
	   }
	
	   BorderFrame frame = new BorderFrame();
	   frame.setVisible(true);
	   f2 = new OtherVehiclesFrame();
	      
	   
   }
 
   
   private static void printRow()
   {
	   for(int i=0; i<ROW_LENGTH; i++)
	   {
		   System.out.println(row[i]);
	   }
	   System.out.println();
   }
   
   public static void setTrucks()
   {
	   int br=0;
	   while(br < NUM_OF_TRUCKS)
	   {
		   int position = rand.nextInt(ROW_LENGTH);
		   if(row[position] == null)
		   {
			   row[position]=new Truck();
			   trucks.add((Truck)row[position]);
			   vehicles.add((Truck)row[position]);
			   br++;
		   }
	   }
   }
	   
   public static void setBuses()
   {
	   int br=0;
	   while(br < NUM_OF_BUSES)
	   {
		   int position = rand.nextInt(ROW_LENGTH);
		   if(row[position] == null)
		   {
			   row[position]=new Bus();
			   buses.add((Bus)row[position]);
			   vehicles.add((Bus)row[position]);
			   br++;
		   }
	   }
   }
   
   public static void setCars()
   {
	   int br=0;
	   while(br < NUM_OF_CARS)
	   {
		   int position = rand.nextInt(ROW_LENGTH);
		   if(row[position] == null)
		   {
			   row[position]=new Car();
			   cars.add((Car)row[position]);
			   vehicles.add((Car)row[position]);
			   br++;
		   }
	   }
   }
	   
   
}
