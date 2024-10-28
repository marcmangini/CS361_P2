package fa.nfa;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * This class represents a Non-deterministic Finite Automaton or NFA. It implements
 * the NFAInterface and provides methods for state transitions, epsilon closures,
 * and checking if a given input string is accepted by the NFA.
 */
public class NFA implements NFAInterface{
    private Set<NFAState> states;
    private Set<Character> sigma;
    private Set<NFAState> finalStates;
    private NFAState startState;

    /**
     * Constructs an empty NFA with no states, symbols, or final states.
     */
    public NFA() {
        states = new HashSet<>();
        sigma = new HashSet<>();
        finalStates = new HashSet<>();
    }

     /**
     * Returns the set of states reachable from the specified state on the given symbol.
     *
     * @param from the state to start from
     * @param onSymb the symbol on which to transition
     * @return a set of states reachable on the symbol from the starting state
     */
    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.toStates(onSymb);
    }

     /**
     * Computes the epsilon closure of the given state, which includes all states
     * reachable from the initial state via epsilon transitions.
     *
     * @param s the state from which to compute epsilon closure
     * @return a set of states in the epsilon closure of the given state
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> eClosureStates = new HashSet<>();
        Stack<NFAState> stack = new Stack<>();

        eClosureStates.add(s); 
        stack.push(s); 

        for (NFAState state : states) {
            state.visited = false;
        }

        while (!stack.isEmpty()) {
            NFAState currentState = stack.pop();
            if (!currentState.visited) {
                Set<NFAState> epsilonTransitions = currentState.toStates('e');
                if (epsilonTransitions != null) {
                    eClosureStates.addAll(epsilonTransitions);
                    stack.addAll(epsilonTransitions);
                }
                currentState.visited = true;
            }
        }

        return eClosureStates;
    }

    /**
     * Computes the max number of NFA states that can be active during
     * the traversal of a given string.
     *
     * @param s the input string
     * @return the maximum number of active states at any point during traversal
     */
    @Override
    public int maxCopies(String s) {
        int maxCopies = 0;

        Set<NFAState> currentStates = new HashSet<>();
        Set<NFAState> nextStates = new HashSet<>();
    
        if (startState == null) {
            return -1;  
        }
    
        currentStates.add(startState);
        currentStates.addAll(eClosure(startState));
    
        maxCopies = Math.max(maxCopies, currentStates.size());
    
        for (char symbol : s.toCharArray()) {
            for (NFAState state : currentStates) {
                Set<NFAState> transitions = state.toStates(symbol);
                if (transitions != null) {
                    nextStates.addAll(transitions);
                }
            }
    
            Set<NFAState> closureStates = new HashSet<>();
            for (NFAState state : nextStates) {
                closureStates.addAll(eClosure(state));
            }
    
            currentStates.clear();
            currentStates.addAll(closureStates);
            maxCopies = Math.max(maxCopies, currentStates.size());
    
            nextStates.clear();
        }
        return maxCopies;
    }

     /**
     * Adds a transition from a given state to a set of target states on a specified symbol.
     *
     * @param fromState the name of the state from which the transition originates
     * @param toStates the names of the target states to which the transition goes
     * @param onSymb the symbol on which the transition occurs
     * @return true if the transition was added successfully, false otherwise
     */
    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        NFAState from = getState(fromState);
        
        // CheckS if the from exists
        if (from == null) {
            return false; 
        }

        // Checks if the symbol is part of the alphabet
        if (!sigma.contains(onSymb) && onSymb != 'e') {
            return false; 
        }

        for (String toStateName : toStates) {
            NFAState to = getState(toStateName);
            if (to == null) {
                return false; 
            }
            from.setTransition(onSymb, to);
        }
        addSigma(onSymb); 
        return true;
    }

    /**
     * Checks if an NFA can also be a DFA.
     *
     * @return true if the NFA is a DFA
     */
    @Override
    public boolean isDFA() {
        if (states == null || sigma == null) {
            return false; 
        }
    
        for (NFAState state : states) {
            // Checks for null state
            if (state == null) {
                continue; 
            }
    
            // Verifies each symbol in the alphabet
            for (Character symbol : sigma) {
                Set<NFAState> transitions = state.toStates(symbol);
                
                // If theres multiple transitions its not a DFA
                if (transitions != null && transitions.size() > 1) {
                    return false; 
                }
            }
    
            // Checks for epsilon transitions
            if (state.toStates('e') != null) {
                return false; 
            }
        }
    
        return true; 
    }
    
    

    /**
     * Adds a new state to the NFA.
     *
     * @param name the name of the state to add
     * @return true if the state was added successfully and false if it already exists
     */
    @Override
    public boolean addState(String name) {
        if (getState(name) != null) {
            return false; 
        }
        states.add(new NFAState(name)); 
        return true;
    }

    /**
     * Sets a specified state as a final state.
     *
     * @param name the name of the state
     * @return true if the state was found and set as final, false otherwise
     */
    @Override
    public boolean setFinal(String name) {
        NFAState state = getState(name);
        if (state == null) {
            return false; 
        }
        finalStates.add(state); 
        return true;
    }

    /**
     * Sets the state as the start state.
     *
     * @param name the name of the state
     * @return true if the start state was set successfully, false otherwise
     */
    @Override
    public boolean setStart(String name) {
        for (NFAState state : states) {
            if (state.getName().equals(name)) {
                state.setStartState(true);
                startState = state; 
                return true;
            }
        }
        return false;
    }


    /**
     * Adds a symbol to the NFA's alphabet.
     *
     * @param symbol the symbol to add
     */
    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

     /**
     * Checks if the NFA accepts a string.
     *
     * @param s the input string to check
     * @return true if the input string is accepted by the NFA, false otherwise
     */
    @Override
    public boolean accepts(String s) {
        if (startState == null) {
            return false;
        }
    
        Set<NFAState> currentStates = eClosure(startState);
    
        for (char symbol : s.toCharArray()) {
            Set<NFAState> nextStates = new HashSet<>();
            for (NFAState state : currentStates) {
                Set<NFAState> transitions = getToState(state, symbol);
                if (transitions != null) {
                    nextStates.addAll(transitions);
                }
            }
    
            // Applies epsilon closure to the next states
            currentStates = new HashSet<>();
            for (NFAState state : nextStates) {
                currentStates.addAll(eClosure(state)); 
            }
        }
    
        // Checks if any of the current states are final states
        for (NFAState state : currentStates) {
            if (finalStates.contains(state)) {
                return true;
            }
        }
    
        return false;
    }
    
    /**
     * Gets the alphabet of the NFA.
     *
     * @return a set of characters representing the alphabet
     */
    @Override
    public Set<Character> getSigma() {
        return new HashSet<>(sigma);
    }

    /**
     * Finds a state by name within the NFA.
     *
     * @param name the name of the state to find
     * @return the state with the specified name, or null if not found
     */
    @Override
    public NFAState getState(String name) {
        for (NFAState state : states) {
            if (state.getName().equals(name)) {
                return state; 
            }
        }
        return null;
    }

    /**
     * Checks if the state is a final state.
     *
     * @param name the name of the state to check
     * @return true if the state is a final state, false otherwise
     */
    @Override
    public boolean isFinal(String name) {
        for (NFAState state : finalStates) {
            if (state.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the state is a start state.
     *
     * @param name the name of the state to check
     * @return true if the state is a start state, false otherwise
     */
    @Override
    public boolean isStart(String name) {
        for (NFAState state : states) {
            if (state.isStartState() && state.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
}
