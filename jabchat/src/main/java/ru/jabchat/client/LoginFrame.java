package ru.jabchat.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private static final String ICONS_PATH 		 =  "resources/icons/";
	
	private JPanel contentPane;
	private JPanel titlePane;
	private JPanel loginPane;
	private JPanel settingPane;
	
	private JTextField usernameChooser;
	private JLabel     chooseUsernameLabel;

	private JButton changeColor;
	private JButton enterServer;
	
	private JPanel usernamePanel;
	
	public LoginFrame(){
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		
		titlePane = new JPanel();
		titlePane.setLayout(new BorderLayout());
		
		JLabel title = new JLabel();
		title.setIcon(new ImageIcon(ICONS_PATH + "title.gif"));
		
		titlePane.add(title,BorderLayout.CENTER);
		
		settingPane = new JPanel();
		settingPane.setLayout(new BorderLayout());
		
		usernamePanel = new JPanel();
		usernamePanel.setLayout(new FlowLayout());
		
		chooseUsernameLabel = new JLabel();
		chooseUsernameLabel.setIcon(new ImageIcon(ICONS_PATH + "choose.png"));
		usernameChooser = 	new JTextField(10);
		usernameChooser.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		usernameChooser.setForeground(Color.BLACK);
		usernameChooser.setBackground(new Color(180, 156, 99));
	/*	usernameChooser.addKeyListener(new KeyAdapter() {
        	  public void keyPressed(KeyEvent e) {
        		  int keyPressed = e.getKeyCode();
        		  boolean isEnter = keyPressed == 10;
        		  if (isEnter){
        			enterChat();
        		  }
        	    }
  		});*/
		
		changeColor = new JButton();
		changeColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		changeColor.setIcon(new ImageIcon(ICONS_PATH + "usercolor.png"));
		changeColor.setBackground(Color.BLACK);
	/*	changeColor.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	userColor = JColorChooser.showDialog(loginFrame, "Цвет вашего текста", userColor);
                settings.setMyColor(userColor);
            	if (userColor == null){
            		Random random = new Random();
            		userColor = new Color(random.nextInt(65535));//Color.BLUE;
            	}
              }
          });*/
		
		changeColor.setForeground(new Color(180, 156, 99));
		
		usernamePanel.add(chooseUsernameLabel);
		usernamePanel.add(usernameChooser);
		
	    enterServer = new JButton();
		enterServer.setIcon(new ImageIcon(ICONS_PATH + "login.png"));
		enterServer.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
		enterServer.setBackground(Color.BLACK);
		enterServer.setForeground(new Color(180, 156, 99));
		
		settingPane.add(usernamePanel, BorderLayout.CENTER);
		settingPane.add(changeColor, BorderLayout.SOUTH);
		
		JLabel probel = new JLabel();
		
		loginPane = new JPanel();
		loginPane.setLayout(new BorderLayout());
		loginPane.add(probel, BorderLayout.NORTH);
		loginPane.add(enterServer, BorderLayout.CENTER);
		
		contentPane.add(titlePane, BorderLayout.NORTH);
		contentPane.add(settingPane, BorderLayout.CENTER);
		contentPane.add(loginPane, BorderLayout.SOUTH);
		
		titlePane.setBackground(Color.BLACK);
		settingPane.setBackground(Color.BLACK);
		usernamePanel.setBackground(Color.BLACK);
		loginPane.setBackground(Color.BLACK);
		
		setBounds(600, 300, 1000, 400);
		setLayout(new BorderLayout());
		add(contentPane, BorderLayout.CENTER);
		setSize(240, 230);
		setResizable(false);
		setVisible(true);
	}
	
	
	public static void main(String [] args){
		
		LoginFrame login = new LoginFrame();
	}

}
