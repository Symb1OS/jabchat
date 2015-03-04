package ru.jabchat.client;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.commons.io.IOUtils;
import org.springframework.dao.EmptyResultDataAccessException;

import ru.jabchat.server.dao.ChatDao;
import ru.jabchat.server.dao.UserDao;
import ru.jabchat.server.model.ChatModel;
import ru.jabchat.server.model.UserModel;
import ru.jabchat.utils.Config;
import ru.jabchat.utils.Converter;
import ru.jabchat.utils.EditorDocument;
import ru.jabchat.utils.MiddleUrl;
import ru.jabchat.utils.ObjectRecorder;
import ru.jabchat.utils.Settings;
import ru.jabchat.utils.StringCrypter;
import ru.jabchat.utils.UrlPoint;

public class Chat {

	public static final String APPLICATION_NAME  =  "JabChat 0.1";
	
	private static final String ICONS_PATH 		 =  "resources/icons/";
	private static final String SMILES_PATH      =  "smiles/";
	private static final String PREVIEW_ICON 	 =   ICONS_PATH + "icon.png";
	private static final String APPLICATION_ICON =   ICONS_PATH + "chat.png";
	
	private final static String LINK_ATTRIBUTE   =  "linkact";
	
	private static final Set<String> PICTURE_FORMAT = new HashSet<String>();
	static{
		PICTURE_FORMAT.add(".png");
		PICTURE_FORMAT.add(".jpg");
		PICTURE_FORMAT.add(".jpeg");
		PICTURE_FORMAT.add(".gif");
	}
	
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
	
	private static JTextArea  messageBox;
	private JTextPane chatBox;
	private JTextPane textPane;
	
	private EditorDocument doc;
	
	private JPanel contentPane;
	private JPanel titlePane;
	private JPanel loginPane;
	private JPanel settingPane;
	private JPanel usernamePanel;
	
	private JScrollPane chatScrollPane;
	
	private JButton sendMessage;
	private JButton smilesButton;
	private JButton changeColor;
	private JButton enterServer;
	
	private JTextField usernameChooser;
	
	private JLabel     chooseUsernameLabel;
	
	private StyleContext sc;
	private Style system;
	private Style regularBlue;
	
	private Color userColor;
	
	private Settings settings = new Settings(); 
	
	private Style getUserStyle(int color){
    	if(color == 0)
    		return  system;
    	else{
	        Style userStyle = sc.addStyle("ConstantWidth", null);
	        StyleConstants.setFontFamily(userStyle, "Serif");
	        StyleConstants.setFontSize(userStyle, 15);
	        StyleConstants.setForeground(userStyle, new Color(color));
	        return userStyle;
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
       
    	loginFrame = new JFrame(APPLICATION_NAME);
    
    	Image im = Toolkit.getDefaultToolkit().getImage(APPLICATION_ICON);
    	loginFrame.setIconImage(im);
    	
    	loginFrame.setBounds(600, 300, 1000, 400);
		loginFrame.setLayout(new BorderLayout());
		loginFrame.setSize(240, 230);
		loginFrame.setResizable(false);
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	contentPane = new JPanel();
    	contentPane.setBackground(Color.BLACK);
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
		
		usernameChooser = 	new JTextField(12);
		usernameChooser.setHorizontalAlignment(JTextField.CENTER);
		usernameChooser.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		usernameChooser.setForeground(Color.BLACK);
		usernameChooser.setBackground(new Color(180, 156, 99));
		usernameChooser.addKeyListener(new KeyAdapter() {
        	  public void keyPressed(KeyEvent e) {
        		  int keyPressed = e.getKeyCode();
        		  boolean isEnter = (keyPressed == 10);
        		  if (isEnter){
        			enterChat();
        		  }
        	    }
  		});
			
		try {
			
			String ip = InetAddress.getLocalHost().getHostAddress();
			usernameChooser.setText(usersDao.getUser(crypter.encrypt(ip)).getUserName());

		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (EmptyResultDataAccessException emptyDsException) {
			usernameChooser.setText("");
		}
		
		changeColor = new JButton();
		changeColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		changeColor.setBackground(Color.BLACK);
		changeColor.setContentAreaFilled(false);
		changeColor.setIcon(new ImageIcon(ICONS_PATH + "usercolor.png"));
		changeColor.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				changeColor.setBorder(BorderFactory.createLineBorder(new Color(180, 156, 99), 3));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				changeColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
			}

			public void mouseClicked(MouseEvent e) {
				userColor = JColorChooser.showDialog(loginFrame, "Цвет вашего текста", userColor);
				settings.setMyColor(userColor);
				if (userColor == null) {
					Random random = new Random();
					userColor = new Color(random.nextInt(65535));
				}
			}
				
		});
		
