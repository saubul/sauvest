import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ChatModel } from 'src/app/model/chat.model';
import { ChatMessage } from 'src/app/model/chatMessage.model';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { CdkVirtualScrollViewport } from '@angular/cdk/scrolling'
import { faPlusCircle } from '@fortawesome/free-solid-svg-icons'
import { UserService } from 'src/app/service/user.service';
import { UserModel } from 'src/app/model/user.model';
import { ChatService } from 'src/app/service/chat.service';
import { WebsocketService } from 'src/app/service/websocket.service';
import { imagesDirectory } from 'src/global';
import { FirebaseService } from 'src/app/service/firebase.service';
import { Observable, shareReplay } from 'rxjs';

@Component({
  selector: 'app-chats',
  host: { class: 'bg-white rounded-md p-2 col-start-2 col-end-7' },
  templateUrl: './chats.component.html',
  styleUrls: ['./chats.component.css']
})
export class ChatsComponent implements OnInit {
  
  isLoggedIn: boolean;
  loading: boolean = false;
  chats: Array<ChatModel>
  messages: Array<ChatMessage>
  sendMessageForm: FormGroup
  chatMessage: ChatMessage
  chosenChatId: number;
  userModel: UserModel;
  @ViewChild(CdkVirtualScrollViewport) cdkScroll: CdkVirtualScrollViewport
  plus = faPlusCircle;
  img_src: string = imagesDirectory;
  profile_img_src: string = this.img_src + 'profile.png';
  usersPhotos: Map<string, string> = new Map<string, string>

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private authenticationService: AuthenticationService,
              private userService: UserService,
              private chatService: ChatService,
              private websocketService: WebsocketService,
              private firebaseService: FirebaseService) {

  }

  ngOnInit(): void {
    this.loading = true;
    if(this.activatedRoute.snapshot.params.username !== this.authenticationService.getUsername()) {
      this.router.navigateByUrl('/error');
      return;
    };

    this.userService.getUserByUsername(this.activatedRoute.snapshot.params.username).subscribe(
      {
        next: (data) => {
          this.userModel = data;
        },
        error: (error) => {
          this.router.navigateByUrl('/signIn')
          console.log(error)
        }
      }
    );

    this.chatService.getAllUserChats(this.authenticationService.getUsername()).subscribe(
      {
        next: (data) => {
          this.chats = data;
          this.chosenChatId = data[0]?.id;
          if(data && data.length > 0) {
            this.showChatMessagesAndSubscribe(data[0]);
            this.websocketService.subscribe(`/topic/${this.chosenChatId}`, (): any => {
              this.showChatMessages(this.chosenChatId);
            });
          }
          this.loading = false;
        },
        error: (error) => {
          console.log(error);
          this.loading = false;
        }
      }
    );

    this.sendMessageForm = new FormGroup(
      {
        message: new FormControl('')
      }
    );

    this.chatMessage = {
      content: ''
    };

    setTimeout(() => this.loading = false, 500);
  }

  sendMessage() {
    this.chatMessage.content = this.sendMessageForm.get('message')?.value;
    this.chatMessage.username = this.authenticationService.getUsername();
    this.chatMessage.chatId = this.chosenChatId;

    this.websocketService.sendMessage(this.chatMessage, this.chosenChatId);
    this.sendMessageForm.get("message")?.setValue('');
  }

  showChatMessagesAndSubscribe(chat: ChatModel) {
    this.loading = true;
    this.usersPhotos.clear();
    for(let i = 0; i <chat.usersUsername?.length; i++) {
      this.usersPhotos.set(chat.usersUsername[i], this.profile_img_src);
      this.firebaseService.getImagePath('profile', chat.usersUsername[i]).subscribe(
        {
          next: (data) => {
            this.usersPhotos.set(chat.usersUsername[i], data);
            this.loading = false;
          },
          error: (error) => {
            console.log(error);
            this.loading = false;
          }
        }
      )
    }

    let chatId = chat.id;
    let saveChatId = this.chosenChatId;
    this.chatService.getAllChatMessages(chatId).subscribe(
      {
        next: (data) => {
          this.messages = data;
          this.chosenChatId = chatId;
          this.scrollToBottom();
        },
        error: (error) => {
          console.log(error)
        }
      }
    );
    if(chatId !== this.chosenChatId) {
      //this.websocketService.unsubscribe(`/topic/${this.chosenChatId}`);
      this.websocketService.subscribe(`/topic/${chatId}`, (): any => {
        this.showChatMessages(chatId);
      });
    }
  }

  showChatMessages(chatId: number) {
    this.chatService.getAllChatMessages(chatId).subscribe(
      {
        next: (data) => {
          this.messages = data;
          this.chosenChatId = chatId;
          this.scrollToBottom();
        },
        error: (error) => {
          console.log(error)
        }
      }
    );
  }

  scrollToBottom() {
    
    setTimeout(() => {
      this.cdkScroll.scrollTo({
        bottom: 0,
        behavior: 'auto',
      });
    }, 0);
  }

}
