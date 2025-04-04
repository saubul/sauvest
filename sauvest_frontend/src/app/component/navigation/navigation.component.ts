import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { FirebaseService } from 'src/app/service/firebase.service';
import { NavigationService } from 'src/app/service/navigation.service';
import { imagesDirectory } from 'src/global';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  isLoggedIn: boolean = false;
  isShowMenu: boolean = false;
  username: string;
  img_src: string = imagesDirectory;

  profile_img_src: string = this.img_src + '/profile.png';
  loading: boolean = false;

  constructor(private router: Router,
              private authenticationService: AuthenticationService,
              private localStorage: LocalStorageService,
              private navigationService: NavigationService,
              private firebaseService: FirebaseService) {

  }

  ngOnInit(): void {
    this.loading = true;
    this.navigationService.isShowMenu.subscribe(
      {
        next: (data) => this.isShowMenu = data,
        error: (error) => console.log(error)
      }
    );
    this.authenticationService.isLoggedIn.subscribe(
      {
        next: (data) => {
          this.isLoggedIn = data;
          if(data) {
            this.username = this.authenticationService.getUsername();
            this.firebaseService.getImagePath('profile', this.username).subscribe(
              {
                next: (data) => {
                  this.profile_img_src = data;
                  this.loading = false;
                },
                error: (error) => {
                  this.profile_img_src = this.img_src + '/profile.png';
                  this.loading = false;
                  console.log(error);
                }
              }
            );
          } else {
            this.profile_img_src = this.img_src + '/profile.png';
          }
        },
        error: (error) => console.log(error)
      }
    );
    let refreshToken = this.authenticationService.getRefreshToken();
    if (refreshToken) {
      this.authenticationService.validateToken(refreshToken).subscribe(
        {
          next: (data) => {
            this.isLoggedIn = data
            if(data) {
              this.username = this.authenticationService.getUsername();
              this.firebaseService.getImagePath('profile', this.username).subscribe(
                {
                  next: (data) => {
                    this.profile_img_src = data;
                    this.loading = false;
                  },
                  error: (error) => {
                    this.profile_img_src = this.img_src + '/profile.png';
                    this.loading = false;
                    console.log(error);
                  }
                }
              );
            }
          },
          error: (error) => console.log(error)
        }
      )
    }
    setTimeout(() => this.loading = false, 500)
  }

  routeToMain() {
    if(this.router.url == '/') {
      window.location.reload();
    } else {
      this.router.navigateByUrl('/');
    }
  }

  showMenu() {
    this.isShowMenu = !this.isShowMenu;
  }

  closeMenu() {
    this.isShowMenu = false;
  }

  signOut() {
    this.closeMenu();
    this.authenticationService.signOut();
    this.router.navigateByUrl('/');
  }

  nagivateToUserProfile() {
    this.router.navigateByUrl(`/user/${this.username}/profile`).then(() => {
      location.reload();
      this.closeMenu();
    });
    
  }
}
