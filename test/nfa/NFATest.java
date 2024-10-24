package test.nfa;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import fa.nfa.NFA;

public class NFATest {

	/**
	 * Helper method to create the first NFA (nfa1).
	 * This NFA has two states: "a" and "b".
	 * It adds two symbols '0' and '1' to the alphabet.
	 * It defines transitions:
	 * - "a" -> "a" on '0'
	 * - "a" -> "b" on '1'
	 * - "b" -> "a" on epsilon transition.
	 * Also tests invalid transitions and state additions.
	 */
	private NFA nfa1() {
		NFA nfa = new NFA();

		// Add symbols '0' and '1' to the alphabet
		nfa.addSigma('0');
		nfa.addSigma('1');

		// Add and set states, and mark final state
		assertTrue(nfa.addState("a"));
		assertTrue(nfa.setStart("a"));

		assertTrue(nfa.addState("b"));
		assertTrue(nfa.setFinal("b"));

		// Check invalid state additions and settings
		assertFalse(nfa.addState("a"));  // Adding duplicate state
		assertFalse(nfa.setStart("c"));  // Setting non-existent state "c" as start
		assertFalse(nfa.setFinal("d"));  // Setting non-existent state "d" as final

		// Add valid transitions between states
		assertTrue(nfa.addTransition("a", Set.of("a"), '0'));
		assertTrue(nfa.addTransition("a", Set.of("b"), '1'));
		assertTrue(nfa.addTransition("b", Set.of("a"), 'e'));  // Epsilon transition

		// Test invalid transitions (from/to non-existent states, invalid symbols)
		assertFalse(nfa.addTransition("c", Set.of("a"), '0'));  // Non-existent state "c"
		assertFalse(nfa.addTransition("a", Set.of("b"), '3'));  // Invalid symbol '3'
		assertFalse(nfa.addTransition("b", Set.of("d", "c"), 'e'));  // Non-existent states "d" and "c"

		return nfa;  // Return the created NFA for further tests
	}

	/**
	 * Test 1.1: Basic NFA instantiation for nfa1.
	 * Confirms that NFA object is successfully created.
	 */
	@Test
	public void test1_1() {
		NFA nfa = nfa1();  // Instantiate nfa1
		System.out.println("nfa1 instantiation done");
	}

	/**
	 * Test 1.2: Verify the correctness of the states and settings in nfa1.
	 * - Checks that the states are correctly added and named.
	 * - Confirms that "a" is the start state and "b" is the final state.
	 */
	@Test
	public void test1_2() {
		NFA nfa = nfa1();
		assertNotNull(nfa.getState("a"));  // "a" exists
		assertEquals(nfa.getState("a").getName(), "a");  // Name of state "a" is correct
		assertEquals(nfa.getState("a"), nfa.getState("a"));  // Ensures correct reference to state "a"
		assertTrue(nfa.isStart("a"));  // "a" is start state
		assertTrue(nfa.isFinal("b"));  // "b" is final state
		System.out.println("nfa1 correctness done");
	}

	/**
	 * Test 1.3: Check if nfa1 behaves as a DFA (Deterministic Finite Automaton).
	 * nfa1 should not be a DFA because it has epsilon transitions.
	 */
	@Test
	public void test1_3() {
		NFA nfa = nfa1();
		assertFalse(nfa.isDFA());  // nfa1 is not a DFA (has epsilon transition)
		System.out.println("nfa1 isDFA done");
	}

	/**
	 * Test 1.4: Test the epsilon closure for states in nfa1.
	 * - For state "a", only "a" is in its epsilon closure.
	 * - For state "b", both "a" and "b" are in its epsilon closure due to the epsilon transition.
	 */
	@Test
	public void test1_4() {
		NFA nfa = nfa1();
		assertEquals(nfa.eClosure(nfa.getState("a")), Set.of(nfa.getState("a")));  // Epsilon closure of "a"
		assertEquals(nfa.eClosure(nfa.getState("b")), Set.of(nfa.getState("a"), nfa.getState("b")));  // Epsilon closure of "b"
		System.out.println("nfa1 eClosure done");
	}

	/**
	 * Test 1.5: Test whether nfa1 accepts or rejects specific strings.
	 * - Confirms correct acceptance/rejection based on transitions and the NFA's language.
	 */
	@Test
	public void test1_5() {
		NFA nfa = nfa1();
		assertFalse(nfa.accepts("0"));  // Does not accept "0"
		assertTrue(nfa.accepts("1"));   // Accepts "1"
		assertFalse(nfa.accepts("00")); // Does not accept "00"
		assertTrue(nfa.accepts("101")); // Accepts "101"
		assertFalse(nfa.accepts("e"));  // Does not accept empty string
		System.out.println("nfa1 accepts done");
	}

	/**
	 * Test 1.6: Test the max number of NFA copies created during the execution of input strings.
	 * Confirms the number of copies (states) the NFA generates for each input.
	 */
	@Test
	public void test1_6() {
		NFA nfa = nfa1();
		assertEquals(nfa.maxCopies("0"), 1);   // One copy for "0"
		assertEquals(nfa.maxCopies("1"), 2);   // Two copies for "1"
		assertEquals(nfa.maxCopies("00"), 1);  // One copy for "00"
		assertEquals(nfa.maxCopies("101"), 2); // Two copies for "101"
		assertEquals(nfa.maxCopies("e"), 1);   // One copy for empty string
		assertEquals(nfa.maxCopies("2"), 1);   // One copy for invalid symbol
		System.out.println("nfa1 maxCopies done");
	}

	/**
	 * Helper method to create the second NFA (nfa2).
	 * This NFA has multiple states (q0, q1, q2, q3, q4), epsilon transitions, and a loop on "q0".
	 */
	private NFA nfa2() {
		NFA nfa = new NFA();

		// Add symbols '0' and '1' to the alphabet
		nfa.addSigma('0');
		nfa.addSigma('1');

		// Add and set states, and mark final state
		assertTrue(nfa.addState("q0"));
		assertTrue(nfa.setStart("q0"));
		assertTrue(nfa.addState("q1"));
		assertTrue(nfa.addState("q2"));
		assertTrue(nfa.addState("q3"));
		assertTrue(nfa.addState("q4"));
		assertTrue(nfa.setFinal("q3"));

		// Test invalid state additions and settings
		assertFalse(nfa.addState("q3"));  // Duplicate state
		assertFalse(nfa.setStart("q5"));  // Non-existent start state
		assertFalse(nfa.setFinal("q6"));  // Non-existent final state

		// Add valid transitions between states
		assertTrue(nfa.addTransition("q0", Set.of("q0"), '0'));  // Loop on "q0" with '0'
		assertTrue(nfa.addTransition("q0", Set.of("q0"), '1'));  // Loop on "q0" with '1'
		assertTrue(nfa.addTransition("q0", Set.of("q1"), '1'));  // Transition "q0" -> "q1" on '1'
		assertTrue(nfa.addTransition("q1", Set.of("q2"), 'e'));  // Epsilon transition "q1" -> "q2"
		assertTrue(nfa.addTransition("q2", Set.of("q4"), '0'));  // Transition "q2" -> "q4" on '0'
		assertTrue(nfa.addTransition("q2", Set.of("q2", "q3"), '1'));  // Transition "q2" -> "q2" and "q3" on '1'
		assertTrue(nfa.addTransition("q4", Set.of("q1"), '0'));  // Transition "q4" -> "q1" on '0'

		// Test invalid transitions (from/to non-existent states, invalid symbols)
		assertFalse(nfa.addTransition("q0", Set.of("q5"), '0'));  // Non-existent state "q5"
		assertFalse(nfa.addTransition("q0", Set.of("q3"), '3'));  // Invalid symbol '3'
		assertFalse(nfa.addTransition("q5", Set.of("q0", "q2"), 'e'));  // Non-existent state "q5"

		return nfa;  // Return the created NFA for further tests
	}

