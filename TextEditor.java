import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
	TextEditor class creates a simple text editor application
	giving three different font types, two font styles, and also
	the ability 'Save', 'Save As', 'Open' or create a 'New' file
*/
public class TextEditor extends JFrame
{
	// Constants for set up of the note taking area
	public static final int WIDTH = 600;
	public static final int HEIGHT = 500;
	public static final int LINES = 20;
	public static final int CHAR_PER_LINE = 40;

	// Objects in GUI
	private JTextArea theText; // Text area to take notes
	private JPanel textPanel;  // Scrolling text area panel
	private JMenuBar mBar;     // Horizontal menu bar
	private JMenu fileMenu;   // Vertical menu for notes
	private JMenu textMenu;
	private JMenuItem newFile;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem exit;
	private JRadioButton mono;
	private JRadioButton serif;
	private JRadioButton sans;
	private JCheckBox italic;
	private JCheckBox bold;
	private JScrollPane scrollPane;
	private JFileChooser fileChooser;
	private ButtonGroup group;
	private String fileName;
   
   

	// THESE ITEMS ARE NOT YET USED
	// YOU WILL BE CREATING THEM IN THIS LAB
	private JMenu fontMenu; // Vertical menu for views
	private JScrollPane scrolledText;   // Scroll bars

	// Default notes
	private String note1 = "No Note 1.";
	private String note2 = "No Note 2.";

	/**
		Default constructor creates the swing window and loads
		the necessary components
	*/

