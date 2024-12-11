import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GameFrame extends JFrame {
    private TextSource textSource = new TextSource();
    private ScorePanel scorePanel = new ScorePanel();
    private EditPanel editPanel = new EditPanel(textSource);
    private GamePanel gamePanel = new GamePanel(textSource, scorePanel);
    
    private MusicControl backgroundMusic; //배경음 컨트롤 객체
    
    public GameFrame() {
        setTitle("Fish Hunter");
        setSize(1200, 600);
        makeMenu();
        makeSplit();
        this.setResizable(false);
        
        backgroundMusic = new MusicControl("C:/자바학습/MiniProject/src/Aquarium.wav");
        backgroundMusic.play(true);
        
        setVisible(true);
    }
    
    private void makeSplit() {
        JSplitPane hPane = new JSplitPane();
        hPane.setDividerLocation(900);
        getContentPane().add(hPane, BorderLayout.CENTER);

        JSplitPane vPane = new JSplitPane();
        vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        vPane.setDividerLocation(300);
        vPane.setTopComponent(scorePanel);
        vPane.setBottomComponent(editPanel);

        hPane.setRightComponent(vPane);
        hPane.setLeftComponent(gamePanel);
    }
    private void makeMenu() {
        JMenuBar mb = new JMenuBar();
        this.setJMenuBar(mb);

        JMenu fileMenu = new JMenu("실행메뉴");
        mb.add(fileMenu);
        
        JMenu startMenu = new JMenu("시작");
        fileMenu.add(startMenu);

        // 난이도 설정
        JMenuItem easyMode = new JMenuItem("Easy"); // 난이도1
        JMenuItem normalMode = new JMenuItem("Normal"); // 난이도2
        JMenuItem hardMode = new JMenuItem("Hard"); // 난이도3

        startMenu.add(easyMode);
        startMenu.add(normalMode);
        startMenu.add(hardMode);

        // 난이도별 게임 시작
        easyMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String nickname = JOptionPane.showInputDialog("사용자 닉네임을 입력하세요:");
                if (nickname != null && !nickname.trim().isEmpty()) {
                    gamePanel.setPlayerName(nickname); // 닉네임 저장
                    gamePanel.setSpeed(600);
                    gamePanel.startGame();
                }
            }
        });

        normalMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String nickname = JOptionPane.showInputDialog("사용자 닉네임을 입력하세요:");
            	if (nickname != null && !nickname.trim().isEmpty()) {
                    gamePanel.setPlayerName(nickname); // 닉네임 저장
                	gamePanel.setSpeed(400);
                    gamePanel.startGame();
            	}
            }
        });

        hardMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String nickname = JOptionPane.showInputDialog("사용자 닉네임을 입력하세요:");
            	if (nickname != null && !nickname.trim().isEmpty()) {
                    gamePanel.setPlayerName(nickname); // 닉네임 저장
                	gamePanel.setSpeed(200);
                    gamePanel.startGame();
            	}
            }
        });

        // 일시정지 버튼
        JMenuItem stopItem = new JMenuItem("일시정지");
        fileMenu.add(stopItem);
        stopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gamePanel.pauseGame();
            }
        });
        
        // 게임재개 버튼
        JMenuItem resumeItem = new JMenuItem("게임재개");
        fileMenu.add(resumeItem);
        resumeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gamePanel.resumeGame();
            }
        });
        
        fileMenu.addSeparator();

        // 종료 버튼
        JMenuItem exitItem = new JMenuItem("게임종료");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	backgroundMusic.stop();
                System.exit(0); // 프로그램 종료
            }
        });
        
        JMenu musicMenu = new JMenu("배경음메뉴");
        mb.add(musicMenu);
        JMenuItem musicStartMenu = new JMenuItem("음악재생");
        JMenuItem musicStopMenu = new JMenuItem("음악정지");
        musicMenu.add(musicStartMenu);
        musicMenu.add(musicStopMenu);
        
        musicStartMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!backgroundMusic.isPlaying()) { //현재 재생중이지 않으면 재생
	        		backgroundMusic.play(true);
	        	}
			}
        });
        
        musicStopMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(backgroundMusic.isPlaying()) { //현재 재생중이면 정지
	        		backgroundMusic.stop();
	        	}
			}
        });
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
