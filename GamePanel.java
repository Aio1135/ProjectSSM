import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.*;

public class GamePanel extends JPanel {
    private TextSource textSource = new TextSource();
    private ScorePanel scorePanel = null;
    private JTextField text = new JTextField(10);
    private GameGroundPanel ground = new GameGroundPanel();
    private HealthPanel healthPanel = new HealthPanel();
    private InputPanel input = new InputPanel();
    private MusicControl eventMusics[] = new MusicControl[3]; //이벤트 효과음 객체
    private int speed = 300; // 단어의 기본 이동속도 설정
    private boolean gameOver = false; // 게임 오버 상태 확인용
    private String playerName = "Unknown"; // 기본 플레이어 이름
    private int score = 0;
    
    ImageIcon icon = new ImageIcon("playerImg.png");
    private JLabel playerLabel = new JLabel(icon);
    
    public GamePanel(TextSource textSource, ScorePanel scorePanel) {
        this.textSource = textSource;
        this.scorePanel = scorePanel;
        this.setLayout(new BorderLayout());
        add(ground, BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);
        add(healthPanel, BorderLayout.NORTH);
        ground.add(playerLabel);
    }
    
    public void setSpeed(int speed) { // 실행 메뉴의 버튼 3개 선택 시 단어 이동속도 조절
        this.speed = speed;
    }

    public void startGame() {
    	scorePanel.resetScore(); //게임 재시작시 스코어 리셋
    	System.out.println("체력리셋됨");
        healthPanel.resetHealth(); //체력 초기화
    	ground.stopThreads(); //기존에 진행하고 있던 게임스레드 중지
        ground.initializeThreads(); //단어와 스레드 초기화
        ground.startThreads(); //단어들이 이동하기 시작
        gameOver = false; //게임 오버 초기화
    }
    
    public void pauseGame() {
        ground.pauseGame(); // 게임 일시정지호출
    }

    public void resumeGame() {
        ground.resumeGame(); // 게임재개 호출
    }

