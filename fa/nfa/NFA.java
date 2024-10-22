package fa.nfa;

import java.util.HashSet;
import java.util.Set;

import fa.State;

public class NFA implements NFAInterface{
    private Set<NFAState> states;
    private Set<Character> sigma;
    private Set<NFAState> finalStates;

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
        throw new UnsupportedOperationException("Not supported yet.");
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
                if (transitions.size() > 1 || state.getEpsilonTransitions().size() > 0) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStart'");
    }

    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

    @Override
    public boolean accepts(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accepts'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFinal'");
    }

    @Override
    public boolean isStart(String name) {
        NFAState state = getState(name);
        return state != null && state.isStart;
    }
    
}
