package com.example.odometryapp_v10.RobotSimulation;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import internal.Skystone.MecanumDrivetrain;
import internal.Structure.Odometry;
import internal.Structure.Path;
import internal.Structure.Pose;

public class RobotSim extends javax.swing.JFrame implements ActionListener, ItemListener, KeyListener {

	public static Pose startingPosition = new Pose(8, 37, 0);// (32.813, 8.875, Math.PI);

	private static final long serialVersionUID = 1L;
	static Canvas canvas;

	private static javax.swing.JLabel jLabel1;
	private static javax.swing.JLabel jLabel2;

	public RobotSim() {
		initComponents();
	}

	public static Odometry odometry;

	static boolean done = false;

	static int w, h;

	public static void setHeading(double headingRadians) {
		try {
			BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + "/src/robot-4.png"));
			final double rads = -headingRadians + (Math.PI / 2);
			final double sin = Math.abs(Math.sin(rads));
			final double cos = Math.abs(Math.cos(rads));
			if (!done) {
				w = (int) image.getWidth();
				h = (int) image.getHeight();
				done = true;
			}
			final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
			final AffineTransform at = new AffineTransform();
			at.translate(w / 2, h / 2);
			at.rotate(rads, 0, 0);
//			at.translate(-8 / Math.sqrt(2), -8 / Math.sqrt(2));
			at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
			final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			rotateOp.filter(image, rotatedImage);
//			jLabel2.setBounds((int) ((odometry.getCurrentCoordinate().x * 800 / 144) - (w / 2)),
//					(int) (((144 - odometry.getCurrentCoordinate().y) * 800 / 144) - (h / 2)),
//					(int) Math.floor(image.getWidth() * cos + image.getHeight() * sin),
//					(int) Math.floor(image.getHeight() * cos + image.getWidth() * sin));
			jLabel2.setIcon(new ImageIcon(rotatedImage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setPosition(double x, double y) {
		Rectangle bounds = jLabel2.getBounds();
		int w = (int) (bounds.width / 2);
		int h = (int) (bounds.height / 2);
		int pixels_x = (int) (x * 800 / 144);// was 5
		int pixels_y = (int) ((144 - y) * 800 / 144);// was 2
		jLabel2.setBounds(new java.awt.Rectangle(pixels_x - 50, pixels_y - 50, 100, 100));
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

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();

		// init window
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("RobotSim");
		setBounds(new java.awt.Rectangle(0, 23, 800, 822));
		setMaximumSize(new java.awt.Dimension(850, 850));
		setMinimumSize(new java.awt.Dimension(750, 750));
		setPreferredSize(new java.awt.Dimension(800, 822));
		setSize(new java.awt.Dimension(800, 1062));
		setResizable(true);

		addKeyListener(this);

		jLabel1.setText("Hello world");
		jLabel1.setLocation(new java.awt.Point(25, 25));
		jLabel1.setIcon(getImageIcon(new File(System.getProperty("user.dir") + "/src/field3.png"))); // NOI18N

		jLabel2.setText("");
		jLabel2.setBounds(new java.awt.Rectangle(600, 600, 100, 100));
		jLabel2.setIcon(getImageIcon(new File(System.getProperty("user.dir") + "/src/robot-3.png")));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jLabel2)
						.addGap(0, 4235, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
								jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800,
								javax.swing.GroupLayout.PREFERRED_SIZE)
//                    .addComponent(jLabel2))
						).addGap(0, 4212, Short.MAX_VALUE)));

