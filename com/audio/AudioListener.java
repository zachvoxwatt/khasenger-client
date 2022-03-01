package audio;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioListener implements LineListener
{
	private boolean debugON;
	private SoundFile sound;
	
	public AudioListener(SoundFile s, boolean shouldDebug)
	{
		this.sound = s;
		this.debugON = shouldDebug;
	}
	
	@Override
	public void update(LineEvent event) 
	{
		LineEvent.Type let = event.getType();
		
		if (debugON) System.out.printf("LineEvent Type: %s\n", let.toString());
		if (this.sound.isPaused() || this.sound.isLooping()) return;
		
		if (let.equals(LineEvent.Type.STOP))
		{
			this.sound.closeAssets();
			this.sound.getSchedule().cancel(true);
			this.sound.markAsUnused();
		}
	}
}