		usernamePanel.add(chooseUsernameLabel);
		usernamePanel.add(usernameChooser);
		
	    enterServer = new JButton();
		enterServer.setIcon(new ImageIcon(ICONS_PATH + "login.png"));
		enterServer.setBackground(Color.BLACK);
		enterServer.setContentAreaFilled(false);
		enterServer.addActionListener(new enterServerButtonListener());
		enterServer.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				enterServer.setBorder(BorderFactory.createLineBorder(new Color(180, 156, 99), 3));
				
			}
		
			@Override
			public void mouseExited(MouseEvent e) {
				enterServer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
				
			}
			
		});
		
		settingPane.add(usernamePanel, BorderLayout.CENTER);
		settingPane.add(changeColor, BorderLayout.SOUTH);
		settingPane.setBackground(Color.BLACK);
		
		JLabel probel = new JLabel();
		
		loginPane = new JPanel();
		loginPane.setBackground(Color.BLACK);
		loginPane.setLayout(new BorderLayout());
		loginPane.add(probel, BorderLayout.NORTH);
		loginPane.add(enterServer, BorderLayout.CENTER);
		
		titlePane.setBackground(Color.BLACK);
		usernamePanel.setBackground(Color.BLACK);
		contentPane.add(titlePane, BorderLayout.NORTH);
		contentPane.add(settingPane, BorderLayout.CENTER);
		contentPane.add(loginPane, BorderLayout.SOUTH);
		
		titlePane.setBackground(Color.BLACK);
		settingPane.setBackground(Color.BLACK);
		usernamePanel.setBackground(Color.BLACK);
		loginPane.setBackground(Color.BLACK);

		loginFrame.add(contentPane, BorderLayout.CENTER);
		
    }
    
    private class TextMotionListener extends MouseInputAdapter {
		public void mouseMoved(MouseEvent e) {
			Element elem = doc.getCharacterElement(chatBox.viewToModel(e.getPoint()));
			AttributeSet as = elem.getAttributes();
			if (StyleConstants.isUnderline(as))
				chatBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
			else
				chatBox.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
    
	private class TextClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				Element elem = doc.getCharacterElement(chatBox.viewToModel(e.getPoint()));
				AttributeSet as = elem.getAttributes();
				URLLinkAction fla = (URLLinkAction) as.getAttribute(LINK_ATTRIBUTE);
				if (fla != null)
					fla.execute();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
	}
	
	private List<String> getListWords(String message){
    	List<String> listWords = new ArrayList<String>();
    	message  = message + " ";
    	String word = "";
    	char[] arrayMessage = message.toCharArray();
    	for (int i = 0; i < arrayMessage.length; i++) {
			if (arrayMessage[i] != ' '){
				word = word + arrayMessage[i];
			}else {
				listWords.add(word);
				word = "";
			}
		}
    	return listWords;
    }
	
    private boolean isSmilesExist(String message){
    	
    	List<String> words = getListWords(message);
    	
    	for (String word : words) {
    		boolean isSmile = Smiles.SMILE_NAME.get(word) != null; 
    		if(isSmile){
    			return true;
    		}
		}
    	return false;
    	
    }
	
    public void display() {
    	
    	Image im = Toolkit.getDefaultToolkit().getImage(APPLICATION_ICON);
    	chatFrame.setIconImage(im);
    	chatFrame.setMinimumSize(new Dimension(300, 200));
    	chatFrame.setVisible(false);
    	
    	rowCount = currentCntRow = chatDao.getLastRow();
      
    	this.sc = new StyleContext();
        this.system = sc.addStyle("ConstantWidth", null);
        StyleConstants.setFontFamily(system, "Serif");
        StyleConstants.setFontSize(system, 15);
        StyleConstants.setForeground(system, new Color(0,112,15));
        
        this.regularBlue = sc.addStyle("regularBlue", null);
		StyleConstants.setForeground(regularBlue, Color.BLUE);
		StyleConstants.setUnderline(regularBlue, true);
        
        this.doc = new EditorDocument(sc);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        Font font = new Font("Times New Roman",Font.PLAIN , 14);
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        
        messageBox = new JTextArea();
        messageBox.setFont(font);
        messageBox.setBorder(border);
        messageBox.requestFocusInWindow();
    	messageBox.setLineWrap(true);
		messageBox.setWrapStyleWord(false);
        messageBox.addKeyListener(new KeyAdapter() {
      	  public void keyPressed(KeyEvent e) {
      		  int keyPressed = e.getKeyCode();
      		  boolean isEnter = (keyPressed == 10);
      		  boolean needTranslate = (keyPressed == 82) && e.isControlDown();
      		  if (isEnter){
      			  sendMessage();
      		  }else if (needTranslate) {
      			  String tmp = messageBox.getText();
				  messageBox.setText(Converter.engToRu(tmp));
			}
      	    }
		});
        
        sendMessage = new JButton();
        sendMessage.setIcon(new ImageIcon(ICONS_PATH + "send.png"));
        sendMessage.setPreferredSize(new Dimension(80, 35));
        sendMessage.addActionListener(new sendMessageButtonListener());
        
        smilesButton = new JButton();
        smilesButton.setPreferredSize(new Dimension(35, 35));
        smilesButton.setIcon(new ImageIcon(ICONS_PATH + "smile.png"));
        smilesButton.addActionListener(new selectSmileButtonListener());

        this.textPane = new JTextPane(doc);
        this.textPane.setBackground(Color.WHITE);
        this.chatScrollPane = new JScrollPane(textPane);   
        
        chatBox = new JTextPane(doc);
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.addMouseListener(new TextClickListener());
        chatBox.addMouseMotionListener(new TextMotionListener());
        
		DefaultCaret caret = (DefaultCaret) chatBox.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(messageBox, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(smilesButton);
        buttonPanel.add(sendMessage);
        
        southPanel.add(chatPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.EAST);
        southPanel.setPreferredSize(new Dimension(35,40));

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
        allContent.setLayout(new BorderLayout());
        allContent.add(mainPanel, BorderLayout.CENTER);
        allContent.add(userWin.getWindow(), BorderLayout.EAST);
        
        chatFrame.add(allContent);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setSize(600, 300);
        chatFrame.setVisible(true);
        
    }
    
    private boolean offUsersExist(){
    	return usersDao.offExists();
    }
    
    class selectSmileButtonListener implements ActionListener {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		
    		SwingUtilities.invokeLater(new Runnable() {
    	            @Override
    	            public void run() {
    	                try {
    	                    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
    	                } catch (Exception e) {
    	                    e.printStackTrace();
    	                }
    	              
    	            	Smiles smiles = new Smiles();
    	            	smiles.view();
    	            }
    	        });
    	}
    }
    
    private void sendMessage(){
    	
        if (messageBox.getText().length() < 1) {
        } else if (messageBox.getText().equals(".clear")) {
            chatBox.setText("Cleared all messages\n");
            messageBox.setText("");
        } else {
            ChatModel chatModel = new ChatModel(crypter.encrypt(user.getUserName()), crypter.encrypt(messageBox.getText().trim()), new Timestamp(new Date().getTime()));
            chatDao.insertMessage(chatModel);
            
            if (offUsersExist()){
            	//Notification notification = new Notification(user.getUserName(), messageBox.getText());
            	//notification.sendMail();
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
					
					try{
						ChatModel chatModel = chatDao.getMessage(i);
						String timeSend = new SimpleDateFormat("[HH:mm]").format(chatModel.getSendTime()).toString();
						String message = chatModel.getMessage();
						Style style = getUserStyle( chatModel.getColor());
						
						print(timeSend, message, style);
						
					}catch(BadLocationException badLocationException){
						badLocationException.printStackTrace();
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
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
		
		private String checkUrlValid(String message){
		
			String messageUrl = message.trim();
			boolean isWww = messageUrl.indexOf("www") != -1;
			boolean isNoHttp = messageUrl.indexOf("http") == -1;
			boolean urlNotValid = messageUrl.indexOf("www.www") != -1;
			
			if(urlNotValid){
				messageUrl= messageUrl.replaceAll("www.www", "http://www");
			}else if (isNoHttp && isWww) {
				messageUrl = messageUrl.replaceAll("www.", "http://");
			}
			else{
			}
			return messageUrl;
			
		}

		private void print(String sendTime, String message, Style style) throws BadLocationException, MalformedURLException, IOException {
			
			MiddleUrl middleUrl = new MiddleUrl(message);
			List<UrlPoint> messages = middleUrl.getPoints();
			
			doc.insertString(doc.getLength(), sendTime + " - ",	style);
			for (UrlPoint urlPoint : messages) {
				
				String messageUrl = urlPoint.getMessage();
				messageUrl = messageUrl.trim();
				if(urlPoint.isUrl()){
					
					int posFormat = messageUrl.lastIndexOf(".");
					String format = messageUrl.substring(posFormat, messageUrl.length()).toLowerCase();
					boolean isPicture = PICTURE_FORMAT.contains(format);
					if (isPicture) {
						messageUrl = checkUrlValid(messageUrl);
						doc.insertString(doc.getLength(), "\n",	style);
						printPicture(sendTime, style, messageUrl, format);
					}else {
						messageUrl = checkUrlValid(messageUrl);
						regularBlue.addAttribute(LINK_ATTRIBUTE, new URLLinkAction(messageUrl));
						doc.insertString(doc.getLength(), messageUrl + " ", regularBlue);
					}
					
				}else {
					printMessage(sendTime, messageUrl, style);
				}
			}
			
			doc.insertString(doc.getLength(), "\n",	style);
		}

		@SuppressWarnings("unused")
		private void checkAndPrint(String sendTime, String message, Style style)
				throws IOException, MalformedURLException, BadLocationException {

			doc.insertString(doc.getLength(), sendTime + " - " , style);
			
			String urlString = message;
			regularBlue.addAttribute(LINK_ATTRIBUTE, new URLLinkAction(message));

			int posFormat = message.lastIndexOf(".");
			String format = message.substring(posFormat, message.length()).toLowerCase();
			
			boolean isPicture = PICTURE_FORMAT.contains(format);
			if (isPicture) {
				printPicture(sendTime, style, urlString, format);
			} else {
				printLink(sendTime, message, style);
			}
		}

		private void printMessage(String sendTime, String message, Style style)
				throws BadLocationException {
		
			message = message.trim();
			
			if (message.startsWith("Пользователь")){
				doc.insertString(doc.getLength(),  message , style);
			}else{
					
					boolean smileExist = isSmilesExist(message);
					if(smileExist){
						
						List<String> words = getListWords(message);
						for (String word : words) {
							boolean isSmile = Smiles.SMILE_NAME.get(word) != null;
							if (isSmile){
								doc.setIcon(doc.getLength(), " ", new ImageIcon(Smiles.SMILE_NAME.get(word)));
							}else {
								doc.insertString(doc.getLength(), word + " ", style);
							}
						}
						
					}else {
						doc.insertString(doc.getLength(), message + " ", style);
					}
			}
			
		}

		private void printLink(String sendTime, String message, Style style)
				throws BadLocationException {
			doc.insertString(doc.getLength(), sendTime + " - ",	style);
			doc.insertString(doc.getLength(), message + "\n", regularBlue);
		}

		private void printPicture(String sendTime, Style style, String urlString,
				String format) throws IOException, MalformedURLException,
				BadLocationException {
			
			Config.setProxy();
		
			/*urlString= urlString.replaceAll("www.www", "http://www");
			urlString = urlString.replaceAll("www.", "http://");*/
			
			HttpURLConnection httpConn = (HttpURLConnection) new URL(urlString).openConnection();
			InputStream inStream = httpConn.getInputStream();
			ImageIcon imageIcon = getImageIcon(format, inStream);
			
			doc.setIcon(doc.getLength(), " ", imageIcon);
			doc.insertString(doc.getLength(), "\n", style);
		}

		private ImageIcon getImageIcon(String format, InputStream inStream) throws IOException {
			
			Image image;
			ImageIcon imageIcon;
			
			boolean isGif = format.endsWith(".gif");
			if(!isGif){
				image = ImageIO.read(inStream);
				imageIcon = new ImageIcon(image);
			}else {
				byte[] bytes = IOUtils.toByteArray(inStream);
				imageIcon = new ImageIcon(bytes);
				
			}
			
			return imageIcon;
		}

		@SuppressWarnings("unused")
		private void recordGif(InputStream inStream)	throws FileNotFoundException, IOException {
			
			try {

				OutputStream outputStream = new FileOutputStream(new File(ICONS_PATH + "default.gif"));

				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = inStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}

				outputStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
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
    	
    		if (userName.isEmpty()){
    			userName = usersDao.getDefaultName(crypter.encrypt(ip)).getUserName();
    		}	
    		
    		user = usersDao.login(crypter.encrypt(ip), crypter.encrypt(userName), color );
    		
		}catch(UnknownHostException e){
			e.printStackTrace();
		}
    	
    	startLoginRow = chatDao.getLastRow();
    	incMessage = startLoginRow;
        loginFrame.setVisible(false);
        reloadTimer.start();
        display();
       
        ChatModel chatModel = new ChatModel(crypter.encrypt("Система"), crypter.encrypt("Пользователь " + user.getUserName() + " присоединяется к беседе."), new Timestamp(new Date().getTime()));
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
    				
    				MouseListener mouseListener = new MouseAdapter() {
    					@Override
    					public void mousePressed(MouseEvent e) {
    						tray.remove(trayIcon);
    						trayActive = false;
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
    
    @SuppressWarnings("unused")
	private static boolean isUrl(String message){
  	  if (message.startsWith("www") || message.startsWith("http")){
    		  return true;
  	  }
		  return false;
  }
    
    private class URLLinkAction extends AbstractAction{

    	private static final long serialVersionUID = 1L;
		private String url;

        URLLinkAction(String bac){
             url = bac;
        }

           protected void execute() {
                   
        	   try {
                         String osName = System.getProperty("os.name").toLowerCase();
                         Runtime rt = Runtime.getRuntime();
                  if (osName.indexOf( "win" ) >= 0) {
                             rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
                        }
                              else if (osName.indexOf("mac") >= 0) {
                                rt.exec( "open " + url);
                  }
                
                        else if (osName.indexOf("ix") >=0 || osName.indexOf("ux") >=0 || osName.indexOf("sun") >=0) {
                             String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                               "netscape","opera","links","lynx"};

                             StringBuffer cmd = new StringBuffer();
                             for (int i = 0 ; i < browsers.length ; i++)
                                  cmd.append((i == 0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");

                             rt.exec(new String[] { "sh", "-c", cmd.toString() });
                        }
             }
             catch (Exception ex)
             {
                  ex.printStackTrace();
             }
               }

           public void actionPerformed(ActionEvent e){
                   execute();
           }
   }
    
    static class Smiles extends JFrame{
    	
		private static final long serialVersionUID = 1L;
		
    	public static final Map<String, String> SMILE_PATH = new HashMap<String, String>();
    	
    	 /**
         *
         *получаем имена смайликов, добавляем их в коллекцию под полной директорией 
         *и в значение ключа ставим их название с предстоящим двоеточием
         *Таким образом, чтобы добавить новые смайлики достаточно кинуть новые в папку с остальными смайлами
         */
    	public static final Map<String, String> SMILE_NAME = new HashMap<String, String>();
	    	static{
	 
		    		File listFile = new File(ICONS_PATH + SMILES_PATH);
		    		File exportFiles[] = listFile.listFiles();
		    		String[] names = new String[exportFiles.length];
		    		for (int i = 0; i < names.length; i++) {
		    			String fileName = exportFiles[i].getName();
		    			if (fileName.endsWith(".gif")){
		    				names[i] = fileName;
		    			}
		    		}
		    		
		    		for (int countSmiles = 0; countSmiles < names.length; countSmiles++) {
						SMILE_PATH.put(ICONS_PATH + SMILES_PATH + names[countSmiles], ":" + names[countSmiles].replace(".gif", ""));
						SMILE_NAME.put(":" + names[countSmiles].replace(".gif", ""), ICONS_PATH + SMILES_PATH + names[countSmiles]);
					}
	    		}
	    	
    	private JPanel  contentPane;
    	private JPanel  smilePane;
    	private JPanel  okPane;
    	private JButton okButton;
    	
    	private void view(){
    		
    		setLayout(new BorderLayout());
    		
    		contentPane = new JPanel();
    		contentPane.setLayout(new BorderLayout());
    		
    		smilePane = new JPanel();
    		smilePane.setLayout(new FlowLayout());
    		
    		okPane = new JPanel();
    		okPane.setLayout(new BorderLayout());
    		okPane.setPreferredSize(new Dimension(50,30));
    		
    		okButton = new JButton("OK");
    		okButton.addActionListener(new ActionListener() {
    			
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				setVisible(false);
    				
    			}
    		});
    		
    		okPane.add(okButton);
    		
    		String[] names = getSmileNames();
    		
    		for (int count = 0; count < names.length; count++) {
    			
    			final JButton smile = new JButton();
    			smile.setPreferredSize(new Dimension(50,50));
    			smile.setIcon(new ImageIcon(ICONS_PATH + SMILES_PATH + names[count]));
    			smile.addActionListener(new ActionListener() {
    				@Override
    				public void actionPerformed(ActionEvent e) {
    					messageBox.append(SMILE_PATH.get(smile.getIcon().toString().trim()) + " ");
    				}
    			});
    			
    			smilePane.add(smile);
    			
    		}
    		
    		contentPane.add(smilePane, BorderLayout.CENTER);
    		contentPane.add(okPane, BorderLayout.SOUTH);
    		add(contentPane);
    		
    	  	Image im = Toolkit.getDefaultToolkit().getImage(ICONS_PATH + "smilefrane.png");
        
    	  	setIconImage(im);
    		setSize(640, 300);
    		setVisible(true);
    		
    	}
    	
    	private String[] getSmileNames() {
    		File listFile = new File(ICONS_PATH + SMILES_PATH);
    		File exportFiles[] = listFile.listFiles();
    		String[] names = new String[exportFiles.length];
    		for (int i = 0; i < names.length; i++) {
    			String fileName = exportFiles[i].getName();
    			if (fileName.endsWith(".gif")){
    				names[i] = fileName;
    			}
    			
    		}
    		return names;
    	}
    	
    }

   }