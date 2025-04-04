import { Injectable } from '@angular/core';
import { AngularFireStorage } from '@angular/fire/compat/storage';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FirebaseService {

  constructor(private angularFireStorage: AngularFireStorage) {

  }

  uploadImage(image: File, type:string, username: string) {

    this.angularFireStorage.upload(`/images/${type}/${username}`, image)
    

  }

  getImagePath(type:string, username: string): Observable<string> {
    return this.angularFireStorage.ref('/images').child(`/${type}/${username}`).getDownloadURL();
  }

}
