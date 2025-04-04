import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserModel } from 'src/app/model/user.model';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit{
  img_src: string = '/assets/img/';

  constructor() {

  }

  ngOnInit(): void {
  }
}
