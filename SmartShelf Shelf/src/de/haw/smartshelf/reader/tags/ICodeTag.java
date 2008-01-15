package de.haw.smartshelf.reader.tags;

public class ICodeTag extends RFIDTag {

	protected static String TYPE = "ICodeTag";

	protected int stat1;

	protected int dsfid;

	public ICodeTag() {
		super();
	}

	public ICodeTag(String id) {
		super(id);
	}

	protected void initialize() {
		this.type = TYPE;
	}

	public int getStat1() {
		return stat1;
	}

	public void setStat1(int stat1) {
		this.stat1 = stat1;
	}

	public int getDsfid() {
		return dsfid;
	}

	public void setDsfid(int dsfid) {
		this.dsfid = dsfid;
	}
}
