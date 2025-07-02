package database;

public class EmptySetException extends Exception {
	public EmptySetException() {
		super("Il result set è vuoto.");
	}
}

