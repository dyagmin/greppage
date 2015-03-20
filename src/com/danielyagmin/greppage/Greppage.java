package com.danielyagmin.greppage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;

import javax.swing.*;

public class Greppage implements NewSearchListener {

    private JFrame mWindow;
    private JTabbedPane mMainTabbedPane;

    private Greppage() {

        JFrame.setDefaultLookAndFeelDecorated(false);
        mWindow = new JFrame();
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Greppage");
        JMenuItem saveResultsMenuItem = new JMenuItem("Save Search");
        JMenuItem newSearchMenuItem = new JMenuItem("New Search");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch(UnsupportedLookAndFeelException e) {
        } catch(ClassNotFoundException e) {
        } catch(IllegalAccessException e) {
        } catch(InstantiationException e) {
        }

        mWindow.setTitle("Greppage");
        mWindow.setSize(600, 400);
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        newSearchMenuItem.setMnemonic(KeyEvent.VK_N);
        exitMenuItem.setMnemonic(KeyEvent.VK_X);

        newSearchMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new NewSearchPrompt(mWindow, (NewSearchListener) Greppage.this);
            }

        });

        exitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }

        });

        menu.add(newSearchMenuItem);
        menu.add(exitMenuItem);
        menuBar.add(menu);
        mWindow.setJMenuBar(menuBar);

        mMainTabbedPane = new JTabbedPane();

        mWindow.add(mMainTabbedPane);
        mWindow.setVisible(true);

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run(){
                new Greppage();
            }

        });
    }

    public void newSearch(Options options) {
        JPanel panel = new JPanel();
        mMainTabbedPane.addTab("", panel);
        final SearchResults searchResults = new SearchResults(options, panel);
        searchResults.setSaveActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                SaveResultsPrompt saveResultsPrompt = new SaveResultsPrompt(mWindow, searchResults);
            }

        });
    }

}
