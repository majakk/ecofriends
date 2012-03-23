import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ItemsHandler extends DefaultHandler{

	static final String TAG = "ITEMS"; // for Log	

	boolean in_entrytag = false;
	boolean in_idtag = false;
	boolean in_publishedtag = false;
	boolean in_linktag = false;
	boolean in_titletag = false;
	boolean in_contenttag = false;
	boolean in_updatedtag = false;
	boolean in_atag = false;
	boolean in_emtag = false;
	
	String tableClass = "";
	String tableClassName = "clientLoaderTable";

	//int counter = 0;

	int entrycounter = 0;
	//int tdcounter = 0;

	int indexer[] = new int[100];

	

	private ParsedItemsDataSet myParsedItemsDataSet = new ParsedItemsDataSet(null, null, null, null, null);
	private List<ParsedItemsDataSet> myData = new LinkedList<ParsedItemsDataSet>();



	// ===========================================================
	// Getter & Setter
	// ===========================================================


	public ParsedItemsDataSet getParsedData(int index) {
		//return this.myParsedExampleDataSet;
		return this.myData.get(index);
	}

	public int getCounterValue() {
		return entrycounter;
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
		
	
		if (localName.equals("entry")) {
			entrycounter++;
			//System.out.println("A");
			this.in_entrytag = true;	
			/*if(entrycounter > 0){
				indexer[entrycounter] = myData.size();	
			}*/
		}else if (localName.equals("id")) {
			this.in_idtag = true;			
		}else if (localName.equals("published")) {
			this.in_publishedtag = true;			
		}else if (localName.equals("title")) {
			this.in_titletag = true;
		}else if (localName.equals("content")) {
			this.in_contenttag = true;
		}else if (localName.equals("updated")) {
			this.in_updatedtag = true;		
		}else if (localName.equals("a")) {
			this.in_atag = true;		
		}else if (localName.equals("em")) {
			this.in_emtag = true;		
		}
	}


	/** Gets be called on closing tags like: 
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("entry")) {
			
			this.myData.add(new ParsedItemsDataSet(myParsedItemsDataSet.getIdString(), 
					myParsedItemsDataSet.getPublishedString(), 
					myParsedItemsDataSet.getTitleString(),
					myParsedItemsDataSet.getContentString(),
					null));
			myParsedItemsDataSet.setTitleString("");
			myParsedItemsDataSet.setContentString("");
			this.in_updatedtag = false;
			
		}else if (localName.equals("id")) {
			this.in_idtag = false;			
		}else if (localName.equals("published")) {
			this.in_publishedtag = false;			
		}else if (localName.equals("title")) {
			this.in_titletag = false;
		}else if (localName.equals("content")) {
			this.in_contenttag = false;
		}else if (localName.equals("updated")) {
			this.in_updatedtag = false;		
		}else if (localName.equals("a")) {
			this.in_atag = false;		
		}else if (localName.equals("em")) {
			this.in_emtag = false;		
			//System.out.println("EM");
		}
		//System.out.println("B");
	}
	

	/** Gets be called on the following structure: 
	 * <tag>characters</tag> */
	@Override
    public void characters(char ch[], int start, int length) {
		if(this.in_entrytag){
			
			if(this.in_idtag){
	    		myParsedItemsDataSet.setIdString(new String(ch, start, length));
				//Log.v(TAG, status);
	    		//System.out.println(myParsedItemsDataSet.getIdString());
	    	}
			if(this.in_publishedtag){
	    		myParsedItemsDataSet.setPublishedString(new String(ch, start, length));
				//Log.v(TAG, String.valueOf(length));
	    	}
			if(this.in_titletag){
	    		myParsedItemsDataSet.addToTitleString(new String(ch, start, length));
				//Log.v(TAG, String.valueOf(length));
	    	}
			if(this.in_contenttag){
				//if(!this.in_atag){
				myParsedItemsDataSet.addToContentString(new String(ch, start, length));
				//}else{
				//	myParsedItemsDataSet.addToContentString("");
				//}
				//Log.v(TAG, String.valueOf(length));
	    		
	    		//System.out.println(myParsedItemsDataSet.getContentString());					
	    	}			
			
		}
    }

}