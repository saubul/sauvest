import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { imagesDirectory } from 'src/global';
import { faArrowUp } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {

  username: string;
  img_src: string = imagesDirectory;

  topArrow = faArrowUp;

  constructor(private authenticationService: AuthenticationService,
              private router: Router) {

  }

  ngOnInit(): void {  

    this.username = this.authenticationService.getUsername();
    
  }

  nagivateToUserProfile() {
    this.router.navigateByUrl(`/user/${this.username}/profile`).then(() => {
      location.reload();
    });
  }

  goTop() {
    window.scroll({ 
      top: 0, 
      left: 0, 
      behavior: 'smooth' 
    });
  }

}
