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
    DefaultTableModel tableModel = new DefaultTableModel();
    Box mainBox = Box.createHorizontalBox();
    Box leftBox = Box.createVerticalBox();
    Box field_button = Box.createHorizontalBox();
    Box filterBox = Box.createHorizontalBox();
    JLabel search = new JLabel("Поиск");
    JTextField searchField = new JTextField();
    JButton searchButton = new JButton("Найти");
    JRadioButton filterAuthor = new JRadioButton("по автору");
    JRadioButton filterName = new JRadioButton("по названию");
    JLabel results = new JLabel("Найденные книги:");
    JTable books;
    JScrollPane scrollPaneForBooks;
    JTextArea book = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(book);
    JButton exit = new JButton("Выход");
    public UserPanel(){

        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = connection.prepareStatement("SELECT book_id, book_name, book_author " +
                    "FROM books WHERE book_name LIKE ?");
            ps.setString(1, "%" + searchField.getText() + "%");

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

            books = new JTable(tableModel);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        books.setSize(300, 200);
        scrollPaneForBooks = new JScrollPane(books);
        searchField.setMaximumSize(new Dimension(250, 25));

        book.setColumns(55);
        book.setRows(40);
        book.setLineWrap(true);
        book.setWrapStyleWord(true);
        book.setEditable(false);

        field_button.add(searchField);
        field_button.add(searchButton);

        filterBox.add(filterAuthor);
        filterBox.add(filterName);

        leftBox.add(Box.createVerticalStrut(30));
        leftBox.add(search);
        leftBox.add(field_button);
        leftBox.add(filterBox);
        leftBox.add(Box.createVerticalStrut(30));
        leftBox.add(results);
        leftBox.add(scrollPaneForBooks);
        leftBox.add(Box.createVerticalGlue());

        mainBox.add(Box.createHorizontalStrut(20));
        mainBox.add(leftBox);
        mainBox.add(Box.createHorizontalStrut(20));
        mainBox.add(scrollPane);
        mainBox.add(Box.createHorizontalStrut(20));
        mainBox.add(exit);
        mainBox.add(Box.createHorizontalStrut(20));

        books.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1){
                    int bookIndex = Integer.parseInt((String) books.getValueAt(books.getSelectedRow(), 0));
                    System.out.println(bookIndex);

                    try {
                        connection = DriverManager.getConnection(url, user, password);
                        PreparedStatement ps = connection.prepareStatement("SELECT book_text " +
                                "FROM books WHERE book_id = ?");
                        ps.setInt(1, bookIndex);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()){
                            book.setText(rs.getString("book_text"));
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });

        add(mainBox);

        setVisible(true);
    }

}