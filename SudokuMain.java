import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/* Feldnummerierung:

cID   1  2  3    4  5  6    7  8  9    Block IDs
rID -------------------------------    -----------------------------  
 1 |  1  2  3 |  4  5  6 |  7  8  9 | |         |         |         |
 2 | 10 11 12 | 13 14 15 | 16 17 18 | |    1    |    2    |    3    |
 3 | 19 20 21 | 22 23 24 | 25 26 27 | |         |         |         |
   | ---------+----------+--------- | | --------+---------+-------- |
 4 | 28 29 30 | 31 32 33 | 34 35 36 | |         |         |         |
 5 | 37 38 39 | 40 41 42 | 43 44 45 | |    4    |    5    |    6    |
 6 | 46 47 48 | 49 50 51 | 52 53 54 | |         |         |         |
   | ---------+----------+--------- | | --------+---------+-------- |
 7 | 55 56 57 | 58 59 60 | 61 62 63 | |         |         |         |
 8 | 64 65 66 | 67 68 69 | 70 71 72 | |    7    |    8    |    9    |
 9 | 73 74 75 | 76 77 78 | 79 80 81 | |         |         |         |
    -------------------------------    ----------------------------- 

 */

public class SudokuMain {
	
	static String fileNameLoadWOExt;

	public static void main(String[] args) {

		// Neues Fenster
		JFrame frame = new JFrame("Initialize Sudoku");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

		JPanel mainPanel = new JPanel();
		mainPanel.setSize(new Dimension(512,612));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel,BoxLayout.X_AXIS));
		northPanel.setPreferredSize(new Dimension(512,60));
		northPanel.setBackground(Color.LIGHT_GRAY);

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridLayout(1,1));
		titlePanel.setBackground(Color.LIGHT_GRAY);

		JLabel titleLabel = new JLabel("Please fill in the initially given fields", SwingConstants.CENTER);
		titleLabel.setFont(new Font(Font.DIALOG,Font.BOLD,18));

		titlePanel.add(titleLabel);
		northPanel.add(titlePanel);
		mainPanel.add(northPanel);

		// Sudoku Panel, in 3x3 Blocks aufgeteilt
		JPanel sudokuPanel = new JPanel();
		sudokuPanel.setLayout(new GridLayout(3, 3));
		sudokuPanel.setPreferredSize(new Dimension(512,512));


		JPanel[] blocks = new JPanel[9];
		for (int i=1;i<=9;i++) {
			JPanel b = new JPanel();
			b.setBackground(Color.LIGHT_GRAY);
			b.setLayout(new GridLayout(3, 3));
			b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			blocks[i-1] = b;
		}

		JTextField[] fields = new JTextField[81];
		for (int i=1;i<=81;i++) {
			JTextField tf = new JTextField();
			tf.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
			tf.setHorizontalAlignment(JTextField.CENTER);
			tf.setDocument(new JTextFieldLimit(1));
			tf.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {tf.selectAll();}
				public void focusLost(FocusEvent e) {}
			});
			tf.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					if ((c<'1'||c>'9') && c!=KeyEvent.VK_BACK_SPACE && c!=KeyEvent.VK_ENTER && c!=KeyEvent.VK_DELETE) {
						e.consume();
						JOptionPane.showMessageDialog(tf, "<html><center>Input '" + c + "' not allowed!<br>Allowed inputs are<br>numbers from 1 to 9.</center></html>", "Illegal input", JOptionPane.ERROR_MESSAGE);
					} else if (c == KeyEvent.VK_ENTER) {
						e.consume();
					}
				}
			});
			fields[i-1] = tf;
		}

		JPanel[] fieldPanels = new JPanel[81];
		for (int i=1;i<=81;i++) {
			JPanel fP = new JPanel();
			fP.setLayout(new GridLayout(1,1));
			fP.add(fields[i-1]);
			fieldPanels[i-1] = fP;
		}

		insertFieldsIntoBlocks(blocks, fieldPanels);

		for (JPanel block : blocks) {
			sudokuPanel.add(block);
		}

		mainPanel.add(sudokuPanel);

		JTextArea logTextArea = new JTextArea();
		logTextArea.setEditable(false);
		logTextArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
		logTextArea.setLineWrap(false);
		logTextArea.setWrapStyleWord(false);

		JScrollPane logScrollPane = new JScrollPane (logTextArea);
		logScrollPane.setPreferredSize(new Dimension(400, 612));

		JPanel ioPanel = new JPanel();
		ioPanel.setLayout(new GridLayout(3,1));
		ioPanel.setSize(new Dimension(60,60));

		JButton loadButton = new JButton("Load");
		loadButton.setSize(new Dimension(60,20));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser loadSudokuFileChooser = new JFileChooser();
				loadSudokuFileChooser.setCurrentDirectory(new File(SudokuMain.class.getProtectionDomain().getCodeSource().getLocation().getFile()));
				loadSudokuFileChooser.setFileFilter(new FileNameExtensionFilter("Single-lined Sudoku (*.txt)", "txt"));
				loadSudokuFileChooser.getActionMap().get("viewTypeDetails").actionPerformed(null);
				loadSudokuFileChooser.setPreferredSize(new Dimension(640, 480));
				int loadSudokuFileChooserResult = loadSudokuFileChooser.showOpenDialog(frame);
				if (loadSudokuFileChooserResult == JFileChooser.APPROVE_OPTION) {
					clear(fields, logTextArea);
					String filePathLoad = loadSudokuFileChooser.getSelectedFile().getPath();
					int nameStart = Integer.MIN_VALUE;
					if 	(filePathLoad.lastIndexOf('/') != -1) nameStart = filePathLoad.lastIndexOf('/')+1;
					else if (filePathLoad.lastIndexOf('\\') != -1)  nameStart = filePathLoad.lastIndexOf('\\')+1;
					fileNameLoadWOExt = filePathLoad.substring(nameStart, filePathLoad.lastIndexOf('.'));
					BufferedReader br;
					readSudokuFile:
					try {
						br = new BufferedReader(new FileReader(filePathLoad));
						String vals = br.readLine();
						if (vals == null) {
							appendTextAndScrollDown(logTextArea, "Error reading \"" + fileNameLoadWOExt + "\":\nEmpty file.");
							JOptionPane.showMessageDialog(frame, "<html><center>" + "Error reading \"" + fileNameLoadWOExt + "\":<br>Empty file.</center></html>", "Empty file", JOptionPane.ERROR_MESSAGE);
							for (JTextField f : fields) {
								f.setText("");
							}
							break readSudokuFile;
						}
						if (vals.length() != 81) {
							appendTextAndScrollDown(logTextArea, "Error reading \"" + fileNameLoadWOExt + "\":\nInvalid number of entries (" + vals.length() + ").\nNumber of entries must be 81.");
							JOptionPane.showMessageDialog(frame, "<html><center>" + "Error reading \"" + fileNameLoadWOExt + "\":<br>Invalid number of entries (" + vals.length() + ").<br><br>Number of entries must be 81.</center></html>", "Invalid number of entries", JOptionPane.ERROR_MESSAGE);
							for (JTextField f : fields) {
								f.setText("");
							}
							break readSudokuFile;
						}
						for (int i=1; i<=81; i++) {
							try {
								fields[i-1].setText(String.valueOf(Integer.parseInt(String.valueOf(vals.charAt(i-1)))));
								if (vals.charAt(i-1)=='0') {
									appendTextAndScrollDown(logTextArea, "Error reading \"" + fileNameLoadWOExt + "\":\nInvalid value \"0\" at entry " + i + ".\nValue must be between 1 and 9.");
									JOptionPane.showMessageDialog(frame, "<html><center>" + "Error reading \"" + fileNameLoadWOExt + "\":<br>Invalid value \"0\" at entry " + i + ".<br><br>Vaules must be between 1 and 9.</center></html>", "Invalid value 0", JOptionPane.ERROR_MESSAGE);
									for (JTextField f : fields) {
										f.setText("");
									}
									break readSudokuFile;
								}
							} catch (NumberFormatException e) {
								if (vals.charAt(i-1) == '.') {
									fields[i-1].setText("");
								} else {
									appendTextAndScrollDown(logTextArea, "Error reading \"" + fileNameLoadWOExt + "\":\nInvalid character \"" + vals.charAt(i-1) + "\" at entry " + i + ".\n");
									JOptionPane.showMessageDialog(frame, "<html><center>" + "Error reading \"" + fileNameLoadWOExt + "\":<br>Invalid character \"" + vals.charAt(i-1) + "\" at entry " + i + ".<br><br>Valid characters are numbers from 1 to 9 or dot ('.')" + "</center></html>", "Invalid character", JOptionPane.ERROR_MESSAGE);
									for (JTextField f : fields) {
										f.setText("");
									}
									break readSudokuFile;
								}
							}
						}
						if (br.readLine() != null) {
							appendTextAndScrollDown(logTextArea, "Ignoring extra lines in file \"" + fileNameLoadWOExt + "\"");
						}
						br.close();
						SudokuMain.appendTextAndScrollDown(logTextArea, "Sudoku \"" + fileNameLoadWOExt + "\" sucessfully loaded\n");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		JButton validateButton = new JButton("Validate");
		validateButton.setSize(new Dimension(60,20));
		validateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Sudoku validation = initSudoku(fields, logTextArea);
				validation.validateInput(sudokuPanel);
			}
		});

		JButton saveButton = new JButton("Save");
		saveButton.setSize(new Dimension(60,20));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser saveSudokuFileChooser = new JFileChooser();
				saveSudokuFileChooser.setCurrentDirectory(new File(SudokuMain.class.getProtectionDomain().getCodeSource().getLocation().getFile()));
				saveSudokuFileChooser.setFileFilter(new FileNameExtensionFilter("Single-lined Sudoku (*.txt)", "txt"));
				saveSudokuFileChooser.getActionMap().get("viewTypeDetails").actionPerformed(null);
				saveSudokuFileChooser.setPreferredSize(new Dimension(640, 480));
				int saveSudokuFileChooserResult = saveSudokuFileChooser.showSaveDialog(frame);
				if (saveSudokuFileChooserResult == JFileChooser.APPROVE_OPTION) {
					String fileNameSave = saveSudokuFileChooser.getSelectedFile().getPath();
					if (!fileNameSave.endsWith(".txt")) {
						fileNameSave += ".txt";
					}
					FileWriter saveFileWriter = null;
					try {
						saveFileWriter = new FileWriter(fileNameSave);
						String vals = "";
						for (int i=1; i<=81; i++) {
							if (fields[i-1].getText().length() ==0) {
								vals += ".";
							}
							else {
								vals += fields[i-1].getText();
							}
						}
						saveFileWriter.write(vals);
					} catch (IOException e) {
						e.printStackTrace();
					}
					finally {
						try {
							saveFileWriter.flush();
							saveFileWriter.close();
						} catch (IOException io) {
							System.out.println("Error while flushing/closing fileWriter");
						}
					}
					appendTextAndScrollDown(logTextArea, "Saved to \"" + fileNameSave + "\"\n");
				}
			}
		});
		
		ioPanel.add(loadButton);
		ioPanel.add(validateButton);
		ioPanel.add(saveButton);

		northPanel.add(ioPanel);

		JButton candidatesButton = new JButton("Candidates");
		candidatesButton.setMargin(new Insets(0, 0, 0, 0));
		candidatesButton.addActionListener(e -> showCandidates(fields, logTextArea, sudokuPanel, mainPanel));

		JButton stebByStepButton = new JButton("Step By Step");
		stebByStepButton.setMargin(new Insets(0, 0, 0, 0));
		stebByStepButton.addActionListener(e -> stepByStep(fields, logTextArea, sudokuPanel, mainPanel));

		JButton runButton = new JButton("Solve");
		runButton.setMargin(new Insets(0, 0, 0, 0));
		runButton.setMnemonic(KeyEvent.VK_ENTER);
		runButton.addActionListener(e -> solveSudoku(fields, logTextArea, sudokuPanel, mainPanel));

		JButton clearButton = new JButton("Clear");
		clearButton.setMargin(new Insets(0, 0, 0, 0));
		clearButton.addActionListener(e -> clear(fields, logTextArea));

		JButton exitButton = new JButton("Exit");
		exitButton.setMargin(new Insets(0, 0, 0, 0));
		exitButton.addActionListener(e -> System.exit(0));

		JPanel southPanel = new JPanel();
		southPanel.setPreferredSize(new Dimension(512, 40));
		southPanel.setLayout(new GridLayout(1, 4));
		southPanel.setBackground(Color.LIGHT_GRAY);
		southPanel.add(candidatesButton);
		southPanel.add(stebByStepButton);
		southPanel.add(runButton);
		southPanel.add(clearButton);
		southPanel.add(exitButton);

		mainPanel.add(southPanel);

		frame.add(mainPanel);
		frame.add(logScrollPane);

		frame.setSize(new Dimension(862, 612));
		frame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(new Dimension(912, 612+frame.getInsets().top));

	}

	private static Sudoku initSudoku(JTextField[] fields, JTextArea ta) {
		
		int[] v = new int[81];

		for (int i=1;i<=81;i++) {
			try {
				v[i-1] = Integer.parseInt(fields[i-1].getText());
			} catch (NumberFormatException f) {
				v[i-1] = 0;
			}
		}

		ArrayList<Integer> initFields = new ArrayList<Integer>();
		ArrayList<Integer> initVals = new ArrayList<Integer>();
		for (int i=1;i<=81;i++) {
			if (v[i-1]>=1 && v[i-1]<=9) {
				initFields.add(i);
				initVals.add(v[i-1]);
			}
		}
		Sudoku sudoku = new Sudoku(initFields, initVals);

		sudoku.setLogTextArea(ta);

		return sudoku;
	}

	private static void showCandidates(JTextField[] fields, JTextArea logTextArea, JPanel sudokuPanel, Component parent) {

		Sudoku sudoku = initSudoku(fields, logTextArea);
		
		boolean isValid = sudoku.validateInput(sudokuPanel);
		
		if (!isValid) return;

		JFrame candidatesFrame = new JFrame();
		if (fileNameLoadWOExt != null) candidatesFrame.setTitle("Candidates: " + fileNameLoadWOExt);
		else candidatesFrame.setTitle("Candidates");
		candidatesFrame.pack();
		candidatesFrame.setSize(512, 512);
		candidatesFrame.setResizable(false);
		candidatesFrame.setVisible(true);
		candidatesFrame.setSize(new Dimension(512, 512+candidatesFrame.getInsets().top));
		candidatesFrame.setLocationRelativeTo(parent);

		JPanel candidatesPanel = new JPanel();
		candidatesPanel.setLayout(new GridLayout(3,3));
		candidatesPanel.setBackground(Color.LIGHT_GRAY);

		JPanel[] blocksCandidates = new JPanel[9];
		for (int i=1;i<=9;i++) {
			JPanel bC = new JPanel();
			bC.setLayout(new GridLayout(3,3));
			bC.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			blocksCandidates[i-1] = bC;
		}

		JPanel[] fieldsCandidates = new JPanel[81];
		for (int i=1;i<=81;i++) {
			if (sudoku.getFieldByFieldID(i).getVal() == 0) { // Kandidaten
				fieldsCandidates[i-1] = fillCandidates(sudoku.getFieldByFieldID(i), i);
			} else { // Zahlen
				JPanel fC = new JPanel();
				fC.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
				fC.setLayout(new GridLayout(1,1));
				fC.setBackground(Color.LIGHT_GRAY);
				JLabel vL = new JLabel(String.valueOf(sudoku.getFieldByFieldID(i).getVal()),SwingConstants.CENTER);
				vL.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
				vL.setForeground(Color.BLACK);
				fC.add(vL);
				fieldsCandidates[i-1] = fC;
			}

		}

		insertFieldsIntoBlocks(blocksCandidates, fieldsCandidates);

		for (JPanel block : blocksCandidates) {
			candidatesPanel.add(block);
		}

		candidatesFrame.add(candidatesPanel);

	}

	private static void stepByStep(JTextField[] fields, JTextArea logTextArea, JPanel sudokuPanel, Component parent) {
		
//		long t0 = System.nanoTime();

		Sudoku sudoku = initSudoku(fields, logTextArea);
		
		boolean isValid = sudoku.validateInput(sudokuPanel);
		
		if (!isValid) return;

		ArrayList<Field[][]> stepsArray = sudoku.solve(fileNameLoadWOExt);

		JFrame stepsFrame = new JFrame();
		if (fileNameLoadWOExt != null) stepsFrame.setTitle("Step-by-step solution: " + fileNameLoadWOExt);
		else stepsFrame.setTitle("Step-by-step solution");
		stepsFrame.pack();
		Container stepsContainer = stepsFrame.getContentPane();

		JPanel cardPanel = new JPanel();
		CardLayout cl = new CardLayout();
		cardPanel.setLayout(cl);

		for (Field[][] s : stepsArray) {

			JPanel numberPanel = new JPanel();
			numberPanel.setLayout(new GridLayout(1,1));
			numberPanel.setBackground(Color.LIGHT_GRAY);
			numberPanel.setPreferredSize(new Dimension(512,30));
			JLabel numberLabel = new JLabel("", SwingConstants.CENTER);
			numberLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
			numberLabel.setText("Run " + sudoku.getFoundInRunNo().get(stepsArray.indexOf(s)) + "/" + sudoku.getFoundInRunNo().get(sudoku.getFoundInRunNo().size()-1) + ", Step " + stepsArray.indexOf(s) + "/" + (stepsArray.size()-1) + ": " + sudoku.getFoundByType().get(stepsArray.indexOf(s)));
			numberPanel.add(numberLabel);

			JPanel stepPanel = new JPanel();
			stepPanel.setLayout(new BorderLayout());
			stepPanel.setBackground(Color.LIGHT_GRAY);

			JPanel fieldsPanel = new JPanel();
			fieldsPanel.setLayout(new GridLayout(3,3));
			fieldsPanel.setBackground(Color.LIGHT_GRAY);

			Field[] fieldsArray = Sudoku.toArray1D(s);

			JPanel[] blocksSteps = new JPanel[9];
			for (int i=1;i<=9;i++) {
				JPanel bS = new JPanel();
				bS.setLayout(new GridLayout(3,3));
				bS.setBackground(Color.LIGHT_GRAY);
				bS.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
				blocksSteps[i-1] = bS;
			}

			JPanel[] fieldsSteps = new JPanel[81];
			for (int i=1;i<=81;i++) {
				if (fieldsArray[i-1].getVal() == 0) {
					fieldsSteps[i-1] = fillCandidates(fieldsArray[i-1], i);
				} else {
					JPanel fSt = new JPanel();
					fSt.setLayout(new GridLayout(1,1));
					fSt.setBackground(Color.LIGHT_GRAY);
					fSt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
					JLabel lS = new JLabel("", SwingConstants.CENTER);
					lS.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
					lS.setText(String.valueOf(fieldsArray[i-1].getVal()));
					lS.setForeground(new Color(10,150,30));
					int v;
					try {
						v = Integer.parseInt(fields[i-1].getText());
					} catch (NumberFormatException f) {
						v = 0;
					}
					if (v != 0) {
						lS.setForeground(Color.BLACK);
					}
					fSt.add(lS);
					fieldsSteps[i-1] = fSt;
				}
			}

			insertFieldsIntoBlocks(blocksSteps, fieldsSteps);

			for (JPanel block : blocksSteps) {
				fieldsPanel.add(block);
			}

			stepPanel.add(numberPanel, BorderLayout.NORTH);
			stepPanel.add(fieldsPanel, BorderLayout.CENTER);
			cardPanel.add(stepPanel);

		}

		JPanel navPanel = new JPanel (new GridLayout(1,4));

		JButton firstButton = new JButton("\u23EE");
		initNavButton(firstButton);
		firstButton.addActionListener(e -> cl.first(cardPanel));
		JButton backButton = new JButton("\u23F4");
		initNavButton(backButton);
		backButton.addActionListener(e -> cl.previous(cardPanel));
		backButton.setMnemonic(KeyEvent.VK_LEFT);
		JButton nextButton = new JButton("\u23F5");
		initNavButton(nextButton);
		nextButton.addActionListener(e -> cl.next(cardPanel));
		nextButton.setMnemonic(KeyEvent.VK_RIGHT);
		JButton lastButton = new JButton("\u23ED");
		initNavButton(lastButton);
		lastButton.addActionListener(e -> cl.last(cardPanel));

		navPanel.add(firstButton);
		navPanel.add(backButton);
		navPanel.add(nextButton);
		navPanel.add(lastButton);
		stepsContainer.add(navPanel, BorderLayout.SOUTH);
		stepsContainer.add(cardPanel);

		stepsFrame.setSize(512, 572);
		stepsFrame.setResizable(false);
		stepsFrame.setVisible(true);
		stepsFrame.setSize(new Dimension(512, 572+stepsFrame.getInsets().top));
		stepsFrame.setLocationRelativeTo(parent);
		
//		long t1 = System.nanoTime();
//		appendTextAndScrollDown(logTextArea, "Elapsed time (Entire solving process): " + Math.round((t1 - t0) * Math.pow(10, -6)*10.0)/10.0 + " ms");
		

	}

	private static void initNavButton(JButton nB) {
		nB.setPreferredSize(new Dimension(128,30));
		nB.setMargin(new Insets(0, 0, 0, 0));
		nB.setFont(new Font(Font.DIALOG,Font.BOLD,24));
	}

	private static void solveSudoku(JTextField[] fields, JTextArea logTextArea, JPanel sudokuPanel, Component parent) {
		
//		long t0 = System.nanoTime();

		Sudoku sudoku = initSudoku(fields, logTextArea);

		boolean isValid = sudoku.validateInput(sudokuPanel);
		
		if (!isValid) return;

		sudoku.solve(fileNameLoadWOExt);

		JFrame solvedFrame = new JFrame();
		if (fileNameLoadWOExt != null) solvedFrame.setTitle("Sudoku solution: " + fileNameLoadWOExt);
		else solvedFrame.setTitle("Sudoku solution");
		if (sudoku.getFreeFields().size()>0) {
			if (fileNameLoadWOExt != null) solvedFrame.setTitle("Abort status: " + fileNameLoadWOExt);
			else solvedFrame.setTitle("Abort status");
		}
		solvedFrame.pack();
		solvedFrame.setSize(512, 512);
		solvedFrame.setResizable(false);
		Dimension dimSolved = Toolkit.getDefaultToolkit().getScreenSize();
		solvedFrame.setLocation(dimSolved.width/2-solvedFrame.getSize().width/2, dimSolved.height/2-solvedFrame.getSize().height/2);
		solvedFrame.setVisible(true);
		solvedFrame.setSize(new Dimension(512, 512+solvedFrame.getInsets().top));
		solvedFrame.setLocationRelativeTo(parent);

		JPanel solvedPanel = new JPanel();
		solvedPanel.setLayout(new GridLayout(3,3));
		solvedPanel.setBackground(Color.LIGHT_GRAY);

		JPanel[] blocksSolved = new JPanel[9];
		for (int i=1;i<=9;i++) {
			JPanel bS = new JPanel();
			bS.setLayout(new GridLayout(3,3));
			bS.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			blocksSolved[i-1] = bS;
		}

		JPanel[] fieldsSolved = new JPanel[81];
		for (int i=1;i<=81;i++) {
			if (sudoku.getFieldByFieldID(i).getVal() == 0) {
				fieldsSolved[i-1] = fillCandidates(sudoku.getFieldByFieldID(i), i);
			} else {
				JPanel fS = new JPanel();
				fS.setLayout(new GridLayout(1,1));
				fS.setBackground(Color.LIGHT_GRAY);
				fS.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
				JLabel lS = new JLabel("", SwingConstants.CENTER);
				lS.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
				lS.setText(String.valueOf(sudoku.getFieldByFieldID(i).getVal()));
				lS.setForeground(new Color(10,150,30));
				int v;
				try {
					v = Integer.parseInt(fields[i-1].getText());
				} catch (NumberFormatException f) {
					v = 0;
				}
				if (v != 0) {
					lS.setForeground(Color.BLACK);
				}
				fS.add(lS);
				fieldsSolved[i-1] = fS;
			}
		}

		insertFieldsIntoBlocks(blocksSolved, fieldsSolved);

		for (JPanel block : blocksSolved) {
			solvedPanel.add(block);
		}

		solvedFrame.add(solvedPanel);

//		long t1 = System.nanoTime();
//		appendTextAndScrollDown(logTextArea, "Elapsed time (Entire solving process): " + Math.round((t1 - t0) * Math.pow(10, -6)*10.0)/10.0 + " ms");
		
	}

	private static void insertFieldsIntoBlocks(JPanel[] blocks, JPanel[] fields) {
		blocks[0].add(fields[ 0]);
		blocks[0].add(fields[ 1]);
		blocks[0].add(fields[ 2]);
		blocks[0].add(fields[ 9]);
		blocks[0].add(fields[10]);
		blocks[0].add(fields[11]);
		blocks[0].add(fields[18]);
		blocks[0].add(fields[19]);
		blocks[0].add(fields[20]);
		blocks[1].add(fields[ 3]);
		blocks[1].add(fields[ 4]);
		blocks[1].add(fields[ 5]);
		blocks[1].add(fields[12]);
		blocks[1].add(fields[13]);
		blocks[1].add(fields[14]);
		blocks[1].add(fields[21]);
		blocks[1].add(fields[22]);
		blocks[1].add(fields[23]);
		blocks[2].add(fields[ 6]);
		blocks[2].add(fields[ 7]);
		blocks[2].add(fields[ 8]);
		blocks[2].add(fields[15]);
		blocks[2].add(fields[16]);
		blocks[2].add(fields[17]);
		blocks[2].add(fields[24]);
		blocks[2].add(fields[25]);
		blocks[2].add(fields[26]);
		blocks[3].add(fields[27]);
		blocks[3].add(fields[28]);
		blocks[3].add(fields[29]);
		blocks[3].add(fields[36]);
		blocks[3].add(fields[37]);
		blocks[3].add(fields[38]);
		blocks[3].add(fields[45]);
		blocks[3].add(fields[46]);
		blocks[3].add(fields[47]);
		blocks[4].add(fields[30]);
		blocks[4].add(fields[31]);
		blocks[4].add(fields[32]);
		blocks[4].add(fields[39]);
		blocks[4].add(fields[40]);
		blocks[4].add(fields[41]);
		blocks[4].add(fields[48]);
		blocks[4].add(fields[49]);
		blocks[4].add(fields[50]);
		blocks[5].add(fields[33]);
		blocks[5].add(fields[34]);
		blocks[5].add(fields[35]);
		blocks[5].add(fields[42]);
		blocks[5].add(fields[43]);
		blocks[5].add(fields[44]);
		blocks[5].add(fields[51]);
		blocks[5].add(fields[52]);
		blocks[5].add(fields[53]);
		blocks[6].add(fields[54]);
		blocks[6].add(fields[55]);
		blocks[6].add(fields[56]);
		blocks[6].add(fields[63]);
		blocks[6].add(fields[64]);
		blocks[6].add(fields[65]);
		blocks[6].add(fields[72]);
		blocks[6].add(fields[73]);
		blocks[6].add(fields[74]);
		blocks[7].add(fields[57]);
		blocks[7].add(fields[58]);
		blocks[7].add(fields[59]);
		blocks[7].add(fields[66]);
		blocks[7].add(fields[67]);
		blocks[7].add(fields[68]);
		blocks[7].add(fields[75]);
		blocks[7].add(fields[76]);
		blocks[7].add(fields[77]);
		blocks[8].add(fields[60]);
		blocks[8].add(fields[61]);
		blocks[8].add(fields[62]);
		blocks[8].add(fields[69]);
		blocks[8].add(fields[70]);
		blocks[8].add(fields[71]);
		blocks[8].add(fields[78]);
		blocks[8].add(fields[79]);
		blocks[8].add(fields[80]);
	}

	private static JPanel fillCandidates(Field f, int i) {
		JPanel fS = new JPanel();
		fS.setBackground(Color.LIGHT_GRAY);
		fS.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		fS.setLayout(new GridLayout(3,3));
		ArrayList<Integer> candidates = f.getCandidates();
		for (int c=1;c<=9;c++) {
			if (candidates.contains(Integer.valueOf(c))) {
				JLabel lC = new JLabel(String.valueOf(c), SwingConstants.CENTER);
				lC.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
				lC.setForeground(new Color(20,80,255));
				fS.add(lC);
			}
			else fS.add(new JLabel("", SwingConstants.CENTER));
		}
		return fS;
	}

	private static void clear(JTextField textFields[], JTextArea ta) {
		for (JTextField tf : textFields) {
			tf.setText(null);
		}
		ta.setText(null);
	}

	// Append log text to log text area and scroll down
	public static void appendTextAndScrollDown(JTextArea ta, String str) {
		ta.append(str + "\n");
		ta.setCaretPosition(ta.getText().length());
	}

}
