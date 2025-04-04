import { Component, OnInit } from '@angular/core';
import { NavigationService } from './service/navigation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from './service/authentication.service';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  isMainPage: BehaviorSubject<boolean>;

  constructor(private navigationService: NavigationService,
              private authenticationService: AuthenticationService) {
                this.isMainPage = authenticationService.isMainPage;
  }
  ngOnInit(): void {
    if(window.location.pathname === '/') {
      this.authenticationService.isMainPage.next(true);
    } else {
      this.authenticationService.isMainPage.next(false);
    }
  }

  closeMenu() {
    this.navigationService.isShowMenu.next(false)
  }

}
