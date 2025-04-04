import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { UsernamePasswordPayload } from './signInRequest.payload';

@Component({
  selector: 'app-sing-in',
  templateUrl: './sing-in.component.html',
  styleUrls: ['./sing-in.component.css']
})
export class SingInComponent implements OnInit {

  signInFormGroup: FormGroup;
  signInReuqestPayload: UsernamePasswordPayload;
  isError: boolean;
  registerSuccessMessage: string;
  isEnabled: boolean;

  constructor(private authenticationService: AuthenticationService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
    
  }

  ngOnInit(): void {
    this.isEnabled = true;
    this.signInFormGroup = new FormGroup(
      {
        username: new FormControl('', Validators.required),
        password: new FormControl('', Validators.required)
      }
    )

    this.signInReuqestPayload = { 
      username: '',
      password: ''
    }

    this.activatedRoute.queryParams
      .subscribe(params => {
        if (params.registered !== undefined && params.registered === 'true') {
          this.registerSuccessMessage = 'Успешная регистрация. Пожалуйста, проверьте вашу электронную почту '
            + ', чтобы активировать аккаунт, прежде чем войти!';
        }
      });
  }

  signIn(): void {
    this.signInReuqestPayload.username = this.signInFormGroup.get('username')?.value;
    this.signInReuqestPayload.password = this.signInFormGroup.get('password')?.value;


    this.authenticationService.verificateAccount(this.signInReuqestPayload.username).subscribe(
      {
        next: (data) => {
          if(!data) {
            this.isEnabled = false;
          } else {
            this.isEnabled = true;
            this.authenticationService.getToken(this.signInReuqestPayload).subscribe(
              {
                next: (data) => {
                  if(data) {
                    this.isError = false
                    this.router.navigateByUrl(`/user/${this.signInReuqestPayload.username}/profile`);
                  } else {
                    this.isError = true
                  }
                },
                error: (error) => {
                  this.isError = true
                  console.log(error)
                }
              }
            )
          }
        }
      }
    )

    
  }

}
