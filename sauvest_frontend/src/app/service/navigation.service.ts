import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  isShowMenu: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor() { }

  showMenu() {
    this.isShowMenu.next(!this.isShowMenu.getValue())
  }
}
