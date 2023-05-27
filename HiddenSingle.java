public class HiddenSingle {
	
	private Field field;
	
	private int candidate;
	
	private String type;

	private int unitNumber;

	public HiddenSingle(Field f, int c, String t) {
		this.field = f;
		this.candidate = c;
		this.type = t;
		if 		(t == "Row") this.unitNumber = this.field.getRID();
		else if (t == "Col") this.unitNumber = this.field.getCID();
		else if (t == "Block") this.unitNumber = this.field.getBID();
		else this.unitNumber = Integer.MIN_VALUE;
	}

	public Field getField() {
		return this.field;
	}

	public void setField(Field f) {
		this.field = f;
	}

	public int getCandidate() {
		return this.candidate;
	}

	public void setCandidate(int c) {
		this.candidate = c;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String t) {
		this.type = t;
	}

	public int getUnitNumber() {
		return this.unitNumber;
	}

	public void setUnitNumber(int n) {
		this.unitNumber = n;
	}

}
