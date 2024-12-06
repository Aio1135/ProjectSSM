import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GamePanel extends JPanel {
    private TextSource textSource = new TextSource();
    private ScorePanel scorePanel = null;
    private JTextField text = new JTextField(10);
    private GameGroundPanel ground = new GameGroundPanel();
    private InputPanel input = new InputPanel();
    private int speed = 300; // 단어의 기본 이동속도 설정
    
    ImageIcon icon = new ImageIcon("playerImg.png");
    private JLabel playerLabel = new JLabel(icon);
    
    public GamePanel(TextSource textSource, ScorePanel scorePanel) {
        this.textSource = textSource;
        this.scorePanel = scorePanel;
        this.setLayout(new BorderLayout());
        add(ground, BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);
        
        ground.add(playerLabel);
    }

    public void setSpeed(int speed) { // 실행 메뉴의 버튼 3개 선택 시 단어 이동속도 조절
        this.speed = speed;
    }

    public void startGame() {
        ground.initializeThreads(); // 단어와 스레드 초기화
        ground.startThreads(); // 단어들이 이동하기 시작
    }

    class GameGroundPanel extends JPanel {
        private JLabel[] fish = new JLabel[10];
        private MovingThread[] threads = new MovingThread[10];
        
        public GameGroundPanel() {
            setLayout(null);
            setBackground(Color.CYAN);
        }

        public void initializeThreads() {
            removeAll(); // 기존 레이블 제거
            add(playerLabel);
            playerLabel.setBounds(0, 200, 100, 100);
            System.out.println(playerLabel.getBounds());
            for (int i = 0; i < fish.length; i++) {
                fish[i] = new JLabel(textSource.get());
                fish[i].setSize(100, 20);
                fish[i].setLocation(600, (int) (Math.random() * 400)); // 오른쪽 끝에서 시작
                fish[i].setForeground(Color.BLACK);
                add(fish[i]);

                threads[i] = new MovingThread(fish[i]); // 새로운 스레드 생성
            }
            repaint();
        }

        public void startThreads() {
            for (MovingThread thread : threads) {
                if (thread != null) {
                    thread.start(); // 각 단어의 스레드 시작
                }
            }
            repaint();
        }

        public void checkInput(String input) {
            for (int i = 0; i < fish.length; i++) { // 현재 이동 중인 단어들 중 일치하는지 확인
                if (fish[i].getText().equals(input)) { // 사용자 입력값과 단어가 일치하면
                    remove(fish[i]); // 화면에서 제거
                    threads[i].stopThread(); // 스레드 중지
                    scorePanel.increase(); // 점수 증가
                    repaint();

                    // 단어 새로 생성
                    fish[i] = new JLabel(textSource.get()); // txt에서 단어 하나 가져옴
                    fish[i].setSize(100, 20);
                    fish[i].setLocation(700, (int) (Math.random() * 400));
                    fish[i].setForeground(Color.BLACK);
                    add(fish[i]);
                    threads[i] = new MovingThread(fish[i]); // 새 스레드 생성
                    threads[i].start();
                    break;
                }
            }
        }
    }

    class MovingThread extends Thread {
        private JLabel label;
        private boolean running = true;

        public MovingThread(JLabel label) {
            this.label = label;
        }

        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    SwingUtilities.invokeLater(() -> {
                        int x = label.getX() - 5; // 왼쪽으로 이동
                        if (x < 0) { // 화면 밖으로 나가면 오른쪽 끝으로 재배치
                            label.setLocation(700, label.getY());
                        } else {
                            label.setLocation(x, label.getY());
                        }
                    });
                    Thread.sleep(speed); // 단어의 속도
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        public void stopThread() {
            running = false;
        }
    }

    class InputPanel extends JPanel {
        public InputPanel() {
            this.setBackground(Color.LIGHT_GRAY);
            add(text);

            text.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String input = text.getText();
                    if (input.length() == 0) return; // 공백이 입력되면 종료

                    ground.checkInput(input); // 입력된 단어와 일치 여부 확인하고 점수추가
                    text.setText(""); // 텍스트 필드 초기화
                }
            });
        }
    }
}
