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



public class Window extends JFrame {

    private JTabbedPane mMainTabbedPane;
    private JMenuItem mExitMenuItem = new JMenuItem("Exit");
    private JMenuItem mSaveResultsMenuItem = new JMenuItem("New Search");
    private JMenuItem mNewSearchMenuItem = new JMenuItem("New Search");

    public Window() {
        super();
        initialize();
    }

    public void addExitListener(ActionListener al) {
        mExitMenuItem.addActionListener(al);
    }

    public void addNewSearchButtonListener(ActionListener al) {
        mNewSearchMenuItem.addActionListener(al);
    }

    public void addResultTabPanel(ResultTabPanel panel) {
        mMainTabbedPane.addTab(panel.getTitle(), panel);
    }

    private void initialize() {
        setTitle("Greppage");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initializeMenu();
        mMainTabbedPane = new JTabbedPane();
        add(mMainTabbedPane);
        setVisible(true);
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Greppage");

        mNewSearchMenuItem.setMnemonic(KeyEvent.VK_N);
        mExitMenuItem.setMnemonic(KeyEvent.VK_X);

        menu.add(mNewSearchMenuItem);
        menu.add(mExitMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

}
