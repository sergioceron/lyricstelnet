public enum Color {
	BLACK   ( 30 ),
	RED     ( 31 ),
	GREEN   ( 32 ),
	YELLOW  ( 33 ),
	BLUE    ( 34 ),
	MAGENTA ( 35 ),
	CYAN    ( 36 ),
	WHITE   ( 37 );

	private int code;

	private Color(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}
}
