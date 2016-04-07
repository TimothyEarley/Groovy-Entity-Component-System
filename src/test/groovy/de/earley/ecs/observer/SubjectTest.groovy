package de.earley.ecs.observer

import spock.lang.Specification

/**
 * Created 07/04/16
 * @author Timothy Earley
 */
class SubjectTest extends Specification {
	def "NotifyObservers"() {
		given: "a subject and observer"
		def subject = new Subject()
		def observed = false
		subject.addObserver {
			observer, arg ->
				observed = true
				assert arg == "test"
		}

		when: "observer called"
		subject.notifyObservers("test")

		then: "observed should be true"
		observed
	}
}
