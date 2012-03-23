import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class BlogsHandler extends DefaultHandler{

	static final String TAG = "Blogs"; // for Log	

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
	boolean in_atag = false;
	boolean in_btag = false;
	boolean in_idtag = false;
	boolean in_h3tag = false;
	boolean in_spantag = false;
	boolean in_linkstag = false;
	boolean in_infotag = false;
	boolean in_ultag = false;
	
	String tableClass = "";
	String tableClass2 = "";
	String pClass = "";
	String tableClassName = "title";
	String tableClassName2 = "translate-info";
	String pClassName = "links";

	//int counter = 0;

	int entrycounter = 0;
	//int tdcounter = 0;
	int hitscount = 0;
	
	int secondp = 0;

	int indexer[] = new int[100];

	

	private ParsedBlogsDataSet myParsedBlogsDataSet = new ParsedBlogsDataSet(null, null, null, null, null);
	private List<ParsedBlogsDataSet> myData = new LinkedList<ParsedBlogsDataSet>();



	// ===========================================================
	// Getter & Setter
	// ===========================================================


	public ParsedBlogsDataSet getParsedData(int index) {
		//return this.myParsedExampleDataSet;
		return this.myData.get(index);
	}

	public int getCounterValue() {
		return entrycounter;
	}
	
	public int getHitsValue() {
		return hitscount;
	}
	
	public int getDataIndex(){
		return myData.size();
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
			tableClass = atts.getValue("class");
			if(tableClass != null){
				if(tableClass.equalsIgnoreCase(tableClassName)){
					this.in_idtag = true;
				}
				else{
					this.in_idtag = false;
				}
			}
			tableClass2 = atts.getValue("class");
			if(tableClass2 != null){
				if(tableClass2.equalsIgnoreCase(tableClassName2)){
					this.in_infotag = true;
				}
				else{
					this.in_infotag = false;
				}
			}
			
			this.in_divtag = true;
			/*if(entrycounter > 0){
				indexer[entrycounter] = myData.size();	
			}*/
		}else if (localName.equals("b")) {
			this.in_btag = true;			
		}else if (localName.equals("p")) {
			secondp++;
			pClass = atts.getValue("class");
			if(pClass != null){
				if(pClass.equalsIgnoreCase(pClassName)){
					this.in_linkstag = true;
				}
				else{
					this.in_linkstag = false;
				}
			}
			
			
			
			this.in_ptag = true;			
		}else if (localName.equals("h3")) {
			secondp = 0;
			this.in_h3tag = true;
		}else if (localName.equals("span")) {
			this.in_spantag = true;
		}else if (localName.equals("a")) {
			this.in_atag = true;
		}else if (localName.equals("ul")) {
			this.in_ultag = true;
		}
	}


	/** Gets be called on closing tags like: 
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("div")) {			
			
			this.in_divtag = false;
			this.in_idtag = false;
			this.in_infotag = false;
			
		}else if (localName.equals("b")) {
			this.in_btag = false;			
		}else if (localName.equals("p")) {
			this.in_ptag = false;			
			this.in_linkstag = false;
		}else if (localName.equals("h3")) {
			secondp++;
			this.in_h3tag = false;			
		}else if (localName.equals("span")) {
			this.in_spantag = false;			
		}else if (localName.equals("a")) {
			this.in_atag = false;			
		}else if (localName.equals("ul")) {
			this.in_ultag = false;			
		}
		//System.out.println("B");
	}
	

	/** Gets be called on the following structure: 
	 * <tag>characters</tag> */
	@Override
    public void characters(char ch[], int start, int length) {
		if(this.in_divtag){
			
			if(this.in_idtag && this.in_ptag){
	    		myParsedBlogsDataSet.setIdString(new String(ch, start, length));
				//Log.v(TAG, status);
	    		
	    		Pattern intsOnly = Pattern.compile("\\d+");
	    		Matcher makeMatch = intsOnly.matcher(myParsedBlogsDataSet.getIdString());
	    		//String inputInt = "";
	    		while(makeMatch.find()){
	    			String inputInt = makeMatch.group();
	    			//System.out.println(inputInt);
	    			this.hitscount = Integer.parseInt(inputInt);
	    		}
	    		
	    		
	    		//System.out.println(myParsedRecipesDataSet.getIdString());
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
		if(this.in_h3tag && this.in_atag && !this.in_spantag){			
    		myParsedBlogsDataSet.addToTitleString(new String(ch, start, length));
    		//myParsedBlogsDataSet.setContentString(new String(ch, start, length));
    		
    		/*this.myData.add(new ParsedBlogsDataSet(myParsedBlogsDataSet.getIdString(), 
					null, 
					myParsedBlogsDataSet.getTitleString(),
					myParsedBlogsDataSet.getContentString(),
					null));*/
			//myParsedBlogsDataSet.setTitleString("");
			//myParsedBlogsDataSet.setContentString("");
    	}
		/*if(this.in_h3tag && this.in_atag && this.in_spantag){
			String adam = myParsedBlogsDataSet.getTitleString();
			adam = adam.replaceAll("\r", "");
			adam = adam.replaceAll("\n", "");
			adam = adam.replaceAll("  ", " ");
			adam = adam.replaceAll("null", "");
			adam = adam.trim();
			//System.out.println(adam);
			this.myData.add(new ParsedBlogsDataSet(myParsedBlogsDataSet.getIdString(), 
					null, 
					adam,
					myParsedBlogsDataSet.getContentString(),
					null));
			myParsedBlogsDataSet.setTitleString("");
			myParsedBlogsDataSet.setContentString("");
		}*/
		if(this.in_ptag && !this.in_divtag && !this.in_ultag && secondp == 3){			
    		//myParsedBlogsDataSet.addToTitleString(new String(ch, start, length));
    		myParsedBlogsDataSet.addToContentString(new String(ch, start, length));
    		
    		/*this.myData.add(new ParsedBlogsDataSet(myParsedBlogsDataSet.getIdString(), 
					null, 
					myParsedBlogsDataSet.getTitleString(),
					myParsedBlogsDataSet.getContentString(),
					null));*/
    		//System.out.println(secondp + ":" + myParsedBlogsDataSet.getContentString());
			//myParsedBlogsDataSet.setTitleString("");
			//myParsedBlogsDataSet.setContentString("");
    	}
		if(!this.in_ptag && !this.in_divtag && this.in_ultag && secondp == 3){	
			String adam = myParsedBlogsDataSet.getTitleString();
			adam = adam.replaceAll("\r", "");
			adam = adam.replaceAll("\n", "");
			adam = adam.replaceAll("  ", " ");
			adam = adam.replaceAll("null", "");
			adam = adam.trim();
			
			String bertil = myParsedBlogsDataSet.getContentString();
			bertil = bertil.replaceAll("\r", "");
			bertil = bertil.replaceAll("\n", "");			
			bertil = bertil.replaceAll("  ", " ");
			bertil = bertil.replaceAll("null", "");
			bertil = bertil.trim();
			//System.out.println(bertil);
			this.myData.add(new ParsedBlogsDataSet(myParsedBlogsDataSet.getIdString(), 
					null, 
					adam,
					bertil,
					null));
			myParsedBlogsDataSet.setTitleString("");
			myParsedBlogsDataSet.setContentString("");
		}
		
		
    }

}