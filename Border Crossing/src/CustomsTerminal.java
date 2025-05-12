import java.util.*;
public class CustomsTerminal {
	private boolean isReserved;
	private boolean isBlocked;
	public CustomsTerminal()
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
