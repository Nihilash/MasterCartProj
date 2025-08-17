package ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import model.CartItem;

public class PDFGenerator {

    /**
     * Generates a simple HTML invoice file at user-specified location.
     */
    public static void generateInvoice(String username, List<CartItem> cartItems, double totalAmount) {
        try {
            // Open file chooser dialog for user to pick save location
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Invoice");
            fileChooser.setSelectedFile(new File("Invoice_" + username + "_" + System.currentTimeMillis() + ".html"));

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                // Load background image from inside JAR
                String backgroundImage = PDFGenerator.class
                        .getResource("/Mastercartimages/invoice.jpg")
                        .toExternalForm();

                try (FileWriter writer = new FileWriter(fileToSave)) {
                    writer.write("<html><head>");
                    writer.write("<title>MasterCart Invoice</title>");
                    writer.write("<style>");
                    writer.write("body { "
                            + "font-family: Arial, sans-serif; "
                            + "margin: 20px; "
                            + "background-image: url('" + backgroundImage + "'); "
                            + "background-size: cover; "
                            + "background-repeat: no-repeat; "
                            + "background-position: center center;"
                            + "}");
                    writer.write("h1 { text-align: center; }");
                    writer.write("table { width: 100%; border-collapse: collapse; margin-top: 20px; background-color: rgba(255,255,255,0.9); }");
                    writer.write("th, td { border: 1px solid #333; padding: 8px; text-align: center; }");
                    writer.write("th { background-color: rgba(242,242,242,0.9); }");
                    writer.write(".total { text-align: right; font-weight: bold; font-size: 16px; margin-top: 20px; }");
                    writer.write("</style>");
                    writer.write("</head><body>");

                    writer.write("<h1>MasterCart Invoice</h1>");
                    writer.write("<p>User: " + username + "</p>");
                    writer.write("<p>Date: " + new java.util.Date() + "</p>");
                    writer.write("<p>Invoice ID: " + System.currentTimeMillis() + "</p>");

                    // Table header
                    writer.write("<table>");
                    writer.write("<tr>");
                    writer.write("<th>ID</th>");
                    writer.write("<th>Product</th>");
                    writer.write("<th>Category ID</th>");
                    writer.write("<th>Qty</th>");
                    writer.write("<th>Unit Price</th>");
                    writer.write("<th>Total</th>");
                    writer.write("</tr>");

                    for (CartItem item : cartItems) {
                        writer.write("<tr>");
                        writer.write("<td>" + item.getId() + "</td>");
                        writer.write("<td>" + item.getProduct().getName() + "</td>");
                        writer.write("<td>" + item.getProduct().getCategoryId() + "</td>");
                        writer.write("<td>" + item.getQuantity() + "</td>");
                        writer.write("<td>₹" + String.format("%.2f", item.getUnitPriceInr()) + "</td>");
                        writer.write("<td>₹" + String.format("%.2f", item.getLineTotal()) + "</td>");
                        writer.write("</tr>");
                    }

                    writer.write("</table>");
                    writer.write("<p class='total'>Grand Total: ₹" + String.format("%.2f", totalAmount) + "</p>");
                    writer.write("</body></html>");

                    System.out.println("Invoice saved at: " + fileToSave.getAbsolutePath());
                }
            } else {
                System.out.println("Invoice generation cancelled by user.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

