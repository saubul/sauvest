import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { social_url } from 'src/global';
import { CommentPayload } from '../component/post-view/comment.payload';
import { CommentModel } from '../model/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private httpClient: HttpClient) { }

  getAllCommentsByPost(postId: number): Observable<Array<CommentModel>> {
    return this.httpClient.get<Array<CommentModel>>(social_url + '/comment/post' + postId);
  }

  postComment(commentPayload: CommentPayload): Observable<CommentPayload> {
    return this.httpClient.post<CommentPayload>(social_url + '/comment', commentPayload);
  }
}
