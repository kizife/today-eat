package com.ssafy.cafe.controller.rest;

import com.ssafy.cafe.model.dto.Combination;
import com.ssafy.cafe.model.service.CombinationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/combination")
@CrossOrigin("*")
public class CombinationRestController {

    @Autowired
    private CombinationService cService;


    // 특정 dosirock_id에 해당하는 도시락 조합 반환
    @GetMapping("/{dosirockId}")
    @Operation(summary = "{dosirockId}에 해당하는 도시락 조합의 정보를 반환한다.")
    public ResponseEntity<Combination> getCombinationById(@PathVariable int dosirockId) {
        Combination combination = cService.getCombinationById(dosirockId);
        return combination != null
                ? new ResponseEntity<>(combination, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 사용자 ID에 해당하는 도시락 조합 리스트 반환
    @GetMapping("/user/{userId}")
    @Operation(summary = "{userId}에 해당하는 도시락 조합 리스트를 반환한다.")
    public ResponseEntity<List<Combination>> getCombinationByUserId(@PathVariable String userId) {
        List<Combination> combinations = cService.getCombinationByUserId(userId);
        return new ResponseEntity<>(combinations, HttpStatus.OK);
    }
    
    
    @PostMapping()
    @Operation(summary = "새로운 도시락 조합을 추가한다.")
    public ResponseEntity<Integer> addCombination(@RequestBody Combination combination) {
        // 도시락 조합 추가 후, id를 반환받음
        int dosirockId = cService.insertCombination(combination);
        
        // 도시락 아이디와 함께 CREATED 상태 코드 반환
        return new ResponseEntity<>(dosirockId, HttpStatus.CREATED);
    }

    // 도시락 조합 업데이트
    @PutMapping("/{dosirockId}")
    @Operation(summary = "{dosirockId}에 해당하는 도시락 조합을 업데이트한다.")
    public ResponseEntity<Void> updateCombination(@PathVariable int dosirockId, @RequestBody Combination combination) {
        combination.setDosirockId(dosirockId);  // URL에서 받은 dosirockId를 combination에 설정
        cService.updateCombination(dosirockId, combination);  // 서비스 레이어에서 업데이트 처리
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 성공적으로 처리된 경우 204 No Content 반환
    }

    // 도시락 조합 삭제
    @DeleteMapping("/{dosirockId}")
    @Operation(summary = "{dosirockId}에 해당하는 도시락 조합을 삭제한다.")
    public ResponseEntity<Void> deleteCombination(@PathVariable int dosirockId) {
        cService.deleteCombination(dosirockId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
