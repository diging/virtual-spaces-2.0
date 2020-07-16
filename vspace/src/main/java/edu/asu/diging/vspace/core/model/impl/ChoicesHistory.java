package edu.asu.diging.vspace.core.model.impl;

import java.util.Stack;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChoicesHistory{
    private Stack<String> sequenceSlideHistory = new Stack<>();
    
    public void addToSequenceSlideHistory(String sequenceId, String slideId) {
        sequenceSlideHistory.push(sequenceId+","+slideId);
    }
    
    public void addToSequenceSlideHistory(String lastElement) {
        sequenceSlideHistory.push(lastElement);
    }
    
    public Stack<String> getFromSequenceSlideHistory() {
        return sequenceSlideHistory;
    }
    
    public String removeLastElementFromSequenceSlideHistory() {
        return sequenceSlideHistory.pop();
    }
    
}
