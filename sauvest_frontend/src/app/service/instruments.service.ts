import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { market_url, social_url } from 'src/global';
import { InstrumentModel } from '../model/instrument.model';
import { PostModel } from '../model/post.model';
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root'
})
export class InstrumentsService {

  constructor(private httpClient: HttpClient,
              private localStorage: LocalStorageService) {

  }

  getInstrumentByContent(field: string, isShare: boolean): Observable<InstrumentModel> {
    let ssoToken = this.localStorage.retrieve('ssoToken')
    let instrumentType = isShare? 'share' : 'bond';
    return this.httpClient.post<InstrumentModel>(market_url + `/instrument/${instrumentType}`, {ssoToken: ssoToken, field: field});
  }

  getAllShares(): Observable<Array<InstrumentModel>> {
    let ssoToken = this.localStorage.retrieve('ssoToken')
    return this.httpClient.post<Array<InstrumentModel>>(market_url + '/instrument/shares', {ssoToken: ssoToken});
  }
  getAllBonds(): Observable<Array<InstrumentModel>> {
    let ssoToken = this.localStorage.retrieve('ssoToken')
    return this.httpClient.post<Array<InstrumentModel>>(market_url + '/instrument/bonds', {ssoToken: ssoToken});
  }

  getPostsByInstrument(instrument: string): Observable<Array<PostModel>> {
    let ssoToken = this.localStorage.retrieve('ssoToken')
    let httpParams: HttpParams = new HttpParams().append('instrument', instrument);
    return this.httpClient.get<Array<PostModel>>(`${social_url}/post/instrument`, {params: httpParams});
  }

  getInstrumentsByPost(postId: string): Observable<Array<InstrumentModel>> {
    let httpParams: HttpParams = new HttpParams().append('postId', postId);
    return this.httpClient.get<Array<InstrumentModel>>(`${social_url}/instrument/post`, {params: httpParams});
  }

  convertInstrumentFields(instrument: InstrumentModel): InstrumentModel {
    switch(instrument.instrumentType) {
      case 'SHARE_TYPE_COMMON':
        instrument.instrumentType = 'Акция';
        break;
      case 'SHARE_TYPE_ADR':
        instrument.instrumentType = 'Американская депозитарная расписка';
        break;
      case 'SHARE_TYPE_GDR':
        instrument.instrumentType = 'Глобальная депозитарная расписка';
        break;
      case 'SHARE_TYPE_REIT':
        instrument.instrumentType = 'Инвестиционный фонд недвижимости';
        break;
      case 'SHARE_TYPE_PREFERRED':
        instrument.instrumentType = 'Привилегированная акция';
        break;
      case 'BOND':
        instrument.instrumentType = 'Облигация';
        break;
      default:
        break;
    }
    switch(instrument.classCode) {
      case 'SPBXM':
        instrument.classCode = 'СПБ биржа';
        break;
      case 'SPBHKEX':
        instrument.classCode= 'Гонконгская биржа';
        break;
      default:
        break;
    }

    return instrument;
  }
}
