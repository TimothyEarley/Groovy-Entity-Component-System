package de.earley.ecs.core

import de.earley.ecs.observer.Subject
import groovy.transform.Canonical

/**
 * Created 24/03/16
 * @author Timothy Earley
 *
 * Manages a bunch of entities and components.
 * This glues together the data provided by prototypes,
 * the actual entities cloned from these (spwn) and the
 * system componnets that update entities. Also includes
 * Observables for spawning and removing
 *
 *
 * Prototypes can be created in a script as defined in EntityDataDSL
 *
 * Example for health observer:
 *
 * <code>
 * onSpawn.addObserver {
 *  observer, entity ->
 *      entity.props.addPropertyChangeListener("health") {
 *          if (it.newValue <= 0) entity.removed()
 *      }
 *  }
 * </code>
 *
 *
 *
 */
@Canonical
class EntityComponentSystem {

	/**
	 * Runs the consumer on each newly created entity.
	 * Especially useful for adding listeners to the props
	 * map of an entity without having to register a full blown component
	 */
	final onSpawn = new Subject()

	/**
	 * Called when an entity gets removed
	 */
	final onRemove = new Subject()

	/**
	 * All entities start as prototypes from which live entities are created
	 */
	Map<String, Entity> prototypes = [:];

	/**
	 * Currently active entities
	 */
	final List<Entity> liveEntities = [];

	/**
	 * List of systems to be run in each update on all entities.
	 * Each component is responsible for filtering appropriate entities
	 */
	private List<Component> components = [];

	public void update() {
		components.each {
			it.update(liveEntities)
		}

		// removed entities
		liveEntities.removeIf {
			def remove = it.removed
			if (remove) onRemove.notifyObservers(it)
			remove
		}
	}

	public spawn(String name, Map args = [:]) {
		// Extend the prototype
		def entity = prototypes[name]?.clone()
		// Throw an error if not successful
		if (entity == null) throw new NoSuchElementException("No entity named $name (currently available: ${prototypes.keySet()})")
		// add the custom args
		entity << args
		spawn(entity)
	}

	public spawn(Entity entity) {
		onSpawn.notifyObservers(entity)
		liveEntities << entity
	}

	public void addComponent(Component component) {
		components << component
	}

	public boolean empty() {
		liveEntities.empty
	}

}
