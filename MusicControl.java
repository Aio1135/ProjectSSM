import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicControl {
    private Clip clip;

    //오디오 파일 로드
    public MusicControl(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            return;
        }
    }

    //음악 재생 함수
    public void play(boolean loop) {
        if (clip != null) { //이미 음악 실행중이면 다시 재생 x
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY); //무한 반복
            }
            clip.start();
        }
    }

    //음악 중지 함수
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    
    //음악 재생 여부 확인
    public boolean isPlaying() {
        return clip != null && clip.isRunning(); //오디오파일이 있고, 재생중이면 true반환
    }
}
