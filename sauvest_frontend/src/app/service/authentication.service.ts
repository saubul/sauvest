import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';
import { api_v1_url, auth_url, social_url } from 'src/global';
import { UsernamePasswordPayload } from '../component/sing-in/signInRequest.payload';
import { SignUpRequestPayload } from '../component/sing-up/signUpRequest.payload';
import { TokenResponsePayload } from '../payload/tokenResponsePayload';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  
  isLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  isMainPage: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private httpClient: HttpClient, 
              private localStorage: LocalStorageService) { 
  }

  getToken(usernamePasswordPayload: UsernamePasswordPayload): Observable<boolean> {
    let headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.httpClient.post(auth_url + '/auth/token', usernamePasswordPayload, {headers: headers}).pipe(
      map(data => {
        let tokens = JSON.parse(JSON.stringify(data))
        this.localStorage.store('accessToken', tokens['accessToken']);
        this.localStorage.store('refreshToken', tokens['refreshToken']);
        this.localStorage.store('username', tokens['username']);
        this.isLoggedIn.next(true);
        return true;
      })
    )
  }

  refreshToken(): Observable<TokenResponsePayload> {
      let jwtHeader = new HttpHeaders().set('Authorization', 'Bearer ' + this.getRefreshToken())
      return this.httpClient.post<TokenResponsePayload>(auth_url + '/auth/token/update', null, {headers: jwtHeader})
                            .pipe(tap(data => {
                              this.localStorage.clear('accessToken');
                              this.localStorage.store('accessToken', data.accessToken);
                            }))
  }

  validateToken(token: string): Observable<boolean> {
    return this.httpClient.post<boolean>(auth_url + '/auth/token/validation', {token: token})
  }

  signUp(signUpRequestPayload: SignUpRequestPayload): Observable<any> {
    this.localStorage.store('ssoToken', signUpRequestPayload.ssoToken);
    return this.httpClient.post(auth_url + '/auth/account', signUpRequestPayload, {responseType: 'text'})
  }

  activateAccount(token: string): Observable<Boolean> {
    return this.httpClient.post<Boolean>(auth_url + '/auth/account/activation', {token: token});
  }

  verificateAccount(username: string): Observable<boolean> {
    return this.httpClient.post<boolean>(auth_url + '/auth/account/verification', {username: username});
  }

  checkIsAdmin(username: string): Observable<boolean> {
    return this.httpClient.post<boolean>(auth_url + '/auth/account/role', {username: username, roleName: 'ADMIN'});
  }

  signOut(): void {
    this.localStorage.clear('accessToken');
    this.localStorage.clear('username');
    this.localStorage.clear('refreshToken');
    this.isLoggedIn.next(false);
  }

  getUsername(): string {
    return this.localStorage.retrieve('username');
  }
  getRefreshToken() {
    return this.localStorage.retrieve('refreshToken');
  }
  getAccessToken() {
    return this.localStorage.retrieve('accessToken');
  }
}
