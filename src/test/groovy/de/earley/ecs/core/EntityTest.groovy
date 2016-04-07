package de.earley.ecs.core

import spock.lang.Specification

/**
 * Created 07/04/16
 * @author Timothy Earley
 */
class EntityTest extends Specification {

	def "LeftShift"() {
		given: "an entity with props"
		def entity = new Entity(props: props)

		when: "props get added"
		entity << add

		then: "props should be extended"
		entity.props == newProps

		where:
		props     | add        | newProps
		[:]       | [:]        | [:]
		[a: true] | [a: false] | [a: false]
		[a: 1]    | [b: 1]     | [a: 1, b: 1]

	}

	def "left shift observer"() {

		given: "an entity with observer"
		def changes = 0
		def entity = new Entity(props: [a: 1]).with {
			props.addPropertyChangeListener {
				changes++
			}
			return delegate
		}

		when: "property added and them changed"
		entity << [a: 0]
		entity.props.a = 1

		then: "2 changes should have occurred"
		changes == 2

	}

	def "Clone"() {

		given: "an entity to clone"
		def entity = new Entity(props: [test: 1])

		when: "the entity is cloned"
		def clone = entity.clone()

		then: "the properties should be cloned as well"
		entity != clone
		entity.props == clone.props
		!entity.props.is(clone.props)

	}

	def "Remove"() {

		given: "an entity"
		def entity = new Entity()

		when: "the entity gets removed"
		entity.remove()

		then: "the removed flag should be true"
		entity.removed

	}

	def "GetId"() {
		given: "two entities"
		def a = new Entity()
		def b = new Entity()

		when: "a clone is created"
		def c = a.clone()

		then: "the id should differ"
		a.id != b.id != c.id
	}

	def "GetProps"() {
		given: "an entity with props"
		def entity = new Entity(props: [test: true])

		when: "nothing to do"

		then: "we can access the props"
		entity.props.test
	}

	def "SetProps"() {
		given: "an entity"
		def entity = new Entity()

		when: "properties get set"
		entity.props = [a: true]

		then: "the properties are set"
		entity.props.a
	}
}
