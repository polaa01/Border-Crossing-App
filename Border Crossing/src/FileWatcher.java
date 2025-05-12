import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class FileWatcher implements Runnable {
	public static Map<String, Integer> terminalsStates = new HashMap<>();
   
	public FileWatcher()
	{
		super();
	}
	
	@Override
	public void run()
	{
		try
		{
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(Main.CONFIG_DIRECTORY);
			dir.register(watcher, ENTRY_MODIFY);
			System.out.println("Watch Service registrovan za direktorijum: " + dir.getFileName());
			
			while(true)
			{
				WatchKey key=null;
				try
				{
					key = watcher.take();
				}
				catch(InterruptedException ex)
				{
					System.out.println(ex.getMessage());
					Main.myLogger.logger.warning(ex.getMessage());
				}
				
				for(WatchEvent<?> event: key.pollEvents())
				{
					WatchEvent.Kind<?> kind = event.kind();
					WatchEvent<Path> ev = (WatchEvent<Path>)event;
					Path fileName = ev.context();
					System.out.println(kind.name() + ": " + fileName);
					
					if(kind.equals(ENTRY_MODIFY)&& fileName.toString().equals(Main.FILE_NAME))
					{
						List<String> lines = Files.readAllLines(dir.resolve(fileName));
						for(String line: lines)
						{
							String []params = line.split(" ");
							String varName = params[0];
							Integer terminalState = Integer.parseInt(params[1]);
							terminalsStates.put(varName, terminalState);
							
						}
						
						for(Map.Entry<String, Integer> entry:terminalsStates.entrySet())
						{
							if(entry.getKey().equals("pk"))
							{
								//System.out.println("pk pk");
								synchronized(Vehicle.pk) {
									if(entry.getValue()>0) {
										Vehicle.pk.setBlocked(false);
										System.out.println(Vehicle.pk.isBlocked());
										Vehicle.pk.notifyAll();
									}
									else
									{
										Vehicle.pk.setBlocked(true);
										System.out.println("Jesam li blokiran: "+Vehicle.pk.isBlocked());
										
									}
								}
							}
							
							else if(entry.getKey().equals("p1"))
							{
								boolean status=false;
								
								synchronized(Vehicle.p1) {
									if(entry.getValue()>0) {
										Vehicle.p1.setBlocked(false);
										System.out.println(Vehicle.p1.isBlocked());
										status=true;
										
									}
									else
									{
										Vehicle.p1.setBlocked(true);
										System.out.println("Jesam li blokiran: "+Vehicle.p1.isBlocked());
										
									}
								}
								if(status) {
									synchronized(Vehicle.policeTerminalLock) {
										Vehicle.policeTerminalLock.notifyAll();
									}
								}
							}
							
							else if(entry.getKey().equals("p2"))
							{
								
								boolean status=false;
								synchronized(Vehicle.p2) {
									if(entry.getValue()>0) {
										Vehicle.p2.setBlocked(false);
										status=true;
										System.out.println(Vehicle.p2.isBlocked());
										Vehicle.p2.notifyAll();
									}
									else
									{
										Vehicle.p2.setBlocked(true);
										System.out.println("Jesam li blokiran: "+Vehicle.p2.isBlocked());
										
									}
								}
								if(status) {
									synchronized(Vehicle.policeTerminalLock) {
										Vehicle.policeTerminalLock.notifyAll();
									}
								}
							}
							
							else if(entry.getKey().equals("c1"))
							{
								
								boolean status=false;
								synchronized(Vehicle.c1) {
									if(entry.getValue()>0) {
										Vehicle.c1.setBlocked(false);
										System.out.println(Vehicle.c1.isBlocked());
										status=true;
										
									}
									else
									{
										Vehicle.c1.setBlocked(true);
										System.out.println("Jesam li blokiranc1: "+Vehicle.c1.isBlocked());
										
									}
								}
								if(status) {
									synchronized(Bus.customsTermianlForBusLock) {
										Bus.customsTermianlForBusLock.notifyAll();
									}
								}
							}
							
							else if(entry.getKey().equals("ck"))
							{
								
								boolean status=false;
								synchronized(Vehicle.ck) {
									if(entry.getValue()>0) {
										Vehicle.ck.setBlocked(false);
										System.out.println(Vehicle.ck.isBlocked());
										status=true;
										
									}
									else
									{
										Vehicle.ck.setBlocked(true);
										System.out.println("Jesam li blokiran: "+Vehicle.ck.isBlocked());
										
									}
								}
								if(status) {
									synchronized(Truck.customsTerminalForTruckLock) {
										Truck.customsTerminalForTruckLock.notifyAll();
									}
								}
							}
							
							
							
							
						}
					}
					
				}
				boolean valid=key.reset();
				if(!valid)
				{
					break;
				}
			}
		}
		catch(IOException ex)
		{
			//System.out.println(ex.getMessage());
			Main.myLogger.logger.warning(ex.getMessage());
		}
	}
}
