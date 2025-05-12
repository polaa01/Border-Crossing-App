import java.io.File;
import java.io.IOException;
import java.util.logging.*;
public class MyLogger {
	public Logger logger;
	public FileHandler fileHandler;
	
	public MyLogger(String fileName) {
		File file = new File(fileName);
		try {
		fileHandler = new FileHandler(fileName,true);
		}catch (IOException ex) {
			System.exit(-1);
		}
		logger = Logger.getLogger(fileName);
		logger.addHandler(fileHandler);
		SimpleFormatter simpleFormatter = new SimpleFormatter();
		fileHandler.setFormatter(simpleFormatter);
		logger.setLevel(Level.WARNING);
			
	}
	

}
