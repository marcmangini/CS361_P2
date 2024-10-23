package fa.nfa;

import java.util.HashSet;
import java.util.Set;

public class NFA implements NFAInterface{
    private Set<NFAState> states;
    private Set<Character> sigma;
    private Set<NFAState> finalStates;
    private NFAState startState;

    public NFA() {
        states = new HashSet<>();
        sigma = new HashSet<>();
        finalStates = new HashSet<>();
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTransition(onSymb);
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> eClosureStates = new HashSet<>();
        Set<NFAState> stack = new HashSet<>();
        
        eClosureStates.add(s);
        stack.add(s);

        while (!stack.isEmpty()) {
            NFAState currentState = stack.iterator().next(); 
            stack.remove(currentState); 

            // Checks for epsilon transitions
            for (NFAState nextState : currentState.getEpsilonTransitions()) {
                if (!eClosureStates.contains(nextState)) {
                    eClosureStates.add(nextState); 
                    stack.add(nextState); 
                }
            }
        }

        return eClosureStates; 
    }

    @Override
    public int maxCopies(String s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDFA() {
        for (NFAState state : states) {
            for (Character symbol : sigma) {
                Set<NFAState> transitions = state.getTransition(symbol);
                if (transitions.size() > 1 || !state.getEpsilonTransitions().isEmpty()) {
                    return false; 
                }
            }
        }
        return true;
    }

    @Override
    public boolean addState(String name) {
        if (getState(name) != null) {
            return false; 
        }
        states.add(new NFAState(name)); 
        return true;
    }

    @Override
    public boolean setFinal(String name) {
        NFAState state = getState(name);
        if (state == null) {
            return false; 
        }
        finalStates.add(state); 
        return true;
    }

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


    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

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
    

    @Override
    public Set<Character> getSigma() {
        return new HashSet<>(sigma);
    }

    @Override
    public NFAState getState(String name) {
        for (NFAState state : states) {
            if (state.getName().equals(name)) {
                return state; 
            }
        }
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        for (NFAState state : states) {
            if (state.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

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
