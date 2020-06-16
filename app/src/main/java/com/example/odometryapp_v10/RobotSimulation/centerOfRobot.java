package com.example.odometryapp_v10.RobotSimulation;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class centerOfRobot extends javax.swing.JFrame implements ActionListener, ItemListener {

	public centerOfRobot() {
		initComponents();
	}

	private javax.swing.JLabel jLabel1;
	private static javax.swing.JLabel jLabel2;

	public static void main(String[] args) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(centerOfRobot.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(centerOfRobot.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(centerOfRobot.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			Logger.getLogger(centerOfRobot.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new centerOfRobot().setVisible(true);
//                setPosition(72.0,72.0);
//                setHeading(Math.toRadians(270));
			}
		});
	}

	private void initComponents() {
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		// menu bar init
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Program");
		JMenuItem menuItem = new JMenuItem("Run", KeyEvent.VK_T);
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);

//		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();

		// init window
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("centerOfRobot");
		setBounds(new java.awt.Rectangle(0, 0, 800, 822));
		setMaximumSize(new java.awt.Dimension(850, 850));
		setMinimumSize(new java.awt.Dimension(750, 750));
		setPreferredSize(new java.awt.Dimension(800, 822));
		setSize(new java.awt.Dimension(800, 1062));
		setResizable(true);

		jLabel2.setText("");
		jLabel2.setBounds(new java.awt.Rectangle(0, 0, 100, 100));
		jLabel2.setIcon(getImageIcon(new File(System.getProperty("user.dir") + "/src/robot-3.png")));

//		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
//		getContentPane().setLayout(layout);
//		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//				.addGroup(layout.createSequentialGroup()
//						.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800,
//								javax.swing.GroupLayout.PREFERRED_SIZE)
//						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
////                .addComponent(jLabel2)
//						.addGap(0, 4235, Short.MAX_VALUE)));
//		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//				.addGroup(layout.createSequentialGroup()
//						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
//								jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800,
//								javax.swing.GroupLayout.PREFERRED_SIZE)
////                    .addComponent(jLabel2))
//						).addGap(0, 4212, Short.MAX_VALUE)));
//
//		jLabel1.add(jLabel2);

		pack();
	}

	public void itemStateChanged(ItemEvent e) {

	}

	public ImageIcon getImageIcon(File f) {
		Image im = null;
		try {
			im = ImageIO.read(f);
		} catch (IOException ex) {
			Logger.getLogger(centerOfRobot.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new ImageIcon(im);
	}

	public void actionPerformed(ActionEvent e) {

	}

}
