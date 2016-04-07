package de.earley.ecs.core

import spock.lang.Shared
import spock.lang.Specification

/**
 * Created 07/04/16
 * @author Timothy Earley
 */
class EntityComponentSystemTest extends Specification {

	@Shared
	def protos = [
			a: new Entity(props: [a: true]),
			b: new Entity(props: [a: false]),
	]

	def "Update and remove"() {
		given: "an ecs"
		def ecs = new EntityComponentSystem(prototypes: protos)
		ecs.spawn('a')
		ecs.liveEntities[0].remove()

		when: "an update occurs"
		ecs.update()

		then: "The entity gets removed"
		ecs.liveEntities.size() == 0
	}

	def "Spawn"() {
		given: "An ecs"
		def ecs = new EntityComponentSystem(prototypes: protos)

		when: "we spawn an entity"
		ecs.spawn("a")

		then: "a new entity gets added"
		ecs.liveEntities.size() == 1
		ecs.liveEntities[0].props == protos.a.props
	}

	def "AddComponent"() {
		given: "an ecs"
		def ecs = new EntityComponentSystem(prototypes: protos)
		5.times { ecs.spawn('a') }
		def count = 0

		when: "a component gets added"
		ecs.addComponent {
			it.each { count++ }
		}
		ecs.update()

		then: "after the update count should be equals to the entity count"
		ecs.liveEntities.size() == count
	}

	def "GetOnSpawn"() {
		given: "an ecs"
		def ecs = new EntityComponentSystem(prototypes: protos)
		def count = 0

		when: "a spawn listener gets added after one spawn and before some others"
		ecs.spawn('b')
		ecs.onSpawn.addObserver {
			observer, entity -> count++
		}
		5.times {
			ecs.spawn('a')
		}

		then: "the count must be equals to the spawned entities after the listener was added"
		count == 5

	}

	def "GetOnRemove"() {

		given: "an ecs"
		def ecs = new EntityComponentSystem(prototypes: protos)
		def count = 0
		10.times { ecs.spawn('b') }

		when: "a remove listener gets added inbetween some removes"
		ecs.with {
			liveEntities[0].remove()
			update()
			onRemove.addObserver {
				observer, entity -> count++
			}
			3.times {
				liveEntities[it].remove()
			}
			update()
		}

		then: "the count must be equals to the removed entities after the listener was added"
		count == 3

	}
}
