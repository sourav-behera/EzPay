import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-transaction',
  standalone: false,
  templateUrl: './transaction.component.html',
  styleUrl: './transaction.component.css',
})
export class TransactionComponent {
  @Input() transactionId!: string;
  @Input() type!: string;
  @Input() amount!: number;
  @Input() status!: string;
  @Input() date!: Date;
  @Input() message?: string;
}