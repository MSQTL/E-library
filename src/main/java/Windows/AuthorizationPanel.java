package Windows;

import javax.swing.*;
import java.awt.*;

public class AuthorizationPanel extends JPanel {
    Dimension panelSize = new Dimension(190, 70);
    Dimension fieldSize = new Dimension(100, 20);
    Font text = new Font("Century Gothic", Font.PLAIN, 20);
    Font button = new Font("Century Gothic", Font.ITALIC, 18);
    Font head = new Font("Century Gothic",Font.PLAIN, 25);
    Font field = new Font("MV Boli", Font.PLAIN, 17);
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
        int y = 150;

        header.setBounds(380, 30 + y, 200, 25);
        header.setFont(head);

        loginLabel.setBounds(280, 100 + y, 230, 25);
        loginLabel.setFont(text);

        loginField.setBounds(530, 100 + y, 165, 30);
        loginField.setFont(field);
        loginField.setPreferredSize(fieldSize);

        passwordLabel.setBounds(280, 165 + y, 230, 25);
        passwordLabel.setFont(text);

        passwordField.setEchoChar('*');
        passwordField.setBounds(530, 165 + y, 165, 30);
        passwordField.setFont(field);
        passwordField.setPreferredSize(fieldSize);

        welcomeButton.setBounds(530, 220 + y,165, 30);
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