import java.awt.Color;
import java.io.*;

import javax.swing.*;

public class ScorePanel extends JPanel {
    private int score = 0;
    private JLabel scoreLabel = new JLabel(Integer.toString(score));
    private JTextArea rankingArea = new JTextArea(10, 20);

    public ScorePanel() {
        this.setBackground(Color.YELLOW);
        this.add(new JLabel("현재 점수:"));
        this.add(scoreLabel);

        // 랭킹이 보여지는 구역 설정
        rankingArea.setEditable(false);
        rankingArea.setLineWrap(true);
        rankingArea.setWrapStyleWord(true);
        this.add(new JScrollPane(rankingArea));
        
        loadRanking(); // 초기 랭킹 표시
    }
    
    public void resetScore() { //게임 재시작시 스코어 리셋
    	score = 0;
    	scoreLabel.setText(Integer.toString(score));
    }
    
    public int getScore() { //랭킹 저장시 점수 불러오기
    	return score;
    }
    
    public void increase() {
        score += 10;
        scoreLabel.setText(Integer.toString(score));
    }
    
    // 랭킹 불러오기
    public void loadRanking() {
        try (BufferedReader br = new BufferedReader(new FileReader("Ranking.txt"))) {
            StringBuilder rankingText = new StringBuilder();
            String line;
            int rank = 1;
            while ((line = br.readLine()) != null && rank <= 5) {
                rankingText.append(rank++).append("위 : ").append(line).append("\n");
            }
            rankingArea.setText(rankingText.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 점수 업데이트 시 랭킹 갱신
    public void updateRanking() {
        loadRanking();
    }
}
