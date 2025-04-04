import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router'
import { ChatsComponent } from './app/component/chats/chats.component';
import { ErrorComponent } from './app/component/error/error.component';
import { InstrumentsComponent } from './app/component/instruments/instruments.component';
import { MainComponent } from './app/component/main/main.component';
import { NewsComponent } from './app/component/news/news.component';
import { PostViewComponent } from './app/component/post-view/post-view.component';
import { ProfileComponent } from './app/component/profile/profile.component';
import { SettingsComponent } from './app/component/settings/settings.component';
import { SingInComponent } from './app/component/sing-in/sing-in.component';
import { SingUpComponent } from './app/component/sing-up/sing-up.component';
import { SsoTokenInfoComponent } from './app/component/sso-token-info/sso-token-info.component';
import { SubscriptionsComponent } from './app/component/subscriptions/subscriptions.component';
import { SuccessActivateComponent } from './app/component/success-activate/success-activate.component';
import { UserPageComponent } from './app/component/user-page/user-page.component';
import { InstrumentViewComponent } from './app/component/instrument-view/instrument-view.component';
import { CreateInstrumentComponent } from './app/component/create-instrument/create-instrument.component';

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'signIn', component: SingInComponent },
  { path: 'signUp', component: SingUpComponent },
  { path: 'successActivate/:token', component: SuccessActivateComponent },
  { path: 'error', component: ErrorComponent },
  { path: 'user', component: UserPageComponent, children: [
    {
      path: ':username/profile',
      component: ProfileComponent
    },
    {
      path: ':username/subscriptions',
      component: SubscriptionsComponent
    },
    {
      path: ':username/news',
      component: NewsComponent
    },
    {
      path: ':username/chats',
      component: ChatsComponent
    },
    {
      path: ':username/settings',
      component: SettingsComponent
    }
  ] },
  { path: 'post/:id', component: PostViewComponent },
  { path: 'ssoTokenInfo', component: SsoTokenInfoComponent},
  { path: 'instruments', component: InstrumentsComponent},
  { path: 'instruments/:figi', component: InstrumentViewComponent},
  { path: 'createInstrument', component: CreateInstrumentComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
