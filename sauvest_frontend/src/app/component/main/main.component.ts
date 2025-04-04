import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { imagesDirectory } from 'src/global';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  loading: boolean = false;
  img_src: string = imagesDirectory;

  constructor(private authenticationService: AuthenticationService, 
              private router: Router) {

  }

  ngOnInit(): void {
    this.authenticationService.isMainPage.next(true);
    setTimeout(() => {
      this.authenticationService.isMainPage.next(false);
      this.router.navigateByUrl('/signIn');
    }, 3000)
  }

}
