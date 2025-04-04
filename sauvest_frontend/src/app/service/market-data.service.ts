import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, NgZone } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { market_url, social_url } from 'src/global';
import { EventSourcePolyfill } from 'event-source-polyfill';
import { LocalStorageService } from 'ngx-webstorage';
import { CandleModel } from '../model/candle.model';

@Injectable({
  providedIn: 'root'
})
export class MarketDataService {

  constructor(private httpClient: HttpClient,
              private ngZone: NgZone,
              private localStorage: LocalStorageService) { }

  getEventSourceMarketData(figi: string, candleInterval: string): EventSourcePolyfill {
    let ssoToken = this.localStorage.retrieve('ssoToken')
    let eventSource = new EventSourcePolyfill(`${market_url}/marketData/instrumentData?figi=${figi}&interval=${candleInterval}`, {
      headers: {
        'Authorization': 'Bearer ' + this.localStorage.retrieve('accesstoken'),
        'SsoToken': ssoToken
      }
    });
    return eventSource;
  }

  getInstrumentMarketData(eventSource: EventSourcePolyfill): Observable<Array<CandleModel>> {
    let observable = new Observable<Array<CandleModel>>(
      (observer) => {
        eventSource.onmessage = (event) => {
          observer.next(JSON.parse(event.data));          
        }
        eventSource.onerror = (error) => {
          console.log(error);
          eventSource.close();
        }
      }
    );

    return observable;
  }

  getInstrumentMarketDataSync(figi: string, candleInterval: string): Observable<Array<CandleModel>> {
    let ssoToken = this.localStorage.retrieve('ssoToken')
    return this.httpClient.post<Array<CandleModel>>(`${market_url}/marketData/instrumentDataSync`,
      {ssoToken: ssoToken, figi: figi, interval: candleInterval}
    );
  }
}
