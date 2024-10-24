package fa.nfa;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

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
        return from.toStates(onSymb);
    }

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
        for (NFAState state : finalStates) {
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
