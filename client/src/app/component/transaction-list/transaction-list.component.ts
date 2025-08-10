import { ChangeDetectionStrategy, Component } from '@angular/core';
import { provideNativeDateAdapter } from '@angular/material/core';
import { Transaction } from '../../model/transaction';

let transactions: Transaction[] = [
  new Transaction('TRX101', 'UPI', 500, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX102', 'BANK', 600, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX103', 'UPI', 550, 'Pending',   new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX104', 'BANK', 320, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX105', 'UPI', 500, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX106', 'BANK', 600, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX107', 'UPI', 550, 'Pending',   new Date('2023-10-01'), 'Payment for groceries'),
  new Transaction('TRX108', 'BANK', 320, 'Completed', new Date('2023-10-01'), 'Payment for groceries'),
];

@Component({
  selector: 'app-transaction-list',
  standalone: false,
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css',
  providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TransactionListComponent {
  dataSource: Transaction[];
  selectedFilter: string = '';
  constructor() {
    this.dataSource = transactions;
  }
}
