package edu.asu.diging.vspace.core.model.impl;

import java.util.Stack;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SequenceHistory{

    class SequenceHistoryData{
        String sequenceId;
        String branchingPointId;
        public SequenceHistoryData(String sequenceId, String branchingPointId) {
            this.sequenceId = sequenceId;
            this.branchingPointId = branchingPointId;
        }
        public String getSequenceId() {
            return sequenceId;
        }
        public String getBranchingPointId() {
            return branchingPointId;
        }

    }

    private Stack<SequenceHistoryData> sequenceSlideHistory = new Stack<>();

    public void addToHistory(String choiceId, String branchingPointId) {
        sequenceSlideHistory.push(new SequenceHistoryData(choiceId, branchingPointId));
    }

    public boolean hasHistory() {
        return !sequenceSlideHistory.isEmpty();
    }

    public void popFromHistory() {
        sequenceSlideHistory.pop();
    }

    public String peekBranchingPointId() {
        return sequenceSlideHistory.peek().getBranchingPointId();
    }

    public String peekSequenceId() {
        return sequenceSlideHistory.peek().getSequenceId();
    }

    public void flushFromHistory() {
        sequenceSlideHistory.removeAllElements();
    }

}
