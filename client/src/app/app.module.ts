import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TransactionComponentComponent } from './component/transaction-component/transaction-component.component';
import { TransactionStatusComponentComponent } from './component/transaction-status-component/transaction-status-component.component';

@NgModule({
  declarations: [
    AppComponent,
    TransactionComponentComponent,
    TransactionStatusComponentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  // bootstrap: [AppComponent]
  bootstrap: [TransactionComponentComponent] // Change the bootstrap to the component you want to start with
})
export class AppModule { }
