package facebook.bot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	
	final static Logger logger = LoggerFactory.getLogger(App.class);

	// TODO: create design with spring-boot to seek popular entries by browser
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		logger.info("starting app");

		new Thread().sleep(Process.in1Second.getTime());
	    
		logger.info("starting thread TSearch");
		TSearch tSearch = new TSearch(Process.in10Seconds.getTime());
		tSearch.start();
		
		new Thread().sleep(Process.in1Second.getTime());
		
		logger.info("starting thread TGroup");
		TGroup tGroup = new TGroup(Process.in10Seconds.getTime());
		tGroup.start();
		
		new Thread().sleep(Process.in1Second.getTime());
		
		logger.info("starting thread TBot");
		TBot tBot = new TBot(Process.in15Minutes.getTime());
		tBot.start();
		
		// TODO: needs strategy
		//logger.info("starting thread TUser");
		//TUser tUser = new TUser(Process.in3Seconds.getTime());
		//tUser.start();
		
		loadTypes();
	}
	
	@SuppressWarnings("static-access")
	private static void loadTypes() throws InterruptedException {
		while (true) {
			TSearch.addWord("Tecnologia");
			TSearch.addWord("Startup");
			TSearch.addWord("Programação");
			TSearch.addWord("Desenvolvimento");
			
			TBot.addType(TSearch.type);
			TBot.addType(TGroup.type);
			
			new Thread().sleep(Process.in30Seconds.getTime());
		}
	}
}