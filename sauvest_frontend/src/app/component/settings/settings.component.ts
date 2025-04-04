import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserModel } from 'src/app/model/user.model';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { FirebaseService } from 'src/app/service/firebase.service';
import { UserService } from 'src/app/service/user.service';
import { imagesDirectory } from 'src/global';

@Component({
  selector: 'app-settings',
  host: { class: 'bg-white rounded-md p-2 col-start-2 col-end-7' },
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  image: File;

  img_src: string = imagesDirectory;
  profile_img_src: string = this.img_src + '/profile.png';

  userModel: UserModel;
  loading: boolean = false;

  updateUserFormGroup: FormGroup;

  constructor(private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private firebaseService: FirebaseService,
    private authenticationService: AuthenticationService) {

  }

  ngOnInit(): void {
    this.loading = true;
    if(this.activatedRoute.snapshot.params.username !== this.authenticationService.getUsername()) {
      this.router.navigateByUrl('/error')
      return
    }
    this.userService.getUserByUsername(this.activatedRoute.snapshot.params.username).subscribe(
      {
        next: (data) => {
          this.userModel = data;
          this.firebaseService.getImagePath('profile', this.userModel.username).subscribe(
            {
              next: (data) => {
                this.profile_img_src = data;
                this.loading = false;
              },
              error: (error) => {
                console.log(error)
                this.loading = false
              }
            }
          )
        },
        error: (error) => {
          this.router.navigateByUrl('/signIn')
          console.log(error)
        }
      }
    );
    this.updateUserFormGroup = new FormGroup(
      {
        name: new FormControl(''),
        surname: new FormControl(''),
        ssoToken: new FormControl('')
      }
    )
  }


  uploadImage(event: any) {
    this.image = event.target.files[0];
    this.userService.uploadImage(this.activatedRoute.snapshot.params.username, this.image)
    this.firebaseService.getImagePath('profile', this.userModel.username).subscribe(
      {
        next: (data) => {
          this.profile_img_src = data;
        },
        error: (error) => console.log(error)
      }
    )

  }

  profileImageClicked() {
    let element: HTMLElement = document.getElementById('imageUploadInput') as HTMLElement;
    element.click();
  }

  updateUser() {
    let updatedUser: UserModel = Object.assign({}, this.userModel);
    updatedUser.name = this.updateUserFormGroup.get('name')?.value;
    updatedUser.surname = this.updateUserFormGroup.get('surname')?.value;
    updatedUser.ssoToken = this.updateUserFormGroup.get('ssoToken')?.value;
    this.userService.updateUser(updatedUser).subscribe(
      {
        next: (data) => {
          this.userModel = data;
        },
        error: (error) => {
          console.log(error)
        }
      }
    )
  }
}

