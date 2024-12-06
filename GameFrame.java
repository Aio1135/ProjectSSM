import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GameFrame extends JFrame {
    private TextSource textSource = new TextSource();
    private ScorePanel scorePanel = new ScorePanel();
    private EditPanel editPanel = new EditPanel(textSource);
    private GamePanel gamePanel = new GamePanel(textSource, scorePanel);

    public GameFrame() {
        setTitle("Fish Hunter");
        setSize(800, 600);
        makeMenu();
        makeSplit();
        this.setResizable(false);
        setVisible(true);
    }

    private void makeSplit() {
        JSplitPane hPane = new JSplitPane();
        hPane.setDividerLocation(650);
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
        JMenuItem begginerMode = new JMenuItem("Begginer"); // 난이도1
        JMenuItem hardMode = new JMenuItem("Hard"); // 난이도2
        JMenuItem hellMode = new JMenuItem("Hell"); // 난이도3

        startMenu.add(begginerMode);
        startMenu.add(hardMode);
        startMenu.add(hellMode);

        // 난이도별 게임 시작
        begginerMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gamePanel.setSpeed(600);
                gamePanel.startGame();
            }
        });

        hardMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gamePanel.setSpeed(400);
                gamePanel.startGame();
            }
        });

        hellMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gamePanel.setSpeed(250);
                gamePanel.startGame();
            }
        });

        // 일시정지 버튼
        JMenuItem stopItem = new JMenuItem("일시정지");
        fileMenu.add(stopItem);
        stopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            }
        });

        fileMenu.addSeparator();

        // 종료 버튼
        JMenuItem exitItem = new JMenuItem("게임종료");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // 프로그램 종료
            }
        });
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
