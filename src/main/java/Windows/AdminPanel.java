package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AdminPanel extends JPanel {
    String url = "jdbc:mysql://localhost:3306/e_library";
    String user = "root";
    String password = "1234";
    Connection connection;
    String bookText;
    DefaultTableModel tableModel = new DefaultTableModel();

    JMenuBar menu;

    Color color = new Color(139, 69, 19, 85);
    JTable table;
    JScrollPane scrPanel;
    JButton addBook = new JButton("Добавить книгу");

    Font text = new Font("Century Gothic", Font.PLAIN, 20);
    JButton exit = new JButton("Выход");
    JTextField bookPath = new JTextField("Путь");
    JTextField bookAuthor = new JTextField("Автор");
    JTextField bookName = new JTextField("Название");

    JTextField login = new JTextField("Логин");

    JTextField pass = new JTextField("Пароль");

    JButton addUser = new JButton("Добавить пользователя");
    JTabbedPane tb;

    public AdminPanel(){

        setLayout(null);

        connection();

        table.setFont(text);
        table.setRowHeight(30);

        bookPath.setBounds(10, 20, 100, 20);
        bookAuthor.setBounds(120, 20, 100, 20);
        bookName.setBounds(230, 20, 100, 20);
        addBook.setBounds(340, 20, 150, 20);

        login.setBounds(10, 20, 100, 20);
        pass.setBounds(120, 20, 100, 20);
        addBook.setBounds(230, 20, 100, 20);

        exit.setBounds(780, 600, 100, 30);

        scrPanel = new JScrollPane(table);
        scrPanel.setBounds(10, 50, 940, 300);

        tb = new JTabbedPane(JTabbedPane.TOP);
        tb.setBounds(10, 0, 960, 580);
        JPanel books = new JPanel();

        books.setLayout(null);
        books.add(scrPanel);
        books.add(bookPath);
        books.add(bookAuthor);
        books.add(bookName);
        books.add(addBook);

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
    }



    void connection(){
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT book_id, book_name, book_author " +
                    "FROM books");

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int col = 1; col <= resultSetMetaData.getColumnCount(); col++) {
                tableModel.addColumn(resultSetMetaData.getColumnName(col));
            }
            while (rs.next()){

                Vector<String> rows = new Vector<String>();
                rows.add(Integer.toString(rs.getInt(1)));
                for(int col = 2; col <= resultSetMetaData.getColumnCount(); col++){
                    rows.add(rs.getString(col));
                }
                tableModel.addRow(rows);
            }

            table = new JTable(tableModel);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}