	/**
	 * Test 2.1: Basic NFA instantiation for nfa2.
	 * Confirms that NFA object is successfully created.
	 */
	@Test
	public void test2_1() {
		NFA nfa = nfa2();
		System.out.println("nfa2 instantiation done");
	}

	/**
	 * Test 2.2: Verify the correctness of the states and settings in nfa2.
	 * - Checks that the states are correctly added and named.
	 * - Confirms that "q0" is the start state and "q3" is the final state.
	 */
	@Test
	public void test2_2() {
		NFA nfa = nfa2();
		assertNotNull(nfa.getState("q0"));  // "q0" exists
		assertEquals(nfa.getState("q3").getName(), "q3");  // Name of state "q3" is correct
		assertNull(nfa.getState("q5"));  // Non-existent state "q5"
		assertEquals(nfa.getState("q2"), nfa.getState("q2"));  // Ensures correct reference to state "q2"
		assertTrue(nfa.isStart("q0"));  // "q0" is start state
		assertTrue(nfa.isFinal("q3"));  // "q3" is final state
		assertFalse(nfa.isFinal("q6"));  // Non-existent state "q6" is not final
		System.out.println("nfa2 correctness done");
	}

	/**
	 * Test 2.3: Check if nfa2 behaves as a DFA (Deterministic Finite Automaton).
	 * nfa2 should not be a DFA because it has epsilon transitions and multiple transitions for some states.
	 */
	@Test
	public void test2_3() {
		NFA nfa = nfa2();
		assertFalse(nfa.isDFA());  // nfa2 is not a DFA
		System.out.println("nfa2 isDFA done");
	}

	/**
	 * Test 2.4: Test the epsilon closure for states in nfa2.
	 * - For state "q0", only "q0" is in its epsilon closure.
	 * - For state "q1", both "q1" and "q2" are in its epsilon closure due to the epsilon transition.
	 */
	@Test
	public void test2_4() {
		NFA nfa = nfa2();
		assertEquals(nfa.eClosure(nfa.getState("q0")), Set.of(nfa.getState("q0")));  // Epsilon closure of "q0"
		assertEquals(nfa.eClosure(nfa.getState("q1")), Set.of(nfa.getState("q1"), nfa.getState("q2")));  // Epsilon closure of "q1"
		assertEquals(nfa.eClosure(nfa.getState("q3")), Set.of(nfa.getState("q3")));  // Epsilon closure of "q3"
		assertEquals(nfa.eClosure(nfa.getState("q4")), Set.of(nfa.getState("q4")));  // Epsilon closure of "q4"
		System.out.println("nfa2 eClosure done");
	}

	/**
	 * Test 2.5: Test whether nfa2 accepts or rejects specific strings.
	 * - Confirms correct acceptance/rejection based on transitions and the NFA's language.
	 */
	@Test
	public void test2_5() {
		NFA nfa = nfa2();
		assertTrue(nfa.accepts("1111"));     // Accepts "1111"
		assertFalse(nfa.accepts("e"));       // Does not accept empty string
		assertFalse(nfa.accepts("0001100")); // Does not accept "0001100"
		assertTrue(nfa.accepts("010011"));   // Accepts "010011"
		assertFalse(nfa.accepts("0101"));    // Does not accept "0101"
		System.out.println("nfa2 accepts done");
	}

	/**
	 * Test 2.6: Test the max number of NFA copies created during the execution of input strings.
	 * Confirms the number of copies (states) the NFA generates for each input.
	 */
	@Test
	public void test2_6() {
		NFA nfa = nfa2();
		assertEquals(nfa.maxCopies("1111"), 4);     // Four copies for "1111"
		assertEquals(nfa.maxCopies("e"), 1);        // One copy for empty string
		assertEquals(nfa.maxCopies("0001100"), 4);  // Four copies for "0001100"
		assertEquals(nfa.maxCopies("010011"), 4);   // Four copies for "010011"
		assertEquals(nfa.maxCopies("0101"), 3);     // Three copies for "0101"
		System.out.println("nfa2 maxCopies done");
	}

	/**
	 * Helper method to create the third NFA (nfa3).
	 * This NFA has different states (W, L, I, N) and epsilon transitions between them.
	 */
	private NFA nfa3() {
		NFA nfa = new NFA();

		// Add symbols to the alphabet ('#', '0', '1')
		nfa.addSigma('#');
		nfa.addSigma('0');
		nfa.addSigma('1');

		// Add and set states, and mark final state
		assertTrue(nfa.addState("W"));
		assertTrue(nfa.setStart("W"));
		assertTrue(nfa.addState("L"));
		assertTrue(nfa.addState("I"));
		assertTrue(nfa.addState("N"));
		assertTrue(nfa.setFinal("N"));

		// Test invalid state additions and settings
		assertFalse(nfa.addState("N"));  // Duplicate state
		assertFalse(nfa.setStart("Z"));  // Non-existent start state
		assertFalse(nfa.setFinal("Y"));  // Non-existent final state

		// Add valid transitions between states
		assertTrue(nfa.addTransition("W", Set.of("N"), '#'));  // Transition "W" -> "N" on '#'
		assertTrue(nfa.addTransition("W", Set.of("L"), 'e'));  // Epsilon transition "W" -> "L"
		assertTrue(nfa.addTransition("L", Set.of("L", "N"), '0'));  // Transition "L" -> "L" and "N" on '0'
		assertTrue(nfa.addTransition("L", Set.of("I"), 'e'));  // Epsilon transition "L" -> "I"
		assertTrue(nfa.addTransition("I", Set.of("I"), '1'));  // Loop on "I" with '1'
		assertTrue(nfa.addTransition("I", Set.of("N"), '1'));  // Transition "I" -> "N" on '1'
		assertTrue(nfa.addTransition("N", Set.of("W"), '#'));  // Transition "N" -> "W" on '#'

		// Test invalid transitions
		assertFalse(nfa.addTransition("W", Set.of("K"), '0'));  // Non-existent state "K"
		assertFalse(nfa.addTransition("W", Set.of("W"), '3'));  // Invalid symbol '3'
		assertFalse(nfa.addTransition("ZZ", Set.of("W", "Z"), 'e'));  // Non-existent state "ZZ"

		return nfa;  // Return the created NFA for further tests
	}

