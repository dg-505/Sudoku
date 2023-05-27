import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

public class Sudoku {

	private JTextArea logTextArea;
	private Field[][] grid;
	private ArrayList<Integer> foundInRunNo = new ArrayList<Integer>();
	private ArrayList<String> foundByType = new ArrayList<String>();

	// leerer Konstruktor
	public Sudoku() {
		this.createEmptyArray();
	}

	// Sudoku ueber Field-IDs erstellen
	public Sudoku(ArrayList<Integer> fields, ArrayList<Integer> vals) {

		// leeres Sudoku erstellen
		this.grid = this.createEmptyArray();

		// Sudoku mit uebergebenen Werten fuellen
		for (int i = 0; i < fields.size(); i++) {
			Field field = new Field(fields.get(i), vals.get(i));
			this.grid[field.getRID() - 1][field.getCID() - 1] = field;
		}

		// Kandidaten ausfuellen
		for (Field[] r : this.grid) {
			for (Field c : r) {
				c.setCandidates(this.findCandidatesForField(c));
			}
		}
	}

	// Sudoku ueber Koordinaten erstellen
	public Sudoku(int[][] start) {

		// leeres Sudoku erstellen
		this.grid = this.createEmptyArray();

		// Sudoku fuellen
		for (int[] u : start) {
			this.grid[u[0] - 1][u[1] - 1].setVal(u[2]);
		}

		// Kandidaten ausfuellen
		for (Field[] r : this.grid) {
			for (Field c : r) {
				c.setCandidates(this.findCandidatesForField(c));
			}
		}
	}

	// leeres Sudoku erstellen
	public Field[][] createEmptyArray() {
		this.grid = new Field[9][9];
		int id = 1;
		for (int r = 1; r <= 9; r++) {
			Field[] row = this.grid[r - 1];
			for (int c = 1; c <= 9; c++) {
				row[c - 1] = new Field(id);
				this.grid[r - 1][c - 1] = new Field(id);
				id++;
			}
		}
		this.foundInRunNo = new ArrayList<Integer>();
		this.foundByType = new ArrayList<String>();
		return this.grid;
	}

	public Field[][] getArray() {
		return this.grid;
	}

	public void setArray(Field[][] arr) {
		this.grid = arr;
	}

	public JTextArea getLogTextArea() {
		return this.logTextArea;
	}

	public void setLogTextArea(JTextArea ta) {
		this.logTextArea = ta;
	}

	public ArrayList<Integer> getFoundInRunNo() {
		return this.foundInRunNo;
	}

	public void setFoundInRunNo(ArrayList<Integer> n) {
		this.foundInRunNo = n;
	}

	public ArrayList<String> getFoundByType() {
		return this.foundByType;
	}

	public void setFoundByType(ArrayList<String> s) {
		this.foundByType = s;
	}

	public Field getFieldByFieldID(int i) {
		switch (i) {
		case  1: return getFieldbyCoord(1,1);
		case  2: return getFieldbyCoord(1,2);
		case  3: return getFieldbyCoord(1,3);
		case  4: return getFieldbyCoord(1,4);
		case  5: return getFieldbyCoord(1,5);
		case  6: return getFieldbyCoord(1,6);
		case  7: return getFieldbyCoord(1,7);
		case  8: return getFieldbyCoord(1,8);
		case  9: return getFieldbyCoord(1,9);
		case 10: return getFieldbyCoord(2,1);
		case 11: return getFieldbyCoord(2,2);
		case 12: return getFieldbyCoord(2,3);
		case 13: return getFieldbyCoord(2,4);
		case 14: return getFieldbyCoord(2,5);
		case 15: return getFieldbyCoord(2,6);
		case 16: return getFieldbyCoord(2,7);
		case 17: return getFieldbyCoord(2,8);
		case 18: return getFieldbyCoord(2,9);
		case 19: return getFieldbyCoord(3,1);
		case 20: return getFieldbyCoord(3,2);
		case 21: return getFieldbyCoord(3,3);
		case 22: return getFieldbyCoord(3,4);
		case 23: return getFieldbyCoord(3,5);
		case 24: return getFieldbyCoord(3,6);
		case 25: return getFieldbyCoord(3,7);
		case 26: return getFieldbyCoord(3,8);
		case 27: return getFieldbyCoord(3,9);
		case 28: return getFieldbyCoord(4,1);
		case 29: return getFieldbyCoord(4,2);
		case 30: return getFieldbyCoord(4,3);
		case 31: return getFieldbyCoord(4,4);
		case 32: return getFieldbyCoord(4,5);
		case 33: return getFieldbyCoord(4,6);
		case 34: return getFieldbyCoord(4,7);
		case 35: return getFieldbyCoord(4,8);
		case 36: return getFieldbyCoord(4,9);
		case 37: return getFieldbyCoord(5,1);
		case 38: return getFieldbyCoord(5,2);
		case 39: return getFieldbyCoord(5,3);
		case 40: return getFieldbyCoord(5,4);
		case 41: return getFieldbyCoord(5,5);
		case 42: return getFieldbyCoord(5,6);
		case 43: return getFieldbyCoord(5,7);
		case 44: return getFieldbyCoord(5,8);
		case 45: return getFieldbyCoord(5,9);
		case 46: return getFieldbyCoord(6,1);
		case 47: return getFieldbyCoord(6,2);
		case 48: return getFieldbyCoord(6,3);
		case 49: return getFieldbyCoord(6,4);
		case 50: return getFieldbyCoord(6,5);
		case 51: return getFieldbyCoord(6,6);
		case 52: return getFieldbyCoord(6,7);
		case 53: return getFieldbyCoord(6,8);
		case 54: return getFieldbyCoord(6,9);
		case 55: return getFieldbyCoord(7,1);
		case 56: return getFieldbyCoord(7,2);
		case 57: return getFieldbyCoord(7,3);
		case 58: return getFieldbyCoord(7,4);
		case 59: return getFieldbyCoord(7,5);
		case 60: return getFieldbyCoord(7,6);
		case 61: return getFieldbyCoord(7,7);
		case 62: return getFieldbyCoord(7,8);
		case 63: return getFieldbyCoord(7,9);
		case 64: return getFieldbyCoord(8,1);
		case 65: return getFieldbyCoord(8,2);
		case 66: return getFieldbyCoord(8,3);
		case 67: return getFieldbyCoord(8,4);
		case 68: return getFieldbyCoord(8,5);
		case 69: return getFieldbyCoord(8,6);
		case 70: return getFieldbyCoord(8,7);
		case 71: return getFieldbyCoord(8,8);
		case 72: return getFieldbyCoord(8,9);
		case 73: return getFieldbyCoord(9,1);
		case 74: return getFieldbyCoord(9,2);
		case 75: return getFieldbyCoord(9,3);
		case 76: return getFieldbyCoord(9,4);
		case 77: return getFieldbyCoord(9,5);
		case 78: return getFieldbyCoord(9,6);
		case 79: return getFieldbyCoord(9,7);
		case 80: return getFieldbyCoord(9,8);
		default: return getFieldbyCoord(9,9);
		}
	}

