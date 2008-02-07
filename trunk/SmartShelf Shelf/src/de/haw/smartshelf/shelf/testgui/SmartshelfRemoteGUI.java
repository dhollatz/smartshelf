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
import javax.swing.border.BevelBorder;
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
	private JLabel shelfIDLabel01;
	private JPanel imageOutput02;
	private JLabel shelfIDLabel02;
	private JTextField tagCount02;
	private JTextField tagCount01;
	private JPanel imageOutput01;
	private JPanel statusPanel;
	private JPanel appPanel;
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
					BorderLayout jPanel1Layout = new BorderLayout();
					jPanel1.setLayout(jPanel1Layout);
					jPanel1.setPreferredSize(new java.awt.Dimension(500, 49));
					jPanel1.setEnabled(false);
					jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0,
							10));
					{
						shelfIDLabel01 = new JLabel();
						jPanel1.add(shelfIDLabel01, BorderLayout.WEST);
						shelfIDLabel01.setText("smartshelf01");
					}
					{
						shelfIDLabel02 = new JLabel();
						jPanel1.add(shelfIDLabel02, BorderLayout.EAST);
						shelfIDLabel02.setName("shelfIDLabel02");
						;
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
						imageOutput01 = new JPanel();
						contentPanel.add(imageOutput01);
						GridLayout jPanel2Layout = new GridLayout(5, 5);
						jPanel2Layout.setHgap(5);
						jPanel2Layout.setVgap(5);
						jPanel2Layout.setColumns(5);
						jPanel2Layout.setRows(5);
						imageOutput01.setPreferredSize(new java.awt.Dimension(358,186));
						imageOutput01.setLayout(jPanel2Layout);
						imageOutput01.setSize(61, 240);
						imageOutput01.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					}
					{
						imageOutput02 = new JPanel();
						contentPanel.add(imageOutput02);
						GridLayout imageOutputLayout1 = new GridLayout(5, 5);
						imageOutputLayout1.setColumns(5);
						imageOutputLayout1.setHgap(5);
						imageOutputLayout1.setVgap(5);
						imageOutputLayout1.setRows(5);
						imageOutput02.setLayout(imageOutputLayout1);
						imageOutput02.setPreferredSize(new java.awt.Dimension(
								358, 186));
						imageOutput02.setSize(61, 240);
						imageOutput02.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					}
				}
				{
					statusPanel = new JPanel();
					FlowLayout statusPanelLayout = new FlowLayout();
					appPanel.add(statusPanel, BorderLayout.SOUTH);
					statusPanel.setLayout(statusPanelLayout);
					{
						tagCount01 = new JTextField();
						statusPanel.add(tagCount01);
						tagCount01.setPreferredSize(new java.awt.Dimension(120,
								20));
						tagCount01.setName("tagCount01");
						tagCount01.setEditable(false);
					}
					{
						tagCount02 = new JTextField();
						statusPanel.add(tagCount02);
						tagCount02.setEditable(false);
						tagCount02.setPreferredSize(new java.awt.Dimension(120, 20));
						tagCount02.setName("tagCount02");
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

	public void update01(String[] tags) {
		System.out.println("SmartshelfRemoteGUI.update()");

		imageOutput01.removeAll();
		
		Icon image;
		for (String rfidTag : tags) {
			image = ShelfUtil.getInstance().getImage(rfidTag);
			if (image != null) {
				JLabel imageLabel = new JLabel(image);
				imageOutput01.add(imageLabel);
			} else {
				imageOutput01.add(new JLabel("No Image, muddi! Tag: "
						+ rfidTag));
			}
		}
		tagCount01.setText(Integer.toString(tags.length));
		imageOutput01.validate();
		imageOutput01.repaint();
		// imageOutput01.getToolkit().sync();

	}
	
	public void update02(String[] tags) {
		System.out.println("SmartshelfRemoteGUI.update()");

		imageOutput02.removeAll();
		
		Icon image;
		for (String rfidTag : tags) {
			image = ShelfUtil.getInstance().getImage(rfidTag);
			if (image != null) {
				JLabel imageLabel = new JLabel(image);
				imageOutput02.add(imageLabel);
			} else {
				imageOutput02.add(new JLabel("No Image, muddi! Tag: "
						+ rfidTag));
			}
		}
		tagCount02.setText(Integer.toString(tags.length));
		imageOutput02.validate();
		imageOutput02.repaint();
		// imageOutput02.getToolkit().sync();

	}

	public boolean returnEvent(Event[] events) {
		
		// TODO DRECKIG!! Und es tut mir auch Leeeeiiiiiid... ;(
		try {
			for (Event event : events) {
				if (event.getEventType().equals(ShelfInventoryEventFacade.TYPE_NAME)) {
					ShelfInventoryEventFacade inventoryEvent = new ShelfInventoryEventFacade(event);
					if ("smartshelf01".equals(inventoryEvent.getShelfID())) {
						LOG.info(event.toStringComplete());
						ShelfInventoryEventFacade inventoryEventFacade = new ShelfInventoryEventFacade(event);
						String[] tags = inventoryEventFacade.getRFIDTags();
						if (tags != null) {
							this.update01(tags);
						} else {
							// TODO Umgang mit Ghost-Events...
							LOG.debug("TAGS 01 == NULL");
						}
					} else if ("smartshelf02".equals(inventoryEvent.getShelfID())) {
						LOG.info(event.toStringComplete());
						ShelfInventoryEventFacade inventoryEventFacade = new ShelfInventoryEventFacade(event);
						String[] tags = inventoryEventFacade.getRFIDTags();
						if (tags != null) {
							this.update02(tags);
						} else {
							// TODO Umgang mit Ghost-Events...
							LOG.debug("TAGS 02 == NULL");
						}
					} else if (inventoryEvent.getShelfID() != null){
						LOG.error("Unknown Shelf: " + inventoryEvent.getShelfID());
					}
					
				}
			}
		} catch (EventHeapException e) {
			LOG.error("Error returning Event", e);
		}
		
		// We want more of this... 
		return true;
	}

}
