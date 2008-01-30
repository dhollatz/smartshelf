package de.haw.smartshelf.shelf.testgui;

import iwork.eheap2.Event;
import iwork.eheap2.EventCallback;
import iwork.eheap2.EventHeapException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.text.DefaultStyledDocument;

import org.apache.log4j.Logger;
import org.jdesktop.application.SingleFrameApplication;

import de.haw.smartshelf.eha.EventHeapAdapter;
import de.haw.smartshelf.eha.EventHeapAdapterConfig;
import de.haw.smartshelf.eha.events.EventFactory;
import de.haw.smartshelf.eha.events.ShelfInventoryEventFacade;
import de.haw.smartshelf.reader.tags.RFIDTag;
import de.haw.smartshelf.shelf.ShelfUtil;

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
public class SmartshelfRemoteGUI extends SingleFrameApplication implements EventCallback {
	private static final Logger LOG = Logger.getLogger(SmartshelfRemoteGUI.class);
	private static final String EHA_CONFIG = "config/ehaProperties.xml";
	
	private EventHeapAdapter eventHeapAdapter;
	
	private JToggleButton inventoryToggle;
	private JPanel topPanel;
	private JSeparator jSeparator;
	private JToolBar toolBar;
	private JPanel toolBarPanel;
	private JPanel jPanel1;
	private JLabel shelfIDLabel;
	private JPanel imageOutput;
	private JTextPane testOutput;
	private JScrollPane jScrollPane1;
	private JTextField tagCount;
	private JLabel tagCountLabel;
	private JPanel statusPanel;
	private JPanel appPanel;
	private JTextField shelfIDText;
	private JPanel contentPanel;
	private Event registeredEvent;

