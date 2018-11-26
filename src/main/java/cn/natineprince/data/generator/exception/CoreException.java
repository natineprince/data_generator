package cn.natineprince.data.generator.exception;

public class CoreException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3837475647383726537L;
	
	public CoreException(){
		super();
	}
	
	public CoreException(String message){
		super(message);
	}

	public CoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public CoreException(Throwable cause) {
		super(cause);
	}
	
}
