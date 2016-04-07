package de.earley.ecs.core

import de.earley.ecs.util.MapUtils
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Created 23/03/16
 * @author Timothy Earley
 *
 *
 * Entities are essiantially just a map with data and some utility functions
 *
 */
@Canonical
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true)
class Entity implements Serializable {

	// should be final, but then it cant be serialized without black magic (which I don't have)
	private id = UUID.randomUUID()

	boolean removed

	ObservableMap props = [:]

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
		new Entity(props: newProperties as ObservableMap)
	}

	def remove() {
		removed = true
	}

	/*
	!!!!!
	When serialising, observers are lost
	!!!!!
	 */

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.with {
			writeObject id
			writeBoolean removed
			writeObject props.getContent()
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.with {
			id = readObject() as UUID
			removed = readBoolean()
			props = readObject() as ObservableMap
		}
	}


	// i don't know why the generated one didn't work
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

		return this.removed == otherTyped.removed && this.id == otherTyped.id && this.props == otherTyped.props
	}


}
