package facebook.bot.app;

public enum Process {

	in1Second(1000),//1 second
	in2Seconds(2 * 1000),//2 seconds
	in3Seconds(3 * 1000),//3 seconds
	in5Seconds(5 * 1000),//5 seconds
	in8Seconds(8 * 1000),//8 seconds
	in10Seconds(10 * 1000),//10 seconds
	in13Seconds(13 * 1000),//13 seconds
	in14Seconds(14 * 1000),//14 seconds
	in15Seconds(15 * 1000),//15 seconds
	in20Seconds(20 * 1000),//20 seconds
	in30Seconds(30 * 1000),//30 seconds
	in40Seconds(40 * 1000),//40 seconds
	in90Seconds(90 * 1000),//90 seconds
	in1Minute(60 * 1000),//1 minute
	in2Minutes(2 * 60 * 1000),//2 minute
	in8Minutes(8 * 60 * 1000),//8 minute
	in10Minutes(10 * 60 * 1000),//10 minutes
	in15Minutes(15 * 60 * 1000),//15 minutes
	in30Minutes(30 * 60 * 1000),//30 minutes
	in40Minutes(40 * 60 * 1000),//40 minutes
	in50Minutes(50 * 60 * 1000),//50 minutes
	inOnehour(60 * 60 * 1000),//1 hour
	in90Minutes(90 * 60 * 1000),//1 hour and 30 minutes
	in2Hours(2 * 60 * 60 * 1000),//2 hours
	in3Hours(3 * 60 * 60 * 1000);//3 hours
	
	public final long time;
	
	private Process(long time){
		this.time = time;
	}
		
	public long getTime(){
		return time;
	}
}