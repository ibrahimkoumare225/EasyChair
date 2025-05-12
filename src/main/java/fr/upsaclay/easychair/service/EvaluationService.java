package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.model.Post;

import java.util.List;
import java.util.Optional;

public interface EvaluationService {

    Evaluation save(Evaluation evaluation);

    Evaluation update(Evaluation evaluation);

    Optional<Evaluation> findOne(Long id);
    List<Evaluation> findAll();
    void delete(Long id);


    //@Todo getSpecDegree , getGrade
}
