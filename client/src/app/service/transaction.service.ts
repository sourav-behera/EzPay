import { Injectable } from '@angular/core';
import { transactions } from '../../util/data';
import { Transaction } from '../model/transaction';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private allTransactions: Transaction[] = [];
  private currentTransactions: Transaction[] = [];

  constructor() {
    this.allTransactions = transactions;
    this.currentTransactions = [...this.allTransactions];
  }
  
  /**
   * Returns a copy of the current transactions array.
   */
  public getTransactions(): Observable<Transaction[]> {
    return of([...this.currentTransactions]);
  }
  
  /**
   * Sorts the current list of transactions based on a given key.
   * @param sortBy - The sorting criteria.
   */
  public sortData(sortBy: string): void {
    
    if(sortBy === 'transactionId') {
      this.currentTransactions.sort((firstTransaction, secondTransaction) => {
        // depends on the pattern of transactionId generated
        const idFirst = parseInt(firstTransaction.transactionId.replace(/\D/g, ''), 10);
        const idSecond = parseInt(secondTransaction.transactionId.replace(/\D/g, ''), 10);
        return idSecond - idFirst;
      });
    }
    else if(sortBy === 'latestDate') {
      this.currentTransactions.sort((firstTransaction, secondTransaction) => {
        let value: number = secondTransaction.date.getTime() - firstTransaction.date.getTime();
        if(value == 0) {
          return secondTransaction.date.getTime() - firstTransaction.date.getTime();
        }
        return value;
      });
    }
    else if(sortBy === 'oldestDate') {
      this.currentTransactions.sort((firstTransaction, secondTransaction) => {
        let value: number = firstTransaction.date.getTime() - secondTransaction.date.getTime();
        if(value == 0) {
          return secondTransaction.date.getTime() - firstTransaction.date.getTime();
        }
        return value;
      });
    }
    else if(sortBy === 'increasingAmount') {
      this.currentTransactions.sort((firstTransaction, secondTransaction) => {
        let value: number = firstTransaction.amount - secondTransaction.amount;
        if(value == 0) {
          return secondTransaction.date.getTime() - firstTransaction.date.getTime();
        }
        return value;
      });
    }
    else if(sortBy === 'decreasingAmount') {
      this.currentTransactions.sort((firstTransaction, secondTransaction) => {
        let value: number = secondTransaction.amount - firstTransaction.amount;
        if(value == 0) {
          return secondTransaction.date.getTime() - firstTransaction.date.getTime();
        }
        return value;
      });
    }
  }
  
  /**
   * Filters transactions based on their type.
   * @param type - The transaction type ('income', 'expense', etc.).
   */
  public filterByType(type: string): void {
    this.currentTransactions = this.allTransactions.filter(transaction => transaction.type === type);
  }
  
  /**
   * Filters transactions based on their status.
   * @param status - The transaction status ('completed', 'pending', etc.).
   */
  public filterByStatus(status: string): void {
    this.currentTransactions = this.allTransactions.filter(transaction => transaction.status === status);
  }
  
  /**
   * Filters transactions that fall within a given amount range.
   */
  public filterByAmountRange(min: number | null, max: number | null): void {
    if (min === null || max === null) {
      this.resetFilters();
      return;
    }
    this.currentTransactions = this.allTransactions.filter(t => t.amount >= min! && t.amount <= max!);
  }
  
  /**
   * Filters transactions that fall within a given date range.
   */
  public filterByDateRange(from: Date | null, to: Date | null): void {
    if (!from || !to) {
      this.resetFilters();
      return;
    }
    // Set time to 00:00:00 for accurate 'day' comparison
    const fromDate = new Date(from);
    fromDate.setHours(0, 0, 0, 0);

    const toDate = new Date(to);
    toDate.setHours(23, 59, 59, 999); // Include the entire 'to' day

    this.currentTransactions = this.allTransactions.filter(t => {
      const transactionDate = new Date(t.date);
      return transactionDate >= fromDate && transactionDate <= toDate;
    });
  }
  
  /**
   * Filters transactions that match a specific date.
   */
  public filterByDate(selectedDate: Date | null): void {
    if (!selectedDate) {
      this.resetFilters();
      return;
    }
    this.currentTransactions = this.allTransactions.filter(transaction => 
      transaction.date.getDate() === selectedDate.getDate() &&
      transaction.date.getMonth() === selectedDate.getMonth() &&
      transaction.date.getFullYear() === selectedDate.getFullYear()
    );
  }
  
  /**
   * Filters transactions where the ID includes the search text.
   * @param searchText - The text to search for in the transaction ID.
   */
  public filterBySearchID(searchText: string): void {
    const text = searchText.trim().toLowerCase();
    if (!text) {
      this.resetFilters();
      return;
    }
    this.currentTransactions = this.allTransactions.filter(transaction => 
      transaction.transactionId.toLowerCase().includes(text)
    );
  }
  
  /**
   * Resets all filters, restoring the full list of transactions.
   */
  public resetFilters(): void {
    this.currentTransactions = [...this.allTransactions];
  }
}