//		jLabel2.add(new MyFrame());

		jLabel1.add(jLabel2);
		pack();
	}

	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(RobotSim.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(RobotSim.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(RobotSim.class.getName()).log(Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			Logger.getLogger(RobotSim.class.getName()).log(Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new RobotSim().setVisible(true);
				odometry = new Odometry(startingPosition);
				setPosition(startingPosition.x, startingPosition.y);
				setHeading(startingPosition.heading);
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
//		BackCalculation.timeStampLeft = BackCalculation.timeStampCenter = BackCalculation.timeStampRight = System
//				.currentTimeMillis();
//		BackCalculation.setBackLeftPower(0.0);
//		BackCalculation.setFrontLeftPower(0.0);
//		BackCalculation.setBackRightPower(0.0);
//		BackCalculation.setFrontRightPower(0.0);
//		odometry.startBackgroundPositionUpdates();

//		ArrayList<MovementPose> movementCoordinates = new ArrayList<>();

//		movementCoordinates
//				.add(new MovementPose(new Pose(55, 45, Math.toRadians(135)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(72, 33, Math.toRadians(135)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(72, 27, Math.toRadians(135)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(120, 27, Math.toRadians(270)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(120, 40.75, Math.toRadians(270)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(108, 24, Math.toRadians(180)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(115, 24, Math.toRadians(180)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(113.5, 26, Math.toRadians(180)), MovementPose.MovementType.strafe));
//
//		movementCoordinates
//				.add(new MovementPose(new Pose(40, 30, Math.toRadians(180)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(38, 48, Math.toRadians(180)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(35, 48, Math.toRadians(180)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(35, 33, Math.toRadians(180)), MovementPose.MovementType.strafe));
//
//		movementCoordinates
//				.add(new MovementPose(new Pose(96, 24, Math.toRadians(180)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(114, 26, Math.toRadians(180)), MovementPose.MovementType.strafe));
//		movementCoordinates
//				.add(new MovementPose(new Pose(72, 33, Math.toRadians(180)), MovementPose.MovementType.strafe));

//		poses.add(new Pose(32.8125, 8.875, Math.toRadians(0)));
//		poses.add(new Pose(48, 36, Math.toRadians(0)));
//		poses.add(new Pose(47, 37, Math.toRadians(0)));
//		poses.add(new Pose(47, 27, Math.toRadians(0)));
//		poses.add(new Pose(115, 27, Math.toRadians(0)));
//		poses.add(new Pose(118, 42, Math.toRadians(0)));
//		poses.add(new Pose(120, 27, Math.toRadians(0)));
//		poses.add(new Pose(28, 27, Math.toRadians(0)));
//		poses.add(new Pose(22, 31, Math.toRadians(0)));
//		poses.add(new Pose(22, 35.5, Math.toRadians(0)));
//		poses.add(new Pose(22, 27, Math.toRadians(0)));
//		poses.add(new Pose(127, 27, Math.toRadians(0)));
//		poses.add(new Pose(127, 42.5, Math.toRadians(0)));
//		poses.add(new Pose(120, 20, Math.toRadians(-90)));
//		poses.add(new Pose(120, 37, Math.toRadians(-90)));
//		poses.add(new Pose(108, 24, Math.toRadians(-180)));
//		poses.add(new Pose(115, 24, Math.toRadians(-180)));
//		poses.add(new Pose(72, 36, Math.toRadians(-180)));
//		poses.add(new Pose(72, 36, Math.toRadians(-180)));
//		poses.add(new Pose(27, 37, Math.toRadians(0)));
//		poses.add(new Pose(48, 37, Math.toRadians(0)));
//		poses.add(new Pose(72, 30, Math.toRadians(0)));
//		poses.add(new Pose(100, 38, Math.toRadians(0)));
//		poses.add(new Pose(130, 38, Math.toRadians(0)));
//		poses.add(new Pose(100, 38, Math.toRadians(0)));
//		poses.add(new Pose(72, 32, Math.toRadians(0)));
//		poses.add(new Pose(48, 37, Math.toRadians(0)));
//		poses.add(new Pose(27, 37, Math.toRadians(0)));
//		poses.add(new Pose(48, 37, Math.toRadians(0)));
//		poses.add(new Pose(72, 32, Math.toRadians(0)));
//		poses.add(new Pose(100, 38, Math.toRadians(0)));
//		poses.add(new Pose(140, 38, Math.toRadians(0)));
//		poses.add(new Pose(110, 35, Math.toRadians(0)));
//		poses.add(new Pose(72, 35, Math.toRadians(0)));
//		poses.add(new Pose(72, 35, Math.toRadians(0)));
//
//		ArrayList<Pose> poses = new ArrayList<Pose>();
//
//		poses.add(new Pose(8, 37, Math.toRadians(0)));
//		poses.add(new Pose(60, 32, Math.toRadians(0)));
//		poses.add(new Pose(84, 32, Math.toRadians(0)));
//		poses.add(new Pose(105, 37, Math.toRadians(0)));
//		poses.add(new Pose(125, 37.5, Math.toRadians(0)));
//		poses.add(new Pose(125, 37.5, Math.toRadians(0)));
//
//		Path path = new Path(poses, 0.2);
//
//		MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
//
//		//////////
//
//		ArrayList<Pose> poses2 = new ArrayList<Pose>();
//
//		poses2.add(new Pose(125, 37.5, Math.toRadians(0)));
//		poses2.add(new Pose(105, 37, Math.toRadians(0)));
//		poses2.add(new Pose(84, 33, Math.toRadians(0)));
//		poses2.add(new Pose(60, 33, Math.toRadians(0)));
//		poses2.add(new Pose(33, 35, Math.toRadians(0)));
//		poses2.add(new Pose(33, 35, Math.toRadians(0)));
//
//		Path path2 = new Path(poses2, 0.2);
//
//		MecanumDrivetrain.startBackgroundPositionUpdates(path2, 0.5);

//
//		ArrayList<Pose> poses2 = new ArrayList<Pose>();
//
//		poses2.add(new Pose(125, 37.5, Math.toRadians(0)));
//		poses2.add(new Pose(105, 37, Math.toRadians(0)));
//		poses2.add(new Pose(84, 33, Math.toRadians(0)));
//		poses2.add(new Pose(60, 33, Math.toRadians(0)));
//		poses2.add(new Pose(33, 35, Math.toRadians(0)));
//		poses2.add(new Pose(33, 35, Math.toRadians(0)));
//
//		Path path2 = new Path(poses2, 0.2);
//
//		MecanumDrivetrain.startBackgroundPositionUpdates(path2, 0.5);

//		MecanumDrivetrain.strafeToPosition(new Pose(100, 23, Math.toRadians(-180)), 0.05);
//		MecanumDrivetrain.rampTankToPosition(new Pose(100, 23, Math.toRadians(-180)), 0.05, TankDirection.backward,
//				true);

	}

	private int funcIndex = 0;

	private int i = 0;
	private int n = 0;
	private int t = 0;

	private final int rand = 0;// (int) ((Math.random() * 5));

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode() + ", " + rand);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			funcIndex++;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT + rand) {
			funcIndex--;
		}

		if (e.getKeyCode() == KeyEvent.VK_I + rand) {
			i++;
		}

		if (e.getKeyCode() == KeyEvent.VK_N + rand && i != 0) {
			i = 0;
			n++;
		}

		if (e.getKeyCode() == KeyEvent.VK_I + rand && n != 0) {
			n = 0;
			i++;
		}

		if (e.getKeyCode() == KeyEvent.VK_T + rand && i != 0) {
			functionsToCall(0);
			System.out.println("Init Complete");
		}

		if (e.getKeyCode() == KeyEvent.VK_0) {
			funcIndex = 0;
			functionsToCall(funcIndex);
		}

		functionsToCall(funcIndex);
	}

	public void functionsToCall(int i) {
		if (i == 0) { // init
//			System.out.println("Q");

			BackCalculation.timeStampLeft = BackCalculation.timeStampCenter = BackCalculation.timeStampRight = System
					.currentTimeMillis();
			BackCalculation.setBackLeftPower(0.0);
			BackCalculation.setFrontLeftPower(0.0);
			BackCalculation.setBackRightPower(0.0);
			BackCalculation.setFrontRightPower(0.0);
			odometry.startBackgroundPositionUpdates();
		} else if (i == 1) { // foundation 1
			ArrayList<Pose> poses = new ArrayList<Pose>();

			poses.add(new Pose(8, 37, Math.toRadians(0)));
			poses.add(new Pose(60, 32, Math.toRadians(0)));
			poses.add(new Pose(84, 32, Math.toRadians(0)));
			poses.add(new Pose(105, 37, Math.toRadians(0)));
			poses.add(new Pose(125, 37.5, Math.toRadians(0)));
			poses.add(new Pose(125, 37.5, Math.toRadians(0)));

			Path path = new Path(poses, 0.2);

			MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
		} else if (i == 2) { // block 2
			ArrayList<Pose> poses = new ArrayList<Pose>();

			poses.add(new Pose(125, 37.5, Math.toRadians(0)));
			poses.add(new Pose(105, 37, Math.toRadians(0)));
			poses.add(new Pose(84, 33, Math.toRadians(0)));
			poses.add(new Pose(60, 33, Math.toRadians(0)));
			poses.add(new Pose(20, 35, Math.toRadians(0)));
			poses.add(new Pose(20, 35, Math.toRadians(0)));

			Path path = new Path(poses, 0.2);

			MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
		} else if (i == 3) { // foundation 2
			ArrayList<Pose> poses = new ArrayList<Pose>();

			poses.add(new Pose(20, 35, Math.toRadians(0)));
			poses.add(new Pose(60, 32, Math.toRadians(0)));
			poses.add(new Pose(84, 32, Math.toRadians(0)));
			poses.add(new Pose(105, 37, Math.toRadians(0)));
			poses.add(new Pose(135, 37.5, Math.toRadians(0)));
			poses.add(new Pose(135, 37.5, Math.toRadians(0)));

			Path path = new Path(poses, 0.2);

			MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
		} else if (i == 4) { // block 3
			ArrayList<Pose> poses = new ArrayList<Pose>();

			poses.add(new Pose(135, 37.5, Math.toRadians(0)));
			poses.add(new Pose(105, 37, Math.toRadians(0)));
			poses.add(new Pose(84, 33, Math.toRadians(0)));
			poses.add(new Pose(60, 33, Math.toRadians(0)));
			poses.add(new Pose(28, 35, Math.toRadians(0)));
			poses.add(new Pose(28, 35, Math.toRadians(0)));

			Path path = new Path(poses, 0.2);

			MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
		} else if (i == 5) { // foundation 3
			ArrayList<Pose> poses = new ArrayList<Pose>();

			poses.add(new Pose(28, 35, Math.toRadians(0)));
			poses.add(new Pose(60, 32, Math.toRadians(0)));
			poses.add(new Pose(84, 32, Math.toRadians(0)));
			poses.add(new Pose(105, 37, Math.toRadians(0)));
			poses.add(new Pose(135, 37.5, Math.toRadians(0)));
			poses.add(new Pose(135, 37.5, Math.toRadians(0)));

			Path path = new Path(poses, 0.2);

			MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
		} else if (i == 6) { // block 4

			ArrayList<Pose> poses = new ArrayList<Pose>();

			poses.add(new Pose(135, 37.5, Math.toRadians(0)));
			poses.add(new Pose(105, 37, Math.toRadians(0)));
			poses.add(new Pose(84, 33, Math.toRadians(0)));
			poses.add(new Pose(60, 33, Math.toRadians(0)));
			poses.add(new Pose(36, 35, Math.toRadians(0)));
			poses.add(new Pose(36, 35, Math.toRadians(0)));

			Path path = new Path(poses, 0.2);

			MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
		} else if (i == 7) { // foundation 4
			ArrayList<Pose> poses = new ArrayList<Pose>();

			poses.add(new Pose(36, 35, Math.toRadians(0)));
			poses.add(new Pose(60, 32, Math.toRadians(0)));
			poses.add(new Pose(84, 32, Math.toRadians(0)));
			poses.add(new Pose(105, 37, Math.toRadians(0)));
			poses.add(new Pose(135, 37.5, Math.toRadians(0)));
			poses.add(new Pose(135, 37.5, Math.toRadians(0)));

			Path path = new Path(poses, 0.2);

			MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
		} else if (i == 8) {
			ArrayList<Pose> poses = new ArrayList<Pose>();

			poses.add(odometry.getCurrentPose());
			if (odometry.getCurrentPose().x < 72) {
				poses.add(new Pose(60, 32, Math.toRadians(0)));
				poses.add(new Pose(65, 32, Math.toRadians(0)));
			} else {
				poses.add(new Pose(84, 32, Math.toRadians(0)));
				poses.add(new Pose(65, 32, Math.toRadians(0)));
			}
			poses.add(new Pose(65, 32, Math.toRadians(0)));

			Path path = new Path(poses, 0.2);

			MecanumDrivetrain.startBackgroundPositionUpdates(path, 0.5);
		}
	}

	public void itemStateChanged(ItemEvent e) {

	}

	public ImageIcon getImageIcon(File f) {
		Image im = null;
		try {
			im = ImageIO.read(f);
		} catch (IOException ex) {
			Logger.getLogger(RobotSim.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new ImageIcon(im);
	}

	public static void sleep(long ms) {
		long currentTime = System.currentTimeMillis();
		while (currentTime + ms > System.currentTimeMillis()) {

		}
	}

	public static void makeFile(String fileName, ArrayList<String> fileContents) {
		File file = new File("C:\\Users\\mihir\\OneDrive\\Desktop\\CSV Data\\" + fileName + ".csv");
		try {
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
		}
		try {
			FileWriter writer = new FileWriter(file);
			for (int i = 0; i < fileContents.size(); i++) {
				writer.write(fileContents.get(i));
			}
			writer.close();
		} catch (IOException e) {
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_Q) {

		} else if (e.getKeyCode() == KeyEvent.VK_W) {

		} else if (e.getKeyCode() == KeyEvent.VK_E) {

		} else if (e.getKeyCode() == KeyEvent.VK_R) {

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_Q) {

		} else if (e.getKeyCode() == KeyEvent.VK_W) {

		} else if (e.getKeyCode() == KeyEvent.VK_E) {

		} else if (e.getKeyCode() == KeyEvent.VK_R) {

		}
	}
}
