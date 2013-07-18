package dyagmin.greppage;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class GreppageWindow extends JFrame {



    public static void load() {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                GreppageWindow window = new GreppageWindow();
            }

        });

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

        JMenuItem saveResultsMenuItem = new JMenuItem("Save Search Results");
        saveResultsMenuItem.setMnemonic(KeyEvent.VK_S);
        menu.add(saveResultsMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        menu.add(exitMenuItem);

        menuBar.add(menu);

        this.setJMenuBar(menuBar);

    }

}
