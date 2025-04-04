import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { social_url } from 'src/global';
import { PostModel } from '../model/post.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private httpClient: HttpClient) {

  }

  getPostsByInstrument(instrument: string): Observable<Array<PostModel>> {
    let httpParams = new HttpParams().append('instrument', instrument);
    return this.httpClient.get<Array<PostModel>>(social_url + '/post/instrument', { params: httpParams });
  }

  getPostsByContent(content: string): Observable<Array<PostModel>> {
    return this.httpClient.post<Array<PostModel>>(social_url + '/post/content', {content: content});
  }

  getPostsByUser(username: any): Observable<Array<PostModel>> {
    let httpParams = new HttpParams().append('username', username);
    return this.httpClient.get<Array<PostModel>>(social_url + '/post/user', {params: httpParams});
  }

  getPostById(postId: number): Observable<PostModel> {
    return this.httpClient.get<PostModel>(social_url + `/post/${postId}`);
  }

  getAllPosts(): Observable<Array<PostModel>> {
    return this.httpClient.get<Array<PostModel>>(social_url + '/post');
  }

  deletePostById(postId: number): Observable<Object> {
    let params = new HttpParams().append('postId', postId)
    return this.httpClient.delete(social_url + '/post/delete', {params: params})
  }

  createPost(postModel: PostModel): Observable<void> {
    return this.httpClient.post<void>(social_url + '/post', postModel);
  }


}
