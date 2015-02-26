package ru.jabchat.client;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.commons.io.IOUtils;

import ru.jabchat.server.dao.ChatDao;
import ru.jabchat.server.dao.UserDao;
import ru.jabchat.server.model.ChatModel;
import ru.jabchat.server.model.UserModel;
import ru.jabchat.utils.Config;
import ru.jabchat.utils.EditorDocument;
import ru.jabchat.utils.Notification;
import ru.jabchat.utils.ObjectRecorder;
import ru.jabchat.utils.Settings;
import ru.jabchat.utils.StringCrypter;

public class Chat {

	public static final String APPLICATION_NAME  =  "Vasya&Fedya Production";
	
	private static final String ICONS_PATH 		 =  "resources/icons/";
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
	private JButton sendMessage;
	
	private Settings settings = new Settings(); 
	private JButton changeColor;
	private Color userColor = Color.BLACK;
	
	private JTextField  messageBox;
	private JTextPane chatBox;
	private JTextField usernameChooser = new JTextField(15);

	private StyleContext sc;
	private Style system;
	private Style regularBlue;
	
	public EditorDocument doc;
    public EditorDocument docEdit = new EditorDocument();
	
	public JTextPane textPane;
	public JScrollPane scrollPane;

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

    public void display() {
    	
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

        JPanel southPanel = new JPanel();
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
        chatBox.addMouseListener(new TextClickListener());
        chatBox.addMouseMotionListener(new TextMotionListener());
        
		DefaultCaret caret = (DefaultCaret) chatBox.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

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
        chatFrame.setSize(600, 300);
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
            ChatModel chatModel = new ChatModel(crypter.encrypt(user.getUserName()), crypter.encrypt(messageBox.getText()), new Timestamp(new Date().getTime()));
            chatDao.insertMessage(chatModel);
            
            if (offUsersExist()){
            	Notification notification = new Notification(user.getUserName(), messageBox.getText());
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
						
						if (isUrl(message)) {
							checkAndPrint(timeSend, message, style);
						} else {
							printMessage(timeSend, message, style);

						}
						
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

		private void checkAndPrint(String sendTime, String message, Style style)
				throws IOException, MalformedURLException, BadLocationException {

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
			doc.insertString(doc.getLength(), sendTime + " - " + message + "\n", style);
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
			HttpURLConnection httpConn = (HttpURLConnection) new URL(urlString).openConnection();
			InputStream inStream = httpConn.getInputStream();
			ImageIcon imageIcon = getImageIcon(format, inStream);
			
			doc.insertString(doc.getLength(), sendTime + " - " + "\n", style);
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
				
				//TODO первоначальный вариант
				//
				//			парсим, сохраняем локально, создаем иконку,вставляем в док
				//			recordGif(inStream);
				//			imageIcon = new ImageIcon(ICONS_PATH  + "default.gif");
				//				хотяяяя, нахуя что-то сохранять если можно из потока срау ебануть :D
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
    			user = usersDao.getDefaultName(crypter.encrypt(ip));
    		}else {
    			user = usersDao.login(crypter.encrypt(ip), crypter.encrypt(userName), color );
			}
    		
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
             url=bac;
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
    }