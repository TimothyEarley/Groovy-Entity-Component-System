package de.earley.ecs.util

import de.earley.ecs.core.Entity

/**
 * Created 23/03/16
 * @author Timothy Earley
 *
 *
 * Defines the DSL for entities with data.
 *
 * To function, a entities map must be provided through the context
 *
 * The form is:
 * <code>
 * create (
 *      id: [args...],
 *      id2: [args...], ...
 * )
 * </code>
 *
 * To extend an existing entitiy, use:
 * "id" extend  (
 *      "expandedID": [args...],
 *      "otherExpanded": [args...], ...
 * )
 */
abstract class EntityDataDSL extends Script {

	EntityDataDSL() {
		// add the extend method
		String.metaClass.extend = {
			map ->
				def prototype = entities[delegate as String]
				for (entry in map) {
					//FIXME: if map is empty, error os thrown
					entities[entry.key] = prototype.clone() << entry.value
				}
		}
	}

	def create(Map<String, Map> map) {
		for (entry in map) {
			entities[entry.key] = new Entity () << entry.value
		}
	}


}
