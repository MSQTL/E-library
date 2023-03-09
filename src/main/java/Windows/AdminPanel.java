package Windows;

import javax.swing.*;

public class AdminPanel extends JPanel {
    JButton addBook = new JButton("Добавить книгу");
    JButton exit = new JButton("Выход");
    public AdminPanel(){

        String[] columnNames = {"Название книги", "Описание", "Автор"};
        String [][] data = {
                {"Война и мир", "фывапро", "Толстой"},
                {"Капитанская дочка", "ывапрол", "Пушкин"}
        };
        JTable table = new JTable(data, columnNames);
        table.setSize(300, 200);

        JScrollPane scrPanel = new JScrollPane(table);
        add(scrPanel);

        add(addBook);
        add(exit);

        setVisible(true);
    }
}