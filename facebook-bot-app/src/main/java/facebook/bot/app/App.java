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
		TSearch tSearch = new TSearch(Process.in1Second.getTime());
		tSearch.start();
		tSearch.addWord("palmeiras");
		
		new Thread().sleep(Process.in1Second.getTime());
		
		logger.info("starting thread TGroup");
		TGroup tGroup = new TGroup(Process.in1Second.getTime());
		tGroup.start();
		
		new Thread().sleep(Process.in1Second.getTime());
		
		logger.info("starting thread TBot");
		TBot tBot = new TBot(Process.in30Seconds.getTime());
		tBot.start();
		tBot.addType(TSearch.type);
		tBot.addType(TGroup.type);
		
		/*
		logger.info("starting thread TUser");
		TUser tUser = new TUser(Process.in3Seconds.getTime());
		tUser.start();
		*/
	}
}