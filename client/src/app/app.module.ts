import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TransactionComponent } from './component/transaction/transaction.component';
import { TransactionListComponent } from './component/transaction-list/transaction-list.component';
import { TransactionStatusComponentComponent } from './component/transaction-status-component/transaction-status-component.component';

import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatDivider, MatListModule } from '@angular/material/list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    AppComponent,
    TransactionComponent,
    TransactionStatusComponentComponent,
    TransactionListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatTableModule,
    MatCardModule,
    MatListModule,
    MatPaginatorModule,
    MatIconModule,
    MatDivider
  ],
  providers: [],
  bootstrap: [TransactionListComponent]
})
export class AppModule { }
