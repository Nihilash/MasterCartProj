package ui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private User loggedInUser;
    private Image bgImage;

    public LoginUI(UserService userService) {
        setTitle("MasterCart - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ✅ Load background image from resources (inside src/Mastercartimages/)
        try {
            bgImage = new ImageIcon(
                getClass().getResource("/Mastercartimages/login1.jpg")
            ).getImage();
        } catch (Exception e) {
            System.err.println("⚠️ Could not load background image. Check path: src/Mastercartimages/login1.jpg");
            bgImage = null;
        }

        // Background panel
        JPanel backgroundPanel = new JPanel() {
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

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to MasterCart Login", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.BLACK);

        // Login overlay panel
        JPanel loginOverlay = new JPanel(new GridBagLayout());
        loginOverlay.setBackground(new Color(255, 255, 255, 120)); // semi-transparent
        loginOverlay.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        loginOverlay.setOpaque(true);

        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(15, 15, 15, 15);
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;

        loginOverlay.add(welcomeLabel, innerGbc);

        // Input panel
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

        // Add username/password fields and button
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

        // Add input panel below welcome label
        innerGbc.gridy = 1;
        loginOverlay.add(inputPanel, innerGbc);

        // Add overlay to background
        backgroundPanel.add(loginOverlay, gbc);

        // Login button action
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
