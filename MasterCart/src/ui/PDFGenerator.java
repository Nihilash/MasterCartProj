package ui;

import model.CartItem;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.awt.Desktop;
import java.net.URL;

public class PDFGenerator {

    /**
     * Generates a styled HTML invoice file and optionally opens it in user-selected browser.
     */
    public static void generateInvoice(String username, List<CartItem> cartItems, double totalAmount) {
        try {
            // Ask user where to save invoice
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Invoice");
            fileChooser.setSelectedFile(new File("Invoice_" + username + "_" + System.currentTimeMillis() + ".html"));

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "Invoice generation cancelled.");
                return;
            }

            File fileToSave = fileChooser.getSelectedFile();

            // Load background image from resources
            URL bgUrl = PDFGenerator.class.getResource("/MasterCartimages/invoice.jpg");
            String backgroundImage = (bgUrl != null) ? bgUrl.toURI().toString() : "";

            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write("<html><head>");
                writer.write("<title>MasterCart Invoice</title>");
                writer.write("<style>");
                writer.write("body { "
                        + "font-family: 'Comic Sans MS', cursive, sans-serif; "
                        + "margin: 20px; "
                        + (backgroundImage.isEmpty() ? "" :
                           "background-image: url('" + backgroundImage + "'); ")
                        + "background-size: cover; "
                        + "background-repeat: no-repeat; "
                        + "background-position: center center; } ");
                writer.write("h1 { text-align: center; color: #222; font-style: italic; } ");
                writer.write("p { font-size: 16px; margin: 5px 0; font-style: italic; } ");
                writer.write("table { width: 100%; border-collapse: collapse; margin-top: 20px; "
                        + "background-color: rgba(255,255,255,0.85); font-size: 15px; "
                        + "box-shadow: 0 4px 10px rgba(0,0,0,0.3); border-radius: 8px; } ");
                writer.write("th, td { border: 1px solid #333; padding: 10px; text-align: center; font-style: italic; } ");
                writer.write("th { background-color: rgba(242,242,242,0.95); font-weight: bold; } ");
                writer.write(".total { text-align: right; font-weight: bold; font-size: 18px; margin-top: 20px; font-style: italic; } ");
                writer.write("</style>");
                writer.write("</head><body>");

                writer.write("<h1>🛒 MasterCart Invoice</h1>");
                writer.write("<p><b>User:</b> " + username + "</p>");
                writer.write("<p><b>Date:</b> " + new java.util.Date() + "</p>");
                writer.write("<p><b>Invoice ID:</b> " + System.currentTimeMillis() + "</p>");

                // Table header
                writer.write("<table>");
                writer.write("<tr>");
                writer.write("<th>S.No</th>");
                writer.write("<th>Product</th>");
                writer.write("<th>Category ID</th>");
                writer.write("<th>Quantity</th>");
                writer.write("<th>Unit Price (₹)</th>");
                writer.write("<th>Total (₹)</th>");
                writer.write("</tr>");

                int sno = 1;
                for (CartItem item : cartItems) {
                    writer.write("<tr>");
                    writer.write("<td>" + sno++ + "</td>");
                    writer.write("<td>" + item.getProduct().getName() + "</td>");
                    writer.write("<td>" + item.getProduct().getCategoryId() + "</td>");
                    writer.write("<td>" + item.getQuantity() + "</td>");
                    writer.write("<td>" + String.format("%.2f", item.getUnitPriceInr()) + "</td>");
                    writer.write("<td>" + String.format("%.2f", item.getLineTotal()) + "</td>");
                    writer.write("</tr>");
                }

                writer.write("</table>");
                writer.write("<p class='total'>Grand Total: ₹" + String.format("%.2f", totalAmount) + "</p>");
                writer.write("</body></html>");
            }

            // Show success
            JOptionPane.showMessageDialog(null,
                    "Invoice generated successfully!\nLocation: " + fileToSave.getAbsolutePath(),
                    "Invoice Generated",
                    JOptionPane.INFORMATION_MESSAGE);

            // Ask user if they want to view it
            int choice = JOptionPane.showConfirmDialog(null,
                    "Would you like to view the invoice in a browser?",
                    "Open Invoice", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                String[] browsers = detectBrowsers();

                String selected = (String) JOptionPane.showInputDialog(
                        null,
                        "Select a browser to open the invoice:",
                        "Choose Browser",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        browsers,
                        browsers[0]);

                if (selected != null) {
                    openInBrowser(selected, fileToSave);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error while generating invoice: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String[] detectBrowsers() {
        java.util.List<String> list = new java.util.ArrayList<>();
        list.add("System Default");

        String[][] browsers = {
                {"Chrome", "chrome", "C:/Program Files/Google/Chrome/Application/chrome.exe"},
                {"Brave", "brave", "C:/Program Files/BraveSoftware/Brave-Browser/Application/brave.exe"},
                {"Firefox", "firefox", "C:/Program Files/Mozilla Firefox/firefox.exe"},
                {"Edge", "msedge", "C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe"},
                {"Internet Explorer", "iexplore", "C:/Program Files/Internet Explorer/iexplore.exe"},
                {"Safari", "safari", "/Applications/Safari.app/Contents/MacOS/Safari"}
        };

        for (String[] b : browsers) {
            if (isBrowserAvailable(b[1]) || new File(b[2]).exists()) {
                list.add(b[0]);
            }
        }

        return list.toArray(new String[0]);
    }

    private static boolean isBrowserAvailable(String browserCmd) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String command = os.contains("win") ? "where " : "which ";
            Process process = Runtime.getRuntime().exec(command + browserCmd);
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static void openInBrowser(String browser, File file) throws IOException {
        if (browser.equals("System Default")) {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(file.toURI());
            }
        } else {
            String command = "";
            switch (browser) {
                case "Chrome":
                    command = "\"C:/Program Files/Google/Chrome/Application/chrome.exe\" \"" + file.getAbsolutePath() + "\"";
                    break;
                case "Brave":
                    command = "\"C:/Program Files/BraveSoftware/Brave-Browser/Application/brave.exe\" \"" + file.getAbsolutePath() + "\"";
                    break;
                case "Firefox":
                    command = "\"C:/Program Files/Mozilla Firefox/firefox.exe\" \"" + file.getAbsolutePath() + "\"";
                    break;
                case "Edge":
                    command = "\"C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe\" \"" + file.getAbsolutePath() + "\"";
                    break;
                case "Internet Explorer":
                    command = "\"C:/Program Files/Internet Explorer/iexplore.exe\" \"" + file.getAbsolutePath() + "\"";
                    break;
                case "Safari":
                    command = "open -a Safari \"" + file.getAbsolutePath() + "\"";
                    break;
            }
            if (!command.isEmpty()) {
                Runtime.getRuntime().exec(command);
            }
        }
    }
}
