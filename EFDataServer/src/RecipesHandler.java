import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class RecipesHandler extends DefaultHandler{

	static final String TAG = "RECIPES"; // for Log	

	/*boolean in_entrytag = false;
	boolean in_idtag = false;
	boolean in_publishedtag = false;
	boolean in_linktag = false;
	boolean in_titletag = false;
	boolean in_contenttag = false;
	boolean in_updatedtag = false;
	boolean in_atag = false;
	boolean in_emtag = false;*/
	
	boolean in_divtag = false;
	boolean in_ptag = false;
	boolean in_btag = false;
	boolean in_idtag = false;
	
	String tableClass = "";
	String tableClassName = "hits";

	//int counter = 0;

	int entrycounter = 0;
	//int tdcounter = 0;
	int hitscount = 0;

	int indexer[] = new int[100];

	

	private ParsedRecipesDataSet myParsedRecipesDataSet = new ParsedRecipesDataSet(null, null, null, null, null);
	private List<ParsedRecipesDataSet> myData = new LinkedList<ParsedRecipesDataSet>();



	// ===========================================================
	// Getter & Setter
	// ===========================================================


	public ParsedRecipesDataSet getParsedData(int index) {
		//return this.myParsedExampleDataSet;
		return this.myData.get(index);
	}

	public int getCounterValue() {
		return entrycounter;
	}
	
	public int getHitsValue() {
		return hitscount;
	}


	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void startDocument() throws SAXException {
		//this.myParsedStatusDataSet = new ParsedHofDataSet(null, null, null);
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}



	/** Gets be called on opening tags like: 
	 * <tag> 
	 * Can provide attribute(s), when xml was like:
	 * <tag attribute="attributeValue">*/
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
	
		if (localName.equals("div")) {
			entrycounter++;
			//System.out.println("A");
			tableClass = atts.getValue("id");
			if(tableClass != null){
				if(tableClass.equalsIgnoreCase(tableClassName)){
					this.in_idtag = true;
				}
				else{
					this.in_idtag = false;
				}
			}
			
			this.in_divtag = true;
			/*if(entrycounter > 0){
				indexer[entrycounter] = myData.size();	
			}*/
		}else if (localName.equals("b")) {
			this.in_btag = true;			
		}else if (localName.equals("p")) {
			this.in_ptag = true;			
		}
	}


	/** Gets be called on closing tags like: 
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("div")) {
			
			/*this.myData.add(new ParsedItemsDataSet(myParsedItemsDataSet.getIdString(), 
					myParsedItemsDataSet.getPublishedString(), 
					myParsedItemsDataSet.getTitleString(),
					myParsedItemsDataSet.getContentString(),
					null));
			myParsedItemsDataSet.setTitleString("");
			myParsedItemsDataSet.setContentString("");*/
			this.in_divtag = false;
			this.in_idtag = false;
			
		}else if (localName.equals("b")) {
			this.in_btag = false;			
		}else if (localName.equals("p")) {
			this.in_ptag = false;			
		}
		//System.out.println("B");
	}
	

	/** Gets be called on the following structure: 
	 * <tag>characters</tag> */
	@Override
    public void characters(char ch[], int start, int length) {
		if(this.in_divtag){
			
			if(this.in_idtag && this.in_btag){
	    		myParsedRecipesDataSet.setIdString(new String(ch, start, length));
				//Log.v(TAG, status);
	    		
	    		Pattern intsOnly = Pattern.compile("\\d+");
	    		Matcher makeMatch = intsOnly.matcher(myParsedRecipesDataSet.getIdString());
	    		//String inputInt = "";
	    		while(makeMatch.find()){
	    			String inputInt = makeMatch.group();
	    			//System.out.println(inputInt);
	    			this.hitscount = Integer.parseInt(inputInt);
	    		}
	    		
	    		
	    		//System.out.println(myParsedRecipesDataSet.getIdString());
	    	}
			if(this.in_ptag){
	    		//myParsedItemsDataSet.setPublishedString(new String(ch, start, length));
				//Log.v(TAG, String.valueOf(length));
				;
	    	}
			//if(this.in_titletag){
	    	//	myParsedItemsDataSet.addToTitleString(new String(ch, start, length));
				//Log.v(TAG, String.valueOf(length));
	    	//}
			//if(this.in_contenttag){
				//if(!this.in_atag){
				//myParsedItemsDataSet.addToContentString(new String(ch, start, length));
				//}else{
				//	myParsedItemsDataSet.addToContentString("");
				//}
				//Log.v(TAG, String.valueOf(length));
	    		
	    		//System.out.println(myParsedItemsDataSet.getContentString());					
	    	//}			
			
		}
    }

}