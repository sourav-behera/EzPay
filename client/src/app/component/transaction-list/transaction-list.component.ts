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
  new Transaction('TRX108', 'BANK', 320, 'Completed', new Date('2025-08-08'), 'Payment for groceries'),
  new Transaction('TRX108', 'UPI', 1000, 'Pending', new Date('2025-08-08'), 'Payment for groceries'),
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
  constructor() {}
  
  ngOnInit(): void {
    this.dataSource = transactions;
    this.filteredData = transactions;
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
