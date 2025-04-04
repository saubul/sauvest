import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-create-instrument',
  templateUrl: './create-instrument.component.html',
  styleUrls: ['./create-instrument.component.css']
})
export class CreateInstrumentComponent implements OnInit{

  loading: boolean;

  constructor() {

  }

  ngOnInit(): void {
    this.loading = true;

    this.loading = false;
  }

}
