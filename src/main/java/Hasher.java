

/**
 * Functional Inteface for simplified hashing
 * @param <TYPE> to parse. Any object
 */
@FunctionalInterface
public interface Hasher <TYPE extends Object>{
	/**
	 * Return a single hash representing to the two supplied arguments
	 * @return Idempotent hash value regardless of supplied argument ordinality
	 *   (i.e. hash("Muse", "Coldplay") == ("Coldplay", "Muse"))
	 */
	int hash (TYPE t1, TYPE t2);
}
