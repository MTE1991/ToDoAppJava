import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoListApp {
    public static void main(String[] args) {
        // Create main frame
        JFrame frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());

        // Create title label
        JLabel titleLabel = new JLabel("To Do List", JLabel.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));

        // Create components
        JLabel inputLabel = new JLabel("Task: ", JLabel.LEFT);
        JTextField taskInput = new JTextField(20);
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");
        JButton moveUpButton = new JButton("Move Up");
        JButton moveDownButton = new JButton("Move Down");

        // Date picker
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy/MM/dd hh:mm a");
        dateSpinner.setEditor(dateEditor);

        DefaultListModel<String> taskListModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(taskListModel);
        JScrollPane taskScrollPane = new JScrollPane(taskList);

        // Add components to the frame
        JPanel inputPanel = new JPanel();
        inputPanel.add(inputLabel);
        inputPanel.add(taskInput);
        inputPanel.add(dateSpinner);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        inputPanel.add(moveUpButton);
        inputPanel.add(moveDownButton);
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.add(taskScrollPane, BorderLayout.CENTER);

        // Add action listener to the add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String task = taskInput.getText();
                Date date = (Date) dateSpinner.getValue();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                if (!task.isEmpty()) {
                    taskListModel.addElement(task + " - " + formatter.format(date));
                    taskInput.setText("");
                }
            }
        });

        // Add action listener to the remove button
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    taskListModel.remove(selectedIndex);
                }
            }
        });

        // Add action listener to the move up button
        moveUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex > 0) {
                    String task = taskListModel.remove(selectedIndex);
                    taskListModel.add(selectedIndex - 1, task);
                    taskList.setSelectedIndex(selectedIndex - 1);
                }
            }
        });

        // Add action listener to the move down button
        moveDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex < taskListModel.getSize() - 1) {
                    String task = taskListModel.remove(selectedIndex);
                    taskListModel.add(selectedIndex + 1, task);
                    taskList.setSelectedIndex(selectedIndex + 1);
                }
            }
        });

        // Timer to check for due tasks
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date now = new Date();
                for (int i = 0; i < taskListModel.getSize(); i++) {
                    String task = taskListModel.getElementAt(i);
                    String[] parts = task.split(" - ");
                    if (parts.length > 1) {
                        try {
                            Date taskDate = formatter.parse(parts[1]);
                            if (now.after(taskDate)) {
                                JOptionPane.showMessageDialog(frame, "Task due: " + parts[0]);
                                taskListModel.remove(i);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        timer.start();

        // Display the frame
        frame.setVisible(true);
    }
}
