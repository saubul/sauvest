import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InstrumentAnalysisHistoryModel } from '../model/instrumentAnalysisHistory.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { social_url } from 'src/global';
import { CandleModel } from '../model/candle.model';

@Injectable({
  providedIn: 'root'
})
export class InstrumentAnalysisHistoryService {

  constructor(private httpClient: HttpClient) { }

  public getInstrumentAnalysisHistoryArray(username: string, figi: string): Observable<Array<InstrumentAnalysisHistoryModel>> {
    let httpParams = new HttpParams().append('username', username).append('figi', figi);
    return this.httpClient.get<Array<InstrumentAnalysisHistoryModel>>(social_url + '/instrumentAnalysisHistory', { params: httpParams });
  }

  public saveInstrumentAnalysisHistory(instrumentAnalysisHistory: InstrumentAnalysisHistoryModel) {
    return this.httpClient.post(social_url + '/instrumentAnalysisHistory', instrumentAnalysisHistory);
  }

  public executeTechnicalAnalysis(candles: Array<CandleModel>, date: string, username: string, figi: string): Observable<InstrumentAnalysisHistoryModel> {
    let httpParams = new HttpParams().append('date', date).append('username', username).append('figi', figi);
    return this.httpClient.post<InstrumentAnalysisHistoryModel>(social_url + '/instrumentAnalysisHistory/execute', candles, { params: httpParams });
  }

}
