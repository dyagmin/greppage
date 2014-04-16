package com.danielyagmin.greppage.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class Window extends JFrame {

    public JTabbedPane mMainTabbedPane;
    private JMenuItem saveResultsMenuItem;

    public static void load() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Window();
            }

        });
    }

    private Window() {
        super();
        this.initialize();
    }

    private void initialize() {
        this.setVisible(true);
        this.setTitle("Greppage");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.initializeMenu();
        this.mMainTabbedPane = new JTabbedPane();
        this.add(this.mMainTabbedPane);
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Greppage");
        JMenuItem newSearchMenuItem = new JMenuItem("New Search");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        newSearchMenuItem.setMnemonic(KeyEvent.VK_N);
        menu.add(newSearchMenuItem);
        newSearchMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new NewSearchDialog(Window.this);
            }

        });

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
    }

}
