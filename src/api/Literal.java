package api;

/**
 * @author Ethan
 *
 */
public interface Literal extends Variable {
	public boolean isTrue();
	public boolean isFalse();
	public Literal setValue(boolean e);
	public Literal setValue(Literal l);
}
