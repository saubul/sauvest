import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { social_url } from 'src/global';
import { ProfileModel } from '../model/profile.model';
import { SubscriptionModel } from '../model/subscription.model';
import { UserModel } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  constructor(private httpClient: HttpClient) {

  }

  subscribe(subscriptionModel: SubscriptionModel): Observable<SubscriptionModel>{
    return this.httpClient.post<SubscriptionModel>(social_url + '/subscription', subscriptionModel)
  }

  unsubscribe(subscriptionModel: SubscriptionModel): Observable<SubscriptionModel>{
    let params = new HttpParams().append('username', subscriptionModel.username).append('subUsername', subscriptionModel.subUsername);
    return this.httpClient.delete<SubscriptionModel>(social_url + '/subscription/unsubscribe', {params: params})
  }

  checkIsSubscribed(subscriptionModel: SubscriptionModel): Observable<boolean>{
    let params = new HttpParams().append('username', subscriptionModel.username).append('subUsername', subscriptionModel.subUsername);
    return this.httpClient.get<boolean>(social_url + '/subscription', {params:params});
  }

  getAllSubscriptions(username: string): Observable<Array<ProfileModel>> {
    let params = new HttpParams().append('username', username)
    return this.httpClient.get<Array<ProfileModel>>(social_url + '/subscription/user', {params:params})
  }
  
  
}