	public Field getFieldbyCoord(int r, int c) {
		return this.grid[r - 1][c - 1];
	}

	public Field[] getRowByRowID(int rID) {
		return this.grid[rID - 1];
	}

	public Field[] getRowByFieldID(int fID) {
		if      ( 1 <= fID && fID <=  9) return getRowByRowID(1);
		else if (10 <= fID && fID <= 18) return getRowByRowID(2);
		else if (19 <= fID && fID <= 27) return getRowByRowID(3);
		else if (28 <= fID && fID <= 36) return getRowByRowID(4);
		else if (37 <= fID && fID <= 45) return getRowByRowID(5);
		else if (46 <= fID && fID <= 54) return getRowByRowID(6);
		else if (55 <= fID && fID <= 63) return getRowByRowID(7);
		else if (64 <= fID && fID <= 72) return getRowByRowID(8);
		else                             return getRowByRowID(9);
	}

	public Field[] getColByColID(int cID) {
		Field[] col = new Field[9];
		for (int r = 1; r <= 9; r++) {
			col[r-1] = this.grid[r-1][cID - 1];
		}
		return col;
	}

	public Field[] getColByFieldID(int fID) {
		if      ((fID - 1) % 9 == 0) return getColByColID(1);
		else if ((fID - 2) % 9 == 0) return getColByColID(2);
		else if ((fID - 3) % 9 == 0) return getColByColID(3);
		else if ((fID - 4) % 9 == 0) return getColByColID(4);
		else if ((fID - 5) % 9 == 0) return getColByColID(5);
		else if ((fID - 6) % 9 == 0) return getColByColID(6);
		else if ((fID - 7) % 9 == 0) return getColByColID(7);
		else if ((fID - 8) % 9 == 0) return getColByColID(8);
		else                         return getColByColID(9);
	}

	// get the fields of the block by using the upper left field of the block
	public Field[] getBlockByUpperLeftField(int r0, int c0) {
		Field[] Block = {
				this.grid[r0    ][c0], this.grid[r0    ][c0 + 1], this.grid[r0    ][c0 + 2],
				this.grid[r0 + 1][c0], this.grid[r0 + 1][c0 + 1], this.grid[r0 + 1][c0 + 2],
				this.grid[r0 + 2][c0], this.grid[r0 + 2][c0 + 1], this.grid[r0 + 2][c0 + 2]
		};
		return Block;
	}

	public Field[] getBlockByBlockID(int bID) {
		if      (bID == 1) return getBlockByUpperLeftField(0, 0);
		else if (bID == 2) return getBlockByUpperLeftField(0, 3);
		else if (bID == 3) return getBlockByUpperLeftField(0, 6);
		else if (bID == 4) return getBlockByUpperLeftField(3, 0);
		else if (bID == 5) return getBlockByUpperLeftField(3, 3);
		else if (bID == 6) return getBlockByUpperLeftField(3, 6);
		else if (bID == 7) return getBlockByUpperLeftField(6, 0);
		else if (bID == 8) return getBlockByUpperLeftField(6, 3);
		else               return getBlockByUpperLeftField(6, 6);
	}

	public Field[] getBlockByFieldID(int fID) {
		if      (fID ==  1 || fID ==  2 || fID ==  3 || fID == 10 || fID == 11 || fID == 12 || fID == 19 || fID == 20 || fID == 21) return getBlockByBlockID(1);
		else if (fID ==  4 || fID ==  5 || fID ==  6 || fID == 13 || fID == 14 || fID == 15 || fID == 22 || fID == 23 || fID == 24) return getBlockByBlockID(2);
		else if (fID ==  7 || fID ==  8 || fID ==  9 || fID == 16 || fID == 17 || fID == 18 || fID == 25 || fID == 26 || fID == 27) return getBlockByBlockID(3);
		else if (fID == 28 || fID == 29 || fID == 30 || fID == 37 || fID == 38 || fID == 39 || fID == 46 || fID == 47 || fID == 48) return getBlockByBlockID(4);
		else if (fID == 31 || fID == 32 || fID == 33 || fID == 40 || fID == 41 || fID == 42 || fID == 49 || fID == 50 || fID == 51) return getBlockByBlockID(5);
		else if (fID == 34 || fID == 35 || fID == 36 || fID == 43 || fID == 44 || fID == 45 || fID == 52 || fID == 53 || fID == 54) return getBlockByBlockID(6);
		else if (fID == 55 || fID == 56 || fID == 57 || fID == 64 || fID == 65 || fID == 66 || fID == 73 || fID == 74 || fID == 75) return getBlockByBlockID(7);
		else if (fID == 58 || fID == 59 || fID == 60 || fID == 67 || fID == 68 || fID == 69 || fID == 76 || fID == 77 || fID == 78) return getBlockByBlockID(8);
		else                                                                                                                        return getBlockByBlockID(9);
	}

	public ArrayList<Field> getFreeFields() {
		ArrayList<Field> freeFields = new ArrayList<Field>();
		for (Field[] r : this.grid) {
			for (Field f : r) {
				if (f.getVal() == 0) {
					freeFields.add(f);
				}
			}
		}
		return freeFields;
	}

	public ArrayList<Integer> findCandidatesForField(Field f) {
		// Wenn Feld hat bereits Wert: keine Kandidaten
		if (f.getVal() != 0) return new ArrayList<Integer>();
		// Anfangs: Zahlen 1-9 als Kandidaten moegelich
		// dann: Zeile, Spalte, Block durchgehen
		// und bereits vorhandene Zahlen aus cands loeschen
		ArrayList<Integer> cands = new ArrayList<Integer>();
		for (int i = 1; i <= 9; i++) {
			cands.add(i);
		}
		// Zeile
		Field[] row = this.getRowByFieldID(f.getFID());
		for (Field r : row) {
			int val = r.getVal();
			if (val != 0) {
				cands.remove(Integer.valueOf(val));
			}
		}
		// Spalte
		Field[] col = this.getColByFieldID(f.getFID());
		for (Field c : col) {
			int val = c.getVal();
			if (val != 0) {
				cands.remove(Integer.valueOf(val));
			}
		}
		// Block
		Field[] block = this.getBlockByFieldID(f.getFID());
		for (Field b : block) {
			int val = b.getVal();
			if (val != 0) {
				cands.remove(Integer.valueOf(val));
			}
		}
		return cands;
	}

