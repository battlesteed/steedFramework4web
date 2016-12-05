package steed.util.document;

/**
 * Excel要合并的表格描述类,
 * 详情看org.apache.poi.xssf.usermodel.XSSFSheet.addMergedRegion(CellRangeAddress)
 * @author 战马
 * @see org.apache.poi.xssf.usermodel.XSSFSheet#addMergedRegion(CellRangeAddress)
 */
public class MergedRegion {
	private int firstRow;
	private int lastRow;
	private int firstCol;
	private int lastCol;
	public int getFirstRow() {
		return firstRow;
	}
	public MergedRegion() {
		super();
	}
	public MergedRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
		super();
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.firstCol = firstCol;
		this.lastCol = lastCol;
	}
	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}
	public int getLastRow() {
		return lastRow;
	}
	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}
	public int getFirstCol() {
		return firstCol;
	}
	public void setFirstCol(int firstCol) {
		this.firstCol = firstCol;
	}
	public int getLastCol() {
		return lastCol;
	}
	public void setLastCol(int lastCol) {
		this.lastCol = lastCol;
	}
	
}