    public void stopGame() {
        ground.stopThreads(); // 게임 오버시 스레드 종료 호출
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public class GameGroundPanel extends JPanel {
        private JLabel[] fish = new JLabel[10]; //물고기 객체
        private MovingThread[] threads = new MovingThread[10];
        private double eventProbability = 0.03; //폭탄 및 보물상자 생성확률 
        private Random random = new Random();
        
        private ImageIcon bgIcon = new ImageIcon("backgroundImg.jpg"); //배경화면
        private Image backgroundImg = bgIcon.getImage();       
        private ImageIcon bombIcon = new ImageIcon("bomb.png"); //폭탄이미지
        private ImageIcon treasureIcon = new ImageIcon("treasure.png"); //보물상자이미지
        private ImageIcon[] fishIcons = new ImageIcon[] { //물고기이미지
        		new ImageIcon("fish1.png"),
        		new ImageIcon("fish2.png"),
        		new ImageIcon("fish3.png")
        };
        
        public GameGroundPanel() {
            setLayout(null);
            setBackground(Color.CYAN);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this); //배경 이미지 그리기
        }
        
        public void initializeThreads() {
            removeAll(); // 기존 레이블 제거
            add(playerLabel);
            playerLabel.setBounds(0, 200, 100, 100);
            System.out.println(playerLabel.getBounds());
            
            for (int i = 0; i < fish.length; i++) {
            	createNewFish(i);
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
        
        public void pauseGame() {
            for (MovingThread thread : ground.threads) {
                if (thread != null) {
                    thread.pauseThread(); // 각 스레드 일시정지
                }
            }
        }

        // 모든 스레드 재개
        public void resumeGame() {
            for (MovingThread thread : ground.threads) {
                if (thread != null) {
                    thread.resumeThread(); // 각 스레드 재개
                }
            }
        }
        
        public void stopThreads() {
        	for (MovingThread thread : ground.threads) {
                if (thread != null) {
                    thread.stopThread(); //모든 스레드 종료
                }
            }
        }
        
        public void createNewFish(int i) { //단어가 입력되고 나서 새로 생성
        	ImageIcon randomFishIcon = fishIcons[(int) (Math.random() * fishIcons.length)];
        	String word = textSource.get(); //txt파일에서 단어 가져옴
        	double event = random.nextDouble();
        	if(event < eventProbability / 2) { //폭탄 생성
        		fish[i] = new JLabel(word, bombIcon, JLabel.CENTER);
        		fish[i].putClientProperty("event", "bomb");
        	}
        	else if(event < eventProbability) { //보물상자 생성
        		fish[i] = new JLabel(word, treasureIcon, JLabel.CENTER);
        		fish[i].putClientProperty("event", "treasure");
        	}
        	else { //물고기 생성
        		fish[i] = new JLabel(word, randomFishIcon, JLabel.CENTER);
        		fish[i].putClientProperty("event", "fish");
        	}
        	fish[i].setSize(320, 100);
            fish[i].setLocation((int)((Math.random() * 150)+750), (int)(Math.random() * 400)); //x는 500~650 y는 0~400 사이로 설정
            fish[i].setForeground(Color.WHITE);
            fish[i].setFont(fish[i].getFont().deriveFont(15f));
            add(fish[i]);
            threads[i] = new MovingThread(fish[i]); // 새로운 스레드 생성
            threads[i].start();
        }
        
        public void checkInput(String input) {
            eventMusics[1] = new MusicControl("C:/자바학습/MiniProject/gun.wav");
            eventMusics[2] = new MusicControl("C:/자바학습/MiniProject/treasure.wav");
            for (int i = 0; i < fish.length; i++) { // 현재 이동 중인 단어들 중 일치하는지 확인
                if (fish[i].getText().equals(input)) { // 사용자 입력값과 단어가 일치하면
                    String eventType = (String)fish[i].getClientProperty("event"); //물고기, 보물상자, 폭탄 중 어느 것에 속하는지 구별
                    if(eventType.equals("bomb")) { //폭탄 단어를 친 경우
                    	for(int j=0; j < fish.length; j++) {
                    		remove(fish[j]);
                    		threads[j].stopThread();
                    		scorePanel.increase(5);
                    		createNewFish(j);
                    	}
                    }
                    else if(eventType.equals("treasure")) { //보물상자 단어를 친 경우
                    	eventMusics[2].play(false);
                        remove(fish[i]); // 화면에서 제거
                        threads[i].stopThread(); // 스레드 중지
                        scorePanel.increase(100); // 점수 증가
                        createNewFish(i);
                    }
                    else { //그냥 물고기인 경우
                        eventMusics[1].play(false);
                        remove(fish[i]); // 화면에서 제거
                        threads[i].stopThread(); // 스레드 중지
                        scorePanel.increase(10); // 점수 증가
                        createNewFish(i);
                    }
                    repaint();
                    break;
                }
            }
        }
    }
    
    
    public class HealthPanel extends JPanel{
    	private int startHealth = 4; //플레이어 체력 4로 시작
    	private int currentHealth; //현재 플레이어의 체력
    	private JLabel [] heartLabels;
    	private ImageIcon fullHeartIcon;
    	private ImageIcon emptyHeartIcon;
    	
    	public HealthPanel() {
    		this.setLayout(new FlowLayout(FlowLayout.LEFT));
    		this.setBackground(Color.PINK);

            // 하트이미지 객체
            fullHeartIcon = new ImageIcon("fullHeartIMG.jpg");
            emptyHeartIcon = new ImageIcon("emptyHeartIMG.jpg");
            
            heartLabels = new JLabel[startHealth];
            for(int i = 0; i < startHealth; i++) {
            	heartLabels[i] = new JLabel(emptyHeartIcon); // 초기 상태는 모두 empty
                this.add(heartLabels[i]);
            }
            currentHealth = startHealth; //현재 체력 4로 시작 
            updateHealthDisplay(); // 초기 상태 업데이트
    	}
    	
    	// 체력 감소 함수
        public void decreaseHealth() {
            if (currentHealth > 0) {
                currentHealth--;
                updateHealthDisplay();
            }
        }

        // 체력 증가 함수
        public void increaseHealth() {
            if (currentHealth < startHealth) {
                currentHealth++;
                updateHealthDisplay();
            }
        }

        // 체력 UI 업데이트
        private void updateHealthDisplay() {
            for (int i = 0; i < startHealth; i++) {
                if (i < currentHealth) {
                    heartLabels[i].setIcon(fullHeartIcon); // 현재 체력 이하 부분은 fullHeart
                } else {
                    heartLabels[i].setIcon(emptyHeartIcon); // 나머지는 emptyHeart
                }
            }
        }
        
        //게임 재시작시 체력 초기화
        public void resetHealth() {
        	currentHealth = startHealth;
        	updateHealthDisplay();
        }
        
        // 현재 체력 반환
        public int getHealth() {
            return currentHealth;
        }
    	
    }
    
    public class MovingThread extends Thread {
        private JLabel label;
        private boolean running = true;
        private boolean stopFlag = false; //일시정지상태
        
        public MovingThread(JLabel label) {
            this.label = label;
        }
        
        private void pauseThread() {
        	stopFlag = true;
        }
        
        synchronized private void resumeThread() {
        	stopFlag = false;
        	this.notify();
        }
        
        private void stopThread() { //스레드 완전 종료
        	running = false;
        }
        
        synchronized private void checkWait() {
            while (stopFlag) {
                try {
                    this.wait(); //동기화함수
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        
        @Override
        public void run() {
        	eventMusics[0] = new MusicControl("C:/자바학습/MiniProject/crash.wav");
        	running = true;
            while (running) {
            	checkWait(); //일시정지인지 확인
                try {
                    SwingUtilities.invokeLater(() -> {
                        int x = label.getX() - 5; // 왼쪽으로 이동
                        if (x < 0) { // 화면 밖으로 나가면 오른쪽 끝으로 재배치하고 체력감소
                            label.setLocation(650, label.getY());
                            healthPanel.decreaseHealth(); // 체력 감소
                            eventMusics[0].play(false);
                                if (!isGameOver() && healthPanel.getHealth() <= 0) { // 체력이 0일 때
                                	setGameOver(true);
                                	saveScore(playerName);
                                	scorePanel.updateRanking();
                                    stopGame();
                                    JOptionPane.showMessageDialog(null, "Game Over!");
                                    JOptionPane.showMessageDialog(null, "실행메뉴를 통해 게임을 재시작하세요!");
                                }
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
    
    public void setPlayerName(String name) {
        this.playerName = name;
    }
    
    private void saveScore(String playerName) {
    	score = scorePanel.getScore(); //이번 게임에서 얻은 점수를 읽어옴
        List<String> scores = new ArrayList<>(); //ArrayList 객체 생성
        
        try (BufferedReader br = new BufferedReader(new FileReader("Ranking.txt"))) { //txt파일에 저장되어있는 점수 읽어오기
            String line;
            while ((line = br.readLine()) != null) { //txt파일에서 한줄씩 읽어오고, line에 저장
                scores.add(line); //line에 저장된 내용을 ArrayList에 추가
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 새로 입력된 점수 추가
        scores.add(playerName + "," + score); //이번 게임에서의 플레이어 이름이랑 점수를 콤마로 분리해서 리스트에 추가해줌
        
        //scores 리스트를 점수부분을 추출하여 내림차순으로 정렬  
        scores.sort((s1, s2) -> Integer.compare(Integer.parseInt(s2.split(",")[1]), Integer.parseInt(s1.split(",")[1])));

        // 상위 5명까지만 txt파일에 새로 저장(덮어쓰기)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Ranking.txt"))) {
            for (int i = 0; i < Math.min(5, scores.size()); i++) { //리스트 사이즈와 최대 저장수인 5를 비교해서 더 작은 값이 기준이 됨(공백 저장 방지)
                bw.write(scores.get(i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
