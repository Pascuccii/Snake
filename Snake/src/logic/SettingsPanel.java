package logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import entities.Direction;
import entities.Obstacle;
import logging.ServerLogger;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel implements ActionListener {

	SettingsPanel() {
		this.setPreferredSize(new Dimension(700, 700));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				setVisible(false);
				break;
			}
		}
	}
}
