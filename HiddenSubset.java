import java.util.ArrayList;
import java.util.List;

public class HiddenSubset {
	
	private ArrayList<Field> fields;
	
	private List<Integer> candidates;
	
	private String type;
	
	private int unitNumber;
	
	public HiddenSubset(ArrayList<Field> f, List<Integer> c, String t) {
		this.fields = f;
		this.candidates = c;
		this.type = t;
		if 		(t == "Row") this.unitNumber = this.fields.get(0).getRID();
		else if (t == "Col") this.unitNumber = this.fields.get(0).getCID();
		else if (t == "Block") this.unitNumber = this.fields.get(0).getBID();
		else this.unitNumber = Integer.MIN_VALUE;
	}

	public ArrayList<Field> getFields() {
		return this.fields;
	}

	public void setFields(ArrayList<Field> f) {
		this.fields = f;
	}

	public List<Integer> getCandidates() {
		return this.candidates;
	}

	public void setCandidates(ArrayList<Integer> c) {
		this.candidates = c;
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
