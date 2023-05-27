public class NakedTriple {

	private Field field1;
	private Field field2;
	private Field field3;

	private int candidate1;
	private int candidate2;
	private int candidate3;

	private String type;
	private int unitNumber;
	
	public NakedTriple(Field f1, Field f2, Field f3, int c1, int c2, int c3, String t) {
		this.field1 = f1;
		this.field2 = f2;
		this.field3 = f3;
		this.candidate1 = c1;
		this.candidate2 = c2;
		this.candidate3 = c3;
		this.type = t;
		if 		(t == "Row") this.unitNumber = this.field1.getRID();
		else if (t == "Col") this.unitNumber = this.field1.getCID();
		else if (t == "Block") this.unitNumber = this.field1.getBID();
		else this.unitNumber = Integer.MIN_VALUE;
	}

	public Field getField1() {
		return this.field1;
	}

	public void setField1(Field f1) {
		this.field1 = f1;
	}

	public Field getField2() {
		return this.field2;
	}

	public void setField2(Field f2) {
		this.field2 = f2;
	}

	public Field getField3() {
		return this.field3;
	}

	public void setField3(Field f3) {
		this.field3 = f3;
	}

	public int getCandidate1() {
		return this.candidate1;
	}

	public void setCandidate1(int c1) {
		this.candidate1 = c1;
	}

	public int getCandidate2() {
		return this.candidate2;
	}

	public void setCandidate2(int c2) {
		this.candidate2 = c2;
	}

	public int getCandidate3() {
		return this.candidate3;
	}

	public void setCandidate3(int c3) {
		this.candidate3 = c3;
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
