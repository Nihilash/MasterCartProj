package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import model.User;
import service.UserService;

public class LoginUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private User loggedInUser;

    public LoginUI(UserService userService) {
        setTitle("MasterCart - Login");
        setExtendedState(Frame.MAXIMIZED_BOTH); // full screen
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Background panel
        JPanel backgroundPanel = new JPanel() {
            private Image bgImage;
            {
                try {
                    // Load image from classpath
                    ImageIcon icon = new ImageIcon(getClass().getResource("/Mastercartimages/login1.jpg"));
                    bgImage = icon.getImage();
                } catch (Exception e) {
                    System.err.println("Background image not found!");
                    bgImage = null;
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // --- Welcome label ---
        JLabel welcomeLabel = new JLabel("Welcome to MasterCart Login", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.BLACK);

        // Panel wrapping the login box
        JPanel loginOverlay = new JPanel(new GridBagLayout());
        loginOverlay.setBackground(new Color(255, 255, 255, 180)); // semi-transparent
        loginOverlay.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        loginOverlay.setOpaque(true);

        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(15, 15, 15, 15);
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;

        loginOverlay.add(welcomeLabel, innerGbc);

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints inGbc = new GridBagConstraints();
        inGbc.insets = new Insets(10, 10, 10, 10);
        inGbc.gridx = 0;
        inGbc.gridy = 0;
        inGbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(new Color(200, 200, 200));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);

        inGbc.gridy = 0;
        inputPanel.add(usernameLabel, inGbc);
        inGbc.gridx = 1;
        inputPanel.add(usernameField, inGbc);

        inGbc.gridx = 0;
        inGbc.gridy = 1;
        inputPanel.add(passwordLabel, inGbc);
        inGbc.gridx = 1;
        inputPanel.add(passwordField, inGbc);

        inGbc.gridx = 0;
        inGbc.gridy = 2;
        inGbc.gridwidth = 2;
        inGbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(loginButton, inGbc);

        innerGbc.gridy = 1;
        loginOverlay.add(inputPanel, innerGbc);

        backgroundPanel.add(loginOverlay, gbc);

        // Login action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            char[] pwChars = passwordField.getPassword();
            String password = new String(pwChars);

            try {
                User user = userService.login(username, password);
                if (user != null) {
                    loggedInUser = user;
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials!");
                }
            } finally {
                java.util.Arrays.fill(pwChars, '\0');
            }
        });
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
