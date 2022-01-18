package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.model.SequenceOverview;
import edu.asu.diging.vspace.core.model.impl.Sequence;

@Component
public class SequenceOverviewJsonFormat {
    
    
    /**
     * creating list of SequenceOverview object for sequence nodes in the moduleoverview
     * graph
     * 
     * @param contextPath   This variable holds the contextpath of the application
     * @param SequenceNodeList List of sequences
     * @throws JsonProcessingException
     */
    public List<SequenceOverview> constructNodesForSequences(List<Sequence> sequenceNodeList) {

        List<SequenceOverview> sequenceVertexList = new ArrayList<>();
        if (sequenceNodeList != null) {
            for (Sequence sequenceNode : sequenceNodeList) {
                SequenceOverview sequenceOverview = createSequenceNode(sequenceNode);
                sequenceVertexList.add(sequenceOverview);
            }
        }
        return sequenceVertexList;
    }
    
    private SequenceOverview createSequenceNode(Sequence sequence) {
        
        SequenceOverview sequenceOverview = new SequenceOverview();
        sequenceOverview.setName(sequence.getName());
        sequenceOverview.setId(sequence.getId());
        sequenceOverview.setSlides(sequence.getSlides());
        
        return sequenceOverview;  
    }

}
