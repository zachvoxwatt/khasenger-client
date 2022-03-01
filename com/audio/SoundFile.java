package audio;

import java.net.URL;
import java.util.concurrent.ScheduledFuture;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundFile
{
	private int pauseMark = 0, lastMark = 0;
	private boolean playing = false, paused = false, looping = false, muted = false, muteAction = false, unused = false;
	private boolean DEBUG_FRAMES = false, DEBUG_CLIPS = false;
	
	private URL url;
	private String audioType = "";
	private Clip clip;
	private AudioInputStream ais;
	private AudioController aud;
	private AudioListener alr;
	private Runnable timer;
	private FloatControl volume;
	private ScheduledFuture<?> scheF;
	
	public SoundFile(URL u, String type, AudioController ad, boolean preOpenLine)
	{
		this.url = u;
		this.audioType = type;
		this.aud = ad;
		
		try
		{
			this.clip = AudioSystem.getClip();
			ais = AudioSystem.getAudioInputStream(this.url);
		}
	
		catch (Exception e) { e.printStackTrace(); }
		
		timer = new Runnable()
			{
				public void run()
				{
					if (DEBUG_FRAMES)
					{	
						int currMark = clip.getFramePosition();
						for (int i = 0; i < 2; i++) System.out.println();
						System.out.println("Clip length: " + clip.getFrameLength());
						System.out.println("Clip curpos: " + clip.getFramePosition());
						System.out.printf("Sample Rate: %d\n", currMark - lastMark);
						lastMark = clip.getFramePosition();
					}
				}
		};
		
		if (preOpenLine)
		{
			try { clip.open(ais); } catch (Exception e) { e.printStackTrace(); }
			volume = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
			
			alr = new AudioListener(this, this.DEBUG_CLIPS);
			this.clip.addLineListener(alr);
		}
	}
	
	public void play()
	{
		if (!playing) 
		{
			clip.start();
			playing = true;
			return;
		}
		
		else
		{
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	public void replay()
	{
		clip.start();
		playing = true;
	}
	
	public void resume()
	{
		if (playing && paused)
		{
			clip.setFramePosition(pauseMark);
			clip.start();
			paused = false;
		}
	}
	
	public void pause() 
	{

		this.paused = true;
		this.clip.stop();
		
		int framelength = this.clip.getFrameLength();
		int framepos = this.clip.getFramePosition();
		
		double diff = framepos / framelength;
		
		if (diff < 1) this.pauseMark = framepos;
		else this.pauseMark = (int) ((int) framepos - framelength * (int) diff);
		
		System.out.printf("\n%d\n%d\n%.2f\n%d\n", framelength, framepos, diff, pauseMark);
	}
	
	public void toggleLoop() 
	{ 
		if (looping)
		{
			this.clip.loop(0);
			this.looping = false;
			this.clip.addLineListener(this.alr);
		}
		else
		{
			this.clip.loop(Clip.LOOP_CONTINUOUSLY);
			this.looping = true;
			this.clip.removeLineListener(this.alr);
		//	this.clip.addLineListener(this.apll);
		}
	}
	
	//unused, cancelled feature
	public void toggleMute()
	{
		if (muted)
		{
			muted = false;
			muteAction = true;
			pause();
			muteAction = false;
			volume.setValue(0f);
			resume();
			return;
		}
		
		else
		{
			muted = true;
			muteAction = true;
			pause();
			muteAction = false;
			volume.setValue(-80f);
			resume();
			return;
		}
			
	}
	
	public void stop() { this.clip.stop(); this.playing = false; }
	
	public void closeAssets()
	{
		try
		{
			ais.close();
			clip.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	void reopenLine()
	{
		try 
		{ 
			this.clip = AudioSystem.getClip();
			this.clip.open(this.ais); 
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public void markAsUnused() { this.unused = true; }
	public void assignSchedule(ScheduledFuture<?> sf) { this.scheF = sf; }
	public void shouldDebugFrames(boolean b) { this.DEBUG_FRAMES = b; }
	
	public long getPauseMark() { return this.pauseMark; }
	public boolean isUnused() { return this.unused; }
	public boolean isPaused() { return this.paused; }
	public boolean isPlaying() { return this.playing; }
	public boolean isLooping() { return this.looping; }
	public boolean muteOnAct() { return this.muteAction; }
	public boolean debuggingFramesEnabled() { return this.DEBUG_FRAMES; }
	
	public AudioController getAudioController() { return this.aud; }
	public ScheduledFuture<?> getSchedule() { return this.scheF; }
	public String getAudioType() { return this.audioType; }
	public Runnable getTimerJob() { return this.timer; }
	public URL getSoundURL() { return url; }
	public SoundFile clone() { return new SoundFile(this.url, this.audioType, this.aud, true); } 
}