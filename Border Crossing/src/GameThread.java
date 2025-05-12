
public class GameThread extends Thread {

	@Override
	public void run() {
		try {
			Main.isStarted = true;
	        Vehicle.startTime = System.currentTimeMillis();
	        BorderFrame.timer.schedule(BorderFrame.task, 0, 1000);
	        for (Vehicle v : Main.vehicles) {
				v.start();
			}
			for (Vehicle v : Main.vehicles)
				v.join();
			BorderFrame.timer.cancel();
			
		} catch (InterruptedException e) {

		}
	}

}