	public void filldAndEliminate(Field f) {
		// get the Field at row r and col c
		//		Field f = this.getFieldbyCoord(r, c);
		// set the value of f to val
		if (f.getCandidates().size() > 1) {
			SudokuMain.appendTextAndScrollDown(logTextArea, "Error filling Field (" + f.getRID() + "," + f.getCID() + "): Field has more than one candidate!");
			JOptionPane.showMessageDialog(logTextArea, "<html><center>Error filling Field (" + f.getRID() + "," + f.getCID() + ")!<br><br>Field has more than one candidate.</center></html>", "Error filling Field (" + f.getRID() + "," + f.getCID() + ")", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int val = f.getCandidates().get(0);
		f.setVal(val);
		// eliminate canidate "val" from units :
		for (Field r : this.getRowByFieldID(f.getFID())) r.getCandidates().remove(Integer.valueOf(val)); // Rows
		for (Field c : this.getColByFieldID(f.getFID())) c.getCandidates().remove(Integer.valueOf(val)); // Cols
		for (Field b : this.getBlockByFieldID(f.getFID())) b.getCandidates().remove(Integer.valueOf(val)); // Blocks
		this.getFreeFields().remove(f);
	}

	public int countCandidates() {
		int numCands = 0;
		for (Field[] row : this.grid) {
			for (Field col : row) {
				numCands += col.getCandidates().size();
			}
		}
		return numCands;
	}

	// NakedSingle methods
	public void processNakedSingles(int run, ArrayList<Field[][]> steps) {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "Search for NakedSingles");
		while (true) {
			Field firstNakedSingle = this.firstNakedSingle();
			if (firstNakedSingle == null) break;
			this.filldAndEliminate(firstNakedSingle);
			String msg = "NakedSingle {" + firstNakedSingle.getVal() + "} in Field (" + firstNakedSingle.getRID() + "," + firstNakedSingle.getCID() + ")";
			SudokuMain.appendTextAndScrollDown(this.logTextArea, msg);
			this.addStepToList(steps, run, msg);
		}
//		this.print();
//		this.printFields();
	}

	public Field firstNakedSingle() {
		for (Field f : this.getFreeFields()) {
			if (f.getCandidates().size() == 1) {
				return f;
			}
		}
		return null;
	}

	// HiddenSingle methods
	public void processHiddenSingles(int run, ArrayList<Field[][]> steps) {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nSearch for HiddenSingles");
		while (true) {
			HiddenSingle firstHiddenSingle = this.firstHiddenSingle();
			if (firstHiddenSingle == null) break;
			firstHiddenSingle.getField().setCandidates(new ArrayList<Integer>(Arrays.asList(firstHiddenSingle.getCandidate())));
			this.filldAndEliminate(firstHiddenSingle.getField());
			String msg = "HiddenSingle {" + firstHiddenSingle.getField().getVal() +
					"} in " + firstHiddenSingle.getType() + " " + firstHiddenSingle.getUnitNumber() + ": Field ("
					+ firstHiddenSingle.getField().getRID() + "," + firstHiddenSingle.getField().getCID() + ")";
			this.addStepToList(steps, run, msg);
		
			SudokuMain.appendTextAndScrollDown(this.logTextArea, msg);
		}
//		this.print();
//		this.printFields();
	}

	public HiddenSingle firstHiddenSingle() {
		// Reihenfolge: Erst Reihen, dann Spalten, dann Bloecke
		// nach HiddenSingle durchsuchen
		for (String type : new String[] {"Row", "Col", "Block"}) {
			for (int u = 1; u <= 9; u++) {
				Field[] unit = null;
				if 		(type == "Row") unit = this.getRowByRowID(u);
				else if (type == "Col") unit = this.getColByColID(u);
				else if (type == "Block") unit = this.getBlockByBlockID(u);
				int[] n = this.candidateOccurrencesInUnit(unit);
				// Finde Zahlen i, die nur 1x vorkommen
				for (int i = 1; i <= 9; i++) {
					if (n[i - 1] == 1) {
						// Pruefe Felder in Reihe i: Welches Feld enthaelt Zahl i?
						for (Field f : unit) {
							if (f.getCandidates().contains(Integer.valueOf(i))) {
								// Kandidat fuer Feld f ist nur Zahl i
								return new HiddenSingle(f, i, type);
							}
						}
					}
				}
			}
		}
		return null;
	}

	public int[] candidateOccurrencesInUnit(Field[] unit) {
		int[] n = new int[9];
		for (int i = 1; i <= 9; i++) {
			int c = 0;
			for (Field f : unit) {
				if (f.getCandidates().contains(i)) {
					c++;
				}
			}
			n[i-1] = c;
		}
		return n;
	}

	// NakedPair Methods
	public void processNakedPairs(int run, ArrayList<Field[][]> steps) {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nSearch for NakedPairs");
		ArrayList<NakedPair> deadNakedPairs = new ArrayList<NakedPair>();
		while (true) {
			NakedPair firstNakedPair = this.firstNakedPair(deadNakedPairs);
			if (firstNakedPair == null) break;
			int numCandsBeforeEliminatingFirstNakedPair = this.countCandidates();
			this.eliminateNakedPair(firstNakedPair);
			if (this.countCandidates() < numCandsBeforeEliminatingFirstNakedPair) {
				String msg = "NakedPair {" +
						firstNakedPair.getCandidate1() + "," + firstNakedPair.getCandidate2() + "} in " + firstNakedPair.getType() + " " + firstNakedPair.getUnitNumber() + ": Fields (" +
						firstNakedPair.getField1().getRID() + "," + firstNakedPair.getField1().getCID() + ");(" + firstNakedPair.getField2().getRID() + "," + firstNakedPair.getField2().getCID() + ")";
				this.addStepToList(steps, run, msg);
				SudokuMain.appendTextAndScrollDown(logTextArea, msg);
				
			}
		}
//		this.print();
//		this.printFields();
	}

	private NakedPair firstNakedPair(ArrayList<NakedPair> deadNakedPairs) {
		for (String type : new String[] {"Row", "Col", "Block"}) {
			for (int u = 1; u <= 9; u++) { // go over all units
				Field[] unit = null;
				if 		(type == "Row") unit = this.getRowByRowID(u);
				else if (type == "Col") unit = this.getColByColID(u);
				else if (type == "Block") unit = this.getBlockByBlockID(u);
				int numFreeFieldsInUnit = 0; // In der jeweiligen Unit muessen > 2 freie Felder sein
				for (Field f : unit) if (f.getVal()==0) numFreeFieldsInUnit++;
				for (int i=1; i<=8; i++) { // Alle Zahlenpaare {1,2} bis {8,9}
					for (int j=i+1; j<=9; j++) {
						for (int k=1; k<=8; k++) { // Alle Feld-Kombinationen in unit
							for (int l=k+1; l<=9; l++) {
								Field fk = unit[k-1];
								Field fl = unit[l-1];
								// Kandidatenlisten der beiden Felder muessen Laenge 2 haben und die einzigen Kandidaten muessen die Zahlen i und j sein
								if ((fk.getCandidates().size()==2) &&
										(fl.getCandidates().size()==2) &&
										(fk.getCandidates().containsAll(Arrays.asList(i,j))) &&
										(fl.getCandidates().containsAll(Arrays.asList(i,j))) &&
									  numFreeFieldsInUnit > 2) {
									NakedPair firstNakedPair = new NakedPair(fk,fl, i,j, type);
									boolean isDead = false;
									for (NakedPair dead : deadNakedPairs) {
										if (dead.getField1() == firstNakedPair.getField1() && dead.getField2() == firstNakedPair.getField2() && dead.getType().equals(firstNakedPair.getType())) {
											isDead = true;
										}
									}
									if (!isDead) {
										deadNakedPairs.add(firstNakedPair);
										return firstNakedPair;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public void eliminateNakedPair(NakedPair nakedPair) {
		if (nakedPair.getField1().getRID() == nakedPair.getField2().getRID()) this.eliminateCandidatesOfNakedPairInUnit(nakedPair, this.getRowByFieldID(nakedPair.getField1().getFID()));
		if (nakedPair.getField1().getCID() == nakedPair.getField2().getCID()) this.eliminateCandidatesOfNakedPairInUnit(nakedPair, this.getColByFieldID(nakedPair.getField1().getFID()));
		if (nakedPair.getField1().getBID() == nakedPair.getField2().getBID()) this.eliminateCandidatesOfNakedPairInUnit(nakedPair, this.getBlockByFieldID(nakedPair.getField1().getFID()));
	}

	public void eliminateCandidatesOfNakedPairInUnit(NakedPair nakedPair, Field[] unit) {
		for (Field f : unit) {
			if ((f.getFID() == nakedPair.getField1().getFID()) || (f.getFID() == nakedPair.getField2().getFID()) || (f.getVal() != 0)) continue;
			f.getCandidates().removeAll(nakedPair.getField1().getCandidates());
		}
	}

	// HiddenPair methods
	public void processHiddenPairs(int run, ArrayList<Field[][]> steps) {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nSearch for HiddenPairs");
		while (true) {
			HiddenSubset firstHiddenPair = this.firstHiddenPair();
			if (firstHiddenPair == null) break;
			int numCandsBeforeEliminatingHiddenPair = this.countCandidates();
			List<Integer> hiddenPairCanidates = firstHiddenPair.getCandidates();
			firstHiddenPair.getFields().get(0).getCandidates().retainAll(hiddenPairCanidates);
			firstHiddenPair.getFields().get(1).getCandidates().retainAll(hiddenPairCanidates);
			if (this.countCandidates() < numCandsBeforeEliminatingHiddenPair) {
				String msg = "HiddenPair {" +
						firstHiddenPair.getCandidates().get(0) + "," + firstHiddenPair.getCandidates().get(1) + "} in " + firstHiddenPair.getType() + " " + firstHiddenPair.getUnitNumber() + ": Fields (" +
						firstHiddenPair.getFields().get(0).getRID() + "," + firstHiddenPair.getFields().get(0).getCID() + ");(" + firstHiddenPair.getFields().get(1).getRID() + "," + firstHiddenPair.getFields().get(1).getCID() + ")";
				this.addStepToList(steps, run, msg);
				SudokuMain.appendTextAndScrollDown(logTextArea, msg);
			}
		}
//		this.print();
//		this.printFields();
	}

	public HiddenSubset firstHiddenPair() {
		for (String type : new String[] {"Row", "Col", "Block"}) {
			for (int u = 1; u <= 9; u++) { // go over all units
				Field[] unit = null;
				if 		(type == "Row") unit = this.getRowByRowID(u);
				else if (type == "Col") unit = this.getColByColID(u);
				else if (type == "Block") unit = this.getBlockByBlockID(u);
				int[] candidateOccurrences = candidateOccurrencesInUnit(unit);
				for (int i=1; i<=8; i++) { // Alle Zahlenpaare {1,2} bis {8,9}
					for (int j=i+1; j<=9; j++) {
						// Wenn i und j genau 2 mal vorkommen
						if (candidateOccurrences[i-1] == 2 && candidateOccurrences[j-1] == 2) {
							// Finde Felder, in denen i und j vorkommen
							ArrayList<Field> hiddenPairFields = new ArrayList<Field>();
							for (Field f : unit) {
								if (f.getCandidates().contains(i) && f.getCandidates().contains(j)) {
									hiddenPairFields.add(f);
								}
							}
							if (hiddenPairFields.size() == 2 &&  hiddenPairFields.get(0) != null && hiddenPairFields.get(1) != null && (hiddenPairFields.get(0).getCandidates().size()>2 || hiddenPairFields.get(1).getCandidates().size()>2)) {
								List<Integer> cands = List.of(i,j);
								HiddenSubset firstHiddenPair = new HiddenSubset(hiddenPairFields, cands, type);
								return firstHiddenPair;
//								return new HiddenSubset(hiddenPairFields, List.of(i,j), type);
							}
						}
					}
				}
			}
		}
		
		return null;
	}

	// NakedTriple methods
	private void processNakedTriples(int run, ArrayList<Field[][]> steps) {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nSearch for NakedTriples");
		ArrayList<NakedTriple> deadNakedTriples = new ArrayList<NakedTriple>();
		while (true) {
			NakedTriple firstNakedTriple = this.firstNakedTriple(deadNakedTriples);
			if (firstNakedTriple == null) break;
			int numCandsBeforeEliminatingFirstNakedTriple = this.countCandidates();
			this.eliminateNakedTriple(firstNakedTriple);
			if (this.countCandidates() < numCandsBeforeEliminatingFirstNakedTriple) {
				String msg = "NakedTriple {" +
						firstNakedTriple.getCandidate1() + "," + firstNakedTriple.getCandidate2() + "," + firstNakedTriple.getCandidate3() + "} in " + firstNakedTriple.getType() + " " + firstNakedTriple.getUnitNumber() + ": Fields (" +
						firstNakedTriple.getField1().getRID() + "," + firstNakedTriple.getField1().getCID() + ");(" + firstNakedTriple.getField2().getRID() + "," + firstNakedTriple.getField2().getCID() + ");(" + firstNakedTriple.getField3().getRID() + "," + firstNakedTriple.getField3().getCID() + ")";
				this.addStepToList(steps, run, msg);
				SudokuMain.appendTextAndScrollDown(logTextArea, msg);
			}
			if (this.firstNakedSingle() != null || this.firstHiddenSingle()!= null ) return;
		}
//		this.print();
//		this.printFields();
	}

	private NakedTriple firstNakedTriple(ArrayList<NakedTriple> deadNakedTriples) {
		for (String type : new String[] {"Row", "Col", "Block"}) {
			for (int u = 1; u <= 9; u++) { // go over all units
				Field[] unit = null;
				if 		(type == "Row") unit = this.getRowByRowID(u);
				else if (type == "Col") unit = this.getColByColID(u);
				else if (type == "Block") unit = this.getBlockByBlockID(u);
				int numFreeFieldsInUnit = 0; // In der jeweiligen Unit muessen > 3 freie Felder sein
				for (Field f : unit) if (f.getVal()==0) numFreeFieldsInUnit++;
				for (int i=1; i<=7; i++) { // Alle Zahlen-Tripel {1,2,3} bis {7,8,9}
					for (int j=i+1; j<=8; j++) {
						for (int k=j+1; k<=9; k++) {
							// Kandidatenlisten aller drei Felder duerfen ausschliesslich die Zahlen i,j,k enthalten
							ArrayList<Integer> nonMatchCands = new ArrayList<Integer>();
							for (int c=1; c<=9; c++) if (c!=i && c!=j && c!=k) nonMatchCands.add(c);
							for (int l=1; l<=7; l++) { // Alle Feld-Kombinationen in unit
								for (int m=l+1; m<=8; m++) {
									for (int n=m+1; n<=9; n++) {
										Field fl = unit[l-1];
										Field fm = unit[m-1];
										Field fn = unit[n-1];
										if (fl.getVal()==0 && fm.getVal()==0 && fn.getVal()==0) { 
											boolean flIsNakedTriple = true;
											for (int c : nonMatchCands) if (fl.getCandidates().contains(c)) flIsNakedTriple = false;
											boolean fmIsNakedTriple = true;
											for (int c : nonMatchCands) if (fm.getCandidates().contains(c)) fmIsNakedTriple = false;
											boolean fnIsNakedTriple = true;
											for (int c : nonMatchCands) if (fn.getCandidates().contains(c)) fnIsNakedTriple = false;
											if (flIsNakedTriple && fmIsNakedTriple && fnIsNakedTriple && numFreeFieldsInUnit > 3) {
												NakedTriple firstNakedTriple = new NakedTriple(fl,fm,fn, i,j,k, type);
												boolean isDead = false;
												for (NakedTriple dead : deadNakedTriples) {
													if (dead.getField1() == firstNakedTriple.getField1() && dead.getField2() == firstNakedTriple.getField2() && dead.getField3() == firstNakedTriple.getField3() && dead.getType().equals(firstNakedTriple.getType())) {
														isDead = true;
													}
												}
												if (!isDead) {
													deadNakedTriples.add(firstNakedTriple);
													return firstNakedTriple;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private void eliminateNakedTriple(NakedTriple nakedTriple) {
		if (nakedTriple.getField1().getRID() == nakedTriple.getField2().getRID() && nakedTriple.getField1().getRID() == nakedTriple.getField3().getRID() && nakedTriple.getField2().getRID() == nakedTriple.getField3().getRID()) this.eliminateCandidatesOfNakedTripleInUnit(nakedTriple, this.getRowByFieldID(nakedTriple.getField1().getFID()));
		if (nakedTriple.getField1().getCID() == nakedTriple.getField2().getCID() && nakedTriple.getField1().getCID() == nakedTriple.getField3().getCID() && nakedTriple.getField2().getCID() == nakedTriple.getField3().getCID()) this.eliminateCandidatesOfNakedTripleInUnit(nakedTriple, this.getColByFieldID(nakedTriple.getField1().getFID()));
		if (nakedTriple.getField1().getBID() == nakedTriple.getField2().getBID() && nakedTriple.getField1().getBID() == nakedTriple.getField3().getBID() && nakedTriple.getField2().getBID() == nakedTriple.getField3().getBID()) this.eliminateCandidatesOfNakedTripleInUnit(nakedTriple, this.getBlockByFieldID(nakedTriple.getField1().getFID()));
	}

	private void eliminateCandidatesOfNakedTripleInUnit(NakedTriple nakedTriple, Field[] unit) {
		for (Field f : unit) {
			if ((f.getFID() == nakedTriple.getField1().getFID()) || (f.getFID() == nakedTriple.getField2().getFID()) || (f.getFID() == nakedTriple.getField3().getFID()) || (f.getVal() != 0)) continue;
			f.getCandidates().removeAll(nakedTriple.getField1().getCandidates());
			f.getCandidates().removeAll(nakedTriple.getField2().getCandidates());
			f.getCandidates().removeAll(nakedTriple.getField3().getCandidates());
		}
	}

	// HiddenTriple methods
	private void processHiddenTriples(int run, ArrayList<Field[][]> steps) {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nSearch for HiddenTriples");
		while (true) {
			HiddenSubset firstHiddenTriple = this.firstHiddenTriple();
			if (firstHiddenTriple == null) break;
			int numCandsBeforeEliminatingHiddenTriple = this.countCandidates();
			List<Integer> hiddenTripleCanidates = firstHiddenTriple.getCandidates();
			for (Field f : firstHiddenTriple.getFields()) f.getCandidates().retainAll(hiddenTripleCanidates);
			if (this.countCandidates() < numCandsBeforeEliminatingHiddenTriple) {
				String msg = "HiddenTriple {" +
						firstHiddenTriple.getCandidates().get(0) + "," + firstHiddenTriple.getCandidates().get(1) + "," + firstHiddenTriple.getCandidates().get(2) + "} in " + firstHiddenTriple.getType() + " " + firstHiddenTriple.getUnitNumber() + ": Fields (" +
						firstHiddenTriple.getFields().get(0).getRID() + "," + firstHiddenTriple.getFields().get(0).getCID() + ");(" + firstHiddenTriple.getFields().get(1).getRID() + "," + firstHiddenTriple.getFields().get(1).getCID() + ");(" + firstHiddenTriple.getFields().get(2).getRID() + "," + firstHiddenTriple.getFields().get(2).getCID() + ")";
				this.addStepToList(steps, run, msg);
				SudokuMain.appendTextAndScrollDown(logTextArea, msg);
			}
			if (this.firstNakedSingle() != null || this.firstHiddenSingle()!= null ) return;
		}
//		this.print();
//		this.printFields();
	}

	private HiddenSubset firstHiddenTriple() {
		for (String type : new String[] {"Row", "Col", "Block"}) {
			for (int u = 1; u <= 9; u++) { // go over all units
				Field[] unit = null;
				if 		(type == "Row") unit = this.getRowByRowID(u);
				else if (type == "Col") unit = this.getColByColID(u);
				else if (type == "Block") unit = this.getBlockByBlockID(u);
				int[] candidateOccurrences = candidateOccurrencesInUnit(unit);
				for (int i=1; i<=7; i++) { // Alle Zahlen-Tripel {1,2,3} bis {7,8,9}
					for (int j=i+1; j<=8; j++) {
						for (int k=j+1; k<=9; k++) {
							if (candidateOccurrences[i-1]>0 && candidateOccurrences[j-1]>0 && candidateOccurrences[k-1]>0) {
								for (int l=1; l<=7; l++) { // Alle Feld-Kombinationen in unit
									for (int m=l+1; m<=8; m++) {
										loop:
										for (int n=m+1; n<=9; n++) {
											// i,j,k are only allowed in fields l,m,n
											Field fl = unit[l-1];
											Field fm = unit[m-1];
											Field fn = unit[n-1];
											if ((fl.getVal()!=0 || fm.getVal()!=0 || fn.getVal()!=0) ||
												(fl.getCandidates().size()<=3 && fm.getCandidates().size()<=3 && fn.getCandidates().size()<=3)) continue;
											boolean isHiddenTriple = true;
											for (Field f : unit) if (f!=fl && f!=fm && f!=fn) {
												if ((f.getCandidates().contains(i) || f.getCandidates().contains(j) || f.getCandidates().contains(k))) {
													isHiddenTriple = false;
													continue loop;
												}
											}
											if (isHiddenTriple) {
												return new HiddenSubset(new ArrayList<Field>(List.of(fl, fm, fn)), List.of(i,j,k), type);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	// Row-Block-Checks
	public void performRowBlockChecks(int run, ArrayList<Field[][]> steps) {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nPerform Line-Block-Checks");
		for (String type : new String[] {"Row", "Col"}) { // Do RBC with Rows and Cols
			for (int l=1; l<=9; l++) { // Iterate over each row/col
				Field[] unit = null;
				if (type == "Row") {
					unit = this.getRowByRowID(l); // Rows
				} else {
					unit = this.getColByColID(l); // Cols
				}
				for (int i=1; i<=9; i++) { // go through all numbers 1-9
					ArrayList<Field> containingCandidateI = this.findFieldsInUnitContainingCandidateI(unit, i);
					if (containingCandidateI.size() > 0 ) { // proceed only if there exists at least one field with candidate i
						int blockIdOfFirst = containingCandidateI.get(0).getBID();
						boolean allCandidatesInSameBlock = false;
						for (Field f : containingCandidateI) { // check if all Fields containing i are in the same block
							if (f.getBID() == blockIdOfFirst) {
								allCandidatesInSameBlock = true;
							} else {
								allCandidatesInSameBlock = false;
								break;
							}
						}
						if (allCandidatesInSameBlock == true) { // eliminate all other candidtes in the block
							int numCandsBeforeRBC = this.countCandidates();
							this.removeCandidateIFromUnit(i, containingCandidateI, blockIdOfFirst, "Block"); // remove candidate i from block
							if (this.countCandidates() < numCandsBeforeRBC) {
								String msg = type + "-in-Block with {" + i + "} in " + type + " " + l + ": Only in Block " + blockIdOfFirst; 
								SudokuMain.appendTextAndScrollDown(this.logTextArea, msg);
								this.addStepToList(steps, run, msg);
							}
						}
					}
				}
			}
		}
//		this.print();
//		this.printFields();
	}

	// Block-Row-Checks
	public void performBlockRowChecks(int run, ArrayList<Field[][]> steps) {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nPerform Block-Line-Checks");
		for (int b=1; b<=9; b++) {
			Field[] block = this.getBlockByBlockID(b);
			for (int i=1; i<=9; i++) {
				ArrayList<Field> containingCandidateI = findFieldsInUnitContainingCandidateI(block, i);
				if (containingCandidateI.size() > 0) {
					for (String type : new String[] {"Row", "Col"}) {
						if (type == "Row") {
							int rowIdOfFirst = containingCandidateI.get(0).getRID();
							boolean allCandidatesInSameRow = false;
							for (Field f : containingCandidateI) {
								if (f.getRID() == rowIdOfFirst) {
									allCandidatesInSameRow = true;
								} else {
									allCandidatesInSameRow = false;
									break;
								}
							}
							this.eliminateBRCfromLine(run, steps, b, i, containingCandidateI, type, rowIdOfFirst, allCandidatesInSameRow);
						} else if (type == "Col") {
							int colIdOfFirst = containingCandidateI.get(0).getCID();
							boolean allCandidatesInSameCol = false;
							for (Field f : containingCandidateI) {
								if (f.getCID() == colIdOfFirst) {
									allCandidatesInSameCol = true;
								} else {
									allCandidatesInSameCol = false;
									break;
								}
							}
							this.eliminateBRCfromLine(run, steps, b, i, containingCandidateI, type, colIdOfFirst, allCandidatesInSameCol);
						}
					}
				}
			}
		}
//		this.print();
//		this.printFields();
	}

	// Sub-methods for RBC and BRC
	public void eliminateBRCfromLine(int run, ArrayList<Field[][]> steps, int b, int i, ArrayList<Field> containingCandidateI, String type, int lineIdOfFirst, boolean allCandidatesInSameLine) {
		if (allCandidatesInSameLine == true) { // eliminate all other candidtes in the block
			int numCandsBeforeBRC = this.countCandidates();
			this.removeCandidateIFromUnit(i, containingCandidateI, lineIdOfFirst, type); // remove candidate i from line
			if (this.countCandidates() < numCandsBeforeBRC) {
				String msg = "Block-in-" + type + " with {" + i + "} in Block " + b + ": Only in " + type + " " + lineIdOfFirst;
				SudokuMain.appendTextAndScrollDown(this.logTextArea, msg);
				this.addStepToList(steps, run, msg);
			}
		}
	}

	public ArrayList<Field> findFieldsInUnitContainingCandidateI(Field[] unit, int i) {
		ArrayList<Field> containingCandidateI = new ArrayList<Field>();
		for (Field f : unit) { // collect all Fields containing candidate i
			if (f.getCandidates().contains(i)) {
				containingCandidateI.add(f);
			}
		}
		return containingCandidateI;
	}

	public void removeCandidateIFromUnit(int i, ArrayList<Field> containingCandidateI, int unitIdOfFirst, String type) {
		Field[] unit = null;
		if 		(type == "Row")   unit = this.getRowByRowID(unitIdOfFirst);
		else if (type == "Col")   unit = this.getColByColID(unitIdOfFirst);
		else if (type == "Block") unit = this.getBlockByBlockID(unitIdOfFirst);
		for (Field f : unit) {
			if (!containingCandidateI.contains(f) && f.getCandidates().contains(i)) {
				f.getCandidates().remove(Integer.valueOf(i));
			}
		}
	}

	// Last resort: try-and-error approach using recursive backtracking
	private boolean backtracking() {
		ArrayList<Field> freeFields = this.getFreeFields();
		if (freeFields.size()==0) return true; // Abbruch der Rekursion: Keine freien Felder uebrig
		Field firstFreeField = freeFields.get(0);
		for (int i=1; i<=9; i++) {
			if (!this.unitContainsVal(i, this.getRowByFieldID(firstFreeField.getFID())) &&
				!this.unitContainsVal(i, this.getColByFieldID(firstFreeField.getFID())) &&
				!this.unitContainsVal(i, this.getBlockByFieldID(firstFreeField.getFID()))) {
				firstFreeField.setCandidates(new ArrayList<Integer>(List.of(i)));
				this.filldAndEliminate(firstFreeField);
				if (this.backtracking()) return true;
				firstFreeField.setVal(0);
			}
		}
		return false;
	}

	public boolean unitContainsVal(int i, Field[] row) {
		for (Field f : row) {
			if (f.getVal() == i) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Field[][]> solve(String name) {

		int run = 0;

		this.logTextArea.setText(null);

		ArrayList<Field[][]> steps = new ArrayList<Field[][]>();

		addStepToList(steps, run, "Initial status");

		SudokuMain.appendTextAndScrollDown(this.logTextArea, "### START SOLVING SUDOKU ###");
		if (name != null) SudokuMain.appendTextAndScrollDown(this.logTextArea, "File name: \"" + name + "\"\n");
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "Initial free fields: " + this.getFreeFields().size() + "\n");

		long t0 = System.nanoTime();
		long t1 = Long.MIN_VALUE;
		
		// Main Solving Loop
		while (this.getFreeFields().size() > 0) {

			run++;

			int numCandsBeforeRun = this.countCandidates();

			SudokuMain.appendTextAndScrollDown(this.logTextArea, "BEGIN Run No. " + run + "\n");

			int numFreeFieldsBeforeSingles = this.getFreeFields().size();
			this.processNakedSingles(run, steps); // Check Naked Singles
			this.processHiddenSingles(run, steps); // Check Hidden Singles
			int numFreeFieldsAfterSingles = this.getFreeFields().size();

			// If no (more) Singles exist -> Search for Naked and Hidden Pairs
			if (this.firstNakedSingle() == null && this.firstHiddenSingle() == null && this.getFreeFields().size() > 2) {
				if (numFreeFieldsAfterSingles < numFreeFieldsBeforeSingles) {
					SudokuMain.appendTextAndScrollDown(logTextArea, "\nNo further Singles found.\nProceeding with Pairs...");
				} else {
					SudokuMain.appendTextAndScrollDown(logTextArea, "\nNo Singles found.\nProceeding with pairs...");
				}

				int numCandsBeforePairs = this.countCandidates();
				this.processNakedPairs(run, steps); // Check Naked Pairs
				this.processHiddenPairs(run, steps); // Check Hidden Pairs
				int numCandsAfterPairs = this.countCandidates();
				
				// If Pair techniques didn't produce Singles -> search for Naked and Hidden Triples
				if (this.firstNakedSingle() == null && this.firstHiddenSingle() == null && this.getFreeFields().size() > 2) {
					if (numCandsAfterPairs < numCandsBeforePairs) {
						SudokuMain.appendTextAndScrollDown(logTextArea, "\nPair techniques didn't produce Singles.\nProceeding with Triples...");
					} else {
						SudokuMain.appendTextAndScrollDown(logTextArea, "\nNo Pairs found.\nProceeding with Triples...");
					}

					int numCandsBeforeTriples = this.countCandidates();
					this.processNakedTriples(run, steps); // Check Naked Triples
					this.processHiddenTriples(run, steps); // Check Hidden Triples
					int numCandsAfterTriples = this.countCandidates();
					
					// If pair/triple techniques didn't produce Singles -> go for Row-Block-Checks and Block-Row-Checks
					if (this.firstNakedSingle() == null && this.firstHiddenSingle() == null && this.getFreeFields().size() > 2) {
						if (numCandsAfterTriples < numCandsBeforeTriples) {
							SudokuMain.appendTextAndScrollDown(logTextArea, "\nTriple techniques didn't produce Singles.\nProceeding with LockedCandidates...");
						} else {
							SudokuMain.appendTextAndScrollDown(logTextArea, "\nNo Triples found.\nProceeding with LockedCandidates...");
						}

						int numCandsBeforeLockedCands = this.countCandidates();
						this.performRowBlockChecks(run, steps); // Peform Row-Block-Checks
						this.performBlockRowChecks(run, steps); // Perfom Block-Row-Checks
						int numCandsAfterLockedCands = this.countCandidates();
						
						// If pair/triple/LockedCands didn't produce Singles -> go for further technique
						if (this.firstNakedSingle() == null && this.firstHiddenSingle() == null && this.getFreeFields().size() > 2) {
							if (numCandsAfterLockedCands < numCandsBeforeLockedCands) {
								SudokuMain.appendTextAndScrollDown(logTextArea, "\nLockedCandidates didn't produce Singles.\nNeed further techniques...");
							} else {
								SudokuMain.appendTextAndScrollDown(logTextArea, "\nNo LockedCandidates found.\nNeed further techniques...");
							}

							int numCandsBeforeFurtherTechnique = this.countCandidates();
							// t implement further techniques
							int numCandsAfterFurtherTechnique = this.countCandidates();
							
							// If further technique didn't produce Singles -> go for another further technique
							if (this.firstNakedSingle() == null && this.firstHiddenSingle() == null && this.getFreeFields().size() > 2) {
								if (numCandsAfterFurtherTechnique < numCandsBeforeFurtherTechnique) {
									SudokuMain.appendTextAndScrollDown(logTextArea, "\nFurther technique didn't produce Singles.\nNo further techniques implemented...");
								} else {
									SudokuMain.appendTextAndScrollDown(logTextArea, "\nNo more further technique.\nNo further techniques implemented...");
								}
							} else {
								SudokuMain.appendTextAndScrollDown(logTextArea, "\nFurther technique produced Singles.\nProceeding with next run...");
							}
						} else {
							SudokuMain.appendTextAndScrollDown(logTextArea, "\nLockedCandidates produced Singles.\nProceeding with next run...");
						}
					} else {
						SudokuMain.appendTextAndScrollDown(logTextArea, "\nTriple techniques produced Singles.\nProceeding with next run...");
					}
					
				} else {
					SudokuMain.appendTextAndScrollDown(logTextArea, "\nPair techniques produced Singles.\nProceeding with next run...");
				}
			} else {
				if (this.getFreeFields().size() > 0) SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nNo more HiddenSingles, but new NakedSingles.\nProceeding with next run...");
			}

			SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nEND Run No. " + run + " with " + this.getFreeFields().size() + " remaining free fields\n‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒‒\n");

			int numCandsAfterRun = this.countCandidates();
			if (numCandsAfterRun == 0) {
				t1 = System.nanoTime();
				SudokuMain.appendTextAndScrollDown(this.logTextArea, "Finished: No free fields remaining\n");
			}
			if (numCandsBeforeRun == numCandsAfterRun) {
				t1 = System.nanoTime();
				SudokuMain.appendTextAndScrollDown(this.logTextArea, "Abort: No more solving techniques implemented\n");
				int backTracking = JOptionPane.showConfirmDialog(this.logTextArea.getParent().getParent().getParent(), "<html><center>No solving stratrgies left.<br><br>Continue with try and error?</center></html>", "No solving stratrgies left", JOptionPane.YES_NO_OPTION);
				if (backTracking == 0) {
					SudokuMain.appendTextAndScrollDown(this.logTextArea, "Last resort: Proceeding with try-and-error...\n");
					this.backtracking();
					this.addStepToList(steps, run, "Solution after try-and-error");
				}
				break;
			}
		}

		if (this.getFreeFields().size() == 0) {
			SudokuMain.appendTextAndScrollDown(this.logTextArea, "### SUDOKU SUCESSFULLY SOLVED! ###\n");
		} else {
			SudokuMain.appendTextAndScrollDown(this.logTextArea, "### SUDOKU SOLVING FAILED! ###\n\nAbort status:\n");
//			this.print();
//			this.printFields();
			SudokuMain.appendTextAndScrollDown(this.logTextArea, "\nAborting with " + this.getFreeFields().size() + " remaining free fields\n");
		}

		SudokuMain.appendTextAndScrollDown(this.logTextArea, "Elapsed time: " + Math.round((t1 - t0) * Math.pow(10, -6)*10.0)/10.0 + " ms");

		return steps;

	}

	@SuppressWarnings("unchecked")
	public void addStepToList(ArrayList<Field[][]> steps, int run, String type) {
		int[][] init = new int[81][3];
		for (int i=1;i<=81;i++) {
			Field f = this.getFieldByFieldID(i).clone();
			int r = f.getRID();
			int c = f.getCID();
			int v = f.getVal();
			int[] val = {r,c,v};
			init[i-1] = val;
		}

		Field[][] step = new Sudoku(init).getArray();

		for (Field[] r : step) {
			for (Field c : r) {
				c.setCandidates((ArrayList<Integer>) (this.getArray()[c.getRID()-1][c.getCID()-1].getCandidates().clone()));
			}
		}

		steps.add(step);

		this.foundInRunNo.add(run);
		this.foundByType.add(type);
	}

	public void print() {
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "col 1  2  3   4  5  6   7  8  9  ");
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "row ----------------------------- ");
		int i = 1;
		for (Field[] x : this.grid) {
			String str = " " + i + " |";
			int j = 1;
			for (Field y : x) {
				int val = y.getVal();
				if (val != 0) {
					str += " " + val + " ";
				} else {
					str += "   ";
				}
				if (j % 3 == 0 && j != 9) {
					str += "|";
				}
				j++;
			}
			SudokuMain.appendTextAndScrollDown(this.logTextArea, str + "|");
			if (i % 3 == 0 && i != 9) {
				SudokuMain.appendTextAndScrollDown(this.logTextArea, "   |---------+---------+---------|");
			}
			i++;
		}
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "    ----------------------------- ");
		SudokuMain.appendTextAndScrollDown(this.logTextArea, "");
	}

	public void printFields() {

		for (Field[] r : this.grid) {
			for (Field f : r) {
				f.printField(this.logTextArea);
			}
		}
	}

	public static Field[] toArray1D(Field[][] grid2D) {
		ArrayList<Field> list = new ArrayList<Field>();
		for (Field[] r : grid2D ) {
			for (Field c : r) {
				list.add(c);
			}
		}
		return list.toArray(new Field[81]);
	}

	public boolean validateInput(JPanel sudokuPanel) {
		SudokuMain.appendTextAndScrollDown(logTextArea, "Validating input fields...");
		for (int u=1; u<=9; u++) {
			Field[] row = this.getRowByRowID(u);
			Field[] col = this.getColByColID(u);
			Field[] block = this.getBlockByBlockID(u);
			for (int i=1; i<=9; i++) {
				int nr = 0;
				for (Field r : row) {
					if (r.getVal()==i) {
						nr++;
					}
					if (nr>1) {
						SudokuMain.appendTextAndScrollDown(logTextArea, "Invalid input! Value '" + i + "' occurs\nmultiple times in Row " + u + ".\n");
						JOptionPane.showMessageDialog(sudokuPanel, "<html><center>Invalid input!<br><br>Value '" + i + "' occurs multiple<br>times in Row " + u + ".</center></html>", "Invalid input", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
				int nc = 0;
				for (Field c : col) {
					if (c.getVal()==i) {
						nc++;
					}
					if (nc>1) {
						SudokuMain.appendTextAndScrollDown(logTextArea, "Invalid input! Value '" + i + "' occurs\nmultiple times in Col " + u + ".\n");
						JOptionPane.showMessageDialog(sudokuPanel, "<html><center>Input not valid!<br><br>Value '" + i + "' occurs multiple<br>times in Col " + u + ".</center></html>", "Invalid input", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
				int nb = 0;
				for (Field b : block) {
					if (b.getVal()==i) {
						nb++;
					}
					if (nb>1) {
						SudokuMain.appendTextAndScrollDown(logTextArea, "Invalid input! Value '" + i + "' occurs\nmultiple times in Block " + u + ".\n");
						JOptionPane.showMessageDialog(sudokuPanel, "<html><center>Input not valid!<br><br>Value '" + i + "' occurs multiple<br>times in Block " + u + ".</center></html>", "Invalid input", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
			}
		}
		SudokuMain.appendTextAndScrollDown(logTextArea, "Input is valid! You may continue.\n");
		return true;
	}

}
