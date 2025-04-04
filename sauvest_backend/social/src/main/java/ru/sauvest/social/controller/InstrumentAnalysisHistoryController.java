package ru.sauvest.social.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sauvest.social.dto.CandleDto;
import ru.sauvest.social.dto.InstrumentAnalysisHistoryDto;
import ru.sauvest.social.service.InstrumentAnalysisHistoryService;
import ru.sauvest.social.service.impl.UtilService;

import java.util.List;

@RestController
@RequestMapping("/${sauvest.applicationName}/api/v1/instrumentAnalysisHistory")
@RequiredArgsConstructor
public class InstrumentAnalysisHistoryController {

    private final InstrumentAnalysisHistoryService instrumentAnalysisHistoryService;

    @GetMapping
    public ResponseEntity<List<InstrumentAnalysisHistoryDto>> getByUser(@RequestParam("username") String username,
                                                                        @RequestParam("figi") String figi) {
        return new ResponseEntity<>(instrumentAnalysisHistoryService.findByUsernameAndFigi(username, figi), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InstrumentAnalysisHistoryDto> save(@RequestBody InstrumentAnalysisHistoryDto instrumentAnalysisHistoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instrumentAnalysisHistoryService.save(instrumentAnalysisHistoryDTO));
    }


    @PostMapping("/execution")
    public ResponseEntity<InstrumentAnalysisHistoryDto> ema(@RequestBody List<CandleDto> candleDtoList,
                                                            @RequestParam("date") String date,
                                                            @RequestParam("username") String username,
                                                            @RequestParam("figi") String figi) {
        return ResponseEntity.ok(instrumentAnalysisHistoryService.executeTechnicalAnalysis(candleDtoList,
                UtilService.convertStringToDate(date, "yyyy.MM.dd hh:mm:ss"), username, figi));
    }

}
