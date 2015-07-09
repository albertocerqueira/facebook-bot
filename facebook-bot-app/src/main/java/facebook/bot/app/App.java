package facebook.bot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	final static Logger logger = LoggerFactory.getLogger(App.class);

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		logger.info("starting app");

		new Thread().sleep(Process.in1Second.getTime());
	    
		logger.info("starting thread TSearch");
		TSearch tSearch = new TSearch(Process.in3Seconds.getTime());
		tSearch.start();
		
		new Thread().sleep(Process.in1Second.getTime());
		
		logger.info("starting thread TGroup");
		TGroup tGroup = new TGroup(Process.in3Seconds.getTime());
		tGroup.start();
		
		new Thread().sleep(Process.in1Second.getTime());
		
		logger.info("starting thread TUser");
		TUser tUser = new TUser(Process.in3Seconds.getTime());
		tUser.start();
	}
}