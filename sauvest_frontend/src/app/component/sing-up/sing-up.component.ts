import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { SignUpRequestPayload } from './signUpRequest.payload';

@Component({
  selector: 'app-sing-up',
  templateUrl: './sing-up.component.html',
  styleUrls: ['./sing-up.component.css']
})
export class SingUpComponent implements OnInit {

  signUpFormGroup: FormGroup
  fieldsError: boolean
  signUpRequestPayload: SignUpRequestPayload;

  constructor(private authenticationService: AuthenticationService,
              private router: Router) {

  }

  ngOnInit(): void {
    this.signUpFormGroup  = new FormGroup({
      username: new FormControl(null, Validators.required),
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, [Validators.required, Validators.minLength(8)]),
      repeatPassword: new FormControl(null, Validators.required),
      ssoToken: new FormControl(null, Validators.required)
    })

    this.signUpRequestPayload = {
      username: '',
      password: '',
      email: '',
      ssoToken: ''
    };
  }

  signUp(): void {
    this.signUpRequestPayload.username = this.signUpFormGroup.get('username')?.value;
    this.signUpRequestPayload.email = this.signUpFormGroup.get('email')?.value;
    this.signUpRequestPayload.password = this.signUpFormGroup.get('password')?.value;
    this.signUpRequestPayload.ssoToken = this.signUpFormGroup.get('ssoToken')?.value;

    this.authenticationService.signUp(this.signUpRequestPayload).subscribe(
      {
        next: (data) => {
          this.router.navigate(['/signIn'], { queryParams: { registered: 'true' } });
        },
        error: (error) => {
          this.fieldsError = true
          console.log(error);
        }
      }
    )
  }

}
