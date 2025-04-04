import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {  NgxWebstorageModule } from 'ngx-webstorage';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app/app.component';
import { NavigationComponent } from './app/component/navigation/navigation.component';
import { SingInComponent } from './app/component/sing-in/sing-in.component';
import { SingUpComponent } from './app/component/sing-up/sing-up.component';
import { MainComponent } from './app/component/main/main.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { SuccessActivateComponent } from './app/component/success-activate/success-activate.component';
import { TokenInterceptor } from './app/token-interceptor';
import { ProfileComponent } from './app/component/profile/profile.component';
import { NavigationBarComponent } from './app/component/navigation-bar/navigation-bar.component';
import { NewsComponent } from './app/component/news/news.component';
import { ChatsComponent } from './app/component/chats/chats.component';
import { UserPageComponent } from './app/component/user-page/user-page.component';
import { SettingsComponent } from './app/component/settings/settings.component';
import { AngularFireStorageModule } from '@angular/fire/compat/storage';
import { AngularFireModule } from '@angular/fire/compat';
import { SubscriptionsComponent } from './app/component/subscriptions/subscriptions.component';
import { PostViewComponent } from './app/component/post-view/post-view.component';
import { ErrorComponent } from './app/component/error/error.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SsoTokenInfoComponent } from './app/component/sso-token-info/sso-token-info.component';
import { InstrumentsComponent } from './app/component/instruments/instruments.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { InstrumentViewComponent } from './app/component/instrument-view/instrument-view.component';
import { ChartComponent } from './app/component/chart/chart.component'
import { IgxFinancialChartModule, IgxLegendModule } from "igniteui-angular-charts";
import { CreateInstrumentComponent } from './app/component/create-instrument/create-instrument.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {MatFormFieldModule} from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    SingInComponent,
    SingUpComponent,
    MainComponent,
    SuccessActivateComponent,
    ProfileComponent,
    NavigationBarComponent,
    NewsComponent,
    ChatsComponent,
    UserPageComponent,
    SettingsComponent,
    SubscriptionsComponent,
    PostViewComponent,
    ErrorComponent,
    SsoTokenInfoComponent,
    InstrumentsComponent,
    InstrumentViewComponent,
    ChartComponent,
    CreateInstrumentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgxWebstorageModule.forRoot(),
    AngularFireModule.initializeApp({
      apiKey: "AIzaSyBVFYlXj0fE61nFcZl6kKZPLTeFvpo-BME",
      authDomain: "sauvestcloud.firebaseapp.com",
      projectId: "sauvestcloud",
      storageBucket: "sauvestcloud.appspot.com",
      messagingSenderId: "553199793879",
      appId: "1:553199793879:web:1d1c2075df165bd069a292"
    }),
    NgxPaginationModule,
    AngularFireStorageModule,
    ScrollingModule,
    FontAwesomeModule,
    IgxFinancialChartModule,
    IgxLegendModule,
    BrowserAnimationsModule,
    MatDatepickerModule,
    MatNativeDateModule, 
    MatFormFieldModule,
    MatInputModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
