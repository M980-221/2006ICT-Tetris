package com.pg2.tetris;

import javafx.scene.media.AudioClip;
import java.io.IOException;
import java.nio.*;
import java.nio.file.*;

/** Thread-safe lazy Singleton controlling generated, copyright-free game audio. */
public final class AudioManager {
    private volatile boolean musicEnabled; private volatile boolean soundEnabled;
    private AudioClip music; private AudioClip moveEffect; private AudioClip rotateEffect;
    private AudioClip dropEffect; private AudioClip lockEffect; private AudioClip lineEffect;
    private AudioClip gameOverEffect; private AudioClip pauseEffect;
    private AudioManager(){try{
        music=clip(new double[]{261.63,329.63,392.00,329.63,293.66,349.23,440.00,349.23},.22);music.setCycleCount(AudioClip.INDEFINITE);music.setVolume(.12);
        moveEffect=effect(new double[]{330},.025,.10);
        rotateEffect=effect(new double[]{440,554},.035,.16);
        dropEffect=effect(new double[]{392,262},.045,.20);
        lockEffect=effect(new double[]{196},.055,.14);
        lineEffect=effect(new double[]{523.25,659.25,783.99},.07,.28);
        gameOverEffect=effect(new double[]{392,330,262,196},.12,.25);
        pauseEffect=effect(new double[]{294,220},.055,.15);
    }catch(IOException|RuntimeException e){System.err.println("Audio unavailable: "+e.getMessage());}}
    private static class Holder{private static final AudioManager INSTANCE=new AudioManager();}
    public static AudioManager getInstance(){return Holder.INSTANCE;}
    public synchronized void apply(GameSettings s){soundEnabled=s.isSoundEffects();setMusic(s.isMusic());}
    public synchronized boolean toggleMusic(){setMusic(!musicEnabled);return musicEnabled;}
    private void setMusic(boolean enabled){musicEnabled=enabled;if(music==null)return;if(enabled&&!music.isPlaying())music.play();else if(!enabled)music.stop();}
    public boolean toggleSound(){return soundEnabled=!soundEnabled;}
    public void move(){play(moveEffect);}
    public void rotate(){play(rotateEffect);}
    public void hardDrop(){play(dropEffect);}
    public void lock(){play(lockEffect);}
    public void lineClear(){play(lineEffect);}
    public void gameOver(){play(gameOverEffect);}
    public void pause(){play(pauseEffect);}
    private void play(AudioClip value){if(soundEnabled&&value!=null)value.play();}
    private AudioClip effect(double[] notes,double seconds,double volume)throws IOException{AudioClip value=clip(notes,seconds);value.setVolume(volume);return value;}
    private AudioClip clip(double[] notes,double seconds) throws IOException{
        int rate=22050;int samples=(int)(rate*seconds*notes.length);ByteBuffer b=ByteBuffer.allocate(44+samples*2).order(ByteOrder.LITTLE_ENDIAN);
        b.put("RIFF".getBytes()).putInt(36+samples*2).put("WAVEfmt ".getBytes()).putInt(16).putShort((short)1).putShort((short)1).putInt(rate).putInt(rate*2).putShort((short)2).putShort((short)16).put("data".getBytes()).putInt(samples*2);
        for(int i=0;i<samples;i++){double f=notes[Math.min(notes.length-1,(int)(i/(rate*seconds)))];double envelope=Math.min(1,(i%(int)(rate*seconds))/300.0);b.putShort((short)(Math.sin(2*Math.PI*f*i/rate)*9000*envelope));}
        Path p=Files.createTempFile("tetris-audio-",".wav");Files.write(p,b.array());p.toFile().deleteOnExit();return new AudioClip(p.toUri().toString());
    }
}
