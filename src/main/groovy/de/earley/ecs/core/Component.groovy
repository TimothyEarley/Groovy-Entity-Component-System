package de.earley.ecs.core

import de.earley.ecs.core.Entity

/**
 * Created 24/03/16
 * @author Timothy Earley
 */
interface Component {

	void update(List<Entity> entities);

}