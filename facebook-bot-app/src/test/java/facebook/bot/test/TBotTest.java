package facebook.bot.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook.bot.app.Process;
import facebook.bot.app.TBot;
import facebook.bot.cassandra.test.FacebookPostTest;

public class TBotTest {

	final static Logger logger = LoggerFactory.getLogger(TBotTest.class);

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {
		logger.info("starting app");

		new Thread().sleep(Process.in10Seconds.getTime());

		logger.info("starting thread TBot");
		TBot tBot = new TBot(Process.in90Seconds.getTime(), 5);
		tBot.start();
		
		new Thread().sleep(Process.in15Seconds.getTime());
		
		loadTypes();
	}
	
	@SuppressWarnings("static-access")
	private static void loadTypes() throws InterruptedException {
		FacebookPostTest fpt = new FacebookPostTest();
		fpt.quantity = 30;
		while (true) {
			fpt.insert_facebook_post();
			TBot.addType(FacebookPostTest.type);
			
			new Thread().sleep(Process.in30Seconds.getTime());
		}
	}
}