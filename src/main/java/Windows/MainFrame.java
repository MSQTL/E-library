package Windows;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Objects;

public class MainFrame extends JFrame {
    String url = "jdbc:mysql://localhost:3306/e_library";
    String user = "root";
    String password = "2802";
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    String query;
    AuthorizationPanel authorizationPanel = new AuthorizationPanel();
    AdminPanel adminPanel = new AdminPanel();
    UserPanel userPanel = new UserPanel();
    public MainFrame(){

        setTitle("Электронная библиотека");
        setSize(1000,700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(authorizationPanel);

        adminPanel.addBook.addActionListener(e -> addBook());
        adminPanel.exit.addActionListener(e -> exit());
        userPanel.searchButton.addActionListener(e -> searchBook());
        userPanel.exit.addActionListener(e -> exit());
        authorizationPanel.welcomeButton.addActionListener(e -> {

            int role = authorization();

            if(role == 1){
                authorizationPanel.setVisible(false);
                setContentPane(adminPanel);
                adminPanel.setVisible(true);
            } else if (role == 2) {
                authorizationPanel.setVisible(false);
                setContentPane(userPanel);
                userPanel.setVisible(true);
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

            authorizationPanel.loginField.setText(null);
            authorizationPanel.passwordField.setText(null);
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
            File file = new File("C:/Users/sergm/Desktop/Война и мир.txt");
            FileInputStream is = new FileInputStream(file);

            ps = connection.prepareStatement("insert into books (book_name, book_author, book_text) values (?, ?, ?)");
            ps.setString(1, "Война и мир, 1 и 2 том");
            ps.setString(2, "Толстой");
            ps.setBinaryStream(3, is);
            ps.execute();

            connection.close();
            ps.close();
        }
        catch(IOException | SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public void searchBook(){
        // код для вывода книги в текстареа
        /*try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("select * from books where book_id = 2");
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                String s = rs.getString("book_text");
                userPanel.book.setText(s);
            }

            connection.close();
            ps.close();
            rs.close();
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }*/

        try {
//            userPanel.searchField.setText( userPanel.searchField.getText()
//                    .replace("!", "!!")
//                    .replace("%", "!%")
//                    .replace("_", "!_")
//                    .replace("[", "!["));
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT book_name, book_author " +
                    "FROM books WHERE book_name LIKE ?");
            ps.setString(1, "%" + userPanel.searchField.getText() + "%");

            ResultSet rs = ps.executeQuery();
            String[][] books = new String[2][2];
            int i = 0;
            while (rs.next()){
                books[i][0] = rs.getString("book_name");
                books[i][1] = rs.getString("book_author");
                i++;
            }
            System.out.println(Arrays.toString(books));
            String[] columnNames = {"Название книги", "Автор"};
            userPanel.books = new JTable(books, columnNames);
            userPanel.add(userPanel.books);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void exit(){

        adminPanel.setVisible(false);
        userPanel.setVisible(false);
        setContentPane(authorizationPanel);
        authorizationPanel.setVisible(true);

    }
}