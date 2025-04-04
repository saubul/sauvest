import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { faHeart, faThumbsDown, faComments, } from '@fortawesome/free-solid-svg-icons';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { PostModel } from 'src/app/model/post.model';
import { SubscriptionModel } from 'src/app/model/subscription.model';
import { UserModel } from 'src/app/model/user.model';
import { VotePayload } from 'src/app/payload/vote.payload';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { FirebaseService } from 'src/app/service/firebase.service';
import { PostService } from 'src/app/service/post.service';
import { SubscriptionService } from 'src/app/service/subscription.service';
import { UserService } from 'src/app/service/user.service';
import { VoteService } from 'src/app/service/vote.service';
import { imagesDirectory } from 'src/global';

@Component({
  selector: 'app-profile',
  host: { class: 'col-start-2 col-end-7' },
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  img_src: string = imagesDirectory;
  profile_img_src: string = this.img_src + '/profile.png';

 
  userModel: UserModel;
  loading: boolean = false;
  isCurrUser: boolean = true;
  isSubscribed: boolean;

  heartIcon = faHeart;
  thumbsDownIcon = faThumbsDown;
  commentsIcon= faComments;
  posts$: Array<PostModel>

  votePayload: VotePayload
  isAdmin: boolean;

  constructor(private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private firebaseService: FirebaseService,
    private authenticationService: AuthenticationService,
    private subscriptionService: SubscriptionService,
    private postService: PostService,
    private voteService: VoteService,
    private localStorage: LocalStorageService) {

  }

  ngOnInit(): void {
    this.loading = true;
    this.votePayload = {
      username: this.localStorage.retrieve('username'),
      postId: -1
    }
    this.userService.getUserByUsername(this.activatedRoute.snapshot.params.username).subscribe(
      {
        next: (data) => {
          this.userModel = data;
          if (this.userModel.username !== this.authenticationService.getUsername()) {
            this.isCurrUser = false;
          }
          this.firebaseService.getImagePath('profile', this.userModel.username).subscribe(
            {
              next: (data) => {
                this.profile_img_src = data;
                this.loading = false;
              },
              error: (error) => { 
                console.log(error) 
                this.loading = false
              }
            }
          )
        },
        error: (error) => {
          this.router.navigateByUrl('/signIn')
          console.log(error)
        }
      }
    );
    this.checkIsSubscribed().subscribe(
      {
        next: (data) => this.isSubscribed = data,
        error: (error) => console.log(error)
      }
    )
    this.postService.getPostsByUser(this.activatedRoute.snapshot.params.username).subscribe(
      {
        next: (data) => {
          this.posts$ = data;
          for(let post of this.posts$) {
            this.firebaseService.getImagePath('profile', post.username).subscribe(
              {
                next: (data) => {
                  post.userImg = data;
                  
                }, 
                error: (error) => {
                  post.userImg = imagesDirectory + '/profile.png';
                }
              }
            );
            this.voteService.isLikedPostByUser(post.id, this.localStorage.retrieve('username')).subscribe(
              {
                next: (data) => {
                  post.isLikedByCurrentUser = data;
                }, 
                error: (error) => {
                  console.log(error)
                }
              }
            );
          }
        },
        error: (error) => {
          console.log(error);
        }
      }
    )
  }
  votePost(postId: number) {
    this.votePayload.postId = postId;
    this.voteService.saveVote(this.votePayload).subscribe(
      {
        next: (data) => {
          let post = this.posts$.filter(post => {return post.id === postId});
          if(post[0].isLikedByCurrentUser) {
            post[0].voteCount--;
          } else {
            post[0].voteCount++;
          }
          post[0].isLikedByCurrentUser = !post[0].isLikedByCurrentUser;
        },
        error: (error) => {
          console.log(error)
        }
      }
    );
  }

  subscribe() {
    let subscriptionModel: SubscriptionModel = new SubscriptionModel(this.authenticationService.getUsername(),
                                                                     this.activatedRoute.snapshot.params.username)
    this.subscriptionService.subscribe(subscriptionModel).subscribe(
      {
        next: (data) => {this.isSubscribed = true},
        error: (error) => console.log(error)
      }
    )
  }

  unsubscribe() {
    let subscriptionModel: SubscriptionModel = new SubscriptionModel(this.authenticationService.getUsername(),
                                                                     this.activatedRoute.snapshot.params.username)
    this.subscriptionService.unsubscribe(subscriptionModel).subscribe(
      {
        next: (data) => {this.isSubscribed = false},
        error: (error) => console.log(error)
      }
    )
  }
  goToPost(postId: number) {
    this.router.navigateByUrl(`/post/${postId}`);
  }
  checkIsSubscribed(): Observable<boolean> {
    let subscriptionModel: SubscriptionModel = new SubscriptionModel(this.authenticationService.getUsername(),
                                                                     this.activatedRoute.snapshot.params.username)
    return this.subscriptionService.checkIsSubscribed(subscriptionModel)
  }

}
