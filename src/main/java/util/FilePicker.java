package util;

// Source of this code: http://www.codejava.net/java-se/swing/file-picker-component-in-swing

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilePicker extends JPanel {
    private String textFieldLabel;
    private String buttonLabel;

    private JLabel label;
    private JTextField textField;
    private JButton button;

    private JFileChooser fileChooser;

    private int mode;
    public static final int MODE_OPEN = 1;
    public static final int MODE_SAVE = 2;

    public FilePicker(String textFieldLabel, String buttonLabel, String initialValue, JFileChooser _fileChooser) {
        this.textFieldLabel = textFieldLabel;
        this.buttonLabel = buttonLabel;

        fileChooser = _fileChooser;

        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // creates the GUI
        label = new JLabel(textFieldLabel);

        textField = new JTextField(initialValue, 30);
        button = new JButton(buttonLabel);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });

        add(label);
        add(textField);
        add(button);

    }

    private void buttonActionPerformed(ActionEvent evt) {
        if (mode == MODE_OPEN) {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        } else if (mode == MODE_SAVE) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    public void addFileTypeFilter(String[] extensions, String description) {
        for (String extension : extensions) {
            FileTypeFilter filter = new FileTypeFilter(extension, description);
            FileFilter[] fileFilters = fileChooser.getChoosableFileFilters();
            boolean add = true;
            for (FileFilter fileFilter : fileFilters) {
                if (fileFilter.getDescription().equals(filter.getDescription())) {
                    add = false;
                }
            }
            if (add) {
                fileChooser.addChoosableFileFilter(filter);
            }
        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getSelectedFilePath() {
        return textField.getText();
    }
}