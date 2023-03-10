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
    String password = "2802";
    Connection connection;
    Color color = new Color(139, 69, 19, 85);
    Font text = new Font("Century Gothic", Font.PLAIN, 20);
    DefaultTableModel tableModelBooks = new DefaultTableModel();
    DefaultTableModel tableModelUsers = new DefaultTableModel();
    JTable tableBooks;
    JTable tableUsers;
    JScrollPane scrPanelBooks;
    JScrollPane scrPanelUsers;
    JButton addBook = new JButton("Добавить книгу");
    JButton updateBook = new JButton("Обновить");
    JButton addUser = new JButton("Добавить пользователя");
    JButton updateUser = new JButton("Обновить");
    JButton exit = new JButton("Выход");
    JTextField bookPath = new JTextField("Путь");
    JTextField bookAuthor = new JTextField("Автор");
    JTextField bookName = new JTextField("Название");
    JTextField login = new JTextField("Логин");
    JTextField pass = new JTextField("Пароль");
    JTabbedPane tb;

    public AdminPanel(){

        setLayout(null);

        connection();

        tableBooks.setFont(text);
        tableBooks.setRowHeight(30);

        bookPath.setBounds(10, 20, 100, 20);
        bookAuthor.setBounds(120, 20, 100, 20);
        bookName.setBounds(230, 20, 100, 20);
        addBook.setBounds(340, 20, 150, 20);
        updateBook.setBounds(500, 20, 150, 20);

        login.setBounds(10, 20, 100, 20);
        pass.setBounds(120, 20, 100, 20);
        addUser.setBounds(230, 20, 100, 20);

        exit.setBounds(780, 600, 100, 30);

        scrPanelBooks = new JScrollPane(tableBooks);
        scrPanelBooks.setBounds(10, 50, 940, 300);

        tb = new JTabbedPane(JTabbedPane.TOP);
        tb.setBounds(10, 0, 960, 580);
        JPanel books = new JPanel();

        books.setLayout(null);
        books.add(scrPanelBooks);
        books.add(bookPath);
        books.add(bookAuthor);
        books.add(bookName);
        books.add(addBook);
        books.add(updateBook);

        tb.addTab("Книги", books);

        JPanel users = new JPanel();

        users.setLayout(null);
        users.add(login);
        users.add(pass);
        users.add(addUser);

        tb.addTab("Пользователи", users);

        add(tb);
        add(exit);
        setVisible(true);

        updateBook.addActionListener(e -> updateBook());
        addBook.addActionListener(e -> addBook());
    }

    void connection(){
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT book_id, book_name, book_author " +
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
            PreparedStatement ps = connection.prepareStatement("SELECT book_id, book_name, book_author " +
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
}