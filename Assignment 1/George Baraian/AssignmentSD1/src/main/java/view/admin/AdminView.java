package view.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.Y_AXIS;

public class AdminView extends JFrame {

    private JTextField tfUsername;
    private JTextField tfPassword;
    private JButton createEmployeeButton;
    private JButton deleteEmployeeButton;

    public AdminView() throws HeadlessException{
        setSize(300, 300);
        setLocationRelativeTo(null);
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), Y_AXIS));
        add(tfUsername);
        add(tfPassword);
        add(createEmployeeButton);
        add(deleteEmployeeButton);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {
        tfUsername = new JTextField();
        tfPassword = new JTextField();
        createEmployeeButton = new JButton("Create an employee");
        deleteEmployeeButton = new JButton("Delete employee");
    }

    public String getUsername(){
        return tfUsername.getText();
    }

    public String getPassword(){
        return tfPassword.getText();
    }

    public void setCreateEmployeeButtonListener(ActionListener createEmployeeButtonListener){
        createEmployeeButton.addActionListener(createEmployeeButtonListener);
    }

    public void setDeleteEmployeeButtonListener(ActionListener deleteEmployeeButtonListener){
        deleteEmployeeButton.addActionListener(deleteEmployeeButtonListener);
    }

    public void setVisible(){
        this.setVisible(true);
    }

}