	@Override
	protected void startup() {

		BorderLayout mainFrameLayout = new BorderLayout();
		getMainFrame().getContentPane().setLayout(mainFrameLayout);
		getMainFrame().setPreferredSize(new java.awt.Dimension(500, 300));
		getMainFrame().setSize(540, 308);
		{
			topPanel = new JPanel();
			getMainFrame().getContentPane().add(topPanel);
			BorderLayout panelLayout = new BorderLayout();
			panelLayout.setHgap(10);
			panelLayout.setVgap(10);
			topPanel.setLayout(panelLayout);
			topPanel.setPreferredSize(new java.awt.Dimension(500, 300));
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
								try {
									inventoryToggleActionPerformed(evt);
								} catch (EventHeapException e) {
									LOG.error(e.getLocalizedMessage(), e);
								}
							}
						});
					}
				}
				{
					jSeparator = new JSeparator();
					toolBarPanel.add(jSeparator, BorderLayout.SOUTH);
				}
			}
			{
				appPanel = new JPanel();
				BorderLayout appPanelLayout = new BorderLayout();
				appPanelLayout.setHgap(5);
				appPanelLayout.setVgap(5);
				appPanel.setLayout(appPanelLayout);
				topPanel.add(appPanel, BorderLayout.CENTER);
				appPanel.setPreferredSize(new java.awt.Dimension(500, 240));
				appPanel.setSize(292, 240);
				{
					jPanel1 = new JPanel();
					appPanel.add(jPanel1, BorderLayout.NORTH);
					FlowLayout jPanel1Layout = new FlowLayout();
					jPanel1Layout.setAlignment(FlowLayout.LEFT);
					jPanel1.setLayout(jPanel1Layout);
					jPanel1.setPreferredSize(new java.awt.Dimension(500, 49));
					jPanel1.setEnabled(false);
					jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0,
							10));
					{
						shelfIDLabel = new JLabel();
						jPanel1.add(shelfIDLabel);
						shelfIDLabel.setText("Shelf-ID:");
					}
					{
						shelfIDText = new JTextField();
						jPanel1.add(shelfIDText);
						shelfIDText.setName("shelfIDText");
						shelfIDText.setEditable(true);
						shelfIDText.setText("smartshelf01");
					}
				}
				{
					contentPanel = new JPanel();
					appPanel.add(contentPanel, BorderLayout.CENTER);
					BoxLayout contentPanelLayout = new BoxLayout(contentPanel,
							javax.swing.BoxLayout.X_AXIS);
					contentPanel.setLayout(contentPanelLayout);
					contentPanel.setSize(104, 240);
					{
						jScrollPane1 = new JScrollPane();
						contentPanel.add(jScrollPane1);
						jScrollPane1.setPreferredSize(new java.awt.Dimension(
								131, 186));
						jScrollPane1.setSize(42, 240);
						{
							testOutput = new JTextPane(new DefaultStyledDocument());
							jScrollPane1.setViewportView(testOutput);
							testOutput.setPreferredSize(new java.awt.Dimension(
									100, 247));
						}
					}
					{
						imageOutput = new JPanel();
						contentPanel.add(imageOutput);
						GridLayout imageOutputLayout1 = new GridLayout(5, 5);
						imageOutputLayout1.setColumns(5);
						imageOutputLayout1.setHgap(5);
						imageOutputLayout1.setVgap(5);
						imageOutputLayout1.setRows(5);
						imageOutput.setLayout(imageOutputLayout1);
						imageOutput.setPreferredSize(new java.awt.Dimension(
								358, 186));
						imageOutput.setSize(61, 240);
					}
				}
				{
					statusPanel = new JPanel();
					FlowLayout statusPanelLayout = new FlowLayout();
					appPanel.add(statusPanel, BorderLayout.SOUTH);
					statusPanel.setLayout(statusPanelLayout);
					{
						tagCountLabel = new JLabel();
						statusPanel.add(tagCountLabel);
						tagCountLabel.setName("tagCountLabel");
					}
					{
						tagCount = new JTextField();
						statusPanel.add(tagCount);
						tagCount.setPreferredSize(new java.awt.Dimension(120,
								20));
						tagCount.setName("tagCount");
						tagCount.setEditable(false);
					}
				}
			}
		}
		show(topPanel);
	}

	public static void main(String[] args) {
		launch(SmartshelfRemoteGUI.class, args);
	}

	private void inventoryToggleActionPerformed(ActionEvent evt) throws EventHeapException {
		System.out
				.println("SmartshelfRemoteGUI.inventoryToggleActionPerformed()");
		if (inventoryToggle.isSelected()) {
			try {
				EventHeapAdapterConfig ehaConfig = EventHeapAdapterConfig.getConfig(EHA_CONFIG);
				LOG.debug("EventHeapAdapter configuration successfully obtained");
				LOG.trace(ehaConfig.toString());
				this.eventHeapAdapter = new EventHeapAdapter(ehaConfig);
				this.registeredEvent = EventFactory.createShelfInventoryEvent();
				this.eventHeapAdapter.registerForEvent(this.registeredEvent, this);
				
			} catch (Exception e) {
				LOG.fatal("Configuration failed " + e.getMessage());
				throw new EventHeapException(
						"Could not initialize EventHeapAdapter. " + e.getMessage());
			}
			
		} else {
			eventHeapAdapter.removeEventRegistration(this.registeredEvent);
		}

	}

	public void update(String[] tags) {
		System.out.println("SmartshelfRemoteGUI.update()");
		
		// TODO Umgang mit Ghost-Events...
		if (tags == null) {
			LOG.info("TAGS == NULL");
			return;
		}

		imageOutput.removeAll();
		
		Icon image;
		for (String rfidTag : tags) {
			image = ShelfUtil.getInstance().getImage(rfidTag);
			if (image != null) {
				JLabel imageLabel = new JLabel(image);
				imageOutput.add(imageLabel);
			} else {
				imageOutput.add(new JLabel("No Image, muddi! Tag: "
						+ rfidTag));
			}
		}
		tagCount.setText(Integer.toString(tags.length));
		imageOutput.validate();
		imageOutput.repaint();
		// imageOutput.getToolkit().sync();

	}

	public boolean returnEvent(Event[] events) {
		
		try {
			for (Event event : events) {
				if (event.getEventType().equals(ShelfInventoryEventFacade.TYPE_NAME)) {
					LOG.info(event.toStringComplete());
					ShelfInventoryEventFacade inventoryEventFacade = new ShelfInventoryEventFacade(event);
					String[] tags = inventoryEventFacade.getRFIDTags();
					this.update(tags);
				}
			}
		} catch (EventHeapException e) {
			LOG.error("Error returning Event", e);
		}
		
		// We want more of this... 
		return true;
	}

}