	/**
	 * Test 3.1: Basic NFA instantiation for nfa3.
	 * Confirms that NFA object is successfully created.
	 */
	@Test
	public void test3_1() {
		NFA nfa = nfa3();
		System.out.println("nfa3 instantiation done");
	}

	/**
	 * Test 3.2: Verify the correctness of the states and settings in nfa3.
	 * - Checks that the states are correctly added and named.
	 * - Confirms that "W" is the start state and "N" is the final state.
	 */
	@Test
	public void test3_2() {
		NFA nfa = nfa3();
		assertNotNull(nfa.getState("W"));  // "W" exists
		assertEquals(nfa.getState("N").getName(), "N");  // Name of state "N" is correct
		assertNull(nfa.getState("Z0"));  // Non-existent state "Z0"
		assertEquals(nfa.getState("I").toStates('1'), Set.of(nfa.getState("I"), nfa.getState("N")));  // Correct transitions on '1' for state "I"
		assertTrue(nfa.isStart("W"));  // "W" is start state
		assertTrue(nfa.isFinal("N"));  // "N" is final state
		assertFalse(nfa.isFinal("I"));  // "I" is not final
		System.out.println("nfa3 correctness done");
	}

	/**
	 * Test 3.3: Check if nfa3 behaves as a DFA (Deterministic Finite Automaton).
	 * nfa3 should not be a DFA because it has epsilon transitions and multiple transitions for some states.
	 */
	@Test
	public void test3_3() {
		NFA nfa = nfa3();
		assertFalse(nfa.isDFA());  // nfa3 is not a DFA
		System.out.println("nfa3 isDFA done");
	}

	/**
	 * Test 3.4: Test the epsilon closure for states in nfa3.
	 * - For state "W", the epsilon closure includes "W", "L", and "I".
	 * - For state "N", the epsilon closure includes only "N".
	 */
	@Test
	public void test3_4() {
		NFA nfa = nfa3();
		assertEquals(nfa.eClosure(nfa.getState("W")), Set.of(nfa.getState("W"), nfa.getState("L"), nfa.getState("I")));  // Epsilon closure of "W"
		assertEquals(nfa.eClosure(nfa.getState("N")), Set.of(nfa.getState("N")));  // Epsilon closure of "N"
		assertEquals(nfa.eClosure(nfa.getState("L")), Set.of(nfa.getState("L"), nfa.getState("I")));  // Epsilon closure of "L"
		assertEquals(nfa.eClosure(nfa.getState("I")), Set.of(nfa.getState("I")));  // Epsilon closure of "I"
		System.out.println("nfa3 eClosure done");
	}

	/**
	 * Test 3.5: Test whether nfa3 accepts or rejects specific strings.
	 * - Confirms correct acceptance/rejection based on transitions and the NFA's language.
	 */
	@Test
	public void test3_5() {
		NFA nfa = nfa3();
		assertTrue(nfa.accepts("###"));           // Accepts "###"
		assertTrue(nfa.accepts("111#00"));        // Accepts "111#00"
		assertTrue(nfa.accepts("01#11##"));       // Accepts "01#11##"
		assertFalse(nfa.accepts("#01000###"));    // Does not accept "#01000###"
		assertFalse(nfa.accepts("011#00010#"));   // Does not accept "011#00010#"
		System.out.println("nfa3 accepts done");
	}

	/**
	 * Test 3.6: Test the max number of NFA copies created during the execution of input strings.
	 * Confirms the number of copies (states) the NFA generates for each input.
	 */
	@Test
	public void test3_6() {
		NFA nfa = nfa3();
		assertEquals(nfa.maxCopies("###"), 3);           // Three copies for "###"
		assertEquals(nfa.maxCopies("e"), 3);             // Three copies for empty string
		assertEquals(nfa.maxCopies("011#00010#"), 3);    // Three copies for "011#00010#"
		assertEquals(nfa.maxCopies("23"), 3);            // Three copies for invalid symbol
		assertEquals(nfa.maxCopies("011#00010#"), 3);    // Three copies for "011#00010#"
		System.out.println("nfa3 maxCopies done");
	}


	// Creates the NFA with an epsilon cycle
	private NFA nfa4() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('a');
		nfa.addSigma('b');

		// Add states p, q, r
		assertTrue(nfa.addState("p"));
		assertTrue(nfa.setStart("p"));

		assertTrue(nfa.addState("q"));

		assertTrue(nfa.addState("r"));
		assertTrue(nfa.setFinal("r"));

		// Add epsilon transitions
		assertTrue(nfa.addTransition("p", Set.of("q"), 'e')); // p -> q (epsilon)
		assertTrue(nfa.addTransition("q", Set.of("r"), 'e')); // q -> r (epsilon)
		assertTrue(nfa.addTransition("r", Set.of("p"), 'e')); // r -> p (epsilon) (cycle)

		// Add regular transitions
		assertTrue(nfa.addTransition("q", Set.of("q"), 'a')); // q -> q (on 'a')
		assertTrue(nfa.addTransition("r", Set.of("r"), 'b')); // r -> r (on 'b')

