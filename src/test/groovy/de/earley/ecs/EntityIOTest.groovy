package de.earley.ecs

import de.earley.ecs.core.Entity
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created 07/04/16
 * @author Timothy Earley
 */
class EntityIOTest extends Specification {

	@Shared
	def scriptOne =
			'''
				create 'test': [a: 1, b: 2]
			'''

	@Shared
	def scriptTwo =
			'''
				create 'base': [base: true, a: 1]
				'base'.extend (
					'extended': [base: false]
				)
			'''

	@Shared
	def scriptThree =
			'''
				create 'a': [base: true, a:true], 'b': [nested: [bool: true]]
				'a'.extend 'c': [base: false], 'd': [base: false, a:false]
			'''

	@Shared
	def scriptFour =
			'''
				create 'a': [a:true]
				'a'.extend 'b': [:]
			'''

	def "LoadScript"() {
		given: "a script to load"
		File script = File.createTempFile("temp", ".ge").with {
			deleteOnExit()
			write data
			return delegate
		}

		when: "The script is loaded"
		def result = EntityIO.loadScript(script)

		then: "The result should match the entities loaded"
		result.size() == entities.size()
		if (result) result.each {
			key, value -> entities.get(key) == value.props
		}

		where:
		data        | entities
		""          | [:]
		scriptOne   | [test: [a: 1, b: 2]]
		scriptTwo   | [base: [base: true, a: 1], extended: [base: false, a: 1]]
		scriptThree | [a: [base: true, a: true], b: [nested: [bool: true]], c: [base: false, a: true], d: [base: false, a: false]]
		scriptFour  | [a: [a: true], b: [a: true]]
	}

	def "Save and load"() {

		given: "a temp file"
		File tmp = File.createTempFile("temp", ".tmp").with {
			deleteOnExit()
			return delegate
		}

		when: "saving an object"
		EntityIO.save(tmp, entity)

		then: "loading it should work"
		EntityIO.load(tmp) == entity

		where:
		entity << [
				new Entity(),
				new Entity(props: [a: true, b: [nested: true]]).with {
					props.addPropertyChangeListener {} // should not matter / be deleted
					return delegate
				}
		]

	}

	def "save and load entities as json"() {

		given: "a temp file"
		File tmp = File.createTempFile("temp", ".tmp").with {
			deleteOnExit()
			return delegate
		}

		when: "the entity iss saved"
		EntityIO.saveJSON(tmp, data, pretty)

		then: "the loaded entities are the same"
		EntityIO.loadJSON(tmp) == data

		where:
		data                                                                | pretty
		[new Entity()]                                                      | true
		[new Entity(props: [test: true]), new Entity(props: [test: false])] | true
		[new Entity(props: [test: true]), new Entity(props: [test: false])] | false
		[]                                                                  | true
		[]                                                                  | false
	}

	def "load files from path names"() {
		when: "two files are loaded"
		def result = EntityIO.loadScript ( 'entities/A.ge', 'entities/B.ge' )

		then: "both files were loaded"
		result.size()  == 2
		result.a.props.data
		result.b.props.data
	}

	def "load a directory"() {
		given: "a directoy"
		def dir = 'entities'

		when: 'the dir is loaded'
		def result = EntityIO.loadScriptDirectory 'entities'		

		then: "the result matches with the file"
		result.size()  == 2
		result.a.props.data
		result.b.props.data
	}
}