package com.ssafy.cafe.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.cafe.model.dao.CombinationDao;
import com.ssafy.cafe.model.dto.Combination;

/**
 * @since 2021. 6. 23.
 */
@Service
public class CombinationServiceImpl implements CombinationService {

    @Autowired
    private CombinationDao combinationDao;

    @Override
    @Transactional
    public int insertCombination(Combination combination) {
        // MyBatis insert 호출 (combination 객체에 dosirockId가 자동으로 설정됨)
        combinationDao.insertCombination(combination);
        
        // 삽입된 도시락 조합의 ID를 반환
        return combination.getDosirockId();  // combination 객체에 삽입된 ID를 반환
    }

    @Override
    public Combination getCombinationById(int dosirockId) {
        return combinationDao.selectCombination(dosirockId);
    }


    @Override
    public List<Combination> getCombinationByUserId(String userId) {
        return combinationDao.selectByUserId(userId);
    }

    @Override
    @Transactional
    public void updateCombination(int dosirockId, Combination combination) {
        combinationDao.updateCombination(combination);
    }

    @Override
    @Transactional
    public void deleteCombination(int dosirockId) {
        combinationDao.deleteCombination(dosirockId);
    }

}
