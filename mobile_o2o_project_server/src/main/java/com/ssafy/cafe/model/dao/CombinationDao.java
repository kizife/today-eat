package com.ssafy.cafe.model.dao;

import com.ssafy.cafe.model.dto.Combination;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CombinationDao {

    // 도시락 조합 추가
    void insertCombination(Combination combination);

    // 도시락 조합 업데이트
    void updateCombination(Combination combination);

    // 도시락 조합 삭제
    void deleteCombination(int dosirockId);

    // 도시락 조합 조회 (ID로 조회)
    Combination selectCombination(int dosirockId);

    // 사용자 ID로 도시락 조합 조회
    List<Combination> selectByUserId(String userId);
}
