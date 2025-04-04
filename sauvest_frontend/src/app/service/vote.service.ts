import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { social_url } from 'src/global';
import { VotePayload } from '../payload/vote.payload';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  constructor(private http: HttpClient) {

  }

  saveVote(votePayload: VotePayload) {
    return this.http.post(social_url + '/vote', votePayload);
  }

  isLikedPostByUser(postId: number, username: string): Observable<boolean> {
    let params = new HttpParams().append('postId', postId).append('username', username)
    return this.http.get<boolean>(social_url + '/vote', { params: params })
  }
}
