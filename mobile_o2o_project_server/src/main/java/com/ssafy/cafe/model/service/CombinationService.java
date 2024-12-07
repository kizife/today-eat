package com.ssafy.cafe.model.service;

import com.ssafy.cafe.model.dto.Combination;

import java.util.List;

public interface CombinationService {


    // 특정 dosirock_id에 해당하는 도시락 조합 반환
    Combination getCombinationById(int dosirockId);

    // 사용자 ID에 해당하는 도시락 조합 리스트 반환
    List<Combination> getCombinationByUserId(String userId);

    // 새로운 도시락 조합 추가
    int insertCombination(Combination combination);

    // 도시락 조합 업데이트
    void updateCombination(int dosirockId, Combination combination);

    // 도시락 조합 삭제
    void deleteCombination(int dosirockId);

}

