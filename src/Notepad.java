import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Author: Shyaman Jeewannath
 * ID: s16808
 */
public class Notepad extends JFrame implements ActionListener {


    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, helpMenu;
    private JMenuItem newItem, openItem, saveItem, saveAsItem, exitItem;
    private JMenuItem cutItem, copyItem, pasteItem, selectAllItem;
    private JMenuItem aboutItem, fontItem, colorItem;
    private JFileChooser fileChooser;


    private File currentFile;
    private boolean isModified = false;

    public Notepad() {
        initializeComponents();
        setupMenus();
        setupLayout();
        setupEventHandlers();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Notepad");
        setVisible(true);
    }

    private void initializeComponents() {

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));


        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
        });
    }

    private void setupMenus() {
        menuBar = new JMenuBar();

        // File Menu
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        newItem = new JMenuItem("New", KeyEvent.VK_N);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));

        openItem = new JMenuItem("Open", KeyEvent.VK_O);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

        saveItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        saveAsItem = new JMenuItem("Save As...", KeyEvent.VK_A);
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));

        exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);


        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        cutItem = new JMenuItem("Cut", KeyEvent.VK_T);
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));

        copyItem = new JMenuItem("Copy", KeyEvent.VK_C);
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));

        pasteItem = new JMenuItem("Paste", KeyEvent.VK_P);
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));

        selectAllItem = new JMenuItem("Select All", KeyEvent.VK_A);
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(selectAllItem);


        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        fontItem = new JMenuItem("Font...", KeyEvent.VK_F);
        colorItem = new JMenuItem("Color...", KeyEvent.VK_C);
        aboutItem = new JMenuItem("About", KeyEvent.VK_A);

        helpMenu.add(fontItem);
        helpMenu.add(colorItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {

        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        exitItem.addActionListener(this);

        cutItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);
        selectAllItem.addActionListener(this);

        fontItem.addActionListener(this);
        colorItem.addActionListener(this);
        aboutItem.addActionListener(this);

        // Window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                newFile();
                break;
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "Save As...":
                saveAsFile();
                break;
            case "Exit":
                exitApplication();
                break;
            case "Cut":
                textArea.cut();
                break;
            case "Copy":
                textArea.copy();
                break;
            case "Paste":
                textArea.paste();
                break;
            case "Select All":
                textArea.selectAll();
                break;
            case "Font...":
                showFontChooser();
                break;
            case "Color...":
                showColorChooser();
                break;
            case "About":
                showAboutDialog();
                break;
        }
    }

    private void newFile() {
        if (isModified) {
            int choice = showSaveDialog();
            if (choice == JOptionPane.YES_OPTION) {
                if (!saveFile()) return;
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        textArea.setText("");
        currentFile = null;
        setModified(false);
        updateTitle();
    }

    private void openFile() {
        if (isModified) {
            int choice = showSaveDialog();
            if (choice == JOptionPane.YES_OPTION) {
                if (!saveFile()) return;
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                textArea.setText(content);
                currentFile = file;
                setModified(false);
                updateTitle();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error reading file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean saveFile() {
        if (currentFile == null) {
            return saveAsFile();
        } else {
            try {
                Files.write(Paths.get(currentFile.getPath()), textArea.getText().getBytes());
                setModified(false);
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }

    private boolean saveAsFile() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getPath() + ".txt");
            }

            try {
                Files.write(Paths.get(file.getPath()), textArea.getText().getBytes());
                currentFile = file;
                setModified(false);
                updateTitle();
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    private void exitApplication() {
        if (isModified) {
            int choice = showSaveDialog();
            if (choice == JOptionPane.YES_OPTION) {
                if (!saveFile()) return;
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        System.exit(0);
    }

    private void showFontChooser() {
        Font currentFont = textArea.getFont();


        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        String selectedFont = (String) JOptionPane.showInputDialog(this,
                "Choose a font:", "Font Chooser",
                JOptionPane.PLAIN_MESSAGE, null, fontNames, currentFont.getFontName());

        if (selectedFont != null) {
            String[] sizes = {"8", "10", "12", "14", "16", "18", "20", "24", "28", "32"};
            String selectedSize = (String) JOptionPane.showInputDialog(this,
                    "Choose font size:", "Font Size",
                    JOptionPane.PLAIN_MESSAGE, null, sizes, String.valueOf(currentFont.getSize()));

            if (selectedSize != null) {
                int size = Integer.parseInt(selectedSize);
                textArea.setFont(new Font(selectedFont, currentFont.getStyle(), size));
            }
        }
    }

    private void showColorChooser() {
        Color currentColor = textArea.getForeground();
        Color newColor = JColorChooser.showDialog(this, "Choose Text Color", currentColor);
        if (newColor != null) {
            textArea.setForeground(newColor);
        }
    }

    private void showAboutDialog() {
        String aboutText = "Notepad Application\n\n" +
                "Created with Java Swing\n" +
                "Features:\n" +
                "• File operations (New, Open, Save, Save As, Exit)\n" +
                "• Edit operations (Cut, Copy, Paste, Select All)\n" +
                "• Font and Color customization\n\n" +
                "Author: [Your Name]\n" +
                "Student ID: [Your Student ID]\n" +
                "Course: Advanced Programming Techniques";

        JOptionPane.showMessageDialog(this, aboutText, "About Notepad",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private int showSaveDialog() {
        return JOptionPane.showConfirmDialog(this,
                "Do you want to save changes?", "Save Changes",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private void setModified(boolean modified) {
        this.isModified = modified;
        updateTitle();
    }

    private void updateTitle() {
        String title ="Notepad";
        if (currentFile != null) {
            title += " - " + currentFile.getName();
        } else {
            title += " - Untitled";
        }
        if (isModified) {
            title += "*";
        }
        setTitle(title);
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new Notepad());
    }
}