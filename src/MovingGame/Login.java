package MovingGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Login {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new LoginWindow()); // 만들어 놓은 LoginWindow 호출


    }
}


class LoginWindow {
    private JFrame frame;
    private JTextField idField;
    private JPasswordField passwordField;
    private String playerID;


    public LoginWindow() {
        frame = new JFrame("Login Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 3));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);// 프레임을 보이게 하는 메소드



        // ID 입력 필드
        JLabel idLabel = new JLabel("아이디:");
        frame.add(idLabel);
        // ID 텍스트 필드
        idField = new JTextField();
        frame.add(idField);


        // 비밀번호 입력 필드
        JLabel passwordLabel = new JLabel("비밀 번호:");
        frame.add(passwordLabel);
        // 비밀번호 텍스트 필
        passwordField = new JPasswordField();
        frame.add(passwordField);


        // 로그인 버튼
        JButton loginButton = new JButton("확인");
        loginButton.addActionListener(new SaveButtonListener());
        frame.add(loginButton);

        // 종료 버튼
        JButton plusButton = new JButton("회원가입");
        plusButton.addActionListener(e -> new AccessionWindow()); // 회원가입 액션 실행 버튼
        frame.add(plusButton);

    }

    private class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String playerID = idField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();


            // 입력한 ID혹은 비밀번호가 빈 공백일 경우
            // trim()은 문자의 앞뒤에 있는 공백 을 제거함 - isEmpty()는 문자열이 비어 있는지 확인하는 메소드이고 공백이면 true 이다.
            if (playerID.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "'아이디' 와 '비밀번호'를 입력해주세요.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }



            // 아이디와 비밀번호를 로그인 파일에서 검사
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("src/MovingGame/users.txt"), StandardCharsets.UTF_8))) { // UTF-8 인코딩으로 파일 읽기
                String line;
                boolean loginSuccessful = false;  // 로그인 성공 여부를 추적하는 변수

                // 파일의 각 줄을 읽으며 확인
                while ((line = reader.readLine()) != null) {
                    // 파일에서 읽은 라인이 입력된 playerID와 password 조합과 일치하는지 확인
                    if (line.startsWith(playerID + ":" + password)) {
                        // 로그인 성공 메시지 표시
                        JOptionPane.showMessageDialog(frame, "로그인 되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);

                        // 로그인된 ID 저장
                        String loggedInID = getIdFieldText(); // idField에서 현재 입력된 값을 가져옴
                        setIdFieldText(loggedInID); // 이 값을 내부 저장소에 저장

                        // 현재 로그인 창을 숨김
                        frame.setVisible(false);

                        // 게임 화면을 새 JFrame으로 표시
                        JFrame gameFrame = new JFrame("Falling Object Game");
                        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 게임 창을 닫으면 프로그램 종료
                        gameFrame.setSize(400, 600);  // 게임 창 크기 설정
                        gameFrame.setLocationRelativeTo(null);  // 화면 중앙에 표시
                        gameFrame.add(new GamePanels(playerID));  // 로그인된 ID를 사용하여 GamePanels 객체 생성 후 추가
                        gameFrame.setVisible(true);  // 게임 창 보이기 설정

                        loginSuccessful = true;  // 로그인 성공 여부를 true로 설정
                        break;  // 더 이상 파일을 읽지 않고 반복문 종료
                    }
                }

                // 로그인 실패 시 사용자에게 경고 메시지 표시
                if (!loginSuccessful) {
                    JOptionPane.showMessageDialog(frame, "아이디 또는 비밀번호가 틀렸습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                // 파일 읽기 도중 오류가 발생하면 경고 메시지 표시
                JOptionPane.showMessageDialog(frame, "로그인 중 오류가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }


        }


    }
    public String getIdFieldText() {
        return idField.getText();
    }

    public void setIdFieldText(String text) {
        idField.setText(text);
    }


}

