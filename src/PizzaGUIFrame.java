import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppings;
    private JTextArea receiptArea;
    private JButton orderButton, clearButton, quitButton;
    private final double[] sizePrices = {8.00, 12.00, 16.00, 20.00};
    private final double toppingPrice = 1.00;
    private final double taxRate = 0.07;

    public PizzaGUIFrame() {
        setTitle("Pizza Ordering Form");
        setSize(500,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //crust panel
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(new TitledBorder("Crust Type"));
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-Dish");
        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);

        //size options
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(new TitledBorder("Pizza Size"));
        String[] sizes = {"Small", "Medium", "Large", "Super"};
        sizeComboBox = new JComboBox<>(sizes);
        sizePanel.add(sizeComboBox);

        //toppings
        JPanel toppingsPanel = new JPanel();
        toppingsPanel.setBorder(new TitledBorder("Toppings ($1.00 ap)"));
        toppingsPanel.setLayout(new GridLayout(3,2));
        String[] toppingNames = {"Pepperoni","Mushrooms","Onions","Olives","Bacon","Double Cheese"};
        toppings = new JCheckBox[toppingNames.length];
        for(int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            toppingsPanel.add(toppings[i]);
        }

        //receipt
        //edit: receipt display
        JPanel receiptPanel = new JPanel();
        receiptArea = new JTextArea(10,30);
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        receiptPanel.add(scrollPane);

        //buttons
        JPanel buttonPanel = new JPanel();
        orderButton = new JButton("Place Order");
        clearButton = new JButton("Clear Form");
        quitButton = new JButton("Quit");
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        //panels
        JPanel topPanel = new JPanel(new GridLayout(1,3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingsPanel);
        add(topPanel, BorderLayout.NORTH);
        add(receiptPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        orderButton.addActionListener(new OrderButtonListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> quitApplication());
    }

    private class OrderButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String crust = "";
            if(thinCrust.isSelected()) crust = "Thin Crust";
            else if(regularCrust.isSelected()) crust = "Regular Crust";
            else if(deepDishCrust.isSelected()) crust = "Deep-Dish Crust";

            int sizeIndex = sizeComboBox.getSelectedIndex();
            String size = (String) sizeComboBox.getSelectedItem();
            double basePrice = sizePrices[sizeIndex];

            StringBuilder selectedToppings = new StringBuilder();
            int toppingsCount = 0;
            for(JCheckBox topping : toppings) {
                if(topping.isSelected()) {
                    selectedToppings.append(topping.getText()).append("\n");
                    toppingsCount++;
                }
            }

            if(crust.isEmpty()) {
                JOptionPane.showMessageDialog(PizzaGUIFrame.this, "Please select a crust type.", "Incomplete Order", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double toppingsCost = toppingsCount * toppingPrice;
            double subtotal = basePrice + toppingsCost;
            double tax = subtotal * taxRate;
            double total = subtotal + (subtotal * taxRate);

            receiptArea.setText("=========================================\n");
            receiptArea.append(String.format("%s & %s\t$%.2f\n", crust, size, basePrice));
            receiptArea.append("-----------------------------------------\n");
            receiptArea.append(selectedToppings.toString());
            receiptArea.append(String.format("Subtotal:\t\t$%.2f\n", subtotal));
            receiptArea.append(String.format("Tax (7%%):\t\t$%.2f\n", tax));
            receiptArea.append("-----------------------------------------\n");
            receiptArea.append(String.format("Total:\t\t$%.2f\n", total));
            receiptArea.append("=========================================\n");

        }
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDishCrust.setSelected(false);
        sizeComboBox.setSelectedIndex(0);
        for(JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        receiptArea.setText("");
    }

    private void quitApplication() {
        int choice = JOptionPane.showConfirmDialog(this,"Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
