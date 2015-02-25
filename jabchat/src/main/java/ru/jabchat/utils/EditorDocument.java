package ru.jabchat.utils;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class EditorDocument extends DefaultStyledDocument {
	 
	  private static final long serialVersionUID = -2191570369962370294L;

	  public void setIcon(int pos, String text, ImageIcon ico) throws BadLocationException {
	 
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setIcon(sas, ico);
	    insertString(pos, text, sas);
	 
	  }
	  
	  public void setIcon(int pos, ImageIcon ico) throws BadLocationException {
			 
		    SimpleAttributeSet sas = new SimpleAttributeSet();
		    StyleConstants.setIcon(sas, ico);
		    insertString(pos, "", sas);
		  }
	 
	  public boolean isItalic(int pos) {
	    Element element = getCharacterElement(pos);
	    return StyleConstants.isItalic(element.getAttributes());
	  }
	 
	  public boolean isBold(int pos) {
	    Element element = getCharacterElement(pos);
	    return StyleConstants.isBold(element.getAttributes());
	  }
	 
	  public boolean isUnderline(int pos) {
	    Element element = getCharacterElement(pos);
	    return StyleConstants.isUnderline(element.getAttributes());
	  }
	 
	  public void setItalic(int start, int end, boolean active) {
	 
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setItalic(sas, active);
	    setCharacterAttributes(start, end - start, sas, false);
	  }
	 
	  public void setBold(int start, int end, boolean active) {
	 
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setBold(sas, active);
	    setCharacterAttributes(start, end - start, sas, false);
	  }
	 
	  public void setUnderline(int start, int end, boolean active) {
	 
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setUnderline(sas, active);
	    setCharacterAttributes(start, end - start, sas, false);
	  }
	 
	  public void setFont(int start, int end, String font) {
	 
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setFontFamily(sas, font);
	    setCharacterAttributes(start, end - start, sas, false);
	  }
	 
	  public void setFontSize(int start, int end, int size) {
	 
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setFontSize(sas, size);
	    setCharacterAttributes(start, end - start, sas, false);
	  }
	 
	  public void setForeground(int start, int end, Color col) {
	 
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setForeground(sas, col);
	    setCharacterAttributes(start, end - start, sas, false);
	  }
	 
	  public void setBackground(int start, int end, Color col) {
	 
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setBackground(sas, col);
	    setCharacterAttributes(start, end - start, sas, false);
	  }
	 
	  public EditorDocument() {
	    super();
	  }
	 
	  public EditorDocument(Content arg0, StyleContext arg1) {
	    super(arg0, arg1);
	  }
	 
	  public EditorDocument(StyleContext arg0) {
	    super(arg0);
	  }
	}