	public TextEditor()
	{
		// Create a closeable JFrame with a specific size
		super("Text Editor");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		// Get contentPane and set layout of the window
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// Creates the vertical menus
		createFile();
		createText();

		// Creates horizontal menu bar and
		// adds vertical menus to it
		mBar = new JMenuBar();
		mBar.add(fileMenu);

		// ADD THE viewMenu TO THE MENU BAR HERE
		mBar.add(textMenu);
		setJMenuBar(mBar);

		// Creates a panel to take notes on
		textPanel = new JPanel();
		textPanel.setBackground(Color.blue);
		theText = new JTextArea(LINES, CHAR_PER_LINE);
		theText.setBackground(Color.white);
		theText.setFont(new Font("Monospaced", Font.PLAIN, 12));
		theText.setLineWrap(true);
		theText.setWrapStyleWord(true);

		// CREATE A JScrollPane OBJECT HERE CALLED
		// scrolledText AND PASS IN theText, THEN
		// CHANGE THE LINE BELOW BY PASSING IN scrolledText
		scrollPane = new JScrollPane(theText);

		// textPanel.add(scrolledText);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	}

    /**
		Method createFile Creates menu to hold 'File' options such as 'New',
		'Open', 'Save', 'Save As' and 'Exit'
	*/
	public void createFile()
	{
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newFile = new JMenuItem("New");
		newFile.setActionCommand("New");
		newFile.setMnemonic(KeyEvent.VK_N);
		newFile.addActionListener(new MenuListener());
		fileMenu.add(newFile);

		open = new JMenuItem("Open");
		open.setActionCommand("Open");
		open.setMnemonic(KeyEvent.VK_O);
		open.addActionListener(new MenuListener());
		fileMenu.add(open);

		save = new JMenuItem("Save");
		save.setActionCommand("Save");
		save.setMnemonic(KeyEvent.VK_S);
		save.addActionListener(new MenuListener());
		fileMenu.add(save);

		saveAs = new JMenuItem("Save As");
		saveAs.setActionCommand("Save As");
		saveAs.setMnemonic(KeyEvent.VK_A);
		saveAs.addActionListener(new MenuListener());
		fileMenu.add(saveAs);

		exit = new JMenuItem("Exit");
		exit.setActionCommand("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.addActionListener(new MenuListener());
		fileMenu.add(exit);
	}

	/**
		Method createText Creates menu to hold 'Font' options such as 'Monospaced',
		'Serif', 'Sans Serif', 'Italic' and 'Bold'
	*/
	public void createText()
	{
		textMenu = new JMenu("Font");
		textMenu.setMnemonic(KeyEvent.VK_T);

		mono = new JRadioButton("Monospaced");
		mono.setActionCommand("Monospaced");
		mono.addActionListener(new RadioListener());
		mono.setSelected(true);

		serif = new JRadioButton("Serif");
		serif.setMnemonic(KeyEvent.VK_M);
		serif.setActionCommand("Serif");
		serif.addActionListener(new RadioListener());

		sans = new JRadioButton("SansSerif");
		sans.setMnemonic(KeyEvent.VK_M);
		sans.setActionCommand("SansSerif");
		sans.addActionListener(new RadioListener());

		italic = new JCheckBox("Italic");
		italic.setActionCommand("Change");
		italic.addActionListener(new CheckListener());

		bold = new JCheckBox("Bold");		
		bold.setActionCommand("Change");
		bold.addActionListener(new CheckListener());

		textMenu.add(mono);
		textMenu.add(serif);
		textMenu.add(sans);
		textMenu.addSeparator();
		textMenu.add(italic);
		textMenu.add(bold);

		group = new ButtonGroup();
		group.add(mono);
		group.add(serif);
		group.add(sans);
	}

	/**
		Private inner class that handles the 'File' Menu object's
		action events.
	*/
	private class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();
			
			if (actionCommand.equals("New"))
			{
				fileName = "";
				theText.setText("");
			}
			else if (actionCommand.equals("Open"))
			{
				fileChooser = new JFileChooser();
				
				int result = fileChooser.showOpenDialog(TextEditor.this);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					fileName = fileChooser.getName(fileChooser.getSelectedFile());
					theText.setText("");
					
					try
					{
						FileReader fileReader = new FileReader(file);
						BufferedReader bufferedReader = new BufferedReader(fileReader);
						String line = null;
						while((line = bufferedReader.readLine()) != null)
						{
							theText.append(line + "\n");
						}
						
						bufferedReader.close();
					}
					catch(FileNotFoundException ex)
					{
						System.out.println(
							"Unable to open file '" + 
							fileName + "'");                
					}
					catch(IOException ex)
					{
						System.out.println(
							"Error reading file '" 
							+ fileName + "'");
					}
				}
			}
			else if (actionCommand.equals("Save"))
			{
				if(fileName != null)
				{
					File file = new File(fileName);
					FileWriter overWrite = null;
					
					try
					{
						String path = file.getAbsolutePath();
						if(path.contains(".txt"))
						{
							overWrite = new FileWriter(path, false);
							String text = theText.getText();
							System.out.println(path);
							overWrite.write(text);
							overWrite.close();
						}
						else
						{
							overWrite = new FileWriter(path + ".txt", false);
							overWrite.write(theText.getText());
							overWrite.close();
						}
					}
					catch(FileNotFoundException ex)
					{
						System.out.println(
							"Unable to open file '" + 
							fileName + "'");                
					}
					catch(IOException ex)
					{
						System.out.println(
							"Error saving file '" 
							+ fileName + "'");
					}
				}
				else
				{
					fileChooser = new JFileChooser();	
					int result = fileChooser.showSaveDialog(TextEditor.this);
					File file = fileChooser.getSelectedFile();
					BufferedWriter writer = null;
					if (result == JFileChooser.APPROVE_OPTION)
					{					
						fileName = fileChooser.getName(fileChooser.getSelectedFile());
						
						try
						{
							String path = file.getAbsolutePath();
							if(path.contains(".txt"))
							{
								writer = new BufferedWriter(new FileWriter(path));
								writer.write(theText.getText());
								writer.close();
							}
							else
							{
								writer = new BufferedWriter(new FileWriter(path + ".txt"));
								writer.write(theText.getText());
								writer.close();
							}
						}
						catch(FileNotFoundException ex)
						{
							System.out.println(
								"Unable to open file '" + 
								fileName + "'");                
						}
						catch(IOException ex)
						{
							System.out.println(
								"Error saving file '" 
								+ fileName + "'");
						}
					}
				}
			}
			else if (actionCommand.equals("Save As"))
			{
				fileChooser = new JFileChooser();
								
				int result = fileChooser.showSaveDialog(TextEditor.this);
				File file = fileChooser.getSelectedFile();
				BufferedWriter writer = null;
				if (result == JFileChooser.APPROVE_OPTION)
				{					
					fileName = fileChooser.getName(fileChooser.getSelectedFile());
					
					try
					{
						String path = file.getAbsolutePath();
						if(path.contains(".txt"))
						{
							writer = new BufferedWriter(new FileWriter(path));
							writer.write(theText.getText());
							writer.close();
						}
						else
						{
							writer = new BufferedWriter(new FileWriter(path + ".txt"));
							writer.write(theText.getText());
							writer.close();
						}
					}
					catch(FileNotFoundException ex)
					{
						System.out.println(
							"Unable to save file '" + 
							fileName + "'");                
					}
					catch(IOException ex)
					{
						System.out.println(
							"Error saving file '" 
							+ fileName + "'");
					}
				}
			}
			else if (actionCommand.equals("Exit"))
			{
				System.exit(0);
			}
			else
				theText.setText("Error in memo interface");
		}
   }
   
	/**
		Private inner class that handles the 'Font' Menu object's
		radio button action events.
	*/
	private class RadioListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();
			String font = "";
			
			if(actionCommand == "Monospaced")
			{
				font = "Monospaced";
			}
			else if(actionCommand == "Serif")
			{
				font = "Serif";
			}
			else if(actionCommand == "SansSerif")
			{
				font = "Sans Serif";
			}
			
			if(italic.isSelected() && bold.isSelected())
			{
				theText.setFont(new Font(font, Font.BOLD + Font.ITALIC, 12));
			}
			else if(italic.isSelected())
			{
				theText.setFont(new Font(font, Font.ITALIC, 12));
			}
			else if(bold.isSelected())
			{
				theText.setFont(new Font(font, Font.BOLD, 12));
			}
			else
			{
				theText.setFont(new Font(font, Font.PLAIN, 12));
			}
		}
	}
   
	/**
		Private inner class that handles the 'Font' Menu object's
		check box action events.
	*/
	private class CheckListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String actionCommand = e.getActionCommand();
			
			if(actionCommand == "Change")
			{
				String font = "";
				if(mono.isSelected())
				{
					font = "Monospaced";
				}
				else if(serif.isSelected())
				{
					font = "Serif";
				}
				else if(sans.isSelected())
				{
					font = "Sans Serif";
				}
				
				if(italic.isSelected() && bold.isSelected())
				{
					theText.setFont(new Font(font, Font.BOLD + Font.ITALIC, 12));
				}
				else if(italic.isSelected())
				{
					theText.setFont(new Font(font, Font.ITALIC, 12));
				}
				else if(bold.isSelected())
				{
					theText.setFont(new Font(font, Font.BOLD, 12));
				}
				else
				{
					theText.setFont(new Font(font, Font.PLAIN, 12));
				}
			}
		}
	}
	
	/**
		The main method creates an instance of the
		TextEditor class which causes it to display
		its window allowing the user to use it
	*/
	public static void main(String[] args)
	{
		TextEditor gui = new TextEditor();
		gui.setVisible(true);
	}
}


