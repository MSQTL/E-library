package Windows;

import javax.swing.*;
import java.awt.*;

public class AuthorizationPanel extends JPanel {
    Dimension panelSize = new Dimension(190, 70);
    Dimension fieldSize = new Dimension(100, 20);
    Font text = new Font("Century Gothic", Font.PLAIN, 20);
    Font button = new Font("Century Gothic", Font.ITALIC, 18);
    Font head = new Font("Century Gothic",Font.PLAIN, 25);
    Color color = new Color(37, 114, 247);
    JLabel header = new JLabel("<html><u>Вход в систему</u></html>");
    JLabel loginLabel = new JLabel("Введите Ваш логин: ");
    JTextField loginField = new JTextField();
    JLabel passwordLabel = new JLabel("Введите Ваш пароль: ");
    JPasswordField passwordField = new JPasswordField();
    JButton welcomeButton = new JButton("Войти");
    public AuthorizationPanel(){

        setLayout(null);
        setPreferredSize(panelSize);

        header.setBounds(110, 30, 200, 25);
        header.setFont(head);

        loginLabel.setBounds(10, 100, 230, 25);
        loginLabel.setFont(text);

        loginField.setBounds(260, 100, 165, 30);
        loginField.setPreferredSize(fieldSize);

        passwordLabel.setBounds(10, 165, 230, 25);
        passwordLabel.setFont(text);

        passwordField.setEchoChar('*');
        passwordField.setBounds(260, 165, 165, 30);
        passwordField.setPreferredSize(fieldSize);

        welcomeButton.setBounds(260, 220,165, 25);
        welcomeButton.setHorizontalTextPosition(JButton.CENTER);
        welcomeButton.setVerticalTextPosition(JButton.CENTER);
        welcomeButton.setFont(button);
        welcomeButton.setBackground(color);
        welcomeButton.setForeground(Color.white);

        add(header);
        add(loginLabel);
        add(loginField);
        add(passwordLabel);
        add(passwordField);
        add(welcomeButton);

        setVisible(true);
    }

}