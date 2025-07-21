package com.erns.mot.service;

import com.erns.mot.domain.Opinion;
import com.erns.mot.repository.OpinionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OpinionService {
    private final OpinionRepository opinionRepository;

    @Autowired
    public OpinionService(OpinionRepository opinionRepository) {
        this.opinionRepository = opinionRepository;
    }

    @Transactional
    public Opinion createOpinion(Opinion opinion) {
        return opinionRepository.save(opinion);
    }

    @Transactional
    public Opinion updateOpinion(Long id, Opinion opinionDetails) {
        Optional<Opinion> optionalOpinion = opinionRepository.findById(id);
        if (optionalOpinion.isEmpty()) throw new RuntimeException("Opinion not found");
        Opinion opinion = optionalOpinion.get();
        opinion.setTitle(opinionDetails.getTitle());
        opinion.setContent(opinionDetails.getContent());
        opinion.setAuthorId(opinionDetails.getAuthorId());
        opinion.setAuthorName(opinionDetails.getAuthorName());
        opinion.setReference(opinionDetails.getReference());
        opinion.setAbstractText(opinionDetails.getAbstractText());
        opinion.setKeywords(opinionDetails.getKeywords());
        opinion.setFullText(opinionDetails.getFullText());
        opinion.setStatus(opinionDetails.getStatus());
        opinion.setCategory(opinionDetails.getCategory());
        return opinionRepository.save(opinion);
    }

    @Transactional
    public void deleteOpinion(Long id) {
        opinionRepository.deleteById(id);
    }

    public Optional<Opinion> getOpinion(Long id) {
        return opinionRepository.findById(id);
    }

    public List<Opinion> getAllOpinions() {
        return opinionRepository.findAll();
    }

    @Transactional
    public Opinion updateStatus(Long id, String status) {
        Optional<Opinion> optionalOpinion = opinionRepository.findById(id);
        if (optionalOpinion.isEmpty()) {
            throw new RuntimeException("Opinion not found with id: " + id);
        }
        Opinion opinion = optionalOpinion.get();
        opinion.setStatus(status);
        return opinionRepository.save(opinion);
    }
} 