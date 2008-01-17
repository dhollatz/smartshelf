package de.haw.smartshelf.shelf.testgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import de.haw.smartshelf.shelf.Shelf;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * 
 */
public class SmartshelfGUI extends SingleFrameApplication {
    private JToggleButton inventoryToggle;
    private JPanel topPanel;
    private JSeparator jSeparator;
    private JToolBar toolBar;
    private JPanel toolBarPanel;
    private JPanel imageOutput;
    private JTextPane testOutput;
    private JScrollPane jScrollPane1;
    private JPanel contentPanel;
    private Shelf shelf;

    @Action
    public void open() {
    }

    @Action
    public void save() {
    }

    @Action
    public void newFile() {
    }

    private ActionMap getAppActionMap() {
        return Application.getInstance().getContext().getActionMap(this);
    }

    @Override
    protected void startup() {

        {
        	
        	shelf = new Shelf();
            topPanel = new JPanel();
            BorderLayout panelLayout = new BorderLayout();
            topPanel.setLayout(panelLayout);
            topPanel.setPreferredSize(new java.awt.Dimension(500, 300));
            {
                contentPanel = new JPanel();
                BorderLayout contentPanelLayout = new BorderLayout();
                contentPanelLayout.setHgap(10);
                contentPanelLayout.setVgap(10);
                contentPanel.setLayout(contentPanelLayout);
                topPanel.add(contentPanel, BorderLayout.CENTER);
                {
                	jScrollPane1 = new JScrollPane();
                	contentPanel.add(jScrollPane1, BorderLayout.CENTER);
                	jScrollPane1.setPreferredSize(new java.awt.Dimension(478, 298));
                	{
                		testOutput = new JTextPane(shelf.getDoc());
                		jScrollPane1.setViewportView(testOutput);
                		testOutput.setPreferredSize(new java.awt.Dimension(131, 247));
                		testOutput.setSize(286, 247);
                	}
                }
                {
                	imageOutput = new JPanel();
                	contentPanel.add(imageOutput, BorderLayout.EAST);
                	FlowLayout imageOutputLayout = new FlowLayout();
                	imageOutputLayout.setAlignOnBaseline(true);
                	imageOutput.setLayout(imageOutputLayout);
                	imageOutput.setPreferredSize(new java.awt.Dimension(270, 250));
                	imageOutput.setSize(-1, 300);
                }
            }
            {
                toolBarPanel = new JPanel();
                topPanel.add(toolBarPanel, BorderLayout.NORTH);
                BorderLayout jPanel1Layout = new BorderLayout();
                toolBarPanel.setLayout(jPanel1Layout);
                {
                    toolBar = new JToolBar();
                    toolBarPanel.add(toolBar, BorderLayout.CENTER);
                    {
                    	inventoryToggle = new JToggleButton();
                    	toolBar.add(inventoryToggle);
                    	inventoryToggle.setName("inventoryToggle");
                    	inventoryToggle.addActionListener(new ActionListener() {
                    		public void actionPerformed(ActionEvent evt) {
                    			inventoryToggleActionPerformed(evt);
                    		}
                    	});
                    }
                }
                {
                    jSeparator = new JSeparator();
                    toolBarPanel.add(jSeparator, BorderLayout.SOUTH);
                }
            }
        }
        show(topPanel);
    }

    public static void main(String[] args) {
        launch(SmartshelfGUI.class, args);
    }
    
    private void inventoryToggleActionPerformed(ActionEvent evt) {
    	if (inventoryToggle.isSelected()){
    		shelf.startInventoryLoop();
    	}else{
    		shelf.stopInventoryLoop();
    	}
    		
    }

}
