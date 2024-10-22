package fa.nfa;

import fa.State;
import java.util.*;

/**
 * This class represents a state in a Non-deterministic Finite Automaton (NFA).
 * It extends the abstract class State and manages transitions to other states
 * based on input symbols.
 */
public class NFAState extends State {

    protected Map<Character, Set<NFAState>> transitions;
    protected Set<NFAState> epsilonTransitions;
    protected  boolean isStart;
    protected String name;

    /**
     * Constructs an NFAState with the given name.
     * 
     * @param name The name of the state.
     */
    public NFAState(String name) {
        this.name = name;
        this.isStart = false; 
        this.epsilonTransitions = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    /**
     * Adds a transition from this state to another state based on the input symbol.
     * If a transition on the given symbol already exists, the destination state is added
     * to the set of possible states.
     * 
     * @param symbol The input symbol for the transition.
     * @param toState The state to transition to on the given symbol.
     */
    public void setTransition(Character symbol, NFAState toState) {
        Set<NFAState> toStates = transitions.get(symbol);
        if (toStates == null) {
            toStates = new HashSet<>();
            transitions.put(symbol, toStates);
        }
        toStates.add(toState);
    }

    /**
     * Retrieves the set of states that can be reached from this state on a given symbol.
     * 
     * @param symbol The input symbol.
     * @return A set of states that can be transitioned to on the input symbol.
     *         If no transition exists for the symbol, returns an empty set.
     */
    public Set<NFAState> getTransition(Character symbol) {
        if (transitions.get(symbol) != null) {
            return transitions.get(symbol);
        }
        return null;
    }

    /**
     * Marks this state as the start state of the NFA.
     * 
     * @param isStart True to mark this state as the start state, false otherwise.
     */
    public void setStartState(boolean isStart) {
        this.isStart = isStart;
    }

    /**
     * Checks if this state is the start state of the NFA.
     * 
     * @return True if this is the start state, false otherwise.
     */
    public boolean isStartState() {
        return isStart;
    }

    /**
     * Gets the name of the state.
     * 
     * @return name
     */
    @Override
    public String getName() {
        return name; 
    }

    /**
     * Gets epsilon transition.
     * @return epsilonTransitions
     */
    public Set<NFAState> getEpsilonTransitions() {
        return epsilonTransitions; 
    }

    /**
     * Adds an epsilon transition.
     * 
     * @param toState
     */
    public void addEpsilonTransition(NFAState toState) {
        epsilonTransitions.add(toState);
    }
}
