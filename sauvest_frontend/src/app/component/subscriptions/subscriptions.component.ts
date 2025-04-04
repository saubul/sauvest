import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfileModel } from 'src/app/model/profile.model';
import { UserModel } from 'src/app/model/user.model';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { ChatService } from 'src/app/service/chat.service';
import { FirebaseService } from 'src/app/service/firebase.service';
import { SubscriptionService } from 'src/app/service/subscription.service';
import { UserService } from 'src/app/service/user.service';
import { imagesDirectory } from 'src/global';

@Component({
  selector: 'app-subscriptions',
  host: { class: 'bg-white rounded-md p-2 col-start-2 col-end-7' },
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.css']
})
export class SubscriptionsComponent implements OnInit {

  loading: boolean = false;
  subscribedOnUsers: Array<ProfileModel>;
  img_src: string = imagesDirectory;

  profile_img_src: string = this.img_src + '/profile.png';
  allUsers: Array<ProfileModel>;

  findSubscriptionsFormGroup: FormGroup;

  currUsername: string;

  constructor(private router: Router,
              private authenticationService: AuthenticationService,
              private activatedRoute: ActivatedRoute,
              private subscriptionService: SubscriptionService,
              private firebaseService: FirebaseService,
              private userService: UserService,
              private chatService: ChatService) {

  }

  ngOnInit(): void {
    this.loading = true;
    if(this.activatedRoute.snapshot.params.username !== this.authenticationService.getUsername()) {
      this.router.navigateByUrl('/error')
      return
    }
    this.currUsername = this.activatedRoute.snapshot.params.username;
    this.subscriptionService.getAllSubscriptions(this.authenticationService.getUsername()).subscribe(
      {
        next: (data) => {
          this.subscribedOnUsers = data;
          for(let profile of this.subscribedOnUsers) {
            this.firebaseService.getImagePath('profile', profile.username).subscribe(
              {
                next: (data) => {
                  profile.imgPath = data
                  this.loading = false;
                },
                error: (error) => {
                  profile.imgPath = this.profile_img_src;
                  this.loading = false;
                }
              }
            )
          }
          
        },
        error: (error) => {
          this.router.navigateByUrl('/signIn')
          console.log(error)
        }
      }
    );
    this.findSubscriptionsFormGroup = new FormGroup(
      {
        username: new FormControl('')
      }
    )
    this.getAllUsers();
    setTimeout(() => this.loading = false, 500);
  }

  getAllUsers() {
    this.userService.getAllUsersExceptOne(this.currUsername).subscribe(
      {
        next: (data) => {
          this.allUsers = data;
          for(let profile of this.allUsers) {
            this.firebaseService.getImagePath('profile', profile.username).subscribe(
              {
                next: (data) => {
                  profile.imgPath = data
                  this.loading = false;
                },
                error: (error) => {
                  profile.imgPath = this.profile_img_src;
                  this.loading = false;
                }
              }
            )
          }
        }
      }
    )
  }

  goToUserProfile(username: string) {
    this.router.navigateByUrl(`/user/${username}/profile`)
  }

  findSubscriptionsByUser() {
    let str = this.findSubscriptionsFormGroup.get('username')?.value
    if(!str || str === '') {
      this.subscriptionService.getAllSubscriptions(this.authenticationService.getUsername()).subscribe(
        {
          next: (data) => {
            this.subscribedOnUsers = data;
            for(let profile of this.subscribedOnUsers) {
              this.firebaseService.getImagePath('profile', profile.username).subscribe(
                {
                  next: (data) => {
                    profile.imgPath = data
                    this.loading = false;
                  },
                  error: (error) => {
                    profile.imgPath = this.profile_img_src;
                    this.loading = false;
                  }
                }
              )
            }
            
          },
          error: (error) => {
            console.log(error)
          }
        }
      );
    } else {
      this.userService.getAllUsersByUsernameContaining(str).subscribe(
        {
          next: (data) => {
            this.subscribedOnUsers = data;
            for(let profile of this.subscribedOnUsers) {
              this.firebaseService.getImagePath('profile', profile.username).subscribe(
                {
                  next: (data) => {
                    profile.imgPath = data
                    this.loading = false;
                  },
                  error: (error) => {
                    profile.imgPath = this.profile_img_src;
                    this.loading = false;
                  }
                }
              )
            }
            
          },
          error: (error) => {
            console.log(error)
          }
        }
      )
    }
  }

  createChatWith(username: string) {
    this.chatService.createChat(this.currUsername, username).subscribe(
      {
        next: (data) => this.router.navigateByUrl(`user/${this.currUsername}/chats`),
        error: (error) => console.log(error)
      }
    )
  }

}
