package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class UserPanel extends JPanel {
    String url = "jdbc:mysql://localhost:3306/e_library";
    String user = "root";
    String password = "1234";
    Connection connection;
    Font text = new Font("Century Gothic", Font.PLAIN, 20);
    Font textName = new Font("Times New Roman", Font.PLAIN, 20);
    Font textBook = new Font("Times New Roman", Font.PLAIN, 15);
    Color button = new Color(87, 45, 7);
    DefaultTableModel tableModel = new DefaultTableModel();
    JTextField searchField = new JTextField();
    JLabel nameBook = new JLabel();
    JTable books;
    JScrollPane scrollPaneForBooks;
    JTextArea book = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(book);
    JButton exit = new JButton("Выход");
    public UserPanel(){

        connection();

        setLayout(null);
        scrollPaneForBooks = new JScrollPane(books);

        book.setColumns(55);
        book.setRows(29);
        book.setLineWrap(true);
        book.setWrapStyleWord(true);
        book.setEditable(false);

        nameBook.setBounds(470, 30, 200, 30);
        nameBook.setFont(textName);

        scrollPaneForBooks.setBounds(20, 60, 430, 300);
        scrollPane.setBounds(470, 60, 500, 525);

        exit.setBounds(870, 600, 100, 30);
        exit.setBackground(button);
        exit.setForeground(Color.white);
        exit.setFont(text);

        books.setFont(text);
        books.setRowHeight(30);
        book.setFont(textBook);

        add(nameBook);
        add(scrollPaneForBooks);
        add(scrollPane);
        add(exit);

        setVisible(true);

        books.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1){
                    int bookIndex = Integer.parseInt((String) books.getValueAt(books.getSelectedRow(), 0));
                    System.out.println(bookIndex);

                    try {
                        connection = DriverManager.getConnection(url, user, password);
                        PreparedStatement ps = connection.prepareStatement("SELECT book_name, book_text " +
                                "FROM books WHERE ID = ?");
                        ps.setInt(1, bookIndex);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()){
                            nameBook.setText(rs.getString("book_name"));
                            book.setText(rs.getString("book_text"));
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }
        });

    }

    void connection(){
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT ID, book_name, book_author " +
                    "FROM books WHERE book_name LIKE ?");
            ps.setString(1, "%" + searchField.getText() + "%");

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int col = 1; col <= resultSetMetaData.getColumnCount(); col++) {
                tableModel.addColumn(resultSetMetaData.getColumnName(col));
            }
            while (rs.next()){

                Vector<String> rows = new Vector<>();
                rows.add(Integer.toString(rs.getInt(1)));
                for(int col = 2; col <= resultSetMetaData.getColumnCount(); col++){
                    rows.add(rs.getString(col));
                }
                tableModel.addRow(rows);
            }

            books = new JTable(tableModel);
            books.getColumnModel().getColumn(0).setMaxWidth(20);
            books.getColumnModel().getColumn(2).setMinWidth(100);
            books.getColumnModel().getColumn(2).setMaxWidth(200);

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}