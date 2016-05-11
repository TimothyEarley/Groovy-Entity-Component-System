package de.earley.ecs.core
/**
 * Created 24/03/16
 * @author Timothy Earley
 */
abstract class Component {

	/**
	 * Signals, whether the component can be removed
	 */
	def remove = false

	/**
	 * Updates the entities according to the components task
	 * @param entities the list of available entities
	 */
	abstract update(List<Entity> entities);

}