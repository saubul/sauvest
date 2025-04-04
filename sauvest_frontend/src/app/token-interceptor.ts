import { Injectable } from "@angular/core";
import {  HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { catchError, Observable, switchMap, throwError } from "rxjs";
import { AuthenticationService } from "./service/authentication.service";
import { LocalStorageService } from "ngx-webstorage";
import { Router } from "@angular/router";
import { TokenResponsePayload } from "./payload/tokenResponsePayload";
import { api_v1_url, auth_application_name } from "src/global";

@Injectable({
    providedIn: 'root'
})
export class TokenInterceptor implements HttpInterceptor {

    constructor(private authenticationService: AuthenticationService,
                private localStorage: LocalStorageService,
                private router: Router) {

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if(req.url.indexOf(auth_application_name + api_v1_url) !== -1) {
            return next.handle(req);
        }

        const accessToken = this.authenticationService.getAccessToken();

        if(accessToken) {
            return next.handle(this.addToken(req, accessToken)).pipe(
                catchError(error => {
                    if(error instanceof HttpErrorResponse && error.status === 403) {
                        return this.authenticationService.refreshToken().pipe(
                            switchMap((refreshTokenResponse: TokenResponsePayload) => {
                                return next.handle(this.addToken(req, refreshTokenResponse.accessToken))
                            })
                        ).pipe(catchError(error => {
                            if(error instanceof HttpErrorResponse && error.status === 401) {
                                this.router.navigateByUrl('/signIn');
                            }
                            return throwError(() => new Error(error));
                        }))
                    } else {
                        if(localStorage.length !== 0 && error.status === 401) {
                            this.localStorage.clear('accessToken');
                            this.localStorage.clear('username');
                            this.localStorage.clear('refreshToken');
                            this.localStorage.clear('ssoToken');
                            this.router.navigateByUrl('/signIn');
                        }
                        return throwError(() => new Error(error))
                    }
                })
            )
        }
        return next.handle(req);
    }

    addToken(req: HttpRequest<any>, accessToken: any) {
        return req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' + accessToken)
        });
    }

}