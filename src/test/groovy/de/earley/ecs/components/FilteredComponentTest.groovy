package de.earley.ecs.components

import de.earley.ecs.core.Entity
import spock.lang.Specification
/**
 * Created 21/04/16
 * @author Timothy Earley
 */
class FilteredComponentTest extends Specification {

	def "test filter"() {

		given: "a filtered component"
		def count = 0
		FilteredComponent fc = {
			count = it.size()
		}
		fc.filter << "foo"
		fc.filter << "bar"

		when: "entities are passed"
		fc.update ([
				new Entity(props: [foo: true, bar: []]),
				new Entity(props: [foo: true, bar: [], other: "some text"]),
				new Entity(props: [bar: []]),
				new Entity(props: [foo: true]),
				new Entity(props: []),
				new Entity(props: [other: "foo", test: [foo: "bar"]]),
		])

		then: "only two entities should make it through"
		count == 2

	}

}
