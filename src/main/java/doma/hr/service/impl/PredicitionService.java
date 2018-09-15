package doma.hr.service.impl;

import doma.hr.model.Match;
import doma.hr.repository.impl.PredictionRepository;
import doma.hr.repository.impl.ScheduleRepository;
import doma.hr.service.IPredictionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PredicitionService implements IPredictionService {

    private static final Logger log = LoggerFactory.getLogger(PredicitionService.class);

    private PredictionRepository predictionRepository;
    private ScheduleRepository scheduleRepository;

    @Autowired
    public PredicitionService(PredictionRepository predictionRepository,
                              ScheduleRepository scheduleRepository) {
        this.predictionRepository = predictionRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Match savePrediction(Match matchPrediction) {

        Date matchTime = scheduleRepository.getMatchTime(matchPrediction.getMatchId());
        // checking if match have alreaday started
        if (matchTime.after(new Date(new Date().getTime()))) {
            if (predictionRepository.isMatchPredictionExist(matchPrediction))
                predictionRepository.updatePrediction(matchPrediction);
            else
                predictionRepository.insertPrediction(matchPrediction);
        }
        return matchPrediction;
    }
}
