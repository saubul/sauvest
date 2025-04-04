import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-success-activate',
  templateUrl: './success-activate.component.html',
  styleUrls: ['./success-activate.component.css']
})
export class SuccessActivateComponent implements OnInit {

  token: string;

  constructor(private authenticationService: AuthenticationService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.token = this.activatedRoute.snapshot.params.token;
    this.authenticationService.activateAccount(this.token).subscribe();
  }

}
