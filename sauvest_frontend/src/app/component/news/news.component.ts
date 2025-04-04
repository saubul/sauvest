import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { faComments, faHeart, faThumbsDown, faThumbsUp } from '@fortawesome/free-solid-svg-icons';
import { LocalStorageService } from 'ngx-webstorage';
import { map, Observable, shareReplay, take } from 'rxjs';
import { InstrumentModel } from 'src/app/model/instrument.model';
import { PostModel } from 'src/app/model/post.model';
import { VotePayload } from 'src/app/payload/vote.payload';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { FirebaseService } from 'src/app/service/firebase.service';
import { InstrumentsService } from 'src/app/service/instruments.service';
import { PostService } from 'src/app/service/post.service';
import { VoteService } from 'src/app/service/vote.service';
import { imagesDirectory } from 'src/global';

@Component({
  selector: 'app-news',
  host: { class: 'col-start-2 col-end-7' },
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent {
  
  posts$: Array<PostModel>
  loading: boolean = false;
  findNewsFormGroup: FormGroup;
  commentsIcon = faComments;
  heartIcon = faHeart;
  thumbsDownIcon = faThumbsDown;
  createPostFormGroup: FormGroup;
  findPostsByInstrumentFormGroup: FormGroup;
  votePayload: VotePayload;
  img_src: string = imagesDirectory;
  findInstrumentsFormGroup: FormGroup;
  addedInstruments: Array<InstrumentModel>

  constructor(private authenticationService: AuthenticationService,
              private postService: PostService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private localStorage: LocalStorageService,
              private firebaseService: FirebaseService,
              private voteService: VoteService,
              private instrumentsService: InstrumentsService) {

  }

  ngOnInit(): void {
    if(!this.authenticationService.isLoggedIn) {
      this.router.navigateByUrl('/signIn');
    } else {
      this.loading = true;
      this.addedInstruments = new Array();
      this.findInstrumentsFormGroup = new FormGroup(
        {
          content: new FormControl('')
        }
      );
      this.getPosts();
      this.findNewsFormGroup = new FormGroup (
        {
          content: new FormControl('')
        }
      );
      this.createPostFormGroup = new FormGroup(
        {
          content: new FormControl(''),
        }
      );
      this.findPostsByInstrumentFormGroup = new FormGroup(
        {
          content: new FormControl('')
        }
      )
      this.votePayload = {
        username: this.localStorage.retrieve('username'),
        postId: -1
      };
      setTimeout(() => {this.loading = false}, 500);
    }
  }

  addInstrument() {
      let instrumentFigi = this.findInstrumentsFormGroup.get('content')?.value;
      if(instrumentFigi && instrumentFigi != '') {
        this.instrumentsService.getInstrumentByContent(instrumentFigi, true).subscribe(
          {
            next: (data) => {
              let alreadyAdded = false;
              for(let instrument of this.addedInstruments) {
                if(instrument.figi === data.figi) {
                  alreadyAdded = true;
                }
              }
              if(!alreadyAdded) {
                data = this.instrumentsService.convertInstrumentFields(data);
                this.addedInstruments.push(data);
              }
            },
            error: (error) => {
              console.log(error);
              
            }
          }
        )
      }
  }

  findPostsByInstrument() {
    let content = this.findPostsByInstrumentFormGroup.get('content')?.value;
    if(content && content != '') {
      this.postService.getPostsByInstrument(content).subscribe(
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
              )
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
          }
        }
      )
    } else {
      this.getPosts();
    }
  }

  findImgByUser(username: string): Observable<string> {
    return this.firebaseService.getImagePath('profile', username);
  }

  findNews() {
    let content = this.findNewsFormGroup.get('content')?.value;
    if(content && content != '') {
      this.postService.getPostsByContent(content).subscribe(
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
              )
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
    } else {
      this.getPosts();
    }
  }

  goToPost(postId: number) {
    this.router.navigateByUrl(`/post/${postId}`)
  }

  deletePost(postId: number) {
    this.postService.deletePostById(postId)
  }

  createPost() {
    this.loading = true;
    let postModel: PostModel = new PostModel();
    postModel.content = this.createPostFormGroup.get('content')?.value;
    postModel.username = this.localStorage.retrieve('username');
    postModel.instruments = this.addedInstruments;
    this.postService.createPost(postModel).subscribe(
      {
        next: (data) => {
          this.getPosts();
          this.createPostFormGroup.get('content')?.setValue('');
          this.findInstrumentsFormGroup.get('content')?.setValue('');
          this.addedInstruments = new Array();

        },
        error: (error) => {
          console.log(error);
        }
      }
    );
    setTimeout(() => {this.loading = false}, 500);
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

  private getPosts() {
    
    this.postService.getAllPosts().subscribe(
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
            )
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
          console.log(error)
        }
      }
    )
  }
}
