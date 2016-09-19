package com.jt.test;

import java.io.File;
import java.io.FileWriter;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
public class JdomWriteXml {
 /**
  * @param args
  */
 public static void main(String[] args) {
  SAXBuilder sb = new SAXBuilder();
  Element actions = new Element("actions");
  Document document = new Document(actions);
  Element action1 = new Element("action");
  actions.addContent(action1);
  Element action1_forward1 = new Element("forward");
  action1.addContent(action1_forward1);
  try {
   File f1 = new File("mystruts.xml");
   // XMLOutputter xo=new XMLOutputter(" ",true,"GB2312");
   XMLOutputter xo = new XMLOutputter();
   FileWriter fw = new FileWriter(f1);
   xo.output(document, fw);
   fw.close();
  } catch (Exception e) {
   e.printStackTrace();
  }
  // System.out.println(document.toString());
 }
}
