package de.earley.ecs.util

/**
 * https://gist.github.com/robhruska/4612278
 */
class MapUtils {

	/**
	 * Adds all elements from add into base recursively, i.e. existing maps with be extended, not overridden
	 * @param base the map to add to
	 * @param add he map to add
	 * @return void, but changes are stored in base
	 */
	public static merge(Map base, Map add) {
		add.each {
			key, value ->
				//TODO atm if base has a value and add a map, base gets overridden, is that correct?
			if (base.get(key) && base.get(key) instanceof Map && value instanceof Map) {
				// deep recursive merge
				merge(base.get(key), value)
			} else {
				// no need for deep merge if it only exists in one place or gets completely overridden
				base.put(key, value)
			}
		}
	}

	/**
	 * deeply clones a map
	 * @param orig
	 * @return a new map
	 */
	static <T> T deepcopy(T orig) {
		def bos = new ByteArrayOutputStream()
		def oos = new ObjectOutputStream(bos)
		oos.writeObject(orig); oos.flush()
		def bin = new ByteArrayInputStream(bos.toByteArray())
		def ois = new ObjectInputStream(bin)
		return ois.readObject() as T
	}
}