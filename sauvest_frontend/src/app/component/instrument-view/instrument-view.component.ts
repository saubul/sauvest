import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventSourcePolyfill } from 'event-source-polyfill';
import { CandleModel } from 'src/app/model/candle.model';
import { CandleInterval } from 'src/app/model/candleInterval.enum';
import { InstrumentModel } from 'src/app/model/instrument.model';
import { PostModel } from 'src/app/model/post.model';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { InstrumentsService } from 'src/app/service/instruments.service';
import { MarketDataService } from 'src/app/service/market-data.service';
import { faHeart, faThumbsDown, faComments, } from '@fortawesome/free-solid-svg-icons';
import { imagesDirectory } from 'src/global';
import { VoteService } from 'src/app/service/vote.service';
import { VotePayload } from 'src/app/payload/vote.payload';
import { FirebaseService } from 'src/app/service/firebase.service';
import { LocalStorageService } from 'ngx-webstorage';
import { InstrumentAnalysisHistoryModel } from 'src/app/model/instrumentAnalysisHistory.model';
import { InstrumentAnalysisHistoryService } from 'src/app/service/instrument-analysis-history.service';
import { FormControl, FormGroup } from '@angular/forms';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-instrument-view',
  templateUrl: './instrument-view.component.html',
  styleUrls: ['./instrument-view.component.css']
})
export class InstrumentViewComponent implements OnInit, OnDestroy{
  
  figi: string;
  instrument: InstrumentModel;
  isShare: boolean;
  loading: boolean;
  stocksData: Array<CandleModel>;
  eventSource: EventSourcePolyfill;
  eventSourceTest: EventSource;
  posts: Array<PostModel>;  
  img_src: string = imagesDirectory;
  profile_img_src: string = this.img_src + '/profile.png';
  recomendation: number;
  p: number = 1;
  p2: number = 1;
  currentDate: Date;

  heartIcon = faHeart;
  thumbsDownIcon = faThumbsDown;
  commentsIcon= faComments;
  instrumentAnalysisHistoryArray: Array<InstrumentAnalysisHistoryModel>;

  votePayload: VotePayload;

  technicalAnalysisDone: boolean;
  technicalAnalysisExecuteFormGroup: FormGroup;

  constructor(private activatedRoute: ActivatedRoute,
              private instrumentsService: InstrumentsService,
              private marketDataService: MarketDataService,
              private authenticationService: AuthenticationService,
              private router: Router,
              private voteService: VoteService,
              private firebaseService: FirebaseService,
              private localStorage: LocalStorageService,
              private instrumentAnalysisHistoryService: InstrumentAnalysisHistoryService) {

  }
  ngOnDestroy(): void {
    this.eventSource?.close();
  }

  ngOnInit(): void {
    this.currentDate = new Date();
    this.technicalAnalysisDone = false;
    this.instrumentAnalysisHistoryArray = new Array();
    this.posts = new Array();
    this.recomendation = 1;
    this.loading = true;
    this.figi = this.activatedRoute.snapshot.params.figi;    
    this.technicalAnalysisExecuteFormGroup = new FormGroup(
      {
        date: new FormControl(new Date())
      }
    )
    this.votePayload = {
      username: this.localStorage.retrieve('username'),
      postId: -1
    }
    this.activatedRoute.queryParams.subscribe(
      {
        next: (params) => {
          this.isShare = params.isShare === 'true';          
          this.instrumentsService.getInstrumentByContent(this.figi, this.isShare).subscribe(
            {
              next: (data) => {           
                data = this.instrumentsService.convertInstrumentFields(data);
                this.instrument = data; 
              }, 
              error: (error) => {
                console.log(error);
                
              }
            }
          )
        }
      }
    )
    this.instrumentsService.getPostsByInstrument(this.figi).subscribe(
      {
        next: (data) => {
          this.posts = data;
          for(let post of this.posts) {
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
    this.getInstrumentAnalysisHistory();
    this.getMarketDataSync(CandleInterval.CANDLE_INTERVAL_DAY.toString());
    this.loading = false;
  }

  getInstrumentAnalysisHistory() {
    this.instrumentAnalysisHistoryService.getInstrumentAnalysisHistoryArray(this.localStorage.retrieve('username'), this.figi).subscribe(
      {
        next: (data) => {
          this.instrumentAnalysisHistoryArray = data;
          
        },
        error: (error) => {
          console.log(error);
        }
      }
    )
  }

  technicalAnalysisExecute() {
    let date: Date = this.technicalAnalysisExecuteFormGroup.get('date')?.value;
    this.instrumentAnalysisHistoryService.executeTechnicalAnalysis(this.stocksData, formatDate(date, 'yyyy.MM.dd hh:mm:ss', 'en-US'),
                                                           this.localStorage.retrieve('username'), this.figi).subscribe(
      {
        next: (data) => {
          this.instrumentAnalysisHistoryArray.unshift(data);
        }, 
        error: (error) => {
          console.log(error);
          
        }
      }
    )
  }

  goToPost(postId: number) {
    this.router.navigateByUrl(`/post/${postId}`);
  }

  votePost(postId: number) {
    this.votePayload.postId = postId;
    this.voteService.saveVote(this.votePayload).subscribe(
      {
        next: (data) => {
          let post = this.posts.filter(post => {return post.id === postId});
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

  getMarketData(candleInterval: string) {
    if(!this.authenticationService.isLoggedIn) {
      this.router.navigateByUrl('/signIn');
    } else {
      this.eventSource = this.marketDataService.getEventSourceMarketData(this.figi, candleInterval);
      this.marketDataService.getInstrumentMarketData(this.eventSource).subscribe(
        {
          next: (data) => {
            data.forEach(candle => {
              candle.close = Number(candle.close)
              candle.open = Number(candle.open)
              candle.low = Number(candle.low)
              candle.high = Number(candle.high)
              candle.volume = Number(candle.volume)
              candle.date = new Date(candle.date)
            });
            this.stocksData = data;
            
          }, 
          error: (error) => {
            console.log(error);
            
          }
        }
      );
    }
  }

  getMarketDataSync(candleInterval: string) {
    if(!this.authenticationService.isLoggedIn) {
      this.router.navigateByUrl('/signIn');
    } else {
      this.marketDataService.getInstrumentMarketDataSync(this.figi, candleInterval).subscribe(
        {
          next: (data) => {
            data.forEach(candle => {
              candle.close = Number(candle.close)
              candle.open = Number(candle.open)
              candle.low = Number(candle.low)
              candle.high = Number(candle.high)
              candle.volume = Number(candle.volume)
              candle.date = new Date(candle.date)
            });
            this.stocksData = data;
            
          }, 
          error: (error) => {
            console.log(error);
            
          }
        }
      );
    }
  }

}
