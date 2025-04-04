import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { faArrowCircleRight, faHeart } from '@fortawesome/free-solid-svg-icons';
import { LocalStorageService } from 'ngx-webstorage';
import { PostModel } from 'src/app/model/post.model';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { CommentService } from 'src/app/service/comment.service';
import { FirebaseService } from 'src/app/service/firebase.service';
import { PostService } from 'src/app/service/post.service';
import { VoteService } from 'src/app/service/vote.service';
import { imagesDirectory } from 'src/global';
import { CommentPayload } from './comment.payload';
import { VotePayload } from '../../payload/vote.payload';
import { CommentModel } from 'src/app/model/comment.model';

@Component({
  selector: 'app-post-view',
  templateUrl: './post-view.component.html',
  styleUrls: ['./post-view.component.css']
})
export class PostViewComponent implements OnInit{

  loading: boolean = false;

  post: PostModel;
  commentFormGroup: FormGroup;
  commentPayload: CommentPayload;
  comments: Array<CommentModel>;
  isUsersPost: boolean;
  writeCommentArrow = faArrowCircleRight;
  heartIcon = faHeart;
  img_src: string = imagesDirectory;
  isAdmin: boolean;

  voteFormGroup: FormGroup
  votePayload: VotePayload
  voteNumber: number
  postId = this.activatedRoute.snapshot.params.id;
  usernameMatch: boolean;

  constructor(private authenticationService: AuthenticationService,
              private router: Router,
              private postService: PostService,
              private activatedRoute: ActivatedRoute,
              private firebaseService: FirebaseService,
              private commentService: CommentService,
              private localStorage: LocalStorageService,
              private voteService: VoteService) {

  }

  ngOnInit(): void {
    
    this.loading = true;
    this.authenticationService.checkIsAdmin(this.localStorage.retrieve('username')).subscribe(
      {
        next: (data) => {
          this.isAdmin = data;
          console.log(data);
          
        },
        error: (error) => {
          console.log(error)
        }
      }
    )
    this.voteFormGroup = new FormGroup({})
    this.commentFormGroup = new FormGroup(
      {
        content: new FormControl('')
      }
    );
    this.commentPayload = {
      content: '',
      postId: this.postId,
      username: '',
      dateCreated: new Date()
    };
    this.votePayload = {
      username: this.localStorage.retrieve('username'),
      postId: this.postId
    }
    this.getPost();
    
    this.getCommentsByPost();
    setTimeout(() => this.loading = false, 500);
    
  }

  postComment() {
    if(!this.authenticationService.isLoggedIn) {
      this.router.navigateByUrl('/signIn');
    } else {

      this.commentPayload.content = this.commentFormGroup.get('content')?.value;
      this.commentPayload.username = this.localStorage.retrieve('username');
      this.commentService.postComment(this.commentPayload).subscribe(
        {
          next: (data) => {
            this.commentFormGroup.get('content')?.setValue('');
            this.getCommentsByPost();
          }, 
          error: (error) => {
            console.log(error)
          }
        })
    }
  }

  deletePost() {
      this.postService.deletePostById(this.post.id).subscribe(
        {
          next: (data) => {
            this.router.navigateByUrl('/news');
          },
          error: (error) => {
            console.log(error);
          }
        }
      );
  }

  votePost() {
    this.voteService.saveVote(this.votePayload).subscribe(
      {
        next: (data) => {
          if(this.post.isLikedByCurrentUser) {
            this.post.voteCount--;
          } else {
            this.post.voteCount++;
          }
          this.post.isLikedByCurrentUser = !this.post.isLikedByCurrentUser;
        },
        error: (error) => {
          console.log(error)
        }
      }
    )
  }

  private getCommentsByPost() {
    this.commentService.getAllCommentsByPost(this.postId).subscribe(
      {
        next: (data) => {
          this.comments = data;
          for(let comment of this.comments) {
            this.firebaseService.getImagePath('profile', comment.username).subscribe(
              {
                next: (data) => {
                  comment.userImg = data;
                }, 
                error: (error) => {
                  comment.userImg = imagesDirectory + '/profile.png';
                }
              }
            )
          }
        },
        error: (error) => {
          console.log(error);
        }
      }
    )
  }

  private getPost() {
    this.postService.getPostById(this.postId).subscribe(
      {
        next: (data) => {
          this.post = data;
          console.log(data)
          this.usernameMatch = this.checkUsernameMatch();
          this.firebaseService.getImagePath('profile', this.post.username).subscribe(
            {
              next: (data) => {
                this.post.userImg = data;
              }, 
              error: (error) => {
                this.post.userImg = imagesDirectory + '/profile.png';
              }
            }
          );
          this.voteService.isLikedPostByUser(this.post.id, this.localStorage.retrieve('username')).subscribe(
            {
              next: (data) => {
                this.post.isLikedByCurrentUser = data;
              }, 
              error: (error) => {
                console.log(error)
              }
            }
          );
          
          this.loading = false;
        },
        error: (error) => console.log(error)
        
      }
    )
  }

  private checkUsernameMatch(): boolean {
    if(!this.authenticationService.isLoggedIn) {
      return false;
    } else {
      let usernameStorage = this.localStorage.retrieve('username')
      if(usernameStorage === this.post.username) {
        return true;
      }
      return false;
    }
  }

}
