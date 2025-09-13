# Notepad Application

A desktop text editor built with Java Swing.

## About the Application

This is a GUI-based text editor similar to Windows Notepad, created using Java Swing framework. The application provides essential text editing functionality with a user-friendly interface including menus, dialogs, and keyboard shortcuts.

## Features

### File Operations
- Create new documents
- Open existing text files
- Save documents to file
- Save documents with new filename
- Exit with unsaved changes protection

### Text Editing
- Cut, copy, and paste text
- Select all text functionality
- Multi-line text editing with word wrap
- Scrollable text area for large documents

### User Interface
- Menu bar with File, Edit, and Help menus
- Keyboard shortcuts for quick operations
- File chooser dialogs for opening and saving
- Confirmation dialogs to prevent data loss
- About dialog with application information

### Additional Features
- Font selection (family and size)
- Text color customization
- Document modification tracking
- Automatic file extension handling

## How to Run

1. Compile the Java file:
   ```bash
   javac Notepad.java
   ```

2. Run the application:
   ```bash
   java Notepad
   ```

## Technical Details

- **Language**: Java
- **GUI Framework**: Swing
- **Main Components**: JFrame, JTextArea, JMenuBar, JScrollPane, JFileChooser
- **Design Pattern**: Event-driven programming with ActionListener
- **File Format**: Plain text files (.txt)

## Assignment Requirements

This application fulfills the Advanced Programming Techniques course requirements:
- Basic notepad functionality (text editing, file operations)
- Extended Edit menu (Cut, Copy, Paste, Select All)
- Help menu with About dialog
- Optional enhancements (Font and Color choosers)

## Author

- **Name**: Shyaman Jeewannath
- **Student ID**: s16808
- **Course**: Advanced Programming Techniques- IT3003
