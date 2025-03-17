package MovingGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class FallingObjectGame {
    public static void main(String[] args) {

    }
}


class GamePanels extends JPanel implements ActionListener, KeyListener {
    private Player player;  // 플레이어 객체
    private ArrayList<FallingObject> fallingObjects;  // 떨어지는 물체들의 리스트
    private Timer timer;  // 게임 루프를 위한 타이머
    private Timer scoreTimer; // 게임을 진행할 시 축척되는 점수
    private boolean gameOver;  // 게임 오버 상태를 참과 거짓으로 나타내는 변수
    private int score;  // 시간당 점수를 저장하는 변수
    private int Highscore; // 사용자의 최고 점수를 기록하는 변수
    private JButton restartButton; // "다시 시작" 기능 버튼
    private String playerID;  // 유저 ID

    private Image backgroundImage;  // 배경 이미지를 위한 변수
    private Image gameOverBackground;  // 게임 오버 상태에서 사용할 배경 이미지

    public GamePanels(String playerID) {

        // ImageIO.read 명령어로 배경 이미지를 읽어 로드합니다.
        try {

            // 일반 배경 이미지
            backgroundImage = ImageIO.read(new File("src/MovingGame/game-vector-img.gif"));
            // 게임 오버 배경 이미지
            gameOverBackground = ImageIO.read(new File("src/MovingGame/GameOverImg.png"));

        } catch (IOException e) {  // IOException e의 e는 예외처리 발생 시 처리하는 코드
            e.printStackTrace();  // 예외처리된 e를 출력
        }

        fallingObjects = new ArrayList<>();  // 떨어지는 물체 리스트 초기화
        gameOver = false;  // 게임 오버 상태 초기화
        score = 0;

        this.playerID=playerID; //받아온 아이디


        // 플레이서 생성자를 생성함 (위치, 아이디)
        player = new Player(160, 460, playerID);  // ID를 플레이어 객체에 전달

        // 타이머 설정 (20ms마다 게임을 갱신)
        timer = new Timer(20, this); // Java의 기본 기능 Timer를 통해 만듦
        timer.start();  // 타이머 시작

        // 점수 갱신 타이머 (1초마다 점수 100점 증가)
        scoreTimer = new Timer(1000, new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {//게임 오버가 되지 않으면 점수가 증감 되도록 설정
                    score += 100;  // 점수 100점씩 증가
                    repaint();  // 화면 갱신

                    // 스코어를 갱신할 때 최고 점수가 스코어 보다 높으면 해당 점수를 갱신
                    if (score > Highscore) {
                        Highscore = score;
                        saveHighscore();  // 최고 점수 저장
                    }
                }
            }
        } );// 타이머 객체 안에 actionPerformed 메소드를 넣어 계산 되게함.
        scoreTimer.start();  // 점수 타이머 시작

        addKeyListener(this);  // 키 리스너 추가
        setFocusable(true);  // 패널이 키 입력을 받을 수 있도록 설정

        // 게임 오버 시 재시작 버튼 생성
        restartButton = new JButton("Restart"); // 리셋할 버튼 추가
        restartButton.setBounds(150, 450, 100, 50);  // 버튼 위치 설정
        restartButton.addActionListener(new ActionListener() {

            // 해당 버튼을 누르면 오버라이딩 된 actionPerformed 메소드가 작동 되도록 설정함
            @Override
            public void actionPerformed(ActionEvent e) {
                // 메소드를 오버라이딩 하여 기능을 이어받음
                resetGame();  // 게임 리셋
            }
        } );
        restartButton.setVisible(false);  // 기본적으로 버튼은 보이지 않음
        this.setLayout(null);  // 레이아웃을 null로 설정하여 버튼 위치를 수동으로 지정
        this.add(restartButton);  // 패널에 버튼 추가

        loadHighscore();  // 저장된 최고 점수 불러오기
    }

    // 화면을 그리는 메서드
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // 기본적인 컴포넌트의 paintComponent 호출

        if (gameOver) {
            // 게임 오버 상태일 때
            if (gameOverBackground != null) {
                // 게임 오버 배경 이미지를 그리기
                g.drawImage(gameOverBackground, 0, 0, getWidth(), getHeight(), null);
            }
        } else {
            // 게임이 진행 중일 때
            if (backgroundImage != null) {
                // 일반 배경 이미지를 그리기
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        }

        // 게임 오버 상태일 때 텍스트 표시
        if (gameOver) {
            g.setColor(new Color(67, 46, 84)); // 사용자 정의 컬러;
            g.setFont(new Font("Arial", Font.BOLD, 25));  // 폰트 설정
            g.drawString("Game Over", 100, 100);  // "Game Over" 메시지 출력
            g.drawString("Score: " + score, 100, 150);  // 점수 출력
            g.drawString("High score: " + Highscore, 100, 200);  // 최고 점수를 출력
            g.drawString("User: " + playerID, 100, 250);  // 유저 아이디 출력

            // 게임 오버 상태에서 "다시 시작" 버튼을 보이게 함
            restartButton.setVisible(true);
        } else {
            // 게임 진행 중일 때
            player.draw(g);  // 플레이어 그리기

            // 떨어지는 물체 그리기
            for (FallingObject obj : fallingObjects) {
                obj.draw(g);
            }

            // 점수 그리기 (화면 왼쪽 상단)
            g.setColor(new Color(67, 46, 84)); // 사 사용자 정의 컬러;
            g.setFont(new Font("Arial", Font.BOLD, 20));  // 폰트 설정
            g.drawString("Score: " + score, 10, 60);  // 점수 출력
            g.drawString("User : " + playerID, 10, 30);  // 유저 아이디 출력
        }
    }

    // 게임 로직을 처리하는 메서드 (타이머 이벤트마다 호출됨)
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // 게임이 종료되지 않았다면
            player.move();  // 플레이어 이동

            // 새로운 물체 생성 ( 5% 확률 간격으로 생성)
            if (Math.random() < 0.05) {
                fallingObjects.add(new FallingObject());
            }

            // 떨어지는 물체 위치 업데이트
            ArrayList<FallingObject> toRemove = new ArrayList<>();
            for (FallingObject obj : fallingObjects) {
                obj.fall();  // 물체 떨어지기
                if (obj.getY() > getHeight()) {  // 화면 밖으로 떨어졌다면
                    toRemove.add(obj);  // 리스트에서 제거할 물체 추가
                }
                // 충돌 체크
                if (player.collidesWith(obj)) {
                    gameOver = true;  // 충돌 시 게임 오버 처리
                }
            }
            fallingObjects.removeAll(toRemove);  // 떨어져서 제거된 물체들 삭제

            // 화면 갱신
            repaint();
        }
    }

    // 키 입력 처리 (좌우 화살표 키로 이동)
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.setDirection(-1);  // 왼쪽 키 눌렀을 때 왼쪽으로 이동
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setDirection(1);  // 오른쪽 키 눌렀을 때 오른쪽으로 이동
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setDirection(0);  // 방향키를 떼면 이동 멈춤
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // 게임 리셋 메서드
    private void resetGame() {
        player = new Player(160, 460, playerID);  // 플레이어 위치 초기화
        fallingObjects.clear();  // 떨어지는 물체 리스트 초기화
        score = 0;  // 점수 초기화
        gameOver = false;  // 게임 오버 상태 초기화
        restartButton.setVisible(false);  // 재시작 버튼 숨기기
        repaint();  // 화면 갱신
    }





    // 최고 점수를 저장하는 메서드
    private void saveHighscore() {

        if (playerID == null || playerID.trim().isEmpty()) {
            return; //id가 없으면 값을 계산하지 않음
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("src/MovingGame/highscore.txt"))) { //예외 처리로 해당 파일에 있는 txt를 읽음
            String line;
            boolean idExists = false;
            ArrayList<String> lines = new ArrayList<>();

            // 기존의 최고 점수 목록을 읽어옵니다.
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                if (line.startsWith(playerID + ":")) {
                    idExists = true;  // 해당 ID가 이미 존재하는지 확인
                }
            }

            // 해당 ID가 존재하지 않으면 새로운 기록 추가
            if (!idExists) {
                lines.add(playerID + ":" + score);
            } else {
                // 해당 ID가 이미 있으면 그 ID에 맞는 최고 점수를 갱신
                for (int i = 0; i < lines.size(); i++) {
                    String currentLine = lines.get(i);
                    if (currentLine.startsWith(playerID + ":")) {
                        String[] parts = currentLine.split(":");
                        int existingHighscore = Integer.parseInt(parts[1]);
                        if (score > existingHighscore) {
                            lines.set(i, playerID + ":" + score);  // 최고 점수를 갱신
                        }
                    }
                }
            }

            // 파일에 다시 저장 (덮어쓰기)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/MovingGame/highscore.txt"))) {
                for (String lineToWrite : lines) {
                    writer.write(lineToWrite);  // 각 라인을 파일에 기록
                    writer.newLine();  // 각 기록 후 새로운 줄 추가
                }
            }

        } catch (IOException e) {
            e.printStackTrace();  // IOException 발생 시 예외 출력
        }
    }

    // 저장된 최고 점수를 불러오는 메서드
    private void loadHighscore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/MovingGame/highscore.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String savedID = parts[0];
                    int savedHighscore = Integer.parseInt(parts[1]);
                    // 최고 점수가 더 높은 경우만 갱신
                    if (savedID.equals(playerID) && savedHighscore > Highscore) {
                        Highscore = savedHighscore;  // 최고 점수 갱신
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // IOException 발생 시 예외 출력
        }
    }
}

