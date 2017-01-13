package StringFormatter;

public class StringFormatter {
	String identifier;
	String formattedString;
	
	public StringFormatter(){
		
	}
	public StringFormatter(String _identifier){
		identifier = _identifier;
		LengthChecker();
		UnwantedCharacterEraser();
		NumberCharacterEraser();
		SplitOnInternalCaptalization();
	}
	private void LengthChecker() {
		StringBuilder s = new StringBuilder(identifier);
		  for( int i = 1; i < s.length(); ++i ) {
		  }
	}
	public String getFormattedString(){
		return formattedString;
	} 
	public void UnwantedCharacterEraser() {
		//System.out.println("real: "+identifier);
		StringBuilder s = new StringBuilder(identifier);
		  for( int i = 1; i < s.length(); ++i ) {
		     if( s.charAt(i)=='_' || s.charAt(i)=='.'||s.charAt(i)=='$' ||s.charAt(i)=='%'||s.charAt(i)=='-' ||
		    		 s.charAt(i)=='['||s.charAt(i)==']') {
		    	 s.setCharAt(i, ' ');
		     }
		  }
		  identifier = s.toString();
		  //System.out.println("here: "+s.toString());
		
	}
	public void SplitOnInternalCaptalization(){
		StringBuilder s = new StringBuilder(identifier);
		  for( int i = 1; i < s.length(); ++i ) {
		     if( Character.isUpperCase( s.charAt( i ))) {
		        s.insert( i++, ' ' );
		     }
		  }
		  identifier = s.toString().trim().toLowerCase();
		  //System.out.println("here: "+identifier);
		  formattedString = formattedString + identifier;
		}
	public void NumberCharacterEraser(){
		
		identifier = identifier.replaceAll("[0-9]","");
		//System.out.println(identifier);
		formattedString = identifier;
	}
	
	public String CharacterEraser(String se) {
		//System.out.println("real: "+identifier);
		StringBuilder s = new StringBuilder(se);
		  for( int i = 1; i < s.length(); ++i ) {
		     if( s.charAt(i)=='_' ||s.charAt(i)=='$' ||s.charAt(i)=='%'||s.charAt(i)=='-' ||
		    		 s.charAt(i)=='['||s.charAt(i)==']') {
		    	 s.setCharAt(i, ' ');
		     }
		  }
		  se = s.toString();
		  //System.out.println("here: "+s.toString());
		return se;
	}
	
}
