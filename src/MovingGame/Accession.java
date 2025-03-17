package MovingGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class Accession {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new AccessionWindow()); // 만들어 놓은 LoginWindow 호출

    }
}

class AccessionWindow {
    private JFrame frame;
    private JTextField idField;
    private JPasswordField passwordField;
    private JPasswordField pwcheckField;


    public AccessionWindow() {
        frame = new JFrame("Save User Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 3));
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
        // 비밀번호 텍스트 필드
        passwordField = new JPasswordField();
        frame.add(passwordField);

        // 비밀번호 확인 필드
        JLabel pscheckLabel = new JLabel("비밀번호 확인:");
        frame.add(pscheckLabel);
        pwcheckField = new JPasswordField(); // 변경된 부분
        frame.add(pwcheckField);


        // 로그인 버튼
        JButton loginButton = new JButton("확인");
        loginButton.addActionListener(new SaveButtonListener());
        frame.add(loginButton);

        // 종료 버튼
        JButton plusButton = new JButton("닫기");
        plusButton.addActionListener(e -> frame.dispose()); // 회원가입 액션 실행 버튼
        frame.add(plusButton);

    }

    private class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String playerID = idField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String pwcheck = new String(pwcheckField.getPassword()).trim();



            System.out.println("----비밀번호------"+password);
            System.out.println("----비밀번호 확인------"+pwcheck);


            // 입력한 ID혹은 비밀번호가 빈 공백일 경우
            // trim()은 문자의 앞뒤에 있는 공백 을 제거함 - isEmpty()는 문자열이 비어 있는지 확인하는 메소드이고 공백이면 true 이다.
            if (playerID.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "'아이디' 와 '비밀번호'를 모두 입력해주세요.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(pwcheck)) {
                JOptionPane.showMessageDialog(frame, " '비밀번호' 확인이 옳바르지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 아이디와 비밀번호가 영어 대소문자 및 숫자만 허용되는지 확인
            String regex = "^[a-zA-Z0-9]+$";  // 영문자 대소문자와 숫자만 허용

            if (!playerID.matches(regex)) {
                JOptionPane.showMessageDialog(frame, "'아이디'는 영어 대소문자와 숫자만 사용할 수 있습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.matches(regex)) {
                JOptionPane.showMessageDialog(frame, "'비밀번호'는 영어 대소문자와 숫자만 사용할 수 있습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password == pwcheck) {
                JOptionPane.showMessageDialog(frame, "패스워드 확인이 옳바르지 않습니다..", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            // 아이디 중복 검사
            try (BufferedReader reader = new BufferedReader(new FileReader("src/MovingGame/users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 해당 playerID가 이미 존재하는지 확인
                    if (line.startsWith(playerID + ":")) {
                        // 중복된 경우, 경고 메시지 표시 후 리턴
                        JOptionPane.showMessageDialog(frame, "해당 아이디는 이미 존재합니다.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (IOException ex) {
                // 파일을 읽을 때 오류가 발생하면 경고 메시지 표시
                JOptionPane.showMessageDialog(frame, "파일을 읽는 중 오류가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }




            // 아이디 중복이 없으면, 아이디와 비밀번호를 저장
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/MovingGame/users.txt", true))) {
                // 아이디와 비밀번호를 파일에 추가
                writer.write(playerID + ":" + password);
                writer.newLine();

                // 성공적으로 저장되었다는 메시지 표시
                JOptionPane.showMessageDialog(frame, "저장되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // 입력 필드 초기화
                idField.setText("");
                passwordField.setText("");
                pwcheckField.setText("");

                // 프레임을 닫거나 다른 작업을 진행
                frame.setVisible(false);  // 필요시 프레임을 숨기기

            } catch (IOException ex) {
                // 파일을 저장할 때 오류가 발생하면 경고 메시지 표시
                JOptionPane.showMessageDialog(frame, "파일 저장 중 오류가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }



        }


    }

}