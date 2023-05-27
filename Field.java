import java.util.ArrayList;

import javax.swing.JTextArea;

/* Feldnummerierung:

cID   1  2  3    4  5  6    7  8  9    Block IDs
rID -------------------------------    -----------------------------  
 1 |  1  2  3 |  4  5  6 |  7  8  9 | |         |         |         |
 2 | 10 11 12 | 13 14 15 | 16 17 18 | |    1    |    2    |    3    |
 3 | 19 20 21 | 22 23 24 | 25 26 27 | |         |         |         |
   | ---------+----------+--------- | | --------+---------+-------- |
 4 | 28 29 30 | 31 32 33 | 34 35 36 | |         |         |         |
 5 | 37 38 39 | 40 41 42 | 43 44 45 | |    4    |    5    |    6    |
 6 | 46 47 48 | 49 50 51 | 52 53 54 | |         |         |         |
   | ---------+----------+--------- | | --------+---------+-------- |
 7 | 55 56 57 | 58 59 60 | 61 62 63 | |         |         |         |
 8 | 64 65 66 | 67 68 69 | 70 71 72 | |    7    |    8    |    9    |
 9 | 73 74 75 | 76 77 78 | 79 80 81 | |         |         |         |
    -------------------------------    ----------------------------- 

 */

public class Field implements Cloneable {

	private Integer fID; // Nummer des Feldes
	private Integer rID; // Zeilennummer des Feldes
	private Integer cID; // Spaltennummer des Feldes
	private Integer bID; // Blocknummer des Feldes
	private ArrayList<Integer> candidates; // Moegliche Kandidaten
	private int val; // Wert des Feldes

	// leeres Field
	public Field() {
		this.fID = 0;
		this.rID = 0;
		this.cID = 0;
		this.bID = 0;
		this.candidates = new ArrayList<Integer>();
		this.val = 0;
	}

	// Konstruktor: leeres Feld (mit ID, aber ohne Wert)
	public Field(int id) {

		// IDs
		int[] ids = getIDs(id);
		this.fID = ids[0];
		this.rID = ids[1];
		this.cID = ids[2];
		this.bID = ids[3];

		// Kandidaten
		this.candidates = new ArrayList<Integer>(9);

		// endgueltiger, finaler Wert des Feldes
		this.val = 0;
	}

	/**
	 * Konstruktor: besetzt ein Feld f mit den zugehoerigen IDs und dem Wert val
	 * 
	 * @param id
	 * @param v
	 */
	public Field(int id, int v) {

		// IDs
		int[] ids = getIDs(id);
		this.fID = ids[0];
		this.rID = ids[1];
		this.cID = ids[2];
		this.bID = ids[3];

		// Kandidaten
		this.candidates = new ArrayList<Integer>(9);

		// endgueltiger, finaler Wert des Feldes
		this.val = v;

	}

	// Getter und Setter
	public int getFID() {
		return this.fID;
	}

	public void setFID(int f) {
		this.fID = f;
	}

	public int getRID() {
		return this.rID;
	}

	public void setRID(int r) {
		this.rID = r;
	}

	public int getCID() {
		return this.cID;
	}

	public void setCID(int c) {
		this.cID = c;
	}

	public int getBID() {
		return this.bID;
	}

	public void setBID(int b) {
		this.bID = b;
	}

	public ArrayList<Integer> getCandidates() {
		return this.candidates;
	}

	public void setCandidates(ArrayList<Integer> c) {
		this.candidates = c;
	}

	public int getVal() {
		return this.val;
	}

	public void setVal(int v) {
		this.val = v;
	}

	// Zeilen-ID, Spalten-ID und Block-ID aus
	// uebergebener Feld-ID bestimmen
	private static int[] getIDs(int f) {

		// Reihenfolge: {fID , rID , cID , bID}
		int[] ids = new int[4];

		// Feld-ID
		ids[0] = f;

		// Zeilen-ID
		ids[1] = (int) Math.ceil((Double.valueOf(f) / 9));

		// Spalten-ID
		ids[2] = (int) (f % 9);
		if (ids[2] == 0) {
			ids[2] = 9;
		}

		// Block-ID
		if      ((1 <= ids[1] && ids[1] <= 3) && (1 <= ids[2] && ids[2] <= 3)) ids[3] = 1;
		else if ((1 <= ids[1] && ids[1] <= 3) && (4 <= ids[2] && ids[2] <= 6)) ids[3] = 2;
		else if ((1 <= ids[1] && ids[1] <= 3) && (7 <= ids[2] && ids[2] <= 9)) ids[3] = 3;
		else if ((4 <= ids[1] && ids[1] <= 6) && (1 <= ids[2] && ids[2] <= 3)) ids[3] = 4;
		else if ((4 <= ids[1] && ids[1] <= 6) && (4 <= ids[2] && ids[2] <= 6)) ids[3] = 5;
		else if ((4 <= ids[1] && ids[1] <= 6) && (7 <= ids[2] && ids[2] <= 9)) ids[3] = 6;
		else if ((7 <= ids[1] && ids[1] <= 9) && (1 <= ids[2] && ids[2] <= 3)) ids[3] = 7;
		else if ((7 <= ids[1] && ids[1] <= 9) && (4 <= ids[2] && ids[2] <= 6)) ids[3] = 8;
		else if ((7 <= ids[1] && ids[1] <= 9) && (7 <= ids[2] && ids[2] <= 9)) ids[3] = 9;

		return ids;
	}

	public void printField(JTextArea ta) {
		SudokuMain.appendTextAndScrollDown(ta, "(" + this.rID + "," + this.cID + ") : " + this.val + " , " + this.candidates.toString());

	}

	@Override
	public Field clone() {
		Field f = new Field();
		f.setFID(this.fID);
		f.setRID(this.rID);
		f.setCID(this.cID);
		f.setBID(this.bID);
		f.setCandidates(new ArrayList<Integer>());
		f.setVal(this.val);
		return f;
	}

}
