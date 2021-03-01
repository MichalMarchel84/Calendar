package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EditWindow extends JFrame implements ActionListener {
    private final Entry entry;
    private final JTextField title = new JTextField(50);
    private final JTextArea description = new JTextArea();
    JButton accept = new JButton(I18n.getPhrase("save_button"));
    JButton cancel = new JButton(I18n.getPhrase("cancel_button"));

    EditWindow(Entry entry){
        this.entry = entry;
        title.setText(entry.getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 20));
        description.setText(entry.getDescription());
        double[] cols = {10, 0.3, TableLayout.FILL, TableLayout.FILL, 10};
        double[] rows = {10, 0.07, 0.15, 0.07, TableLayout.FILL, 0.1, 10};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(5);
        lay.setVGap(10);
        JPanel p = new JPanel();
        p.setLayout(lay);
        JPanel temp = new JPanel();
        temp.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        p.add(temp, "1 1 1 5 f f");
        p.add(new JLabel("Title"), "2 1 l c");
        p.add(title, "2 2 3 2 f c");
        p.add(new JLabel("Description"), "2 3 l c");
        p.add(description, "2 4 3 4 f f");
        p.add(accept, "2 5 c c");
        p.add(cancel, "3 5 c c");
        accept.addActionListener(this);
        cancel.addActionListener(this);
        this.add(p);
        this.setSize(new Dimension(500, 300));
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(entry.label);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(accept)){
            entry.setTitle(title.getText());
            entry.setDescription(description.getText());
            this.dispose();
        }
        else if(e.getSource().equals(cancel)){
            this.dispose();
        }
    }
}
