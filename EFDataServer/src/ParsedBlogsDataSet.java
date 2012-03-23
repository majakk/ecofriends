
public class ParsedBlogsDataSet {
	private String idString = null;
	private String publishedString = null;
	private String titleString = null;
	private String contentString = null;
	private String bidsString = null;

	public ParsedBlogsDataSet(String is, String ps, String ts, String cs, String bs){
		idString = is;
		publishedString = ps;
		titleString = ts;
		contentString = cs;
		bidsString = bs;
	}

	public String getIdString() {
		return idString;
	}

	public void setIdString(String extractedString) {
		this.idString = extractedString;
	}

	public String getPublishedString() {
		return publishedString;
	}

	public void setPublishedString(String extractedString) {
		this.publishedString = extractedString;
	}

	public void addToTitleString(String extractedString) {
		this.titleString += extractedString;
	}

	public String getTitleString() {
		return titleString;
	}

	public void setTitleString(String extractedString) {
		this.titleString = extractedString;
	}

	public void addToContentString(String extractedString) {
		this.contentString += extractedString;
	}
	
	public String getContentString() {
		return contentString;
	}

	public void setContentString(String extractedString) {
		this.contentString = extractedString;
	}

	public String getBidsString() {
		return bidsString;
	}

	public void setBidsString(String extractedString) {
		this.bidsString = extractedString;
	}

	/*public String toString(){
		return this.itemString + "  " + this.resourceString + "  " + this.valueString;
	}*/

}