import java.io.Serializable;
import java.util.*;
public class Person implements Serializable {
	private transient Random rand = new Random();
	protected String name;
	public static int globalId=1;
	protected String type;
	private boolean IllegalDocument=false;
	protected String vehicleName;
     public Person()
     {
    	 super();
    	 this.name = "Ime" + rand.nextInt(500)+String.valueOf(globalId++);
     }
     protected transient boolean hasIdDocument = true;
    
     
     public String toString()
     {
    	 return this.name;
     }
     
     public String getType()
     {
    	 return "";
     }
     
     public String getVehicleName() {
    	 return this.vehicleName;
     }
     
     public void setVehicleName(String name) {
    	 this.vehicleName=name;
     }
     
     public boolean hasPassengerLuggage()
     {
    	 return rand.nextInt(100)<70;
     }

	public boolean isIllegalDocument() {
		return IllegalDocument;
	}

	public void setIllegalDocument(boolean illegalDocument) {
		IllegalDocument = illegalDocument;
	}
     
     
     

     
     
}
