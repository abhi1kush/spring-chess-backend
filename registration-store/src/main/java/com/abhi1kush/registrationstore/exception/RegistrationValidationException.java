import java.util.Map;

public class RegistrationValidationException extends RegistrationException {

	private final Map<String, String> fieldErrors;

	public RegistrationValidationException(String errorCode, String message, Map<String, String> fieldErrors) {
		super(errorCode, message);
		this.fieldErrors = fieldErrors;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}
}
