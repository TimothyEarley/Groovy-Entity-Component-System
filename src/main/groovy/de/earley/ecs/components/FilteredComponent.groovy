package de.earley.ecs.components

import de.earley.ecs.core.Component
import de.earley.ecs.core.Entity

/**
 * Extends the component by providing only entities with a given property (top level only atm)
 *
 * Created 21/04/16
 * @author Timothy Earley
 */
abstract class FilteredComponent extends Component {

	List<String> filter = []

	@Override
	def update(List<Entity> entities) {
		updateFiltered entities.findAll {
			entity ->
				def keys = entity.props.keySet()
				keys.containsAll(filter)
		}
	}

	abstract updateFiltered(List<Entity> entities);

}
