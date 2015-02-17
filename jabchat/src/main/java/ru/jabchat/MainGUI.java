package ru.jabchat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

public class MainGUI {
	
	private static final String ipVasya = "10.38.190.228";
	private static final String ipFedya = "";
	
	private static final String nameVasya = "SymbiOS";
	private static final String nameFedya = "Fedor";
	
	private StringCrypter crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});
	private ChatDao dao = new ChatDao();

	private Timer reloadTimer    = new Timer(1000, new ReloadChatBox());
	
	private Timestamp startSession;
	private Integer startLoginRow;
	private Integer currentCntRow;
	  
    String      appName     = "Vasya&Fedya Production";
    MainGUI     mainGUI;
    JFrame      newFrame    = new JFrame(appName);
    JButton     sendMessage;
    JTextField  messageBox;
    JTextArea   chatBox;
    JTextField  usernameChooser;
    JFrame      preFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
              
                MainGUI mainGUI = new MainGUI();
                mainGUI.preDisplay();
            }
        });
    }
    
    
    private void getUserName(){
    	
    	InetAddress ip;
    	usernameChooser = new JTextField(15);
    	
			try {
				
				ip = InetAddress.getLocalHost();
				
				System.out.println(ip.toString());
				
				if (ip.getHostAddress().toString().equals("10.38.190.228")){
					username = nameVasya;
				}  else if (ip.getHostAddress().toString().equals("")) {
					username = nameFedya;
				}else{
					System.out.println("АНАЛИТИКА СЛАМАЛАСЬ ((((");
				}
				
			
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		
    }
    
    public void preDisplay() {
       
    	newFrame.setVisible(false);
        preFrame = new JFrame(appName);
        
        getUserName();
        
        JButton enterServer = new JButton("Login in server");
        enterServer.addActionListener(new enterServerButtonListener());
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);

        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        
        preFrame.setSize(300, 300);
        preFrame.setVisible(true);

    }

    public void display() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();
        messageBox.addKeyListener(new KeyAdapter() {
      	  public void keyPressed(KeyEvent e) {
      		  int keyPressed = e.getKeyCode();
      		  boolean isEnter = keyPressed == 10;
      		  if (isEnter){
      			  sendMessage();
      		  }
      	    }
		});

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
        newFrame.setVisible(true);
    }

    
    private void sendMessage(){

        if (messageBox.getText().length() < 1) {
        } else if (messageBox.getText().equals(".clear")) {
            chatBox.setText("Cleared all messages\n");
            messageBox.setText("");
        } else {
            chatBox.append("<" + username + ">:  " + messageBox.getText()  + "\n");
            
            ChatModel chatModel = new ChatModel(crypter.encrypt(username), crypter.encrypt(messageBox.getText()), new Timestamp(new Date().getTime()));
            dao.insertMessage(chatModel);
            
            messageBox.setText("");
        }
        messageBox.requestFocusInWindow();
    	
    }
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        	sendMessage();
        }
    }
    
    
    class ReloadChatBox implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
		
			currentCntRow = dao.getLastRow();
			boolean needReload = currentCntRow > startLoginRow;
			if (needReload){
				chatBox.setText("");
				
				List<ChatModel> messages = dao.getListMessages(startLoginRow);
				for (ChatModel chatModel : messages) {
					chatBox.append("<" + chatModel.getUserName() + ">:  " + chatModel.getMessage()  + "\n");
				}
				
			}
			
		}
    	
    }
    
    String  username;
    
    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (username.length() < 1) {
                System.out.println("No!");
            } else {
            	startSession = new Timestamp(new Date().getTime());
            	startLoginRow = dao.getLastRow();
                preFrame.setVisible(false);
                reloadTimer.start();
                display();
                
            }
        }

    }
}