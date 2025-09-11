package com.application.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window implements ActionListener {
    private final JTextField textField;
    private final Font font;
    private final JPanel panel;
    private String text;
    private boolean closed;
    private final Dimension windowDimension;
    private final Dimension labelDimension;

    public Window(String title, int width, int height) {
        windowDimension = new Dimension(512, 40);
        labelDimension = new Dimension(512, 30);
        font = new Font("Serif", Font.BOLD, 18);
        Color borderColor = Color.BLACK;

        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        panel = new JPanel();
        fixedSize(panel, windowDimension);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        textField = new JTextField(20);
        textField.setFont(font);
        textField.addActionListener(this);
        fixedSize(textField, labelDimension);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setBorder(BorderFactory.createLineBorder(borderColor, 2));

        panel.add(textField);

        JPanel parentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        parentPanel.add(panel);

        frame.add(parentPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closed = true;
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == textField && !textField.getText().isBlank()) {
            text = textField.getText();
            textField.setText("");
        }
    }

    public void pushMessage(String message) {
        if (panel.getComponentCount() > 65) {
            panel.remove(panel.getComponentCount() - 1);
        }
        JLabel label = new JLabel(message);
        label.setFont(font);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        fixedSize(textField, labelDimension);
        windowDimension.height += 40;

        panel.add(label, panel.getComponentCount() - 1);
        panel.revalidate();
        panel.repaint();
        System.out.println(panel.getComponentCount());
    }

    public String getText() {
        if (text == null || text.isBlank()) return "";
        return text;
    }

    public void resetText(){
        text = null;
    }

    private void fixedSize(JComponent component, Dimension size) {
        component.setMinimumSize(size);
        component.setPreferredSize(size);
        component.setMaximumSize(size);
    }

    public boolean isClosed() {
        return closed;
    }
}
