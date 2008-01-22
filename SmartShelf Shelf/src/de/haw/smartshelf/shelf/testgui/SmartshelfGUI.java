package de.haw.smartshelf.shelf.testgui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import com.sun.tools.javac.comp.Lower;

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
    private JLabel updateTimerSliderLabel;
    private JPanel jPanel1;
    private JSlider updateTimeSlider;
    private JPanel imageOutput;
    private JTextPane testOutput;
    private JScrollPane jScrollPane1;
    private JTextField updateTimeSliderValue;
    private JPanel jPanel2;
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

    	BorderLayout mainFrameLayout = new BorderLayout();
    	getMainFrame().getContentPane().setLayout(mainFrameLayout);
        {
        	
        	shelf = new Shelf();
            topPanel = new JPanel();
            getMainFrame().getContentPane().add(topPanel);
            BorderLayout panelLayout = new BorderLayout();
            panelLayout.setHgap(10);
            panelLayout.setVgap(10);
            topPanel.setLayout(panelLayout);
            topPanel.setPreferredSize(new java.awt.Dimension(500, 300));
            {
                contentPanel = new JPanel();
                BorderLayout contentPanelLayout = new BorderLayout();
                contentPanel.setLayout(contentPanelLayout);
                topPanel.add(contentPanel, BorderLayout.CENTER);
                {
                	jPanel1 = new JPanel();
                	BorderLayout jPanel1Layout = new BorderLayout();
                	jPanel1Layout.setVgap(10);
                	jPanel1Layout.setHgap(10);
                	jPanel1.setLayout(jPanel1Layout);
                	contentPanel.add(jPanel1, BorderLayout.NORTH);
                	jPanel1.setPreferredSize(new java.awt.Dimension(500, 49));
                	jPanel1.setEnabled(false);
                	jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                	{
                		updateTimeSlider = new JSlider();
                		jPanel1.add(updateTimeSlider, BorderLayout.CENTER);
                		updateTimeSlider.setLayout(null);
                		updateTimeSlider.setPreferredSize(new java.awt.Dimension(382, 37));
                		updateTimeSlider.setPaintTicks(true);
                		updateTimeSlider.setSnapToTicks(true);
                		updateTimeSlider.setMajorTickSpacing(10);
                		updateTimeSlider.setMinorTickSpacing(10);
                		updateTimeSlider.addChangeListener(new ChangeListener() {
                			public void stateChanged(ChangeEvent evt) {
                				updateTimeSliderStateChanged(evt);
                			}
                		});
                    	shelf.setUpdateInterval(updateTimeSlider.getValue());
                	}
                	{
                		jPanel2 = new JPanel();
                		BorderLayout jPanel2Layout = new BorderLayout();
                		jPanel2Layout.setHgap(10);
                		jPanel2Layout.setVgap(10);
                		jPanel2.setLayout(jPanel2Layout);
                		jPanel1.add(jPanel2, BorderLayout.EAST);
                		jPanel2.setPreferredSize(new java.awt.Dimension(72, 49));
                		{
                			updateTimerSliderLabel = new JLabel();
                			jPanel2.add(updateTimerSliderLabel, BorderLayout.NORTH);
                			updateTimerSliderLabel.setName("updateTimerSliderLabel");
                			updateTimerSliderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                		}
                		{
                			updateTimeSliderValue = new JTextField();
                			jPanel2.add(updateTimeSliderValue, BorderLayout.CENTER);
                			updateTimeSliderValue.setName("updateTimeSliderValue");
                			updateTimeSliderValue.setHorizontalAlignment(SwingConstants.CENTER);
                			updateTimeSliderValue.setPreferredSize(new java.awt.Dimension(22, 23));
                			updateTimeSliderValue.setEditable(false);
                			updateTimeSliderValue.setText(String.valueOf(shelf.getUpdateInterval()));
                		}
                	}
                }
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
                	imageOutputLayout.setAlignment(FlowLayout.CENTER);  //.setAlignOnBaseline(true);
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
    
    private void updateTimeSliderStateChanged(ChangeEvent evt) {
    	shelf.setUpdateInterval(updateTimeSlider.getValue());
    	updateTimeSliderValue.setText(String.valueOf(shelf.getUpdateInterval()));
    }

}
