package de.earley.ecs.core

import de.earley.ecs.util.MapUtils
import de.earley.ecs.util.SerializableObservableMap
import groovy.transform.ToString
/**
 * Created 23/03/16
 * @author Timothy Earley
 *
 *
 * Entities are essiantially just a map with data and some utility functions
 *
 */
@ToString(includeNames = true)
class Entity implements Serializable {

	//TODO should be final, but then it cant be serialized without black magic (which I don't have)
	UUID id = UUID.randomUUID()

	// does not need to be saved, since it will always be false, except in the middle of an update
	public transient boolean removed

	SerializableObservableMap props = [:]

	/**
	 * Adds the args to the properties
	 * @return this
	 */
	def leftShift(Map args) {
		if (!args) return this;
		// Deep merge ensures that updates to existing keys do not override all other values stored in the key
		MapUtils.merge(props, args)
		return this
	}

	/**
	 * Clones the entity, removing the observers and generating a new id, resets removed as well
	 * @return
	 */
	@Override
	protected Entity clone() {
		// make sure the new map is modifiable by casting to HashMap (getContent is immutable)
		def newProperties = MapUtils.deepcopy props.getContent() as HashMap
		new Entity(props: newProperties as SerializableObservableMap)
	}

	def remove() {
		removed = true
	}

	@Override
	int hashCode() {
		// only id counts
		id.hashCode()
	}

	/**
	 * Compares the id
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false
		}
		if (this.is(other)) {
			return true
		}
		if (!(other instanceof Entity)) {
			return false
		}

		Entity otherTyped = ((other) as Entity)

		id == otherTyped.id
	}


}
