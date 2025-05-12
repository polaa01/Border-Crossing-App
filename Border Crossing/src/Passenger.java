import java.util.*;
public class Passenger extends Person {
	private Random rand = new Random();
	protected boolean hasInvalidDoc=false;
	public Passenger() {
		super();
		this.type = "Passenger";
		
	}

	
	public String toString()
	{
		return super.toString();
	}
	
	public String getType()
	{
		return type;
	}
}
