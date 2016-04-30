package KeyLogger;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationSequencesReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationVectorReader;

public class KeyLogger {

	private static JFrame mainFrame;
	private static JLabel headerLabel;
	private static JLabel statusLabel;
	private static JPanel controlPanel;
	private static String username;
	private static String password;
	private static String checkName;
	private static String checkPassword;
	private static Reader reader;
	private static boolean authenticationCheck = false;

	static Date date;
	static Date date2 = null;
	static int ctr = 9;

	static Date date3;
	static Date date4 = null;

	static String keysPressedString, keyPressedTimeString;
	static String keysReleasedString, keyReleasedTimeString;

	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		prepare();
		mainWin();
	}

	private static void prepare() {

		mainFrame = new JFrame();
		mainFrame
				.setContentPane(new JLabel(
						new ImageIcon(
								"C:\\Users\\abhinav\\workspace\\KeyLoggerFinal v2.1\\test5.jpg")));

		mainFrame.setSize(700, 700);

		// mainFrame.setLayout(new GridLayout(6, 2));
		// mainFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		headerLabel = new JLabel("", JLabel.RIGHT);
		statusLabel = new JLabel("", JLabel.CENTER);

		statusLabel.setSize(350, 100);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(headerLabel);
		// mainFrame.add(controlPanel);
		// mainFrame.add(statusLabel);
		// mainFrame.setVisible(true);

	}

	private static void mainWin() {
		headerLabel.setText("Sign In");

		JLabel namelabel = new JLabel("User ID: ", JLabel.CENTER);
		namelabel.setFont(new Font("Serif", Font.PLAIN, 20));

		JLabel passwordLabel = new JLabel("Password: ", JLabel.CENTER);
		passwordLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		JPanel backGround = new JPanel();
		backGround.setLayout(new GridLayout(6, 2, 10, 10));
		backGround.setLocation(100, 200);
		backGround.setSize(500, 250);

		backGround.setOpaque(false);
		// backGround.setLocation(60, 80);
		JLabel n = new JLabel();

		final ArrayList<Long> pressTime = new ArrayList<Long>();
		final ArrayList<Long> releaseTime = new ArrayList<Long>();

		final JCheckBox dispPass = new JCheckBox();
		dispPass.setHorizontalAlignment(SwingConstants.RIGHT);
		final JTextField userText = new JTextField(6);
		final JPasswordField passwordText = new JPasswordField(6);
		final JButton reset = new JButton("Reset It!");
		final JLabel warning = new JLabel("*Please reset, dont use backspace");

		JButton loginButton = new JButton("Login");
		JButton addData = new JButton("New User");

		backGround.add(namelabel);
		backGround.add(userText);
		backGround.add(passwordLabel);
		backGround.add(passwordText);
		backGround.add(n);
		backGround.add(n);
		backGround.add(dispPass);
		backGround.add(loginButton);
		backGround.add(n);
		backGround.add(n);

		backGround.add(addData);
		backGround.add(reset);
		backGround.add(n);
		backGround.add(n);

		backGround.add(warning);

		mainFrame.add(backGround);
		mainFrame.setVisible(true);

		dispPass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (dispPass.isSelected() == true)
					passwordText.setEchoChar((char) 0);
				else
					passwordText.setEchoChar('•');

			}
		});

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				passwordText.setText(null);
				userText.setText(null);
				pressTime.clear();
				releaseTime.clear();

			}
		});

		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				try {

					String line;
					String[] checkFinal = new String[2];
					String CheckUsername, CheckPass;

					CheckUsername = userText.getText();
					CheckPass = passwordText.getText();
					checkName = CheckUsername;
					checkPassword = CheckPass;
					if (username == null) {
						username = checkName;
					}
					File dir = new File(username);
					if (dir.exists() && dir.isDirectory()) {

						File file = new File(username + "\\Login-"
								+ CheckUsername + ".txt");
						BufferedReader br;
						br = new BufferedReader(new FileReader(file));

						int counter = 0;
						while ((line = br.readLine()) != null) {
							checkFinal[counter] = line;
							counter++;
						}

						if (CheckUsername.contains(checkFinal[0])
								&& CheckPass.equals(checkFinal[1])
								&& CheckPass.contains(checkFinal[1])) {
							// JOptionPane.showMessageDialog(null,
							// "Login Successful!");
							authenticationCheck = true;
						} else {
							// JOptionPane.showMessageDialog(null,
							// "Not Indentified");
						}
						System.out.println();
						br.close();
					} else {
						JOptionPane.showMessageDialog(null,
								"User does not exist!");
					}

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					if (authenticationCheck) {
						BufferedWriter out = new BufferedWriter(new FileWriter(
								username + "\\score-" + checkName + ".txt",
								false));

						for (int i = 0; i < pressTime.size(); ++i) {
							long duration = releaseTime.get(i)
									- pressTime.get(i);
							if (i == 0) {
								out.append("[" + String.valueOf(duration)
										+ ". " + 0 + ".]; ");
							} else {
								long latency = pressTime.get(i)
										- releaseTime.get(i - 1);
								out.append("[" + String.valueOf(duration)
										+ ". " + latency + ".]; ");
							}
						}
						pressTime.clear();
						releaseTime.clear();
						out.newLine();
						out.flush();
						out.close();
						Scorer score = new Scorer(checkName, checkPassword);
						score.getProbability(score.readScoreFile(checkName));

						if (score.getResult(score.readScoreFile(checkName)) == 1) {
							JOptionPane.showMessageDialog(null,
									"Authenticated.");
						} else if (score.getResult(score
								.readScoreFile(checkName)) == -1) {
							JOptionPane.showMessageDialog(null,
									"Authentication Failed.");
						} else if (score.getResult(score
								.readScoreFile(checkName)) == 0) {
							JOptionPane.showMessageDialog(null,
									"Please authenticate again.");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Login Failed");
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FileFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		passwordText.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// int a = e.getKeyCode();
				long presstime = e.getWhen();
				date = new Date(presstime);
				pressTime.add(date.getTime());

			}

			@Override
			public void keyReleased(KeyEvent r) {
				// int a = r.getKeyCode();
				long releasetime = r.getWhen();
				date2 = new Date(releasetime);
				releaseTime.add(date2.getTime());

				if (r.getKeyCode() == 8)
					JOptionPane
							.showMessageDialog(null,
									"Please don't use backspace, press the reset button.");
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		addData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFrame SampleInput = new JFrame();
				SampleInput.setSize(700, 700);
				SampleInput.setLayout(new FlowLayout());
				SampleInput.setContentPane(new JLabel(new ImageIcon(
						"C:\\Users\\Devansh\\Desktop\\test5.jpg")));

				JButton Submit = new JButton(
						"                            CONFIRM");
				Submit.setHorizontalAlignment(SwingConstants.LEFT);

				JTextField UserName = new JTextField(6);
				JLabel l1 = new JLabel("Enter your ID", JLabel.LEFT);
				l1.setFont(new Font("Serif", Font.PLAIN, 20));

				JLabel l2 = new JLabel("Enter your password", JLabel.LEFT);
				l2.setFont(new Font("Serif", Font.PLAIN, 20));
				JLabel l3 = new JLabel("Confirm your password", JLabel.LEFT);
				l3.setFont(new Font("Serif", Font.PLAIN, 20));
				JLabel l4 = new JLabel(
						"*Password must be at least 8 characters");

				JTextField PasswordOnce = new JTextField(6);
				JTextField PassWordTwice = new JTextField(6);

				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new GridLayout(6, 2, 10, 10));
				buttonPane.setLocation(95, 200);
				buttonPane.setSize(500, 250);
				buttonPane.setOpaque(false);

				buttonPane.add(l1);
				buttonPane.add(UserName);

				buttonPane.add(l2);
				buttonPane.add(PasswordOnce);

				buttonPane.add(l3);
				buttonPane.add(PassWordTwice);

				buttonPane.add(n);

				buttonPane.add(Submit);
				// buttonPane.add(n);
				buttonPane.add(l4);

				SampleInput.add(buttonPane);
				SampleInput.setVisible(true);

				Submit.setEnabled(false);

				UserName.addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(KeyEvent arg0) {

					}

					@Override
					public void keyReleased(KeyEvent arg0) {

						if (UserName.getText().isEmpty() == false
								&& PasswordOnce.getText().isEmpty() == false
								&& PassWordTwice.getText().isEmpty() == false)
							Submit.setEnabled(true);
						else
							Submit.setEnabled(false);
					}

					@Override
					public void keyTyped(KeyEvent arg0) {
						// TODO Auto-generated method stub

					}
				});

				reset.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

					}
				});

				PasswordOnce.addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(KeyEvent e21) {
						// TODO Auto-generated method stub

					}

					@Override
					public void keyReleased(KeyEvent e22) {
						if (PassWordTwice.getText().contains(
								PasswordOnce.getText())
								&& PassWordTwice.getText().length() == PasswordOnce
										.getText().length()
								&& PassWordTwice.getText().length() >= 8
								&& UserName.getText().isEmpty() == false)
							Submit.setEnabled(true);
						else
							Submit.setEnabled(false);

					}

					@Override
					public void keyTyped(KeyEvent e23) {
						// TODO Auto-generated method stub

					}
				});

				PassWordTwice.addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(KeyEvent arg0) {

					}

					@Override
					public void keyReleased(KeyEvent r2) {
						if (PassWordTwice.getText().contains(
								PasswordOnce.getText())
								&& PassWordTwice.getText().length() == PasswordOnce
										.getText().length()
								&& PassWordTwice.getText().length() >= 8
								&& UserName.getText().isEmpty() == false)
							Submit.setEnabled(true);
						else
							Submit.setEnabled(false);
					}

					@Override
					public void keyTyped(KeyEvent arg0) {
						// TODO Auto-generated method stub

					}
				});

				Submit.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						System.out.println(PasswordOnce.getText());
						System.out.println(PassWordTwice.getText());
						try {
							username = UserName.getText();
							password = PasswordOnce.getText();
							File dir = new File(username);
							if (!dir.exists()) {
								if (dir.mkdir()) {
									System.out.println("Directory is created!");
								} else {
									System.out
											.println("Failed to create directory!");
								}
							}
							BufferedWriter out = new BufferedWriter(
									new FileWriter(username + "\\Login-"
											+ username + ".txt"));
							/*
							 * PrintWriter out = new PrintWriter("Login-" +
							 * username + ".txt");
							 * 
							 * out.println(username); out.println(password);
							 * out.close();
							 */
							out.append(username);
							out.newLine();
							out.append(password);
							out.newLine();
							out.close();
							SampleInput.dispose();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						BufferedWriter out;
						try {
							out = new BufferedWriter(new FileWriter(username
									+ "\\train-" + username + ".txt", true));
							out.close();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						final ArrayList<Long> pressTime = new ArrayList<Long>();
						final ArrayList<Long> releaseTime = new ArrayList<Long>();
						final JFrame secondFrame = new JFrame();
						secondFrame.setLayout(new FlowLayout());
						secondFrame.setContentPane(new JLabel(new ImageIcon(
								"C:\\Users\\Devansh\\Desktop\\test5.jpg")));

						secondFrame.setSize(700, 700);

						final JPanel set = new JPanel();

						JLabel l = new JLabel("Please enter the data 10 times");
						l.setFont(new Font("Serif", Font.PLAIN, 20));
						JButton reset = new JButton("Reset");

						// set.setLayout(new BoxLayout(set, BoxLayout.Y_AXIS));
						final JButton aage = new JButton("OK ");

						aage.setEnabled(false);

						final JTextField dataSet = new JTextField(20);
						dataSet.setText(null);
						dataSet.setFocusable(true);

						set.setLayout(new GridLayout(6, 2, 10, 10));
						set.setLocation(200, 200);
						set.setSize(300, 250);
						set.setOpaque(false);

						set.add(l);
						set.add(dataSet);
						set.add(aage);
						set.add(reset);

						secondFrame.add(set);

						secondFrame.setVisible(true);

						reset.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								dataSet.setText(null);
								pressTime.clear();
								releaseTime.clear();

							}
						});

						dataSet.addKeyListener(new KeyListener() {

							@Override
							public void keyPressed(KeyEvent e) {
								// int a = e.getKeyCode();
								long presstime = e.getWhen();
								date = new Date(presstime);
								pressTime.add(date.getTime());

							}

							@Override
							public void keyReleased(KeyEvent r) {
								// int a = r.getKeyCode();
								long releasetime = r.getWhen();
								date2 = new Date(releasetime);
								releaseTime.add(date2.getTime());

								if (dataSet.getText().length() == PassWordTwice
										.getText().length()
										&& dataSet.getText().contains(
												PassWordTwice.getText()))
									aage.setEnabled(true);
								else
									aage.setEnabled(false);

								if (r.getKeyCode() == 8)
									JOptionPane
											.showMessageDialog(null,
													"Please don't use backspace, press the reset button.");

							}

							@Override
							public void keyTyped(KeyEvent arg0) {
								// TODO Auto-generated method stub

							}
						});

						aage.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent aa) {

								aage.setEnabled(false);
								dataSet.setFocusable(true);
								try {
									// check = true;
									System.out.println("This is username : "
											+ username);
									BufferedWriter out = new BufferedWriter(
											new FileWriter(username
													+ "\\train-" + username
													+ ".txt", true));

									for (int i = 0; i < pressTime.size(); ++i) {
										long duration = releaseTime.get(i)
												- pressTime.get(i);
										if (i == 0) {
											out.append("["
													+ String.valueOf(duration)
													+ ". " + 0 + ".]; ");
										} else {
											long latency = pressTime.get(i)
													- releaseTime.get(i - 1);
											out.append("["
													+ String.valueOf(duration)
													+ ". " + latency + ".]; ");
										}
									}
									pressTime.clear();
									releaseTime.clear();
									out.newLine();
									out.flush();
									out.close();
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								dataSet.setText(null);

								if (ctr != 1) {
									l.setText("Please enter the data " + ctr
											+ " more times ");
									ctr--;
								} else {
									l.setText("Please enter the data " + ctr
											+ " last time ");
									ctr--;
								}

								if (ctr == -1) {
									secondFrame.dispose();
									ctr = 10;
									Trainer train = new Trainer(username,
											password);
									System.out.println("trainer initialized");
									try {
										train.train();
									} catch (IOException | FileFormatException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						});

					}
				});
			}
		});

	}

	public static int getSize() throws IOException, FileFormatException {

		// reader = new FileReader ("trainingData.txt");
		reader = new FileReader(username + "\\train-" + username + ".txt");
		List<List<ObservationVector>> v = ObservationSequencesReader
				.readSequences(new ObservationVectorReader(2), reader);
		reader.close();

		return v.size();

	}
}