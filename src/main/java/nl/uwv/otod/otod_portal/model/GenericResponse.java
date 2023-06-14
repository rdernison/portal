package nl.uwv.otod.otod_portal.model;

public class GenericResponse {
    private String message;
    private String error;
 
    public GenericResponse(String message, String error) {
        super();
        this.message = message;
        this.error = error;
    }

	public GenericResponse(String message) {
        super();
        this.message = message;
    }
 
    public String getMessage() {
		return message;
	}

	public String getError() {
		return error;
	}

}