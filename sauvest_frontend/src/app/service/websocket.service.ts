import { Injectable } from '@angular/core';
import { ChatMessage } from '../model/chatMessage.model';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { social_application_host, social_application_name } from 'src/global';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  socket: any
  stompClient: any

  constructor() {
    this.socket = new SockJS(social_application_host + social_application_name + '/ws');
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.heartbeat.outgoing = 0;
    this.stompClient.heartbeat.incoming = 0;
    this.stompClient.reconnect_delay = 5000;
  }

  disconnect() {
    if (this.stompClient !== null) {
        this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  subscribe(topic:string, callback: any) {
    const connected: boolean = this.stompClient.connected;
    if(connected) {
      this.subscribeToTopic(topic, callback);
      return;
    }

    this.stompClient.connect({}, (): any => {
      this.subscribeToTopic(topic, callback);
    });
  }

  unsubscribe(topic: string) {
    const connected: boolean = this.stompClient.connected;
    if(connected) {
      this.stompClient.unsubscribe(topic);
    }
  }

  sendMessage(message: ChatMessage, chatId: number) {
    const connected: boolean = this.stompClient.connected;
    if(connected) {
      this.stompClient.send(`/app/chat/${chatId}`, {}, JSON.stringify(message));
      return;
    };

    this.stompClient.connect({}, (): any => {
      this.stompClient.send(`/app/chat/${chatId}`, {}, JSON.stringify(message));
    });
  }

  private subscribeToTopic(topic: string, callback: any): void {
    this.stompClient.subscribe(topic, (): any => callback());
  }
}
