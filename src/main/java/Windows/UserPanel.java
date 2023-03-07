package Windows;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {

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
    JTable books = new JTable(5, 3);
    JTextArea book = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(book);
    JButton exit = new JButton("Выход");
    public UserPanel(){

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
        leftBox.add(books);
        leftBox.add(Box.createVerticalGlue());

        mainBox.add(Box.createHorizontalStrut(20));
        mainBox.add(leftBox);
        mainBox.add(Box.createHorizontalStrut(20));
        mainBox.add(scrollPane);
        mainBox.add(Box.createHorizontalStrut(20));
        mainBox.add(exit);
        mainBox.add(Box.createHorizontalStrut(20));

        add(mainBox);

        setVisible(true);
    }
}