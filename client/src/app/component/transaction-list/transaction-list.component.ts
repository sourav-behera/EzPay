import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { provideNativeDateAdapter } from '@angular/material/core';
import { Transaction } from '../../model/transaction';

let transactions: Transaction[] = [
  new Transaction('TRX101', 'UPI', 500, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX102', 'BANK', 600, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX103', 'UPI', 550, 'Pending',   new Date('2023-10-02'), 'Payment for groceries'),
  new Transaction('TRX104', 'BANK', 320, 'Completed', new Date('2023-10-02'), 'Payment for groceries'),
  new Transaction('TRX105', 'UPI', 500, 'Completed', new Date('2023-10-03'), 'Payment for groceries'),
  new Transaction('TRX106', 'BANK', 600, 'Completed', new Date('2023-10-03'), 'Payment for groceries'),
  new Transaction('TRX107', 'UPI', 550, 'Pending',   new Date('2023-10-04'), 'Payment for groceries'),
  new Transaction('TRX108', 'BANK', 320, 'Completed', new Date('2023-10-04'), 'Payment for groceries'),
  new Transaction('TRX109', 'BANK', 320, 'Completed', new Date('2025-08-08'), 'Payment for groceries'),
  new Transaction('TRX110', 'UPI', 1000, 'Pending', new Date('2025-08-08'), 'Payment for groceries'),
  new Transaction('TRX111', 'UPI', 1000, 'Failed', new Date('2025-08-08'), 'Friendly Transfer'),
];

@Component({
  selector: 'app-transaction-list',
  standalone: false,
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css',
  providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TransactionListComponent implements OnInit{
  dataSource: Transaction[] = [];
  filteredData: Transaction[] = [];
  selectedFilter: string = '';
  
  //variables related to date range
  fromDate: Date|null = null;
  toDate: Date|null = null;
  
  // variables related to amount range
  minimumAmount: number|null = null;
  maximumAmount: number|null = null;
  
  
  constructor() {}
  
  ngOnInit(): void {
    this.dataSource = transactions;
    this.filteredData = transactions;
  }
  
  // handles sorting
  public handleSorting(sortBy: string): void {
    if(sortBy === 'transactionId') {
      this.filteredData.sort((a, b) => {
        // depends on the pattern of transactionId generated
        const idFirst = parseInt(a.transactionId.replace(/\D/g, ''), 10);
        const idSecond = parseInt(b.transactionId.replace(/\D/g, ''), 10);
        return idSecond - idFirst;
      });
    }
    else if(sortBy === 'latestDate') {
      this.filteredData.sort((a, b) => {
        let value:number = b.date.getTime() - a.date.getTime();
        if(value == 0) {
          return b.date.getTime() - a.date.getTime();
        }
        return value;
      });
    }
    else if(sortBy === 'oldestDate') {
      this.filteredData.sort((a, b) => {
        let value:number = a.date.getTime() - b.date.getTime();
        if(value == 0) {
          return b.date.getTime() - a.date.getTime();
        }
        return value;
      });
    }
    else if(sortBy === 'increasingAmount') {
      this.filteredData.sort((a, b) => {
        let value:number = a.amount - b.amount;
        if(value == 0) {
          return b.date.getTime() - a.date.getTime();
        }
        return value;
      });
    }
    else if(sortBy === 'decreasingAmount') {
      this.filteredData.sort((a,b) => {
        let value:number = b.amount - a.amount;
        if(value == 0) {
          return b.date.getTime() - a.date.getTime();
        }
        return value;
      });
    }
  }
  
  // displays transactions accordind to type chosen
  public getTransactionsByType(type:string) :void {
    this.filteredData = this.dataSource.filter(transaction => {
      return transaction.type === type
    })
  }
  
  //displays transactions equal to @param status
  public getTransactionsByStatus(status: string) :void {
    this.filteredData = this.dataSource.filter(transaction => {
      return transaction.status === status
    })
  }
  
  //displays transactions in a range of amount
  public getTransactionsByAmountRange(): void {
    if(this.minimumAmount && this.maximumAmount) {
      this.filteredData = this.dataSource.filter(transaction => {
        return (this.minimumAmount!<=transaction.amount && transaction.amount<=this.maximumAmount!)
      })
    }
  }
  
  //displays transactions in a range of dates
  public getTransactionsByDateRange(): void {
    if(this.toDate && this.fromDate) {
      this.toDate.setHours(0, 0, 0, 0);
      this.fromDate.setHours(0, 0, 0, 0);
      this.filteredData = this.dataSource.filter(transaction => {
        const date = new Date(transaction.date);
        date.setHours(0, 0, 0, 0);
        return (this.fromDate!<=date && date<=this.toDate!)
      });
    }
  }
  
  // displays transactions on the selected Date
  public getTransactionByDate(selectedDate: Date): void {
    this.filteredData = this.dataSource.filter(transaction => {
      return ( transaction.date.getDate() === selectedDate.getDate() && transaction.date.getMonth() === selectedDate.getMonth() && transaction.date.getFullYear() === selectedDate.getFullYear() )
    });
  }
  
  // displays transactions that contains the searchText in transactionID
  public getTransactionsBySearchID(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    const searchText = filterValue.trim().toLowerCase();
    
    if(!searchText) {
      this.filteredData = this.dataSource;
    }
    this.filteredData = this.dataSource.filter(transaction => {
      return transaction.transactionId.toLowerCase().includes(searchText);
    })
  }
}
