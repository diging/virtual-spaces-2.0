package edu.asu.diging.vspace.core.model.impl;

import java.util.Stack;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChoicesHistory{

    class ChoicesHistoryData{
        String choiceId;
        String branchingPointId;
        public ChoicesHistoryData(String choiceId, String branchingPointId) {
            this.choiceId = choiceId;
            this.branchingPointId = branchingPointId;
        }
        public String getChoiceId() {
            return choiceId;
        }
        public String getBranchingPointId() {
            return branchingPointId;
        }

    }

    private Stack<ChoicesHistoryData> sequenceSlideHistory = new Stack<>();

    public void addToSequenceSlideHistory(String choiceId, String branchingPointId) {
        sequenceSlideHistory.push(new ChoicesHistoryData(choiceId, branchingPointId));
    }

    public boolean backNavigationExists() {
        return !sequenceSlideHistory.isEmpty();
    }

    public void removeLastElementFromSequenceSlideHistory() {
        sequenceSlideHistory.pop();
    }

    public String peekTopBranchingPointId() {
        return sequenceSlideHistory.peek().getBranchingPointId();
    }

    public String peekTopChoiceId() {
        return sequenceSlideHistory.peek().getChoiceId();
    }

}
