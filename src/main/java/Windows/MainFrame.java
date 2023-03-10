package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class MainFrame extends JFrame {
    String url = "jdbc:mysql://localhost:3306/e_library";
    String user = "root";
    String password = "1234";
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    String query;
    AuthorizationPanel authorizationPanel = new AuthorizationPanel();
    AdminPanel adminPanel = new AdminPanel();
    UserPanel userPanel;
    public MainFrame(){

        setTitle("Электронная библиотека");
        setSize(1000,700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(authorizationPanel);

        adminPanel.addBook.addActionListener(e -> addBook());
        adminPanel.exit.addActionListener(e -> exit());

        authorizationPanel.welcomeButton.addActionListener(e -> {

            int role = authorization();

            if(role == 1){
                authorizationPanel.setVisible(false);
                setContentPane(adminPanel);
                adminPanel.setVisible(true);
            } else if (role == 2) {
                authorizationPanel.setVisible(false);
                userPanel = new UserPanel();
                setContentPane(userPanel);
                userPanel.setVisible(true);
                userPanel.exit.addActionListener(ex -> exit());
            } else {
                System.out.println("Invalid user!");
            }
        });

        setVisible(true);
    }

    public int authorization(){

        try {
            StringBuilder inputPassword = new StringBuilder();
            for(int i = 0; i < authorizationPanel.passwordField.getPassword().length; i++){
                inputPassword.append(authorizationPanel.passwordField.getPassword()[i]);
            }

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            query = "select * from users";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){

                String login = resultSet.getString("user_login");
                String password = resultSet.getString("user_password");

                if(Objects.equals(login, authorizationPanel.loginField.getText()) && Objects.equals(password, inputPassword.toString())){
                    authorizationPanel.loginField.setText(null);
                    authorizationPanel.passwordField.setText(null);

                    return resultSet.getInt("user_role");
                }
            }

            connection.close();
            resultSet.close();
            statement.close();

            return 0;
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void addBook(){
        try
        {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps;

            String path = adminPanel.bookPath.getText();
            String author = adminPanel.bookAuthor.getText();
            String name = adminPanel.bookName.getText();

            File file = new File(path);
            FileInputStream is = new FileInputStream(file);

            ps = connection.prepareStatement("insert into books (book_name, book_author, book_text) values (?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, author);
            ps.setBinaryStream(3, is);
            ps.execute();

            connection.close();
            ps.close();
        }
        catch(IOException | SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public void exit(){

        adminPanel.setVisible(false);
        userPanel.setVisible(false);
        setContentPane(authorizationPanel);
        authorizationPanel.setVisible(true);

    }
}