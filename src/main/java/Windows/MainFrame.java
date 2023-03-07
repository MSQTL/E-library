package Windows;

import javax.swing.*;
import java.sql.*;
import java.util.Objects;

public class MainFrame extends JFrame {
    String url = "jdbc:mysql://localhost:3306/e_library";
    String user = "root";
    String password = "2802";
    Connection connection;
    Statement stmt;
    ResultSet rs;
    String query;
    AuthorizationPanel authorizationPanel = new AuthorizationPanel();
    AdminPanel adminPanel = new AdminPanel();
    UserPanel userPanel = new UserPanel();
    Box verticalBox = Box.createVerticalBox();
    Box horizontalBox = Box.createHorizontalBox();
    public MainFrame(){

        setTitle("Авторизация");
        setSize(1000,700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(verticalBox);
        verticalBox.add(Box.createVerticalGlue());
        verticalBox.add(authorizationPanel);
        verticalBox.add(Box.createVerticalGlue());
        horizontalBox.add(Box.createHorizontalGlue());

        setContentPane(horizontalBox);

        authorizationPanel.welcomeButton.addActionListener(e -> {

            int role = authorization();

            if(role == 1){
                authorizationPanel.setVisible(false);
                verticalBox.add(adminPanel);
            } else if (role == 2) {
                authorizationPanel.setVisible(false);
                setContentPane(userPanel);
            } else {
                System.out.println("Invalid user!");
            }

        });

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public int authorization(){

        try {
            StringBuilder up = new StringBuilder();
            for(int i = 0; i < authorizationPanel.passwordField.getPassword().length; i++){
                up.append(authorizationPanel.passwordField.getPassword()[i]);
            }

            connection = DriverManager.getConnection(url, user, password);
            stmt = connection.createStatement();
            query = "select * from users";
            rs = stmt.executeQuery(query);
            while (rs.next()){

                String login = rs.getString("user_login");
                String password = rs.getString("user_password");

                if(Objects.equals(login, authorizationPanel.loginField.getText()) && Objects.equals(password, up.toString())){
                    return rs.getInt("user_role");
                }
            }

            return 0;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}