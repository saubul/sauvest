import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { social_url } from 'src/global';
import { ChatModel } from '../model/chat.model';
import { ChatMessage } from '../model/chatMessage.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  
  constructor(private httpClient: HttpClient) { }

  getAllUserChats(username: string): Observable<Array<ChatModel>> {
    return this.httpClient.get<Array<ChatModel>>(`${social_url}/chat/${username}/chats`)
  }

  getAllChatMessages(chatId: number): Observable<Array<ChatMessage>> {
    return this.httpClient.get<Array<ChatMessage>>(`${social_url}/chat/${chatId}/messages`)
  }

  createChat(username1: string, username2: string): Observable<ChatModel> {
    if (username1 > username2) {
      let save = username2;
      username2 = username1;
      username1 = save;
    }
    let chatModel: ChatModel = new ChatModel([username1, username2], username1 + username2);
    return this.httpClient.post<ChatModel>(social_url + '/chat', chatModel);
  }
}
