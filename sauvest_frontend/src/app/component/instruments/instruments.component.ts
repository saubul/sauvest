import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { InstrumentModel } from 'src/app/model/instrument.model';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { InstrumentsService } from 'src/app/service/instruments.service';
import { imagesDirectory } from 'src/global';

@Component({
  selector: 'app-instruments',
  templateUrl: './instruments.component.html',
  styleUrls: ['./instruments.component.css']
})
export class InstrumentsComponent implements OnInit{

  loading: boolean;
  findInstrumentsFormGroup: FormGroup;
  instruments: Array<InstrumentModel>;
  p: number = 1;

  isShowShares: boolean;
  isShowBonds: boolean;
  notFound: boolean;
  isAdmin: boolean;

  img_src: string = imagesDirectory;

  constructor(private instrumentService: InstrumentsService,
              private router: Router,
              private authenticationService: AuthenticationService,
              private localStorage: LocalStorageService) {

  }

  ngOnInit(): void {
    if(!this.authenticationService.isLoggedIn) {
      this.router.navigateByUrl('/signIn');
    } else {
      this.loading = true;
      this.notFound = false;
      this.isShowBonds = false;
      this.isShowShares = true;
      this.findInstrumentsFormGroup = new FormGroup(
        {
          content: new FormControl('')
        }
      );
      this.authenticationService.checkIsAdmin(this.localStorage.retrieve('username')).subscribe(
        {
          next: (data) => {
            this.isAdmin = data;
          },
          error: (error) => {
            console.log(error);
            
          }
        }
      )

      this.findInstruments();
    }
  }

  findInstruments() {
    this.loading = true;
    let content = this.findInstrumentsFormGroup.get('content')?.value;
    if(content) {
      this.instrumentService.getInstrumentByContent(content, this.isShowShares).subscribe(
        {
          next: (data) => {
            this.instruments = new Array();
            data = this.instrumentService.convertInstrumentFields(data);
            this.instruments.push(data);
            this.loading = false;
            this.notFound = false;
          },
          error: (error) => {
            this.loading = false;
            this.notFound = true;
          }
        }
      );
    } else {
      if(this.isShowBonds) {
        this.instrumentService.getAllBonds().subscribe(
          {
            next: (data) => {
              data.map(instrument => {
                instrument = this.instrumentService.convertInstrumentFields(instrument);
              });
              
              this.instruments = data;
              this.loading = false;
              this.notFound = false;
            },
            error: (error) => {
              this.loading = false;
              this.notFound = true;
            }
          }
        );
      } else {
        this.instrumentService.getAllShares().subscribe(
          {
            next: (data) => {
              data.map(instrument => {
                instrument = this.instrumentService.convertInstrumentFields(instrument);
              });
              
              this.instruments = data;
              this.loading = false;
              this.notFound = false;
            },
            error: (error) => {
              this.loading = false;
              this.notFound = true;
            }
          }
        );
      }
    }
  }

  

  showShares() {
    this.isShowShares = true;
    this.isShowBonds = false;
    this.findInstruments();
  }

  showBonds() {
    this.isShowShares = false;
    this.isShowBonds = true;
    this.findInstruments();
  }
  
  goToInstrument(figi: string, instrumentType: string) {
    console.log(instrumentType);
    let isShare = instrumentType === 'Облигация'? false : true;
    this.router.navigateByUrl(`/instruments/${figi}?isShare=${isShare}`)
  }
}
