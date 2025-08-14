import { Injectable } from '@angular/core';
import { of, Observable } from 'rxjs';
import { transactionsList } from './transaction-status-list';
import { TransactionStatus } from '../model/transaction-status';

@Injectable({
  providedIn: 'root'
})
export class TransactionStatusService {
  constructor() { }
  getData$(): Observable<TransactionStatus[]> {
    return of(transactionsList); // of(..) creates an Observable that emits the array
  }
}
