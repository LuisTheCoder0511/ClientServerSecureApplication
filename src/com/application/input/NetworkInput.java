package com.application.input;

import com.application.validator.IpAddressValidator;
import com.application.validator.NameValidator;

import javax.swing.*;

public class NetworkInput {
    private final String name;
    private final String address;
    private final int port;

    public NetworkInput() {
        this(null);
    }

    public NetworkInput(String name){
        String input = "";
        while (input == null || input.isEmpty()) {
            input = JOptionPane.showInputDialog("Enter IPv4 address:");
            if (input.isEmpty()) {
                System.out.println("Cancelled input");
                System.exit(0);
            }
            input = IpAddressValidator.validateIpv4Address(input);
        }
        address = input;

        input = "";
        int portInput = -1;
        while (input.isEmpty()) {
            try {
                input = JOptionPane.showInputDialog("Enter port number:");
                if (input.isEmpty()) {
                    System.out.println("Cancelled input");
                    System.exit(0);
                }
                portInput = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port: " + e.getMessage());
                input = "";
            }
        }
        port = portInput;

        if (name == null){
            input = "";
            while (input == null || input.isEmpty()) {
                input = JOptionPane.showInputDialog("Enter name:");
                if (input.isEmpty()) {
                    System.out.println("Cancelled input");
                    System.exit(0);
                }
                input = NameValidator.isValidName(input);
            }
            this.name = input;
        } else {
            this.name = name;
        }
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }
}
