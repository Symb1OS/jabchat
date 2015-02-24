package ru.jabchat.client;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import ru.jabchat.server.dao.ChatDao;
import ru.jabchat.server.dao.UserDao;
import ru.jabchat.server.model.ChatModel;
import ru.jabchat.server.model.UserModel;
import ru.jabchat.utils.Notification;
import ru.jabchat.utils.ObjectRecorder;
import ru.jabchat.utils.Settings;
import ru.jabchat.utils.StringCrypter;

public class Chat {

	public static final String APPLICATION_NAME =  "Vasya&Fedya Production";
	public static final String PREVIEW_ICON 	=  "resources/icons/icon.png";
	public static final String APPLICATION_ICON =  "resources/icons/chat.png";
	
	private StringCrypter crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});
	
	private ChatDao chatDao = new ChatDao();
	private UserDao usersDao = new UserDao();
	
	private UserModel  user;
	private UserWindow userWin = new UserWindow();

	private Timer reloadTimer    = new Timer(1000, new ReloadChatBox());
	
	private static boolean trayActive;
	private static SystemTray generalTray;
	private static TrayIcon generalTrayIcon;
	
	private Integer rowCount;
	private Integer startLoginRow;
	private Integer incMessage;
	private Integer currentCntRow;
	  
	private static JFrame chatFrame    = new JFrame(APPLICATION_NAME);
	private JFrame loginFrame;
	private JButton sendMessage;
	
	private Settings settings = new Settings(); 
	private JButton changeColor;
	private Color userColor = Color.BLACK;
	
	private JTextField  messageBox;
	private JTextPane chatBox;
	private JTextField usernameChooser = new JTextField(15);

	private StyleContext sc;
	private Style system;
	
	public DefaultStyledDocument doc;
	public JTextPane textPane;
	public JScrollPane scrollPane;
	
    
    private Style getStyle2(int color){
    	if(color == 0)
    		return  system;
    	else{
	        Style style = sc.addStyle("ConstantWidth", null);
	        StyleConstants.setFontFamily(style, "Serif");
	        StyleConstants.setFontSize(style, 15);
	        StyleConstants.setForeground(style, new Color(color));
	        return style;
    	}
    }
    
    
    
    
    
    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
              
                Chat chat = new Chat();
                chat.preDisplay();
            }
        });
    }
    
    public void preDisplay() {
       
    	chatFrame.setVisible(false);
        loginFrame = new JFrame(APPLICATION_NAME);
        
        Image im = Toolkit.getDefaultToolkit().getImage(APPLICATION_ICON);
       
        loginFrame.setIconImage(im);
        chatFrame.setIconImage(im);
        
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        JButton enterServer = new JButton("Login in server");
        changeColor = new JButton("Choose user color");
        changeColor.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	userColor = JColorChooser.showDialog(loginFrame, "Цвет вашего текста", userColor);
            	System.out.println(  userColor.getRGB()  );
                settings.setMyColor(userColor);
            	if (userColor == null){
            		userColor = Color.BLUE;
            	}
              }
          });

        
        changeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				userColor = JColorChooser.showDialog(loginFrame, "Choose a color", userColor);
