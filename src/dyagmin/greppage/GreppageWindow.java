package dyagmin.greppage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class GreppageWindow extends JFrame {

    private static GreppageWindow instance;
    private JTabbedPane tabbedPane;

    public static void load() {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                GreppageWindow.instance = new GreppageWindow();
            }

        });

    }

    public static GreppageWindow getInstance() {
        return GreppageWindow.instance;
    }

    private GreppageWindow() {

        super();
        this.setVisible(true);
        this.setTitle("Greppage");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Greppage");
        
        JMenuItem newSearchMenuItem = new JMenuItem("New Search");
        newSearchMenuItem.setMnemonic(KeyEvent.VK_N);
        menu.add(newSearchMenuItem);
        newSearchMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NewSearchDialog newSearchDialog = new NewSearchDialog(GreppageWindow.getInstance());
            }

        });

        JMenuItem saveResultsMenuItem = new JMenuItem("Save Search Results");
        saveResultsMenuItem.setMnemonic(KeyEvent.VK_S);
        saveResultsMenuItem.setEnabled(false);
        menu.add(saveResultsMenuItem);
        saveResultsMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO save dialog
            }

        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        menu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });

        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        this.tabbedPane = new JTabbedPane();
        this.add(tabbedPane);
    }

    public void addTab(String title, GreppagePanel panel) {
        this.tabbedPane.addTab(title, panel);
    }

}
