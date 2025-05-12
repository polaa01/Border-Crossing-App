import java.util.*;
public class PoliceTerminal {
	private boolean isReserved=false;
	private boolean isBlocked=false;
	public PoliceTerminal()
	{
		super();
	}
	
	public boolean isReserved()
	{
		return this.isReserved;
	}
	
	public void setReserved(boolean isReserved)
	{
		this.isReserved = isReserved;
	}
	
	public boolean isBlocked()
	{
		return this.isBlocked;
	}
	
	public void setBlocked(boolean isBlocked)
	{
		this.isBlocked = isBlocked;
	}

}
