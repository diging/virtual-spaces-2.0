package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.factory.ISequenceFactory;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Transactional
@Service
public class SequenceManager implements ISequenceManager {

    @Autowired
    private ModuleManager moduleManager;

    @Autowired
    private ISequenceFactory sequenceFactory;

    @Autowired
    private SequenceRepository sequenceRepo;

    @Override
    public ISequence storeSequence(String moduleId, SequenceForm sequenceForm) {
        ISequence sequence = sequenceFactory.createSequence(moduleManager.getModule(moduleId), sequenceForm);
        sequenceRepo.save((Sequence) sequence);
        return sequence;
    }

    @Override
    public ISequence getSequence(String sequenceId) {
        Optional<Sequence> mayBeSequence = sequenceRepo.findById(sequenceId);
        if (mayBeSequence.isPresent()) {
            return mayBeSequence.get();
        }
        return null;
    }
}