//				if (userColor == null){
//					userColor = Color.BLUE;
	//			}

			}
		});
        
        enterServer.addActionListener(new enterServerButtonListener());
        
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 0, 0, 10);
        
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;
        
        usernameChooser.addKeyListener(new KeyAdapter() {
        	  public void keyPressed(KeyEvent e) {
        		  int keyPressed = e.getKeyCode();
        		  boolean isEnter = keyPressed == 10;
        		  if (isEnter){
        			enterChat();
        		  }
        	    }
  		});
        
        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        
        loginFrame.add(BorderLayout.NORTH, changeColor);
        loginFrame.add(BorderLayout.CENTER, prePanel);
        loginFrame.add(BorderLayout.SOUTH, enterServer);
        
        loginFrame.setSize(300, 300);
        loginFrame.setVisible(true);

    }

    public void display() {
    	
    	rowCount = currentCntRow = chatDao.getLastRow();
      
    	this.sc = new StyleContext();
        this.system = sc.addStyle("ConstantWidth", null);
        StyleConstants.setFontFamily(system, "Serif");
        StyleConstants.setFontSize(system, 15);
        StyleConstants.setForeground(system, new Color(0,112,15));
        
        
        // -- Create elements --
        this.doc = new DefaultStyledDocument(sc);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        //southPanel.setBackground(Color.CYAN);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(33);
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

        this.textPane = new JTextPane(doc);
        this.textPane.setBackground(Color.WHITE);
        this.scrollPane = new JScrollPane(textPane);   
        
        chatBox = new JTextPane(doc);
        
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));

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

        chatFrame.addWindowListener(new WindowAdapter() {        	 
        	 public void windowActivated(WindowEvent event) {
        		 if (trayActive){
        			 generalTray.remove(generalTrayIcon);
        			 trayActive = false;
        		 }
             }


			public void windowClosing(WindowEvent event) {

				ChatModel chatModel = new ChatModel(crypter.encrypt("Система"), crypter.encrypt("Пользователь " + user.getUserName() + " покинул беседу."), new Timestamp(new Date().getTime()));
				chatDao.insertMessage(chatModel);
				
				UserModel us = new UserModel(user.getId(), crypter.encrypt(user.getIp()), crypter.encrypt(user.getUserName()), crypter.encrypt(user.getStatus()));
				usersDao.disconnect(us);
				System.exit(0);
			}
			
		});
        
        
        JPanel allContent = new JPanel();
        allContent.setLayout(new BoxLayout(allContent, BoxLayout.LINE_AXIS));
        allContent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        allContent.add(Box.createHorizontalGlue());
        allContent.add(mainPanel);
        
        allContent.add(Box.createRigidArea(new Dimension(5, 5)));
        allContent.add(userWin.getWindow());
        
        
        chatFrame.add(allContent);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setSize(470, 300);
        chatFrame.setVisible(true);
        
    }
    
    private boolean offUsersExist(){
    	return usersDao.offExists();
    }
    
    private void sendMessage(){

        if (messageBox.getText().length() < 1) {
        } else if (messageBox.getText().equals(".clear")) {
            chatBox.setText("Cleared all messages\n");
            messageBox.setText("");
        } else {
         //   chatBox.append("<" + user.getUserName() + ">:  " + messageBox.getText()  + "\n");            
            ChatModel chatModel = new ChatModel(crypter.encrypt(user.getUserName()), crypter.encrypt(messageBox.getText()), new Timestamp(new Date().getTime()));
            chatDao.insertMessage(chatModel);
            
            if (offUsersExist()){
            	Notification notification = new Notification(user.getUserName(), messageBox.getText());
            	notification.sendMail();
            }
            
            messageBox.setText("");
            incMessage++;
            
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
			
			userWin.refreshTable(usersDao.getUsers());
			currentCntRow = chatDao.getLastRow();
			
			boolean needUpdate = currentCntRow - rowCount > 0;
			if (needUpdate) {
				
				for (int i = currentCntRow - rowCount - 1; i >= 0; i--) {
					
					ChatModel chatModel = chatDao.getMessage(i);
					String date = new SimpleDateFormat("[HH:mm]").format(chatModel.getSendTime()).toString();
					String text = chatModel.getMessage();
					Style color = getStyle2( chatModel.getColor());
					
					try {
						doc.insertString(doc.getLength(), date +" - " + text + "\n", color );
					} catch (BadLocationException badLocationException) {
						badLocationException.printStackTrace();
					}

				}
			}

			boolean needViewTray = (incMessage < currentCntRow);
			if (needViewTray) {
				incMessage++;
				Tray.viewTrayIcon();

			}

			rowCount = currentCntRow;
		}

    }
    
    public void enterChat(){
    	
    	if(settings.isNull()){
            settings = ObjectRecorder.read();
    	}else{
            ObjectRecorder.write(settings);
    	}
      
    	userColor = settings.getMyColor();
    	
    	String ip 	 	= null;
    	String userName = null;
    	String color 	= String.valueOf(userColor.getRGB());
    	
    	try{
    		ip 		 = InetAddress.getLocalHost().getHostAddress();
    		userName = usernameChooser.getText();
    		user = usersDao.login(crypter.encrypt(ip), crypter.encrypt(userName), color );
		}catch(UnknownHostException e){
			e.printStackTrace();
		}
    	
    	startLoginRow = chatDao.getLastRow();
    	incMessage = startLoginRow;
        loginFrame.setVisible(false);
        reloadTimer.start();
        display();
       
        ChatModel chatModel = new ChatModel(crypter.encrypt("Система"), crypter.encrypt("Пользователь " + userName + " присоединяется к беседе."), new Timestamp(new Date().getTime()));
		chatDao.insertMessage(chatModel);
		
        messageBox.requestFocusInWindow();
        
    }
    
    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        	enterChat();
        }
    }
    
    static class Tray{
    	
		private static void viewTrayIcon() {
    		    final TrayIcon trayIcon;
    			if (SystemTray.isSupported()) {
    				final SystemTray tray = SystemTray.getSystemTray();
    				Image image = Toolkit.getDefaultToolkit().getImage(PREVIEW_ICON);
    				
    				ActionListener exitListener = new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						System.out.println("Exiting...");
    						System.exit(0);
    					}
    				};
    				
    				PopupMenu popup = new PopupMenu();
    				MenuItem defaultItem = new MenuItem("Exit");
    				defaultItem.addActionListener(exitListener);
    				popup.add(defaultItem);
    				trayIcon = new TrayIcon(image, "Tray Demo", popup);
    				ActionListener actionListener = new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						trayIcon.displayMessage("Action Event",
    								"An Action Event Has Been Performed!",
    								TrayIcon.MessageType.INFO);
    					}
    				};
    				
    				MouseListener mouseListener = new MouseListener() {
    					
    					@Override
    					public void mouseReleased(MouseEvent e) {
    						
    					}
    					
    					@Override
    					public void mousePressed(MouseEvent e) {
    						tray.remove(trayIcon);
    						trayActive = false;
    					}
    					
    					@Override
    					public void mouseExited(MouseEvent e) {
    						
    					}
    					
    					@Override
    					public void mouseEntered(MouseEvent e) {
    						
    					}
    					
    					@Override
    					public void mouseClicked(MouseEvent e) {
    						
    					}
    				};
    					
    				trayIcon.setImageAutoSize(true);
    				trayIcon.addActionListener(actionListener);
    				trayIcon.addMouseListener(mouseListener);
    				try {
    					if (!trayActive && !chatFrame.isActive()){
    						tray.add(trayIcon);
    						generalTray = tray;
    						generalTrayIcon = trayIcon;
        					trayIcon.displayMessage("Новое сообщение!", "", TrayIcon.MessageType.INFO);
        					trayActive = true;
        					
    					}
    				} catch (AWTException e) {
    					System.err.println("TrayIcon could not be added.");
    				}
    				
    			} else {
    				System.out.println("АНАЛИТИКА СЛАМАЛАСЬ((((");
    			}
    	      }
		
    	}
    }