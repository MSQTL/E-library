package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class AdminPanel extends JPanel {
    String url = "jdbc:mysql://localhost:3306/e_library";
    String user = "root";
    String password = "1234";
    Connection connection;
    Color color = new Color(37, 114, 247);
    Font text = new Font("Century Gothic", Font.PLAIN, 20);
    Font fontTB = new Font("Yu Gothic UI Light", Font.PLAIN, 17);
    DefaultTableModel tableModelBooks = new DefaultTableModel();
    DefaultTableModel tableModelUsers = new DefaultTableModel();
    JTable tableBooks;
    JTable tableUsers;
    JScrollPane scrPanelBooks;
    JScrollPane scrPanelUsers;
    JButton addBook = new JButton("Добавить книгу");
    JButton addUser = new JButton("Добавить пользователя");
    JButton exit = new JButton("Выход");
    JTextField bookPath = new JTextField("Путь");
    JTextField bookAuthor = new JTextField("Автор");
    JTextField bookName = new JTextField("Название");
    JTextField login = new JTextField("Логин");
    JTextField pass = new JTextField("Пароль");
    JTabbedPane tb;

    public AdminPanel(){

        setLayout(null);

        connectionForTableBooks();
        connectionForTableUsers();

        tableBooks.setFont(text);
        tableBooks.setRowHeight(30);

        bookPath.setBounds(10, 30, 200, 30);
        bookAuthor.setBounds(220, 30, 200, 30);
        bookName.setBounds(430, 30, 200, 30);
        addBook.setBounds(640, 30, 150, 30);
        scrPanelBooks = new JScrollPane(tableBooks);
        scrPanelBooks.setBounds(10, 80, 940, 300);

        tableUsers.setFont(text);
        tableUsers.setRowHeight(30);

        login.setBounds(10, 30, 200, 30);
        pass.setBounds(220, 30, 200, 30);
        addUser.setBounds(430, 30, 200, 30);
        scrPanelUsers = new JScrollPane(tableUsers);
        scrPanelUsers.setBounds(10, 80, 940, 300);

        exit.setBounds(870, 590, 100, 30);
        exit.setBackground(color);
        exit.setForeground(Color.white);
        exit.setFont(text);

        tb = new JTabbedPane(JTabbedPane.TOP);
        tb.setBounds(10, 0, 960, 580);
        tb.setFont(fontTB);

        JPanel books = new JPanel();

        books.setLayout(null);
        books.add(scrPanelBooks);
        books.add(bookPath);
        books.add(bookAuthor);
        books.add(bookName);
        books.add(addBook);

        JPanel users = new JPanel();

        users.setLayout(null);
        users.add(login);
        users.add(pass);
        users.add(addUser);
        users.add(scrPanelUsers);

        tb.addTab("Книги", books);
        tb.addTab("Пользователи", users);

        add(tb);
        add(exit);
        setVisible(true);

        addBook.addActionListener(e -> {

            addBook();
            updateBook();
        });

        addUser.addActionListener(e -> {

            addUser();
            updateUser();
        });
    }

    void connectionForTableBooks(){
        try {

            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT ID, book_name, book_author " +
                    "FROM books");

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int col = 1; col <= resultSetMetaData.getColumnCount(); col++) {
                tableModelBooks.addColumn(resultSetMetaData.getColumnName(col));
            }
            while (rs.next()){

                Vector<String> rows = new Vector<>();
                rows.add(Integer.toString(rs.getInt(1)));
                for(int col = 2; col <= resultSetMetaData.getColumnCount(); col++){
                    rows.add(rs.getString(col));
                }
                tableModelBooks.addRow(rows);
            }

            tableBooks = new JTable(tableModelBooks);
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void updateBook(){
        try {
            DefaultTableModel u = new DefaultTableModel();
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT ID, book_name, book_author " +
                    "FROM books");

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int col = 1; col <= resultSetMetaData.getColumnCount(); col++) {
                u.addColumn(resultSetMetaData.getColumnName(col));
            }
            while (rs.next()){

                Vector<String> rows = new Vector<>();
                rows.add(Integer.toString(rs.getInt(1)));
                for(int col = 2; col <= resultSetMetaData.getColumnCount(); col++){
                    rows.add(rs.getString(col));
                }
                u.addRow(rows);
            }

            tableBooks.setModel(u);
            connection.close();
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

            String path = bookPath.getText();
            String author = bookAuthor.getText();
            String name = bookName.getText();

            File file = new File(path);
            FileInputStream is = new FileInputStream(file);

            ps = connection.prepareStatement("insert into books (book_name, book_author, book_text) " +
                    "values (?, ?, ?)");
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
    void connectionForTableUsers(){
        try {

            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT user_id, user_login, user_password, user_role " +
                    "FROM users");

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int col = 1; col <= resultSetMetaData.getColumnCount(); col++) {
                tableModelUsers.addColumn(resultSetMetaData.getColumnName(col));
            }
            while (rs.next()){

                Vector<String> rows = new Vector<>();
                rows.add(Integer.toString(rs.getInt(1)));
                for(int col = 2; col <= resultSetMetaData.getColumnCount(); col++){
                    rows.add(rs.getString(col));
                }
                tableModelUsers.addRow(rows);
            }

            tableUsers = new JTable(tableModelUsers);
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    void addUser(){

        try
        {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps;

            String loginText = login.getText();
            String passText = pass.getText();

            ps = connection.prepareStatement("insert into users (user_login, user_password, user_role) " +
                    "values (?, ?, 2)");
            ps.setString(1, loginText);
            ps.setString(2, passText);
            ps.execute();

            connection.close();
            ps.close();
        }
        catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    void updateUser(){

        try {
            DefaultTableModel u = new DefaultTableModel();
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT user_id, user_login, user_password, user_role " +
                    "FROM users");

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int col = 1; col <= resultSetMetaData.getColumnCount(); col++) {
                u.addColumn(resultSetMetaData.getColumnName(col));
            }
            while (rs.next()){

                Vector<String> rows = new Vector<>();
                rows.add(Integer.toString(rs.getInt(1)));
                for(int col = 2; col <= resultSetMetaData.getColumnCount(); col++){
                    rows.add(rs.getString(col));
                }
                u.addRow(rows);
            }

            tableUsers.setModel(u);
            connection.close();
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}