package com.application.window;

import com.application.packet.PacketSMS;
import com.application.service.ServiceClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Window implements ActionListener {
    private final Object lock = new Object();

    private final ServiceClass serviceClass;
    private final JTextField textField;
    private final Font font;
    private final JPanel panel;
    private boolean closed;
    private final Dimension windowDimension;
    private final Dimension labelDimension;

    public Window(ServiceClass serviceClass, String title, int width, int height) {
        this.serviceClass = serviceClass;
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
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == textField && !textField.getText().isBlank()) {
            String text = textField.getText();
            textField.setText("");

            PacketSMS packetSMS = new PacketSMS(serviceClass.getName() + ": " + text);
            pushMessage(packetSMS);
            System.out.println("Packet size: " + packetSMS.getSize());
            serviceClass.sendMessage(packetSMS);
        }
    }

    public void pushMessage(PacketSMS packetSMS) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/y h:mm a", Locale.getDefault());
        String formattedTime = packetSMS.getDateTimeSent().format(formatter);
        String message = formattedTime + " | " + packetSMS.getMessage();

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
    }

    private void fixedSize(JComponent component, Dimension size) {
        component.setMinimumSize(size);
        component.setPreferredSize(size);
        component.setMaximumSize(size);
    }

    public Object getLock() {
        return lock;
    }

    public boolean isOpen() {
        return !closed;
    }
}