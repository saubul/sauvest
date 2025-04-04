import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { social_url } from 'src/global';
import { ProfileModel } from '../model/profile.model';
import { UserModel } from '../model/user.model';
import { FirebaseService } from './firebase.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient,
              private firebaseService: FirebaseService) { }


  getAllUsersByUsernameContaining(str: string): Observable<Array<ProfileModel>> {
    let params = new HttpParams().append('username', str);
    return this.httpClient.get<Array<ProfileModel>>(social_url + "/profile", {params: params})
  }

  getAllUsersExceptOne(username: string): Observable<Array<ProfileModel>> {
    let params = new HttpParams().append('username', username)
    return this.httpClient.get<Array<ProfileModel>>(social_url + "/profile/exception", { params: params });
  }

  getUserByUsername(username: string): Observable<UserModel> {
    let params = new HttpParams().append('username', username)
    return this.httpClient.get<UserModel>(social_url + '/user', {params: params})
  }

  updateUser(userModel: UserModel): Observable<UserModel> {
    return this.httpClient.put<UserModel>(social_url + "/user/update", userModel)
  }

  uploadImage(username: string, image: File) {
    this.firebaseService.uploadImage(image, 'profile', username)
  }

}
