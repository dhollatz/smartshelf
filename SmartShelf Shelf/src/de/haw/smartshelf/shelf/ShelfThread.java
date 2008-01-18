package de.haw.smartshelf.shelf;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

import de.haw.smartshelf.reader.tags.RFIDTag;

public class ShelfThread extends Thread implements Runnable {

	Shelf shelf;

	public ShelfThread(Shelf aShelf) {
		this.shelf = aShelf;
		setDaemon(true);
	}

	@Override
	public void run() {
		String out;
		int len;
		while (!isInterrupted()) {
			out = "";
			shelf.setTags(shelf.getAllItems());
			for (RFIDTag tag : shelf.getTags()) {
				out += tag.getId() + "\n";
			}
			try {
				len = shelf.getDoc().getLength();
				shelf.getDoc().remove(0, len);
				shelf.getDoc().insertString(0, out, new SimpleAttributeSet());
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				Thread.sleep(shelf.getUpdateInterval());
			} catch (InterruptedException e) {
				interrupt();
			}
		}
	}
	
	

}
