package org.architecturemining.fam.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.architecturemining.fam.IO.ReadXML;
import org.architecturemining.fam.IO.WriteXML;
import org.architecturemining.fam.model.ConsoleDemo;
import org.architecturemining.fam.model.FunctionalArchitectureModel;;

@SuppressWarnings("serial")


public class Window extends JFrame {
	
	JButton recordLog, stopLog, exportLog;
	JLabel recordingLabel, exportLabel;
	JTextArea textArea;
	ArrayList<JButton> featureButtons;
	
	FunctionalArchitectureModel fam = new FunctionalArchitectureModel();
	
	TraceTable traceTable;
	
	int menuSpace = 0;
	
	public static void main(){
		
		new Window();

	}
	
	public Window(){
		
			WriteXML.main(); 
			ReadXML.readXML(fam);
			
			ConsoleDemo.main(fam);
			
			// Window stuff
			this.setSize(1200, 800);

			this.setTitle("FAM Sequence Creator");
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			this.setLayout(new BorderLayout());
			
			//Panel stuff
			JPanel menuPanel = new JPanel();
			MyPanel famPanel = new MyPanel();
			
			addMenuPanelComponents(menuPanel);
			addFamPanelComponents(fam, famPanel);
			
			this.add(menuPanel, BorderLayout.WEST);
			this.add(famPanel, BorderLayout.CENTER);
			
			this.setVisible(true);
			this.setResizable(true);
		}

	private void addMenuPanelComponents(JPanel menuPanel) {
		
		menuPanel.setLayout(new GridBagLayout());	
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		
		recordingLabel = new JLabel("Recording");
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		menuPanel.add(recordingLabel,c);
		
		recordLog = new JButton("Record new log");
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 5;
		menuPanel.add(recordLog,c);
		
		stopLog = new JButton("Stop log");
		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 5;
		menuPanel.add(stopLog,c);
		
		traceTable = new TraceTable();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.ipady = 200;
		c.ipadx = 20;
		c.insets = new Insets(10,10,10,5);
		c.weighty = 5;
		menuPanel.add(traceTable,c);
		
		/*
		textArea = new JTextArea("");
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 400; 
		c.gridwidth = c.REMAINDER;
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		menuPanel.add(textArea,c);
		 */
		
		exportLabel = new JLabel("Export");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1; //reset
		c.weighty = 1;
		c.ipady = 0; //reset
		c.ipadx = 0;
		
		menuPanel.add(exportLabel,c);
		
		exportLog = new JButton("Export");
		c.gridx = 0;
		c.gridy = 4;
		c.weighty = 5;
		menuPanel.add(exportLog,c);		
	}

	public void addFamPanelComponents(FunctionalArchitectureModel fam, MyPanel famPanel) {
		
		famPanel.setLayout(null);
		
		featureButtons = new ArrayList<JButton>();
		
		int counter = 0;
		
		ListenForButton lForButton = new ListenForButton();
		
		for(int i = 0 ; i < fam.getListModules().size(); i++) {
			
			for(int j = 0; j < fam.getListModules().get(i).getFeatureList().size(); j++) {
	
				featureButtons.add(new JButton(fam.getListModules().get(i).getFeatureList().get(j).getName()));
				
				featureButtons.get(counter).setBounds(
						menuSpace + (int) fam.getListModules().get(i).getFeatureList().get(j).getOrigin().getX(),
									(int) fam.getListModules().get(i).getFeatureList().get(j).getOrigin().getY(), 
						fam.getListModules().get(i).getFeatureList().get(j).getWidth(), 
						fam.getListModules().get(i).getFeatureList().get(j).getHeight()
						);
				
				featureButtons.get(counter).addActionListener(lForButton);
				featureButtons.get(counter).setName(fam.getListModules().get(i).getFeatureList().get(j).getName());
				famPanel.add(featureButtons.get(counter));
			
				counter++;
			}
		}
		
		famPanel.setBorder( BorderFactory.createLineBorder(Color.black));
	}
	
	private class ListenForButton implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			for(int i = 0 ; featureButtons.size() > i ; i++) {
				
				if(e.getSource() == featureButtons.get(i)) {
					
					//textArea.append("-"+ featureButtons.get(i).getName());		
				}	
			}
		}
	}
	
	class MyPanel extends JPanel {
		  
		public void paintComponent(Graphics g) {

			Graphics2D graph2 = (Graphics2D)g;
				
			for(int i = 0 ; i < fam.getListModules().size(); i++) {
				
				graph2.draw(new Rectangle2D.Float(	menuSpace + (float) fam.getListModules().get(i).getOrigin().getX(),
													(float) fam.getListModules().get(i).getOrigin().getY(), 
													fam.getListModules().get(i).getWidth(), 
													fam.getListModules().get(i).getHeight()	));
				graph2.drawString(	 fam.getListModules().get(i).getName(),
						menuSpace + (int)fam.getListModules().get(i).getOrigin().getX() + 7, 
								  	(int)fam.getListModules().get(i).getOrigin().getY() + 15);
			}
				
			for(int i = 0 ; i < fam.getListInfoFlow().size(); i++) {
				
				graph2.draw( new Line2D.Double(	menuSpace +fam.getListInfoFlow().get(i).getSource().getOrigin().getX() + fam.getListInfoFlow().get(i).getSource().getWidth()/2, 
												fam.getListInfoFlow().get(i).getSource().getOrigin().getY() + fam.getListInfoFlow().get(i).getSource().getHeight()/2, 
												menuSpace +fam.getListInfoFlow().get(i).getTarget().getOrigin().getX() + fam.getListInfoFlow().get(i).getTarget().getWidth()/2, 
												fam.getListInfoFlow().get(i).getTarget().getOrigin().getY() + fam.getListInfoFlow().get(i).getTarget().getHeight()/2
												));			
			}
		}
	}
}


