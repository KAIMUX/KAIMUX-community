package lt.itsvaidas.ZCAPI;

import java.text.DecimalFormat;

public class ProcessTimer {
	
	long start;
	long end;
	long spent;
	int current = 0;
	int total = 0;

	public ProcessTimer()
	{
	}

	public ProcessTimer(int total)
	{
		this.total = total;
	}

	public void setCurrent(int current)
	{
		this.current = current;
	}

	public void start()
	{
		start = System.currentTimeMillis();
	}
	
	public void end()
	{
		end = System.currentTimeMillis();
		spent = end - start;
	}
	
	public String tookTime()
	{
		return formatTime(spent);
	}
	
	private String formatTime(long time)
	{
		long mil = time;
		long sec = 0;
		long min = 0;
		long hour = 0;
		
		while (mil >= 1000) {
			mil -= 1000;
			sec++;
			if (sec >= 60) {
				sec = 0;
				min++;
				if (min >= 60) {
					min = 0;
					hour++;
				}
			}
		}
		
		DecimalFormat decimalFormat = new DecimalFormat("00");
		
		if (hour != 0) {
			return decimalFormat.format(hour) + "h. " 
					+ decimalFormat.format(min) + "min. "
					+ decimalFormat.format(sec) + "sec. "
					+ mil + "ms.";
		}
		if (min != 0) {
			return decimalFormat.format(min) + "min. "
					+ decimalFormat.format(sec) + "sec. "
					+ mil + "ms.";
		}
		if (sec != 0) {
			return decimalFormat.format(sec) + "sec. "
					+ mil + "ms.";
		}
		return mil + "ms.";
	}

	public String getProgress() {
		return current + "/" + total;
	}
}
