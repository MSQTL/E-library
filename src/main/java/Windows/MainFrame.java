package Windows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.Buffer;
import java.sql.*;
import java.util.Objects;

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
    UserPanel userPanel = new UserPanel();
    Box verticalBox = Box.createVerticalBox();
    Box horizontalBox = Box.createHorizontalBox();
    public MainFrame(){

        setTitle("Электронная библиотека");
        setSize(1000,700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(verticalBox);
        verticalBox.add(Box.createVerticalGlue());
        verticalBox.add(authorizationPanel);
        verticalBox.add(Box.createVerticalGlue());
        horizontalBox.add(Box.createHorizontalGlue());

        setContentPane(horizontalBox);

        adminPanel.addBook.addActionListener(e -> addBook());
        userPanel.searchButton.addActionListener(e -> searchBook());
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
                    return resultSet.getInt("user_role");
                }
            }
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

            PreparedStatement ps = null;
            File file = new File("C:/Users/student.OP9_WinDC.009/Desktop/ruslan.txt");
            FileInputStream is = new FileInputStream(file);

            ps = connection.prepareStatement("insert into books (book_name, book_author, book_text) values (?, ?, ?)");
            ps.setString(1, "Руслан и Людмила");
            ps.setString(2, "Пушкин");
            ps.setBinaryStream(3, is);
            ps.execute();

        }
        catch(IOException | SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    public void searchBook(){

        try {
            PreparedStatement ps = connection.prepareStatement("select * from books where book_id = 1");
            ResultSet rs = ps.executeQuery();
            Blob blob;

            if(rs.next()){
                StringBuilder book = new StringBuilder();
                System.out.println("1");
                blob = rs.getBlob("book_text");
                BufferedInputStream bufferedInputStream = new BufferedInputStream(blob.getBinaryStream());
                int c = -1;
                while((c = bufferedInputStream.read()) != -1){
                    book.append((char) c);
                }

                String s = rs.getString("book_text");
                userPanel.book.setText(s);
            }
        }
        catch (SQLException | IOException ex){
            throw new RuntimeException(ex);
        }
    }
}