// 플레이어 클래스
class Player {
    private int x, y, speed, direction; //플래이어 x, y, speed / 이동하는 방향 설정
    private String id; //플레이어의 아이디를 설정
    private Image playerImage; // 플레이어 이미지 초기화

    public Player(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.speed = 5;
        this.direction = 0;
        this.id = id;
        try {
            playerImage = ImageIO.read(new File("src/MovingGame/PenguinImg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move() {
        if (direction == -1 && x > 0-40) { //플레이어가 왼쪽으로 움직일 수 있는 최대 display 영역을 정의함
            x -= speed;  // 왼쪽으로 이동
        } else if (direction == 1 && x < 400 -60) { //플레이어가 오른쪽으로 움직일 수 있는 최대 display 영역을 정의함
            x += speed;  // 오른쪽으로 이동
        }
    }

    public void draw(Graphics g) {
        // 플레이어 크기 설정 (80x90으로 변경)
        g.drawImage(playerImage, x, y, 80, 90, null);
    }

    public void setDirection(int direction) {
        //이동하는 방향을 -1 왼쪽 / 0 멈춤 / +1 오른쪽 으로 이동하도록 설정
        this.direction = direction;
    }

    // 충돌 감지 메서드
    public boolean collidesWith(FallingObject obj) {

        // 플레이어의 크기를 80x90으로 수정
        Rectangle playerRect = new Rectangle(x+15, y, 50, 90); // x+15를 하여 80사이즈의 중앙에 정렬플레이어 충돌 크기

        // FallingObject의 크기를 랜덤으로 반영
        Rectangle objectRect = new Rectangle(obj.getX(), obj.getY(), obj.getRandomInt(), obj.getRandomInt()); // 물체 크기
        return playerRect.intersects(objectRect); // 충돌 여부 확인
    }
}


// 떨어지는 물체 클래스
class FallingObject {
    private int x, y, speed, randomInt;
    private Image fallingImage;

    public FallingObject() {
        Random random = new Random();
        this.randomInt = 30 + random.nextInt(4) * 10;  // 물체 크기 랜덤
        this.x = random.nextInt(370);  // X 좌표
        this.y = 0;  // 떨어지는 위치 고정
        this.speed = random.nextInt(3) + 2;  // 물체 속도 랜덤
        try {
            fallingImage = ImageIO.read(new File("src/MovingGame/IcicleImg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fall() {
        y += speed;
    } // 떨어지는 y축 위치와 스피드를 추가하여 실시간 증감

    public void draw(Graphics g) {
        g.drawImage(fallingImage, x, y, randomInt, randomInt, null);
    }

    // randomInt 값 반환
    public int getRandomInt() {
        return randomInt;
    }

    // getter methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
