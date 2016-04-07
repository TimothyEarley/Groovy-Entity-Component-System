package de.earley.ecs.util

import spock.lang.Specification

/**
 * Created 07/04/16
 * @author Timothy Earley
 */
class MapUtilsTest extends Specification {
	def "Merge"() {

		when: "merge the maps"
		MapUtils.merge(base, add)

		then: "result should be merged map"
		base == merged

		where:
		base        | add               | merged
		[a: 1]      | [:]               | [a: 1]
		[a: 1]      | [a: 0]            | [a: 0]
		[a: 1]      | [b: 1]            | [a: 1, b: 1]
		[a: [b: 1]] | [a: [b: 0, c: 1]] | [a: [b: 0, c: 1]]
	}

	def "Deepcopy"() {

		when: "a deep copy is made"
		def result = MapUtils.deepcopy(orig)

		then: "result is equal to orig, but not the same"
		result == orig
		!result.is(orig)
		// TODO test nesting?

		where:
		orig << [
				[1, 2, 3],
				[a: [1, 2, 3], b: [4, 5, 6]],
		]
	}
}
