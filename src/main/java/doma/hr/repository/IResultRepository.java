package doma.hr.repository;

import doma.hr.model.Match;

public interface IResultRepository {

    Match insertResult(Match matchResult);

    Match getResult(Integer matchId);

}