		return nfa;
	}

	// Test 4_1: NFA instantiation and basic operations
	@Test
	public void test4_1() {
		NFA nfa = nfa4();
		// Confirm NFA instantiation
		System.out.println("nfa4 instantiation done");
	}

	// Test 4_2: Verify state properties and NFA correctness
	@Test
	public void test4_2() {
		NFA nfa = nfa4();
		// Check that the states exist
		assertNotNull(nfa.getState("p"));
		assertNotNull(nfa.getState("q"));
		assertNotNull(nfa.getState("r"));

		// Check that state "p" is the start state and state "r" is the final state
		assertTrue(nfa.isStart("p"));
		assertTrue(nfa.isFinal("r"));

		System.out.println("nfa4 correctness done");
	}

	// Test 4_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test4_3() {
		NFA nfa = nfa4();
		// Since there are multiple epsilon transitions and cycles, this is not a DFA
		assertFalse(nfa.isDFA());
		System.out.println("nfa4 isDFA done");
	}

	// Test 4_4: Epsilon closure computation
	@Test
	public void test4_4() {
		NFA nfa = nfa4();
		// The epsilon closure of state "p" should include "p", "q", and "r" (because of the cycle)
		assertEquals(nfa.eClosure(nfa.getState("p")), Set.of(nfa.getState("p"), nfa.getState("q"), nfa.getState("r")));

		// The epsilon closure of state "r" should also include "p", "q", and "r" (because of the cycle)
		assertEquals(nfa.eClosure(nfa.getState("r")), Set.of(nfa.getState("p"), nfa.getState("q"), nfa.getState("r")));

		System.out.println("nfa4 eClosure done");
	}

	// Test 4_5: String acceptance
	@Test
	public void test4_5() {
		NFA nfa = nfa4();
		// The NFA should accept an empty string because of the epsilon transitions from "p" to "r"
		assertTrue(nfa.accepts(""));

		// The NFA should accept "a" because it can loop on "q"
		assertTrue(nfa.accepts("a"));

		// The NFA should accept "b" because it can loop on "r"
		assertTrue(nfa.accepts("b"));

		// The NFA should accept "ab" because it transitions through "q" and "r"
		assertTrue(nfa.accepts("ab"));

		// The NFA should reject "c" since "c" is not part of the alphabet
		assertFalse(nfa.accepts("c"));

		System.out.println("nfa4 accepts done");
	}

	// Test 4_6: Maximum number of NFA copies created during string processing
	@Test
	public void test4_6() {
		NFA nfa = nfa4();

		// The maximum number of copies created for an empty string should be 3 (p, q, and r because of epsilon transitions)
		assertEquals(nfa.maxCopies(""), 3);

		// The maximum number of copies created for "a" should be 3
		assertEquals(nfa.maxCopies("a"), 3);

		// The maximum number of copies created for "ab" should also be 3
		assertEquals(nfa.maxCopies("ab"), 3);

		// The maximum number of copies created for "b" should be 3
		assertEquals(nfa.maxCopies("b"), 3);

		// For "e", it should create only 3 copies (cycle through p, q, r)
		assertEquals(nfa.maxCopies("e"), 3);

		System.out.println("nfa4 maxCopies done");
	}

	// Creates the NFA with multiple epsilon transitions and invalid characters in some paths
	private NFA nfa5() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('x');
		nfa.addSigma('y');

		// Add states s, t, u, v
		assertTrue(nfa.addState("s"));
		assertTrue(nfa.setStart("s"));

		assertTrue(nfa.addState("t"));
		assertTrue(nfa.addState("u"));
		assertTrue(nfa.addState("v"));
		assertTrue(nfa.setFinal("v"));

		// Add epsilon transitions to connect the states
		assertTrue(nfa.addTransition("s", Set.of("t"), 'e')); // s -> t (epsilon)
		assertTrue(nfa.addTransition("t", Set.of("u"), 'e')); // t -> u (epsilon)
		assertTrue(nfa.addTransition("u", Set.of("v"), 'e')); // u -> v (epsilon)

		// Add regular transitions, but only for 'x' and 'y'
		assertTrue(nfa.addTransition("t", Set.of("t"), 'x')); // t -> t (on 'x')
		assertTrue(nfa.addTransition("u", Set.of("u"), 'y')); // u -> u (on 'y')

		return nfa;
	}

	// Test 5_1: NFA instantiation and basic operations
	@Test
	public void test5_1() {
		NFA nfa = nfa5();
		// Confirm NFA instantiation
		System.out.println("nfa5 instantiation done");
	}

	// Test 5_2: Verify state properties and NFA correctness
	@Test
	public void test5_2() {
		NFA nfa = nfa5();
		// Check that the states exist
		assertNotNull(nfa.getState("s"));
		assertNotNull(nfa.getState("t"));
		assertNotNull(nfa.getState("u"));
		assertNotNull(nfa.getState("v"));

		// Check that state "s" is the start state and state "v" is the final state
		assertTrue(nfa.isStart("s"));
		assertTrue(nfa.isFinal("v"));

		System.out.println("nfa5 correctness done");
	}

	// Test 5_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test5_3() {
		NFA nfa = nfa5();
		// Since there are epsilon transitions, this is not a DFA
		assertFalse(nfa.isDFA());
		System.out.println("nfa5 isDFA done");
	}

	// Test 5_4: Epsilon closure computation
	@Test
	public void test5_4() {
		NFA nfa = nfa5();
		// The epsilon closure of state "s" should include "s", "t", "u", and "v"
		assertEquals(nfa.eClosure(nfa.getState("s")), Set.of(nfa.getState("s"), nfa.getState("t"), nfa.getState("u"), nfa.getState("v")));

		// The epsilon closure of state "v" should only include "v"
		assertEquals(nfa.eClosure(nfa.getState("v")), Set.of(nfa.getState("v")));

		System.out.println("nfa5 eClosure done");
	}

	// Test 5_5: String acceptance with valid and invalid characters
	@Test
	public void test5_5() {
		NFA nfa = nfa5();
		// The NFA should accept an empty string due to epsilon transitions leading to the final state "v"
		assertTrue(nfa.accepts(""));

		// The NFA should accept "x" because of the transition on 'x' in state "t"
		assertTrue(nfa.accepts("x"));

		// The NFA should accept "xy" because of transitions on 'x' in state "t" and 'y' in state "u"
		assertTrue(nfa.accepts("xy"));

		// The NFA should reject "xyz" since 'z' is not in the alphabet
		assertFalse(nfa.accepts("xyz"));

		// The NFA should reject "z" immediately because 'z' is not part of the alphabet
		assertFalse(nfa.accepts("z"));

		System.out.println("nfa5 accepts done");
	}

	// Test 5_6: Maximum number of NFA copies created during string processing
	@Test
	public void test5_6() {
		NFA nfa = nfa5();

		// The maximum number of copies created for an empty string should be 4 (s, t, u, and v because of epsilon transitions)
		assertEquals(nfa.maxCopies(""), 4);

		// The maximum number of copies created for "x" should be 4 (since epsilon transitions lead to "v")
		assertEquals(nfa.maxCopies("x"), 4);

		// The maximum number of copies created for "xy" should be 4
		assertEquals(nfa.maxCopies("xy"), 4);

		// The maximum number of copies created for "xyz" should still be 4 before rejecting
		assertEquals(nfa.maxCopies("xyz"), 4);

		// For "z", it should create only 4 copies, but reject due to invalid character
		assertEquals(nfa.maxCopies("z"), 4);

		System.out.println("nfa5 maxCopies done");
	}

	// Creates the NFA with multiple transitions on the same symbol
	private NFA nfa6() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('a');
		nfa.addSigma('b');

		// Add states x, y, z
		assertTrue(nfa.addState("x"));
		assertTrue(nfa.setStart("x"));

		assertTrue(nfa.addState("y"));
		assertTrue(nfa.addState("z"));
		assertTrue(nfa.setFinal("z"));

		// Add multiple transitions on the same symbol
		assertTrue(nfa.addTransition("x", Set.of("y"), 'a')); // x -> y (on 'a')
		assertTrue(nfa.addTransition("x", Set.of("z"), 'a')); // x -> z (on 'a')

		// Add transitions for 'b'
		assertTrue(nfa.addTransition("y", Set.of("z"), 'b')); // y -> z (on 'b')

		return nfa;
	}

	// Test 6_1: NFA instantiation and basic operations
	@Test
	public void test6_1() {
		NFA nfa = nfa6();
		// Confirm NFA instantiation
		System.out.println("nfa6 instantiation done");
	}

	// Test 6_2: Verify state properties and NFA correctness
	@Test
	public void test6_2() {
		NFA nfa = nfa6();
		// Check that the states exist
		assertNotNull(nfa.getState("x"));
		assertNotNull(nfa.getState("y"));
		assertNotNull(nfa.getState("z"));

		// Check that state "x" is the start state and state "z" is the final state
		assertTrue(nfa.isStart("x"));
		assertTrue(nfa.isFinal("z"));

		System.out.println("nfa6 correctness done");
	}

	// Test 6_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test6_3() {
		NFA nfa = nfa6();
		// Since there are multiple transitions on 'a' from state "x", this is not a DFA
		assertFalse(nfa.isDFA());
		System.out.println("nfa6 isDFA done");
	}

	// Test 6_4: Epsilon closure computation (though no epsilon transitions exist here)
	@Test
	public void test6_4() {
		NFA nfa = nfa6();
		// Since there are no epsilon transitions, the closure should only include the state itself
		assertEquals(nfa.eClosure(nfa.getState("x")), Set.of(nfa.getState("x")));
		assertEquals(nfa.eClosure(nfa.getState("y")), Set.of(nfa.getState("y")));
		assertEquals(nfa.eClosure(nfa.getState("z")), Set.of(nfa.getState("z")));

		System.out.println("nfa6 eClosure done");
	}

	// Test 6_5: String acceptance with multiple valid transitions
	@Test
	public void test6_5() {
		NFA nfa = nfa6();
		// The NFA should accept "a" because there is a transition from "x" to "z" on 'a'
		assertTrue(nfa.accepts("a"));

		// The NFA should accept "ab" because one valid path is x -> y -> z
		assertTrue(nfa.accepts("ab"));

		// The NFA should reject "aa" because there are no transitions on 'a' from "z"
		assertFalse(nfa.accepts("aa"));

		// The NFA should reject "b" because there's no transition on 'b' from the start state "x"
		assertFalse(nfa.accepts("b"));

		// The NFA should reject "ba" since 'b' is not allowed from the start state
		assertFalse(nfa.accepts("ba"));

		System.out.println("nfa6 accepts done");
	}

	// Test 6_6: Maximum number of NFA copies created during string processing
	@Test
	public void test6_6() {
		NFA nfa = nfa6();

		// The maximum number of copies created for "a" should be 2 (since x -> y and x -> z)
		assertEquals(nfa.maxCopies("a"), 2);

		// The maximum number of copies created for "ab" should be 2 (due to non-determinism in x -> y -> z)
		assertEquals(nfa.maxCopies("ab"), 2);

		// The maximum number of copies created for "aa" should be 2 (it branches into two states after the first 'a')
		assertEquals(nfa.maxCopies("aa"), 2);

		// The maximum number of copies created for "b" should be 1 (since there's no valid transition from "x" on 'b')
		assertEquals(nfa.maxCopies("b"), 1);

		System.out.println("nfa6 maxCopies done");
	}

	// Creates the NFA to test invalid characters not in Sigma
	private NFA nfa7() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('0');
		nfa.addSigma('1');

		// Add states a, b, c
		assertTrue(nfa.addState("a"));
		assertTrue(nfa.setStart("a"));

		assertTrue(nfa.addState("b"));
		assertTrue(nfa.addState("c"));
		assertTrue(nfa.setFinal("c"));

		// Add transitions on valid characters
		assertTrue(nfa.addTransition("a", Set.of("b"), '0')); // a -> b on '0'
		assertTrue(nfa.addTransition("b", Set.of("c"), '1')); // b -> c on '1'

		return nfa;
	}

	// Test 7_1: NFA instantiation and basic operations
	@Test
	public void test7_1() {
		NFA nfa = nfa7();
		// Confirm NFA instantiation
		System.out.println("nfa7 instantiation done");
	}

	// Test 7_2: Verify state properties and NFA correctness
	@Test
	public void test7_2() {
		NFA nfa = nfa7();
		// Check that the states exist
		assertNotNull(nfa.getState("a"));
		assertNotNull(nfa.getState("b"));
		assertNotNull(nfa.getState("c"));

		// Check that state "a" is the start state and state "c" is the final state
		assertTrue(nfa.isStart("a"));
		assertTrue(nfa.isFinal("c"));

		System.out.println("nfa7 correctness done");
	}

	// Test 7_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test7_3() {
		NFA nfa = nfa7();
		// Since there are no epsilon transitions or multiple transitions on the same symbol, this could be a DFA
		assertTrue(nfa.isDFA());
		System.out.println("nfa7 isDFA done");
	}

	// Test 7_4: Epsilon closure computation (though no epsilon transitions exist here)
	@Test
	public void test7_4() {
		NFA nfa = nfa7();
		// Since there are no epsilon transitions, the closure should only include the state itself
		assertEquals(nfa.eClosure(nfa.getState("a")), Set.of(nfa.getState("a")));
		assertEquals(nfa.eClosure(nfa.getState("b")), Set.of(nfa.getState("b")));
		assertEquals(nfa.eClosure(nfa.getState("c")), Set.of(nfa.getState("c")));

		System.out.println("nfa7 eClosure done");
	}

	// Test 7_5: String acceptance with invalid characters
	@Test
	public void test7_5() {
		NFA nfa = nfa7();
		// The NFA should accept "01" because it's a valid sequence of transitions
		assertTrue(nfa.accepts("01"));

		// The NFA should reject strings with invalid characters (not in Sigma)
		assertFalse(nfa.accepts("a"));     // 'a' is not in Sigma
		assertFalse(nfa.accepts("012"));   // '2' is not in Sigma
		assertFalse(nfa.accepts("1a0"));   // 'a' is not in Sigma
		assertFalse(nfa.accepts("b"));     // 'b' is not in Sigma
		assertFalse(nfa.accepts("01#"));   // '#' is not in Sigma

		System.out.println("nfa7 accepts done");
	}

	// Test 7_6: Maximum number of NFA copies created during string processing
	@Test
	public void test7_6() {
		NFA nfa = nfa7();

		// The maximum number of copies created for "01" should be 1 (deterministic path a -> b -> c)
		assertEquals(nfa.maxCopies("01"), 1);

		// The maximum number of copies created for "a" should be 1 (rejected, no valid transitions)
		assertEquals(nfa.maxCopies("a"), 1);

		// The maximum number of copies created for "012" should be 1 (rejected, invalid character)
		assertEquals(nfa.maxCopies("012"), 1);

		// The maximum number of copies created for "1a0" should be 1 (rejected, invalid character)
		assertEquals(nfa.maxCopies("1a0"), 1);

		System.out.println("nfa7 maxCopies done");
	}

	// Creates NFA with overlapping epsilon and symbol transitions
	private NFA nfa8() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('x');
		nfa.addSigma('y');

		// Add states
		assertTrue(nfa.addState("s"));
		System.out.println("Added state: s");

		assertTrue(nfa.addState("t"));
		System.out.println("Added state: t");

		assertTrue(nfa.addState("u"));
		System.out.println("Added state: u");

		assertTrue(nfa.addState("v"));
		System.out.println("Added state: v");

		// Set the start and final states
		assertTrue(nfa.setStart("s"));
		assertTrue(nfa.setFinal("v"));

		// Add epsilon transitions
		assertTrue(nfa.addTransition("s", Set.of("t"), 'e')); // s -> t (epsilon)
		System.out.println("Added epsilon transition: s -> t");

		assertTrue(nfa.addTransition("t", Set.of("u"), 'e')); // t -> u (epsilon)
		System.out.println("Added epsilon transition: t -> u");

		assertTrue(nfa.addTransition("u", Set.of("v"), 'e')); // u -> v (epsilon)
		System.out.println("Added epsilon transition: u -> v");

		// Add regular transitions for 'x' and 'y'
		assertTrue(nfa.addTransition("t", Set.of("t"), 'x')); // t -> t (on 'x')
		System.out.println("Added regular transition: t -> t (on 'x')");

		assertTrue(nfa.addTransition("u", Set.of("u"), 'y')); // u -> u (on 'y')
		System.out.println("Added regular transition: u -> u (on 'y')");

		return nfa;
	}

	// Test 8_1: NFA instantiation and basic operations
	@Test
	public void test8_1() {
		NFA nfa = nfa8();
		// Confirm NFA instantiation
		System.out.println("nfa8 instantiation done");
	}

	// Test 8_2: Verify state properties and NFA correctness
	@Test
	public void test8_2() {
		NFA nfa = nfa8();
		// Check that the states exist
		assertNotNull(nfa.getState("s"));
		assertNotNull(nfa.getState("t"));
		assertNotNull(nfa.getState("u"));
		assertNotNull(nfa.getState("v"));

		// Check that state "s" is the start state and state "v" is the final state
		assertTrue(nfa.isStart("s"));
		assertTrue(nfa.isFinal("v"));

		System.out.println("nfa8 correctness done");
	}

	// Test 8_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test8_3() {
		NFA nfa = nfa8();
		// Since there are epsilon transitions, this is not a DFA
		assertFalse(nfa.isDFA());
		System.out.println("nfa8 isDFA done");
	}

	// Test 8_4: Epsilon closure computation
	@Test
	public void test8_4() {
		NFA nfa = nfa8();

		// The epsilon closure of state "s" should include "s", "t", "u", and "v"
		assertEquals(nfa.eClosure(nfa.getState("s")), Set.of(nfa.getState("s"), nfa.getState("t"), nfa.getState("u"), nfa.getState("v")));

		// The epsilon closure of state "v" should only include "v"
		assertEquals(nfa.eClosure(nfa.getState("v")), Set.of(nfa.getState("v")));

		System.out.println("nfa8 eClosure done");
	}

	// Test 8_5: String acceptance with overlapping epsilon and symbol transitions
	@Test
	public void test8_5() {
		NFA nfa = nfa8();

		// "x" should be accepted
		assertTrue(nfa.accepts("x"));

		// "y" should be accepted
		assertTrue(nfa.accepts("y"));

		// "xy" should be accepted
		assertTrue(nfa.accepts("xy"));

		// "z" should be rejected
		assertFalse(nfa.accepts("z"));

		System.out.println("nfa8 accepts done");
	}

	// Test 8_6: Maximum number of NFA copies created during string processing
	@Test
	public void test8_6() {
		NFA nfa = nfa8();

		// The maximum number of copies created for an empty string should be 4 (s, t, u, and v because of epsilon transitions)
		assertEquals(nfa.maxCopies(""), 4);

		// The maximum number of copies created for "x" should be 4 (since epsilon transitions lead to "v")
		assertEquals(nfa.maxCopies("x"), 4);

		// The maximum number of copies created for "xy" should be 4
		assertEquals(nfa.maxCopies("xy"), 4);

		// The maximum number of copies created for "xyz" should still be 4 before rejecting
		assertEquals(nfa.maxCopies("xyz"), 4);

		System.out.println("nfa8 maxCopies done");
	}

	// Creates NFA with multiple epsilon transitions converging on a single state
	private NFA nfa9() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('a');
		nfa.addSigma('b');

		// Add states
		assertTrue(nfa.addState("p"));
		assertTrue(nfa.setStart("p"));

		assertTrue(nfa.addState("q"));
		assertTrue(nfa.addState("r"));
		assertTrue(nfa.addState("s")); // Converged state
		assertTrue(nfa.addState("t"));
		assertTrue(nfa.setFinal("t"));

		// Add epsilon transitions
		assertTrue(nfa.addTransition("p", Set.of("q"), 'e'));  // p -> q (epsilon)
		assertTrue(nfa.addTransition("p", Set.of("r"), 'e'));  // p -> r (epsilon)
		assertTrue(nfa.addTransition("q", Set.of("s"), 'e'));  // q -> s (epsilon)
		assertTrue(nfa.addTransition("r", Set.of("s"), 'e'));  // r -> s (epsilon)

		// Add regular transitions
		assertTrue(nfa.addTransition("s", Set.of("t"), 'a'));  // s -> t (on 'a')
		assertTrue(nfa.addTransition("t", Set.of("t"), 'b'));  // t -> t (loop on 'b')

		return nfa;
	}

	// Test 9_1: NFA instantiation and basic operations
	@Test
	public void test9_1() {
		NFA nfa = nfa9();
		// Confirm NFA instantiation
		System.out.println("nfa9 instantiation done");
	}

	// Test 9_2: Verify state properties and NFA correctness
	@Test
	public void test9_2() {
		NFA nfa = nfa9();
		// Check that the states exist
		assertNotNull(nfa.getState("p"));
		assertNotNull(nfa.getState("q"));
		assertNotNull(nfa.getState("r"));
		assertNotNull(nfa.getState("s"));
		assertNotNull(nfa.getState("t"));

		// Check that state "p" is the start state and state "t" is the final state
		assertTrue(nfa.isStart("p"));
		assertTrue(nfa.isFinal("t"));

		System.out.println("nfa9 correctness done");
	}

	// Test 9_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test9_3() {
		NFA nfa = nfa9();
		// Since there are epsilon transitions, this is not a DFA
		assertFalse(nfa.isDFA());
		System.out.println("nfa9 isDFA done");
	}

	// Test 9_4: Epsilon closure computation
	@Test
	public void test9_4() {
		NFA nfa = nfa9();
		// The epsilon closure of state "p" should include "p", "q", "r", and "s"
		assertEquals(nfa.eClosure(nfa.getState("p")), Set.of(nfa.getState("p"), nfa.getState("q"), nfa.getState("r"), nfa.getState("s")));

		// The epsilon closure of state "s" should only include "s"
		assertEquals(nfa.eClosure(nfa.getState("s")), Set.of(nfa.getState("s")));

		System.out.println("nfa9 eClosure done");
	}

	// Test 9_5: String acceptance with multiple epsilon transitions converging
	@Test
	public void test9_5() {
		NFA nfa = nfa9();

		// The NFA should accept "a" because both epsilon paths (p -> q -> s and p -> r -> s) converge on "s" and then go to "t"
		assertTrue(nfa.accepts("a"));

		// The NFA should accept "ab" because of the loop on "t"
		assertTrue(nfa.accepts("ab"));

		// The NFA should reject "b" since it requires first reaching "t" via "a"
		assertFalse(nfa.accepts("b"));

		System.out.println("nfa9 accepts done");
	}

	// Test 9_6: Maximum number of NFA copies created during string processing
	@Test
	public void test9_6() {
		NFA nfa = nfa9();

		// The maximum number of copies created for an empty string should be 4 (p, q, r, and s because of epsilon transitions)
		assertEquals(nfa.maxCopies(""), 4);

		// The maximum number of copies created for "a" should be 4 (since both epsilon paths converge on "s")
		assertEquals(nfa.maxCopies("a"), 4);

		// The maximum number of copies created for "ab" should be 4
		assertEquals(nfa.maxCopies("ab"), 4);

		System.out.println("nfa9 maxCopies done");
	}

	// Creates NFA with overlapping epsilon and symbol transitions leading to non-determinism
	private NFA nfa10() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('x');
		nfa.addSigma('y');

		// Add states
		assertTrue(nfa.addState("a"));
		assertTrue(nfa.setStart("a"));

		assertTrue(nfa.addState("b"));
		assertTrue(nfa.addState("c"));
		assertTrue(nfa.addState("d")); // Final state
		assertTrue(nfa.setFinal("d"));

		// Add epsilon transitions
		assertTrue(nfa.addTransition("a", Set.of("b"), 'e'));  // a -> b (epsilon)
		assertTrue(nfa.addTransition("a", Set.of("c"), 'e'));  // a -> c (epsilon)

		// Add overlapping symbol transitions
		assertTrue(nfa.addTransition("b", Set.of("d"), 'x'));  // b -> d (on 'x')
		assertTrue(nfa.addTransition("c", Set.of("d"), 'y'));  // c -> d (on 'y')
		assertTrue(nfa.addTransition("b", Set.of("b"), 'y'));  // b -> b (on 'y') (loop)

		return nfa;
	}

	// Test 10_1: NFA instantiation and basic operations
	@Test
	public void test10_1() {
		NFA nfa = nfa10();
		// Confirm NFA instantiation
		System.out.println("nfa10 instantiation done");
	}

	// Test 10_2: Verify state properties and NFA correctness
	@Test
	public void test10_2() {
		NFA nfa = nfa10();
		// Check that the states exist
		assertNotNull(nfa.getState("a"));
		assertNotNull(nfa.getState("b"));
		assertNotNull(nfa.getState("c"));
		assertNotNull(nfa.getState("d"));

		// Check that state "a" is the start state and state "d" is the final state
		assertTrue(nfa.isStart("a"));
		assertTrue(nfa.isFinal("d"));

		System.out.println("nfa10 correctness done");
	}

	// Test 10_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test10_3() {
		NFA nfa = nfa10();
		// Since there are epsilon transitions, this is not a DFA
		assertFalse(nfa.isDFA());
		System.out.println("nfa10 isDFA done");
	}

	// Test 10_4: Epsilon closure computation with overlapping transitions
	@Test
	public void test10_4() {
		NFA nfa = nfa10();

		// The epsilon closure of state "a" should include "a", "b", and "c"
		assertEquals(nfa.eClosure(nfa.getState("a")), Set.of(nfa.getState("a"), nfa.getState("b"), nfa.getState("c")));

		// The epsilon closure of state "b" should include "b" only
		assertEquals(nfa.eClosure(nfa.getState("b")), Set.of(nfa.getState("b")));

		System.out.println("nfa10 eClosure done");
	}

	// Test 10_5: String acceptance with overlapping epsilon and symbol transitions
	@Test
	public void test10_5() {
		NFA nfa = nfa10();

		// "x" should be accepted via a -> b -> d
		assertTrue(nfa.accepts("x"));

		// "y" should be accepted via a -> c -> d
		assertTrue(nfa.accepts("y"));

		// "xy" should not be accepted because "y" causes a loop in "b"
		assertFalse(nfa.accepts("xy"));

		System.out.println("nfa10 accepts done");
	}

	// Test 10_6: Maximum number of NFA copies created during string processing
	@Test
	public void test10_6() {
		NFA nfa = nfa10();

		// The maximum number of copies created for an empty string should be 3 (a, b, and c because of epsilon transitions)
		assertEquals(nfa.maxCopies(""), 3);

		// The maximum number of copies created for "x" should be 3 (since epsilon transitions lead to both b and c)
		assertEquals(nfa.maxCopies("x"), 3);

		// The maximum number of copies created for "xy" should be 3 before rejecting
		assertEquals(nfa.maxCopies("xy"), 3);

		System.out.println("nfa10 maxCopies done");
	}

	// Creates NFA with complex epsilon transitions and non-deterministic behavior
	private NFA nfa11() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('0');
		nfa.addSigma('1');
		nfa.addSigma('2');

		// Add states
		assertTrue(nfa.addState("start"));
		assertTrue(nfa.setStart("start"));

		assertTrue(nfa.addState("intermediate1"));
		assertTrue(nfa.addState("intermediate2"));
		assertTrue(nfa.addState("intermediate3"));
		assertTrue(nfa.addState("final"));
		assertTrue(nfa.setFinal("final"));

		// Add epsilon transitions
		assertTrue(nfa.addTransition("start", Set.of("intermediate1"), 'e'));  // start -> intermediate1 (epsilon)
		assertTrue(nfa.addTransition("start", Set.of("intermediate2"), 'e'));  // start -> intermediate2 (epsilon)
		assertTrue(nfa.addTransition("intermediate1", Set.of("intermediate3"), 'e'));  // intermediate1 -> intermediate3 (epsilon)
		assertTrue(nfa.addTransition("intermediate2", Set.of("intermediate3"), 'e'));  // intermediate2 -> intermediate3 (epsilon)

		// Add regular transitions
		assertTrue(nfa.addTransition("intermediate3", Set.of("final"), '0'));  // intermediate3 -> final (on '0')
		assertTrue(nfa.addTransition("final", Set.of("final"), '1'));  // final -> final (loop on '1')

		return nfa;
	}

	// Test 11_1: NFA instantiation and basic operations
	@Test
	public void test11_1() {
		NFA nfa = nfa11();
		// Confirm NFA instantiation
		System.out.println("nfa11 instantiation done");
	}

	// Test 11_2: Verify state properties and NFA correctness
	@Test
	public void test11_2() {
		NFA nfa = nfa11();
		// Check that the states exist
		assertNotNull(nfa.getState("start"));
		assertNotNull(nfa.getState("intermediate1"));
		assertNotNull(nfa.getState("intermediate2"));
		assertNotNull(nfa.getState("intermediate3"));
		assertNotNull(nfa.getState("final"));

		// Check that state "start" is the start state and state "final" is the final state
		assertTrue(nfa.isStart("start"));
		assertTrue(nfa.isFinal("final"));

		System.out.println("nfa11 correctness done");
	}

	// Test 11_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test11_3() {
		NFA nfa = nfa11();
		// Since there are epsilon transitions, this is not a DFA
		assertFalse(nfa.isDFA());
		System.out.println("nfa11 isDFA done");
	}

	// Test 11_4: Epsilon closure computation with complex overlapping transitions
	@Test
	public void test11_4() {
		NFA nfa = nfa11();

		// The epsilon closure of state "start" should include "start", "intermediate1", "intermediate2", and "intermediate3"
		assertEquals(nfa.eClosure(nfa.getState("start")), Set.of(nfa.getState("start"), nfa.getState("intermediate1"), nfa.getState("intermediate2"), nfa.getState("intermediate3")));

		// The epsilon closure of state "intermediate1" should include "intermediate1", "intermediate3"
		assertEquals(nfa.eClosure(nfa.getState("intermediate1")), Set.of(nfa.getState("intermediate1"), nfa.getState("intermediate3")));

		// The epsilon closure of state "final" should only include "final"
		assertEquals(nfa.eClosure(nfa.getState("final")), Set.of(nfa.getState("final")));

		System.out.println("nfa11 eClosure done");
	}

	// Test 11_5: String acceptance with complex epsilon and symbol transitions
	@Test
	public void test11_5() {
		NFA nfa = nfa11();

		// "0" should be accepted because it can reach the final state via epsilon transitions
		assertTrue(nfa.accepts("0"));

		// "01" should be accepted because of the loop on '1' in the final state
		assertTrue(nfa.accepts("01"));

		// "1" should not be accepted because '1' requires first transitioning via '0'
		assertFalse(nfa.accepts("1"));

		System.out.println("nfa11 accepts done");
	}

	// Test 11_6: Maximum number of NFA copies created during string processing
	@Test
	public void test11_6() {
		NFA nfa = nfa11();

		// The maximum number of copies created for an empty string should be 4 (start, intermediate1, intermediate2, and intermediate3)
		assertEquals(nfa.maxCopies(""), 4);

		// The maximum number of copies created for "0" should be 4 (due to epsilon transitions)
		assertEquals(nfa.maxCopies("0"), 4);

		// The maximum number of copies created for "01" should be 4
		assertEquals(nfa.maxCopies("01"), 4);

		System.out.println("nfa11 maxCopies done");
	}

	// Creates the NFA for test 12
	private NFA nfa12() {
		NFA nfa = new NFA();

		// Add sigma for the alphabet
		nfa.addSigma('a');
		nfa.addSigma('b');
		nfa.addSigma('c');

		// Add states s, t, u
		assertTrue(nfa.addState("s"));
		assertTrue(nfa.setStart("s"));

		assertTrue(nfa.addState("t"));
		assertTrue(nfa.addState("u"));  // Introducing state 'u' for nondeterminism
		assertTrue(nfa.setFinal("t"));

		// Add transitions
		assertTrue(nfa.addTransition("s", Set.of("t"), 'a'));  // s -> t on 'a'

		// Introduce nondeterminism: s -> u on 'a' as well
		assertTrue(nfa.addTransition("s", Set.of("u"), 'a'));  // s -> u on 'a'

		assertTrue(nfa.addTransition("t", Set.of("t"), 'b'));  // t loops on 'b'
		assertTrue(nfa.addTransition("t", Set.of("t"), 'a'));  // t -> t on 'a'

		// Add transition from 'u' back to 's' on 'c' to create a looping structure
		assertTrue(nfa.addTransition("u", Set.of("s"), 'c'));  // u -> s on 'c'

		return nfa;
	}

	// Test 12_1: NFA instantiation and basic operations
	@Test
	public void test12_1() {
		NFA nfa = nfa12();
		// Confirm NFA instantiation
		System.out.println("nfa12 instantiation done");
	}

	// Test 12_2: Verify state properties and NFA correctness
	@Test
	public void test12_2() {
		NFA nfa = nfa12();
		// Check that the states exist
		assertNotNull(nfa.getState("s"));
		assertNotNull(nfa.getState("t"));
		assertNotNull(nfa.getState("u"));

		// Check that state "s" is the start state and state "t" is the final state
		assertTrue(nfa.isStart("s"));
		assertTrue(nfa.isFinal("t"));

		System.out.println("nfa12 correctness done");
	}

	// Test 12_3: Determine if the NFA is a DFA (it should not be)
	@Test
	public void test12_3() {
		NFA nfa = nfa12();
		// Since there are multiple transitions on the same symbol, this is not a DFA
		assertFalse(nfa.isDFA());
		System.out.println("nfa12 isDFA done");
	}

	// Test 12_4: Epsilon closure computation with overlapping transitions
	@Test
	public void test12_4() {
		NFA nfa = nfa12();

		// The epsilon closure of state "s" should include only "s"
		assertEquals(nfa.eClosure(nfa.getState("s")), Set.of(nfa.getState("s")));

		// The epsilon closure of state "t" should include only "t"
		assertEquals(nfa.eClosure(nfa.getState("t")), Set.of(nfa.getState("t")));

		System.out.println("nfa12 eClosure done");
	}

	// Test 12_5: String acceptance with complex transitions and loops
	@Test
	public void test12_5() {
		NFA nfa = nfa12();

		// "a" should be accepted via s -> t
		assertTrue(nfa.accepts("a"));

		// "ab" should be accepted via s -> t -> t
		assertTrue(nfa.accepts("ab"));

		// "abc" should be rejected because 'c' transitions back to "s" which is not a final state
		assertFalse(nfa.accepts("abc"));

		// "abab" should be accepted as the NFA loops through t on 'b' and handles 'a'
		assertTrue(nfa.accepts("abab"));

		// "ca" should be rejected as 'c' needs to be after 'a'
		assertFalse(nfa.accepts("ca"));

		System.out.println("nfa12 accepts done");
	}

	// Test 12_6: Maximum number of NFA copies created during string processing
	@Test
	public void test12_6() {
		NFA nfa = nfa12();

		// The maximum number of copies created for an empty string should be 1 (no epsilon transitions here)
		assertEquals(nfa.maxCopies(""), 1);

		// The maximum number of copies created for "a" should be 2 (because of non-determinism: s -> t and s -> u)
		assertEquals(nfa.maxCopies("a"), 2);

		// The maximum number of copies created for "ab" should still be 2 (deterministic transitions after "a")
		assertEquals(nfa.maxCopies("ab"), 2);

		// The maximum number of copies created for "abab" should be 2 (deterministic transitions after "a")
		assertEquals(nfa.maxCopies("abab"), 2);

		System.out.println("nfa12 maxCopies done");
	}


}
